import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MonitorTest {

    private Monitor monitor;

    @Before
    public void setup() {
        monitor = new Monitor();
    }

    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enterInterruptibly();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    // Additional test cases for other methods will be added here...

    @Test
    public void testEnterIf_withUnsatisfiedGuard_shouldNotEnter() {
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        boolean entered = monitor.enterIf(guard);

        assertFalse(entered);
        assertFalse(monitor.isOccupied());
    }
}