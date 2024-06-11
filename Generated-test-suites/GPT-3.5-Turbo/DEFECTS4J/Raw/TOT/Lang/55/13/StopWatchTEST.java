import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        sleep(1000); // Wait for 1 second
        stopWatch.stop();
        Assert.assertTrue(stopWatch.getTime() >= 1000);
    }

    @Test
    public void testResumeAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        sleep(1000); // Wait for 1 second
        stopWatch.suspend();
        sleep(1000); // Wait for another 1 second
        stopWatch.resume();
        sleep(1000); // Wait for another 1 second
        stopWatch.stop();
        Assert.assertTrue(stopWatch.getTime() >= 2000);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        sleep(1000); // Wait for 1 second
        stopWatch.split();
        sleep(1000); // Wait for another 1 second
        stopWatch.unsplit();
        Assert.assertTrue(stopWatch.getTime() >= 1000);
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}