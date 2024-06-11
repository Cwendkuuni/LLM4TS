import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        String expectedTime = "0:0:0.0"; // initial time is zero
        String actualTimeString = DurationFormatUtils.formatDurationHMS(time);
        assertEquals(expectedTime, actualTimeString);
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        String expectedSplitTimeString = "0:0:0.0";
        String expectedTimeString = "0:0:0.0";
        String splitTimeString = DurationFormatUtils.formatDurationHMS(splitTime);
        String timeString = DurationFormatUtils.formatDurationHMS(time);
        assertEquals(expectedSplitTimeString, splitTimeString);
        assertEquals(expectedTimeString, timeString);
    }

    @Test (expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        long pauseTime = System.currentTimeMillis() - stopWatch.getStartTime();
        stopWatch.resume();
        long time = stopWatch.getTime();
        long expectedTime = time - pauseTime;
        String expectedTimeString = DurationFormatUtils.formatDurationHMS(expectedTime);
        String actualTimeString = DurationFormatUtils.formatDurationHMS(time);
        assertEquals(expectedTimeString, actualTimeString);
    }
}