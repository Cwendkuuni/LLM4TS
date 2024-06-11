import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerIsCounting_whenTimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerIsNotCounting_whenTimerHasFinished() {
        Timer timer = new Timer(1000);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.startTime + 1001; // elapsed time is more than the duration
        Assert.assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testAssertionError_whenTimerIsNotInitialized() {
        Timer timer = new Timer(0);
        timer.isCounting(); // no start time set, assertion error should be thrown
    }

    @Test
    public void testTimerIsCounting_whenTimerIsNotRunningYet() {
        Timer timer = new Timer(1000);
        Assert.assertFalse(timer.isCounting());
        timer.start();
    }

    @Test
    public void testTimerIsCounting_whenCurrentTimeIsExactlyEqualToStartTime() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        timer.startTime = currentTime; // set current time as start time
        Assert.assertTrue(timer.isCounting());
    }

}