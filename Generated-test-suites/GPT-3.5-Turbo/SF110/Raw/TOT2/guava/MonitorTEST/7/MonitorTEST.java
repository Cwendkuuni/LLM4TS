import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;

public class MonitorTest {
    
    private Monitor monitor;
    
    @Before
    public void setUp() {
        monitor = new Monitor();
    }
    
    @After
    public void tearDown() {
        monitor = null;
    }
    
    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }
    
    @Test
    public void testEnterInterruptibly() {
        try {
            monitor.enterInterruptibly();
            assertTrue(monitor.isOccupiedByCurrentThread());
        } catch (InterruptedException e) {
            fail("Should not throw InterruptedException");
        }
    }
    
    @Test
    public void testEnterTimeUnit() {
        boolean entered = monitor.enter(1, TimeUnit.SECONDS);
        assertTrue(entered);
    }
    
    // Add more test cases for other methods
    
}