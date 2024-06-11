import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MonitorTest {
    private Monitor monitor;

    @Before
    public void setup() {
        monitor = new Monitor();
    }

    // Test enter()
    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(monitor.isOccupied());
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    // Test enterInterruptibly()
    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enterInterruptibly();
        assertTrue(monitor.isOccupied());
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    // Test enter(long time, TimeUnit unit)
    @Test
    public void testEnterWithTimeout() {
        boolean entered = monitor.enter(1, TimeUnit.SECONDS);
        assertTrue(entered);
        assertTrue(monitor.isOccupied());
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    // Test enterInterruptibly(long time, TimeUnit unit)
    @Test
    public void testEnterInterruptiblyWithTimeout() throws InterruptedException {
        boolean entered = monitor.enterInterruptibly(1, TimeUnit.SECONDS);
        assertTrue(entered);
        assertTrue(monitor.isOccupied());
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    // Test tryEnter()
    @Test
    public void testTryEnter() {
        boolean entered = monitor.tryEnter();
        assertTrue(entered);
        assertTrue(monitor.isOccupied());
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    // TODO: Write more tests to cover all methods in the Monitor class

}