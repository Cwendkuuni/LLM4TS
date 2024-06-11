import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Timer;

public class TimerTest {

    // Test case for the constructor with positive duration
    @Test
    public void testConstructor_PositiveDuration() {
        long durationMillis = 1000;
        Timer timer = new Timer(durationMillis);
        Assert.assertEquals(durationMillis, timer.getDurationMillis());
    }

    // Test case for the constructor with zero duration
    @Test
    public void testConstructor_ZeroDuration() {
        long durationMillis = 0;
        Timer timer = new Timer(durationMillis);
        Assert.assertEquals(durationMillis, timer.getDurationMillis());
    }

    // Test case for the constructor with negative duration
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeDuration() {
        long durationMillis = -1000;
        Timer timer = new Timer(durationMillis);
    }

    // Test case for isCounting when timer is still counting down
    @Test
    public void testIsCounting_True() {
        long durationMillis = 2000;
        Timer timer = new Timer(durationMillis);
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    // Test case for isCounting when timer has reached the end
    @Test
    public void testIsCounting_False() {
        long durationMillis = 1000;
        Timer timer = new Timer(durationMillis);
        timer.start();
        try {
            Thread.sleep(durationMillis + 100); // Wait for the duration to pass
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertFalse(timer.isCounting());
    }

    // Test case for start method
    @Test
    public void testStart() {
        long durationMillis = 500;
        Timer timer = new Timer(durationMillis);
        timer.start();
        Assert.assertNotEquals(-1, timer.getStartTime());
    }

}