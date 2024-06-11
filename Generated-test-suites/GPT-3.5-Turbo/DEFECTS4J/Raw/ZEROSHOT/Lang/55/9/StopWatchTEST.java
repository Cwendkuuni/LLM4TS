import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class StopWatchTest {

    private StopWatch stopWatch;

    @BeforeEach
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @AfterEach
    public void tearDown() {
        stopWatch.reset();
    }

    @Test
    public void testStart() {
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
    }

    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= 1000);
    }

    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() >= 1000);
    }

    @Test
    public void testToString() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        assertNotNull(stopWatch.toString());
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        assertNotNull(stopWatch.toSplitString());
    }
}