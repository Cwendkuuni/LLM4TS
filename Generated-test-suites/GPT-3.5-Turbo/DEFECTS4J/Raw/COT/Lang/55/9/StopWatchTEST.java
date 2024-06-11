import org.junit.Test;

public class StopWatchTest {
    @Test
    public void testStartUnstarted() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // Assert that the running state is now STATE_RUNNING
        assert(stopWatch.getRunningState() == STATE_RUNNING);
    }
}