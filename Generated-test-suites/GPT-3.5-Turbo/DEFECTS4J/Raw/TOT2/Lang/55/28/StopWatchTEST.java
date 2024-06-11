import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch watch = new StopWatch();
        watch.start();
        assertTrue(watch.getTime() >= 0);
    }

    @Test
    public void testStop() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.stop();
        assertTrue(watch.getTime() >= 0);
    }

    @Test
    public void testReset() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.reset();
        assertEquals(0, watch.getTime());
    }

    @Test
    public void testSplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        assertTrue(watch.getSplitTime() >= 0);
    }

    @Test
    public void testUnsplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        watch.unsplit();
        assertEquals(0, watch.getSplitTime());
    }

    @Test
    public void testSuspend() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        assertTrue(watch.getTime() >= 0);
    }

    @Test
    public void testResume() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        watch.resume();
        assertTrue(watch.getTime() >= 0);
    }

    @Test
    public void testEdgeCases() {
        StopWatch watch = new StopWatch();
        // Edge case: Start without calling start
        try {
            watch.getTime();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }

        // Edge case: Stop without calling start
        try {
            watch.stop();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }

        // Edge case: Resume without calling suspend
        try {
            watch.resume();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    @Test
    public void testErrorScenarios() {
        StopWatch watch = new StopWatch();
        // Error scenario: Double start
        try {
            watch.start();
            watch.start();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }

        // Error scenario: Stop without running
        try {
            watch.stop();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }

        // Error scenario: Unsplit without split
        try {
            watch.unsplit();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }

        // Error scenario: Resume without suspend
        try {
            watch.resume();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }
}