import org.apache.commons.lang3.time.StopWatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @After
    public void tearDown() {
        stopWatch.reset();
    }

    @Test
    public void testStartAndStop() {
        stopWatch.start();
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = 0; // The test does not last long enough to measure a meaningful time
        assertEquals("Time should be 0", expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplitAndUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        long actualSplitTime = stopWatch.getSplitTime();
        long expectedSplitTime = 0; // The test does not last long enough to measure a meaningful time
        assertEquals("Split time should be 0", expectedSplitTime, actualSplitTime);

        stopWatch.unsplit();
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = expectedSplitTime; // The split time should be included in the total time
        assertEquals("Time should be split time", expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResume() {
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long pauseTime = System.currentTimeMillis() - startTime;
        stopWatch.resume();
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = pauseTime;
        assertEquals("Time should be pause time", expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() {
        stopWatch.suspend();
    }

    @Test
    public void testToStringAndToSplitString() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        String actualToString = stopWatch.toString();
        String expectedToString = "0m:0s:0.000ms";
        String actualToSplitString = stopWatch.toSplitString();
        String expectedToSplitString = "0m:0s:0.000ms";
        assertEquals("ToString should be correct", expectedToString, actualToString);
        assertEquals("ToSplitString should be correct", expectedToSplitString, actualToSplitString);
    }
}