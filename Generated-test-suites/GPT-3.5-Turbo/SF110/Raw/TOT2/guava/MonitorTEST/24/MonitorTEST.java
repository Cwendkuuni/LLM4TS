import org.junit.Test;
import static org.junit.Assert.*;

public class MonitorTest {

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptibly() {
        Monitor monitor = new Monitor();
        try {
            monitor.enterInterruptibly();
            assertTrue(monitor.isOccupiedByCurrentThread());
        } catch (InterruptedException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testEnterWithTimeout() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.enter(1, java.util.concurrent.TimeUnit.NANOSECONDS));
    }

    // More test cases for other methods can be added here

}