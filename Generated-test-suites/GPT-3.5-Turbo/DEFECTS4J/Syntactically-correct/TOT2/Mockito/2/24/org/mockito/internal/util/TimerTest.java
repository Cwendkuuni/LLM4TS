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
    public void testIsCounting_WhenTimerNotStarted_ExpectFalse() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_WhenCalled_SetStartTime() {
        timer.start();
        assertNotNull(timer.startTime);
    }

    @Test
    public void testIsCounting_WhenTimerStartedAndTimeHasNotElapsed_ExpectTrue() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerStartedAndTimeHasElapsed_ExpectFalse() throws InterruptedException {
        timer.start();
        Thread.sleep(6000); // Wait for timer to elapse
        assertFalse(timer.isCounting());
    }

}