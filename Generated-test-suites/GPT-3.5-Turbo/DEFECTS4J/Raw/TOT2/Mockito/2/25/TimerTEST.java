import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsNotCounting() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertNotEquals(-1, timer.startTime);
    }

    // Edge case: durationMillis is 0
    @Test
    public void testZeroDuration() {
        Timer timer = new Timer(0);
        assertFalse(timer.isCounting());
    }

    // Edge case: durationMillis is negative
    @Test
    public void testNegativeDuration() {
        Timer timer = new Timer(-1000);
        assertFalse(timer.isCounting());
    }

    // Error scenario: start() called before setting durationMillis
    @Test(expected = AssertionError.class)
    public void testStartWithoutDuration() {
        Timer timer = new Timer(-1);
        timer.start();
    }
}