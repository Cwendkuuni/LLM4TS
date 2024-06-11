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
    public void testIsCounting_WhenTimerStarted_ReturnsTrue() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerNotStarted_ReturnsFalse() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerElapsedTimeExceedsDuration_ReturnsFalse() throws InterruptedException {
        timer.start();
        Thread.sleep(6000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_WhenStarted_CheckIfStartTimeSet() {
        timer.start();
        assertNotEquals(timer.getStartTime(), -1);
    }

    @Test
    public void testStart_WhenRestarted_CheckIfStartTimeUpdated() throws InterruptedException {
        timer.start();
        long initialStartTime = timer.getStartTime();
        Thread.sleep(2000);
        timer.start();
        assertNotEquals(initialStartTime, timer.getStartTime());
    }

}