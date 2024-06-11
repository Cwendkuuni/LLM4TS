
import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    private static final long DURATION = 1000; // Duration in milliseconds

    @Test
    public void testIsCounting_TrueWhenStarted() {
        Timer timer = new Timer(DURATION);
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_FalseWhenNotStarted() {
        Timer timer = new Timer(DURATION);
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_FalseAfterDuration() throws InterruptedException {
        Timer timer = new Timer(DURATION);
        timer.start();
        Thread.sleep(DURATION + 100); // Wait for duration + buffer time
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(DURATION);
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }
    
    @Test
    public void testStartTwice() {
        Timer timer = new Timer(DURATION);
        timer.start();
        long startTime1 = timer.startTime;
        timer.start();
        long startTime2 = timer.startTime;
        Assert.assertNotEquals(startTime1, startTime2);
    }
}
