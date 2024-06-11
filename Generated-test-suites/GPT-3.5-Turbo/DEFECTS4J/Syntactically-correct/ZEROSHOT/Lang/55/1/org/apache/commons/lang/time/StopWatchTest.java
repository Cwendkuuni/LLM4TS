import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @After
    public void tearDown() {
        stopWatch = null;
    }

    @Test
    public void testStart() {
        stopWatch.start();
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
        Assert.assertEquals(-1, stopWatch.stopTime);
        Assert.assertNotEquals(-1, stopWatch.startTime);
    }

    @Test
    public void testStartTwice() {
        stopWatch.start();
        try {
            stopWatch.start();
            Assert.fail("Should throw an IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Stopwatch already started. ", e.getMessage());
        }
    }

    @Test
    public void testStartAfterStop() {
        stopWatch.start();
        stopWatch.stop();
        try {
            stopWatch.start();
            Assert.fail("Should throw an IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Stopwatch must be reset before being restarted. ", e.getMessage());
        }
    }

    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        Assert.assertEquals(StopWatch.STATE_STOPPED, stopWatch.runningState);
        Assert.assertNotEquals(-1, stopWatch.stopTime);
    }

    @Test
    public void testStopWithoutStart() {
        try {
            stopWatch.stop();
            Assert.fail("Should throw an IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Stopwatch is not running. ", e.getMessage());
        }
    }

    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.cancel();
        Assert.assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.runningState);
        Assert.assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
        Assert.assertEquals(-1, stopWatch.startTime);
        Assert.assertEquals(-1, stopWatch.stopTime);
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        stopWatch.split();
        Assert.assertEquals(StopWatch.STATE_SPLIT, stopWatch.splitState);
    }

    @Test
    public void testSplitWithoutStart() {
        try {
            stopWatch.split();
            Assert.fail("Should throw an IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Stopwatch is not running. ", e.getMessage());
        }
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        Assert.assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
        Assert.assertEquals(-1, stopWatch.stopTime);
    }

    @Test
    public void testUnsplitWithoutSplit() {
        try {
            stopWatch.unsplit();
            Assert.fail("Should throw an IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Stopwatch has not been split. ", e.getMessage());
        }
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        Assert.assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.runningState);
        Assert.assertNotEquals(-1, stopWatch.stopTime);
    }

    @Test
    public void testSuspendWithoutStart() {
        try {
            stopWatch.suspend();
            Assert.fail("Should throw an IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Stopwatch must be running to suspend. ", e.getMessage());
        }
    }

    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
        Assert.assertEquals(-1, stopWatch.stopTime);
    }

    @Test
    public void testResumeWithoutSuspend() {
        try {
            stopWatch.resume();
            Assert.fail("Should throw an IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Stopwatch must be suspended to resume. ", e.getMessage());
        }
    }

    @Test
    public void testGetTime() {
        long startTime = System.currentTimeMillis();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        Assert.assertEquals(elapsedTime, stopWatch.getTime());
    }

    @Test
    public void testGetTimeWithoutStart() {
        Assert.assertEquals(0, stopWatch.getTime());
    }

    @Test
    public void testGetTimeWithoutStop() {
        stopWatch.start();
        Assert.assertNotEquals(-1, stopWatch.getTime());
    }

    @Test
    public void testGetSplitTime() {
        long startTime = System.currentTimeMillis();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        long expectedSplitTime = endTime - startTime - elapsedTime;
        Assert.assertEquals(expectedSplitTime, splitTime);
    }

    @Test
    public void testGetSplitTimeWithoutSplit() {
        try {
            stopWatch.getSplitTime();
            Assert.fail("Should throw an IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Stopwatch must be split to get the split time. ", e.getMessage());
        }
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
        String timeString = stopWatch.toString();
        Assert.assertNotNull(timeString);
        Assert.assertNotEquals("", timeString);
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
        String splitTimeString = stopWatch.toSplitString();
        Assert.assertNotNull(splitTimeString);
        Assert.assertNotEquals("", splitTimeString);
    }
}