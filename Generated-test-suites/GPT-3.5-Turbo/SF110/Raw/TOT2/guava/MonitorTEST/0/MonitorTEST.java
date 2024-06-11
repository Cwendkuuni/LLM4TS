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
    public void testEnterWithTimeout() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.enter(1, TimeUnit.SECONDS));
    }
    
    @Test
    public void testEnterInterruptiblyWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        assertFalse(monitor.enterInterruptibly(1, TimeUnit.SECONDS));
    }
    
    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.tryEnter());
        monitor.enter();
        assertFalse(monitor.tryEnter());
    }
    
    // Add more individual test cases for other methods
    
    @Test
    public void testEnterWhen() throws InterruptedException {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        
        monitor.enterWhen(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }
    
    @Test
    public void testEnterIf() {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        
        assertTrue(monitor.enterIf(guard));
        assertFalse(monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS));
    }
    
    // Add more test cases to cover edge cases and exceptions
    
}