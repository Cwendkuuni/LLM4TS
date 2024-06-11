import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCounting_Successful() {
        timer.start();
        boolean result = timer.isCounting();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCounting_Finished() throws InterruptedException {
        timer.start();
        Thread.sleep(2000);
        boolean result = timer.isCounting();
        Assert.assertFalse(result);
    }

    @Test
    public void testIsCounting_NotStarted() {
        boolean result = timer.isCounting();
        Assert.assertFalse(result);
    }

    @Test
    public void testStart_StartTimeSet() {
        timer.start();
        long startTime = timer.getStartTime();
        Assert.assertNotEquals(-1, startTime);
    }

    @Test
    public void testIsCounting_VeryShortDuration() throws InterruptedException {
        Timer shortDurationTimer = new Timer(10);
        shortDurationTimer.start();
        Thread.sleep(20);
        boolean result = shortDurationTimer.isCounting();
        Assert.assertFalse(result);
    }
}