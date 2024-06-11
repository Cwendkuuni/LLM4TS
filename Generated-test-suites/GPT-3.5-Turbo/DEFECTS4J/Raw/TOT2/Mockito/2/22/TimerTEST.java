import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    // Test case for isCounting() method
    @Test
    public void testIsCounting() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    // Test case for start() method
    @Test
    public void testStart() {
        Timer timer = new Timer(5000);
        timer.start();
        assertNotEquals(-1, timer.startTime);
    }

    // Edge case for isCounting() method
    @Test
    public void testIsCountingEdgeCase() {
        Timer timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());
    }

    // Exception handling for start() method
    @Test(expected = AssertionError.class)
    public void testStartException() {
        Timer timer = new Timer(5000);
        timer.start();
        timer.start();
    }
}