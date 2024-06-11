import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(5000);
    }

    @Test
    public void testIsCounting_WhenTimerNotStarted_ReturnsFalse() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerStartedButNotExpired_ReturnsTrue() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerExpired_ReturnsFalse() throws InterruptedException {
        timer.start();
        Thread.sleep(5000); // Wait for timer to expire
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_SetsStartTimeToCurrentTime() {
        timer.start();
        assertNotEquals(-1, timer.getStartTime());
    }

}