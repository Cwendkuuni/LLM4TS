import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @Test
    public void testStart() {
        stopWatch.start();
        assertTrue(stopWatch.getTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwiceWithoutReset() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStarting() {
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(stopWatch.getTime(), 0);
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStarting() {
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(stopWatch.getSplitTime(), 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplitting() {
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.getTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStarting() {
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspending() {
        stopWatch.resume();
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.getTime() > 0);
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplitting() {
        stopWatch.getSplitTime();
    }

    @Test
    public void testToString() {
        stopWatch.start();
        stopWatch.stop();
        assertNotNull(stopWatch.toString());
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        assertNotNull(stopWatch.toSplitString());
    }
}