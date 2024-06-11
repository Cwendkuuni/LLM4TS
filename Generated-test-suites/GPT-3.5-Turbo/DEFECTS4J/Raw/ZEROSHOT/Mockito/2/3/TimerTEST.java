import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    private Timer timer;

    @Test
    public void testIsCounting() {
        timer = new Timer(5000);
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testIsNotCounting() {
        timer = new Timer(5000);
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testIsNotCountingWhenTimerNotStarted() {
        timer = new Timer(5000);
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        timer = new Timer(5000);
        timer.start();
        Assert.assertNotEquals(-1, timer.startTime);
    }

}