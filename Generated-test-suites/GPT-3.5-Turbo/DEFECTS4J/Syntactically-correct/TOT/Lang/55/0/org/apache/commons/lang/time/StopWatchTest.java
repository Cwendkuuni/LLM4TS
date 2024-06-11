import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    // Test start() method
    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    // Test stop() method
    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000); // Sleep for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= 1000);
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
    }

    // Test reset() method
    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    }

    // Test split() method
    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(500); // Sleep for 0.5 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        try {
            Thread.sleep(500); // Sleep for 0.5 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long splitTime = stopWatch.getSplitTime();
        assertTrue(splitTime > 500 && splitTime < 1000);
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
    }

    // Test unsplit() method
    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(500); // Sleep for 0.5 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    // Test suspend() method
    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(500); // Sleep for 0.5 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.suspend();
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
    }

    // Test resume() method
    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(500); // Sleep for 0.5 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.suspend();
        try {
            Thread.sleep(500); // Sleep for 0.5 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.resume();
        assertTrue(stopWatch.getTime() > 1000 && stopWatch.getTime() < 1500);
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }

    // Test getTime() method
    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000); // Sleep for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time = stopWatch.getTime();
        assertTrue(time >= 1000 && time < 1500);
    }

    // Test getSplitTime() method
    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(500); // Sleep for 0.5 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        try {
            Thread.sleep(500); // Sleep for 0.5 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long splitTime = stopWatch.getSplitTime();
        assertTrue(splitTime > 500 && splitTime < 1000);
    }

    // Test toString() method
    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000); // Sleep for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String timeString = stopWatch.toString();
        assertTrue(timeString.matches("\\d{2}:\\d{2}:\\d{2}.\\d{3}"));
    }

    // Test toSplitString() method
    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(500); // Sleep for 0.5 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        try {
            Thread.sleep(500); // Sleep for 0.5 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String splitTimeString = stopWatch.toSplitString();
        assertTrue(splitTimeString.matches("\\d{2}:\\d{2}:\\d{2}.\\d{3}"));
    }
}