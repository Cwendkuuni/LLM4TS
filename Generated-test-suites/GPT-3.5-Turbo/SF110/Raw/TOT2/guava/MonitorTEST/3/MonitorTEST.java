import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorTest {

    private Monitor monitor;

    @Before
    public void setUp() {
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

    @Test
    public void testEnter_Timeout() {
        assertFalse(monitor.enter(1, TimeUnit.SECONDS));
    }

    @Test
    public void testEnterInterruptibly_Timeout() {
        assertThrows(InterruptedException.class, () -> {
            monitor.enterInterruptibly(1, TimeUnit.SECONDS);
        });
    }

    @Test
    public void testTryEnter() {
        assertTrue(monitor.tryEnter());
    }

    // More test cases for other methods can be added here

}