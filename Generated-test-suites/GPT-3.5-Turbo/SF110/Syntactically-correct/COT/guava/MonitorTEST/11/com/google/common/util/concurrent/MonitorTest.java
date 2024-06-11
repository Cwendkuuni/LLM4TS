
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.util.concurrent.Monitor;

public class MonitorTest {

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.isOccupied());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }
    
    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enterInterruptibly();
        assertTrue(monitor.isOccupied());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }
    
    @Test
    public void testEnterTimeout() {
        Monitor monitor = new Monitor();
        boolean entered = monitor.enter(1000, TimeUnit.MILLISECONDS);
        assertTrue(entered);
        assertTrue(monitor.isOccupied());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }
    
    @Test
    public void testEnterInterruptiblyTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        boolean entered = monitor.enterInterruptibly(1000, TimeUnit.MILLISECONDS);
        assertTrue(entered);
        assertTrue(monitor.isOccupied());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }
    
    // TODO: Add more test cases for other methods
    
}