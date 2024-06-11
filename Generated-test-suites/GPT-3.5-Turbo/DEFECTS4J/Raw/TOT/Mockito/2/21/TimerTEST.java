import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting_WhenTimerIsNotStarted_ReturnsFalse() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerIsStartedAndNotExpired_ReturnsTrue() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerIsStartedAndExpired_ReturnsFalse() throws InterruptedException {
        Timer timer = new Timer(1000);
        timer.start();
        Thread.sleep(2000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_WhenTimerIsNotStarted_StartTimeIsSet() {
        Timer timer = new Timer(1000);
        timer.start();
        assertNotEquals(-1, timer.startTime);
    }

    @Test
    public void testStart_WhenTimerIsAlreadyStarted_StartTimeIsUpdated() {
        Timer timer = new Timer(1000);
        timer.start();
        long previousStartTime = timer.startTime;
        timer.start();
        assertNotEquals(previousStartTime, timer.startTime);
    }

}