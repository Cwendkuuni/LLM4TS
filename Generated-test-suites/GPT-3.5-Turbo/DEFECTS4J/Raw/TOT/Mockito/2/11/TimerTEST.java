import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000);
    }

    @After
    public void tearDown() {
        timer = null;
    }

    @Test
    public void isCounting_WhenTimerStarted_ReturnsTrue() {
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void isCounting_WhenTimerNotStarted_ReturnsFalse() {
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void isCounting_WhenTimerExpired_ReturnsFalse() throws InterruptedException {
        timer.start();
        Thread.sleep(1100);
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void start_StartTimerWithCurrentTime() {
        timer.start();
        Assert.assertNotEquals(-1, timer.getStartTime());
    }
}