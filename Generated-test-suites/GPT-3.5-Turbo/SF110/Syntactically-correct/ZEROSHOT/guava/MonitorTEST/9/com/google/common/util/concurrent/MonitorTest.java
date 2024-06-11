import org.junit.Test;
import static org.junit.Assert.*;

public class MonitorTest {
    @Test
    public void testEnter() {
        Monitor m = new Monitor();
        m.enter();
        assertTrue(m.isOccupiedByCurrentThread());
        m.leave();
        assertFalse(m.isOccupiedByCurrentThread());
    }
    
    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor m = new Monitor();
        m.enterInterruptibly();
        assertTrue(m.isOccupiedByCurrentThread());
        m.leave();
        assertFalse(m.isOccupiedByCurrentThread());
    }
    
    @Test
    public void testEnterWithTimeout() {
        Monitor m = new Monitor();
        boolean entered = m.enter(1, TimeUnit.SECONDS);
        assertTrue(entered);
        assertTrue(m.isOccupiedByCurrentThread());
        m.leave();
        assertFalse(m.isOccupiedByCurrentThread());
    }
    
    @Test
    public void testEnterInterruptiblyWithTimeout() throws InterruptedException {
        Monitor m = new Monitor();
        boolean entered = m.enterInterruptibly(1, TimeUnit.SECONDS);
        assertTrue(entered);
        assertTrue(m.isOccupiedByCurrentThread());
        m.leave();
        assertFalse(m.isOccupiedByCurrentThread());
    }
    
    // Add tests for other methods
    
}