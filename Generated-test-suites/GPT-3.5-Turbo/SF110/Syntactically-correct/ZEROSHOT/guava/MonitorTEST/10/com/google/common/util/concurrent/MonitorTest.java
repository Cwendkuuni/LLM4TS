import org.junit.*;
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
            fail("Interrupted!");
        }
    }
    
    @Test
    public void testEnterWithTime() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.enter(1000, java.util.concurrent.TimeUnit.MILLISECONDS));
    }
    
    @Test
    public void testEnterInterruptiblyWithTime() {
        Monitor monitor = new Monitor();
        try {
            assertTrue(monitor.enterInterruptibly(1000, java.util.concurrent.TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) {
            fail("Interrupted!");
        }
    }
    
    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.tryEnter());
    }
    
    // Add tests for all other methods in the Monitor class
    
    
    

    
    
    
    

    
}