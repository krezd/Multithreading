class MyThread extends Thread {

    MyThread(String name) {
        super(name);
    }

    public void run() {
        for (int i = 0; i <= 100; i++) {
            if (i % 10 == 0) {
                System.out.printf(Thread.currentThread().getName() + ": ");
                System.out.println(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}

class MyThread2 implements Runnable {
    public void run() {
        for (int i = 0; i <= 100; i++) {
            if (i % 10 == 0) {
                System.out.printf(Thread.currentThread().getName() + ": ");
                System.out.println(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

class Chronometer implements Runnable {
    private static final Object lock = new Object();
    private static int secondsElapsed = 0;

    public static int getSecondsElapsed() {
        return secondsElapsed;
    }

    public static Object getLock() {
        return lock;
    }

    public void run() {
        try {
            while (true) {
                synchronized (lock) {
                    secondsElapsed++;
                    System.out.println("Прошло секунд: " + secondsElapsed);
                    lock.notifyAll();
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class MessageTask implements Runnable {
    private final int interval;
    private int lastMark = 0;

    public MessageTask(int interval) {
        this.interval = interval;
    }


    public void run() {
        while (true) {
            synchronized (Chronometer.getLock()) {
                try {
                    Chronometer.getLock().wait();

                    if (Chronometer.getSecondsElapsed() % interval == 0 && Chronometer.getSecondsElapsed() != lastMark) {
                        System.out.println("Прошло " + interval + " секунд!");
                        lastMark = Chronometer.getSecondsElapsed();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

public class Main {

    public static void main(String[] args) {
        //Первое задание
        MyThread thread = new MyThread("myThread");
        thread.start();
        //Второе задание
        Thread tread2 = new Thread(new MyThread2(), "MyThread2");
        tread2.start();

        Thread clockThread = new Thread(new Chronometer());
        clockThread.start();

        Thread fiveSecondsThread = new Thread(new MessageTask(5));
        fiveSecondsThread.start();

        Thread sevenSecondsThread = new Thread(new MessageTask(7));
        sevenSecondsThread.start();
    }
}