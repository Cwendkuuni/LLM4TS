import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class MonitorTest {
    
    private Monitor monitor;
    
    @Before
    public void setUp() {
        monitor = new Monitor();
    }
    
    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(monitor.isOccupied());
    }
    
    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enterInterruptibly();
        assertTrue(monitor.isOccupied());
    }
    
    @Test
    public void testEnterWithTimeout_Success() {
        assertTrue(monitor.enter(1, TimeUnit.SECONDS));
        assertTrue(monitor.isOccupied());
    }
    
    @Test 
    public void testEnterWithTimeout_Fail() {
        assertFalse(monitor.enter(1, TimeUnit.MILLISECONDS));
        assertFalse(monitor.isOccupied());
    }
    
    @Test
    public void testEnterInterruptiblyWithTimeout_Success() throws InterruptedException {
        assertTrue(monitor.enterInterruptibly(1, TimeUnit.SECONDS));
        assertTrue(monitor.isOccupied());
    }
    
    @Test 
    public void testEnterInterruptiblyWithTimeout_Fail() throws InterruptedException {
        assertFalse(monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS));
        assertFalse(monitor.isOccupied());
    }
    
    // More test cases for other methods...
    
}