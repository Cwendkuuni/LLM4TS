import static org.junit.Assert.*;
import org.junit.Test;

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

    @Test
    public void testEnter_Timeout() {
        Monitor monitor = new Monitor();
        boolean entered = monitor.enter(1, TimeUnit.SECONDS);
        assertTrue(entered);
    }

    @Test
    public void testEnterInterruptibly_Timeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        boolean entered = monitor.enterInterruptibly(1, TimeUnit.SECONDS);
        assertTrue(entered);
    }

    // Other test methods for all public methods in the Monitor class

    @Test
    public void testEnterWhen_GuardNotSatisfied() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        assertThrows(IllegalMonitorStateException.class, () -> monitor.enterWhen(guard));
    }

    // More test methods for edge cases and error scenarios

}