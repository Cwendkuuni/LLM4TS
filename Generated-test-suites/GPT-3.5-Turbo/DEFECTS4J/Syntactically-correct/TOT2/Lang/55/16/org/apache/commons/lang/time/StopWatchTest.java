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
        assertEquals(stopWatch.getTime(), 0);
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertNotEquals(stopWatch.getTime(), 0);
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
        assertEquals(stopWatch.getSplitTime(), stopWatch.getTime());
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertNotEquals(stopWatch.getSplitTime(), stopWatch.getTime());
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertNotEquals(stopWatch.getTime(), 0);
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(stopWatch.getTime(), stopWatch.getTime());
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        stopWatch.stop();
        assertNotEquals(stopWatch.getTime(), 0);
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        assertEquals(stopWatch.getSplitTime(), stopWatch.getTime());
    }

    @Test
    public void testToString() {
        stopWatch.start();
        stopWatch.stop();
        assertNotEquals(stopWatch.toString(), "");
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        assertEquals(stopWatch.toSplitString(), stopWatch.toString());
    }
}