import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorTest {

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enterInterruptibly();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    // Additional test cases for other methods can be added here

    @Test
    public void testEdgeCasesAndExceptions() {
        Monitor monitor = new Monitor();
        ReentrantLock lock = new ReentrantLock();

        // Testing enter method with fair lock
        monitor = new Monitor(true);
        monitor.tryEnter();
        assertTrue(monitor.isOccupiedByCurrentThread());

        // Testing enter method with timeout
        boolean entered = monitor.enter(10, TimeUnit.MILLISECONDS);
        assertTrue(entered);

        // Testing enter method with interrupted exception
        Thread.currentThread().interrupt();
        try {
            monitor.enter(10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // Exception is expected
        }
        assertFalse(monitor.isOccupiedByCurrentThread());

        // Testing enter method with illegal monitor state exception
        Monitor otherMonitor = new Monitor();
        try {
            monitor.enterWhen(otherMonitor.new Guard(otherMonitor) {
                @Override
                public boolean isSatisfied() {
                    return false;
                }
            });
            fail("Should throw IllegalMonitorStateException");
        } catch (IllegalMonitorStateException e) {
            // Exception is expected
        }
    }

}