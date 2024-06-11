import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {

    private StopWatch stopwatch;

    @Before
    public void setUp() {
        stopwatch = new StopWatch();
    }

    @Test
    public void testStartAndStop() {
        stopwatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopwatch.stop();

        long elapsed = stopwatch.getTime();
        assertEquals(1000, elapsed, 100);
    }

    @Test
    public void testReset() {
        stopwatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopwatch.stop();

        stopwatch.reset();
        long elapsed = stopwatch.getTime();
        assertEquals(0, elapsed);
    }

    @Test
    public void testSplitAndUnsplit() {
        stopwatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopwatch.split();

        long splitTime = stopwatch.getSplitTime();
        assertEquals(1000, splitTime, 100);

        stopwatch.unsplit();
        long elapsed = stopwatch.getTime();
        assertEquals(1000, elapsed, 100);
    }

    @Test
    public void testSuspendAndResume() {
        stopwatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopwatch.suspend();

        stopwatch.resume();
        long elapsed = stopwatch.getTime();
        assertEquals(1000, elapsed, 100);
    }
}