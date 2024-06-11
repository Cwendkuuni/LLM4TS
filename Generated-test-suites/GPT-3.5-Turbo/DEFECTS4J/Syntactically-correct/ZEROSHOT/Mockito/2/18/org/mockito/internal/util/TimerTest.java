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
    public void testIsCountingWhenNotStarted() {
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenStarted() {
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingAfterDuration() throws InterruptedException {
        timer.start();
        Thread.sleep(1500); // Wait for more than the duration
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        timer.start();
        Assert.assertNotEquals(-1, timer.getStartTime());
    }
}

