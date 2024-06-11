import static org.junit.Assert.*;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting_WhenTimerNotStarted_ReturnsFalse() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerStartedAndNotExpired_ReturnsTrue() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerStartedAndDurationExpired_ReturnsFalse() throws InterruptedException {
        Timer timer = new Timer(1000);
        timer.start();
        Thread.sleep(2000); // Wait for duration to expire
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_WhenStartCalled_StartTimeIsSet() {
        Timer timer = new Timer(1000);
        timer.start();
        assertNotEquals(-1, timer.getStartTime());
    }

}