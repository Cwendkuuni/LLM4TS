import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimerTest {

    private Timer timer;

    @Before
    public void setup() {
        timer = new Timer(5000);
    }

    @Test
    public void testIsCounting_WhenTimerIsNotStarted_ShouldReturnFalse() {
        boolean counting = timer.isCounting();
        Assert.assertFalse(counting);
    }

    @Test
    public void testIsCounting_WhenTimerIsStartedAndNotExpired_ShouldReturnTrue() {
        timer.start();
        boolean counting = timer.isCounting();
        Assert.assertTrue(counting);
    }

    @Test
    public void testIsCounting_WhenTimerIsStartedAndExpired_ShouldReturnFalse() throws InterruptedException {
        timer.start();
        Thread.sleep(6000);
        boolean counting = timer.isCounting();
        Assert.assertFalse(counting);
    }

    @Test
    public void testStart_ShouldInitializeStartTime() {
        timer.start();
        long startTime = timer.getStartTime();
        Assert.assertNotEquals(-1, startTime);
    }
}