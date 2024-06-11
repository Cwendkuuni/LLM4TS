
import org.junit.Test;
import static org.junit.Assert.*;

public class MonitorTest {
    
    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }
    
    @Test
    public void testEnterInterruptibly() {
        Monitor monitor = new Monitor();
        try {
            monitor.enterInterruptibly();
            assertTrue(monitor.isOccupiedByCurrentThread());
            monitor.leave();
            assertFalse(monitor.isOccupiedByCurrentThread());
        } catch (InterruptedException e) {
            fail("InterruptedException occurred");
        }
    }
    
    @Test
    public void testEnterWithTimeout() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.enter(1, TimeUnit.SECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }
    
    @Test
    public void testEnterInterruptiblyWithTimeout() {
        Monitor monitor = new Monitor();
        try {
            assertTrue(monitor.enterInterruptibly(1, TimeUnit.SECONDS));
            assertTrue(monitor.isOccupiedByCurrentThread());
            monitor.leave();
            assertFalse(monitor.isOccupiedByCurrentThread());
        } catch (InterruptedException e) {
            fail("InterruptedException occurred");
        }
    }
    
    // Rest of the test methods for other public methods
    
}
