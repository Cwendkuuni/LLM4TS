import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerConstructor() {
        Timer timer = new Timer(1000);
        Assert.assertNotEquals(-1, timer.getStartTime());
    }

    @Test
    public void testIsCounting_TimerNotStarted() {
        Timer timer = new Timer(1000);
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_TimerRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timer.getStartTime();
        Assert.assertTrue(timer.isCounting());
        Assert.assertTrue(elapsedTime <= timer.getDurationMillis());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_TimerExpired() {
        Timer timer = new Timer(1000);
        timer.start();
        long durationMillis = timer.getDurationMillis();
        long startTime = timer.getStartTime();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        Assert.assertFalse(timer.isCounting());
        Assert.assertFalse(elapsedTime <= durationMillis);
    }

    @Test
    public void testStart_StartTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        Assert.assertEquals(timer.getStartTime(), currentTime - 1000);
    }

    @Test(expected = IllegalStateException.class)
    public void testStart_TimerAlreadyRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

}