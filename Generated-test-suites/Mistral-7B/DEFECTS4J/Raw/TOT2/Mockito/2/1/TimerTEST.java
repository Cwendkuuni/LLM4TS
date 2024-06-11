import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.util.Timer;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerIsStopped() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.stop();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStart_WhenTimerIsAlreadyRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

    @Test
    public void testStart_WhenDurationIsZero() {
        Timer timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStart_WhenDurationIsNegative() {
        Timer timer = new Timer(-1);
        timer.start();
        fail("Expected IllegalArgumentException but got none");
    }
}