import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        this.stopWatch = new StopWatch();
    }

    @Test
    public void testStart() {
        stopWatch.start();
        assertTrue(stopWatch.getTime() >= 0);
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAgain() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeBeforeSuspend() {
        stopWatch.resume();
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopAgain() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(0, stopWatch.getTime());
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() >= 0);
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        stopWatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        stopWatch.resume();
    }

    @Test
    public void testSuspendAndResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.getTime() >= 0);
        stopWatch.stop();
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        assertTrue(stopWatch.getTime() >= 0);
        stopWatch.stop();
    }

    @Test
    public void testGetTimeWithoutStart() {
        assertEquals(0, stopWatch.getTime());
    }

    @Test(expected = RuntimeException.class)
    public void testGetTimeIllegalState() {
        stopWatch.getTime();
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() >= 0);
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        stopWatch.getSplitTime();
    }

    @Test
    public void testToString() {
        stopWatch.start();
        String timeString = stopWatch.toString();
        assertNotNull(timeString);
        assertTrue(timeString.matches("[0-9]+:[0-9]{2}:[0-9]{2}.[0-9]{3}"));
        stopWatch.stop();
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        assertNotNull(splitTimeString);
        assertTrue(splitTimeString.matches("[0-9]+:[0-9]{2}:[0-9]{2}.[0-9]{3}"));
        stopWatch.stop();
    }

}