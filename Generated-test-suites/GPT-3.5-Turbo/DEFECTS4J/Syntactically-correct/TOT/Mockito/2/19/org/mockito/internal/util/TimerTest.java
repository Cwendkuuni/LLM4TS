import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000);
    }

    @Test
    public void isCounting_WhenTimerHasNotStarted_ReturnsFalse() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void isCounting_WhenTimerIsStillCounting_ReturnsTrue() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void isCounting_WhenTimerHasExpired_ReturnsFalse() throws InterruptedException {
        timer.start();
        Thread.sleep(1000); // Wait for timer to expire
        assertFalse(timer.isCounting());
    }

    @Test
    public void start_TimerShouldStart() {
        timer.start();
        assertTrue(timer.isCounting());
    }

}