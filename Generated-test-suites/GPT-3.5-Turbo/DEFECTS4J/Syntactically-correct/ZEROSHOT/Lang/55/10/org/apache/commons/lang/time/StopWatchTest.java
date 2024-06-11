import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

    // create a StopWatch object
    StopWatch stopWatch = new StopWatch();

    @Test
    public void testStart() {
        // start the stopwatch
        stopWatch.start();

        // check if the stopwatch is in the running state
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenAlreadyRunning() {
        // set the stopwatch running state to running
        stopWatch.runningState = StopWatch.STATE_RUNNING;

        // try starting the stopwatch again
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenStopped() {
        // set the stopwatch running state to stopped
        stopWatch.runningState = StopWatch.STATE_STOPPED;

        // try starting the stopwatch again
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenSuspended() {
        // set the stopwatch running state to suspended
        stopWatch.runningState = StopWatch.STATE_SUSPENDED;

        // try starting the stopwatch again
        stopWatch.start();
    }

    @Test
    public void testStop() {
        // set the stopwatch running state to running
        stopWatch.runningState = StopWatch.STATE_RUNNING;

        // stop the stopwatch
        stopWatch.stop();

        // check if the stopwatch is in the stopped state
        Assert.assertEquals(StopWatch.STATE_STOPPED, stopWatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWhenNotRunning() {
        // set the stopwatch running state to unstarted
        stopWatch.runningState = StopWatch.STATE_UNSTARTED;

        // try stopping the stopwatch
        stopWatch.stop();
    }

    @Test
    public void testReset() {
        // set the stopwatch running state to running
        stopWatch.runningState = StopWatch.STATE_RUNNING;

        // reset the stopwatch
        stopWatch.reset();

        // check if the stopwatch is in the unstarted state
        Assert.assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.runningState);
    }

    @Test
    public void testSplit() {
        // set the stopwatch running state to running
        stopWatch.runningState = StopWatch.STATE_RUNNING;

        // split the stopwatch
        stopWatch.split();

        // check if the stopwatch is in the split state
        Assert.assertEquals(StopWatch.STATE_SPLIT, stopWatch.splitState);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWhenNotRunning() {
        // set the stopwatch running state to unstarted
        stopWatch.runningState = StopWatch.STATE_UNSTARTED;

        // try splitting the stopwatch
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        // set the stopwatch split state to split
        stopWatch.splitState = StopWatch.STATE_SPLIT;

        // unsplit the stopwatch
        stopWatch.unsplit();

        // check if the stopwatch is in the unsplit state
        Assert.assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWhenNotSplit() {
        // set the stopwatch split state to unsplit
        stopWatch.splitState = StopWatch.STATE_UNSPLIT;

        // try unsplitting the stopwatch
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        // set the stopwatch running state to running
        stopWatch.runningState = StopWatch.STATE_RUNNING;

        // suspend the stopwatch
        stopWatch.suspend();

        // check if the stopwatch is in the suspended state
        Assert.assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWhenNotRunning() {
        // set the stopwatch running state to unstarted
        stopWatch.runningState = StopWatch.STATE_UNSTARTED;

        // try suspending the stopwatch
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
        // set the stopwatch running state to suspended
        stopWatch.runningState = StopWatch.STATE_SUSPENDED;

        // resume the stopwatch
        stopWatch.resume();

        // check if the stopwatch is in the running state
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWhenNotSuspended() {
        // set the stopwatch running state to unstarted
        stopWatch.runningState = StopWatch.STATE_UNSTARTED;

        // try resuming the stopwatch
        stopWatch.resume();
    }

    @Test
    public void testGetTime() {
        // set the stopwatch running state to stopped
        stopWatch.runningState = StopWatch.STATE_STOPPED;

        // set the start time and stop time
        stopWatch.startTime = 100;
        stopWatch.stopTime = 200;

        // get the time on the stopwatch
        long time = stopWatch.getTime();

        // check if the time is correct
        Assert.assertEquals(100, time);
    }

    @Test
    public void testGetTimeWhenUnstarted() {
        // set the stopwatch running state to unstarted
        stopWatch.runningState = StopWatch.STATE_UNSTARTED;

        // get the time on the stopwatch
        long time = stopWatch.getTime();

        // check if the time is 0
        Assert.assertEquals(0, time);
    }

    @Test
    public void testGetTimeWhenRunning() {
        // set the stopwatch running state to running
        stopWatch.runningState = StopWatch.STATE_RUNNING;

        // set the start time
        stopWatch.startTime = 100;

        // get the time on the stopwatch
        long time = stopWatch.getTime();

        // check if the time is correct
        Assert.assertEquals(0, time);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTimeWhenIllegalState() {
        // set the stopwatch running state to invalid state
        stopWatch.runningState = -1;

        // try getting the time on the stopwatch
        stopWatch.getTime();
    }

    @Test
    public void testGetSplitTime() {
        // set the stopwatch split state to split
        stopWatch.splitState = StopWatch.STATE_SPLIT;

        // set the start time and stop time
        stopWatch.startTime = 100;
        stopWatch.stopTime = 200;

        // get the split time on the stopwatch
        long splitTime = stopWatch.getSplitTime();

        // check if the split time is correct
        Assert.assertEquals(100, splitTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWhenNotSplit() {
        // set the stopwatch split state to unsplit
        stopWatch.splitState = StopWatch.STATE_UNSPLIT;

        // try getting the split time on the stopwatch
        stopWatch.getSplitTime();
    }

    @Test
    public void testToString() {
        // set the stopwatch running state to stopped
        stopWatch.runningState = StopWatch.STATE_STOPPED;

        // set the start time and stop time
        stopWatch.startTime = 0;
        stopWatch.stopTime = 3687;

        // get the string representation of the stopwatch
        String timeString = stopWatch.toString();

        // check if the string representation is correct
        Assert.assertEquals("0:0:3.687", timeString);
    }

    @Test
    public void testToSplitString() {
        // set the stopwatch split state to split
        stopWatch.splitState = StopWatch.STATE_SPLIT;

        // set the start time and stop time
        stopWatch.startTime = 0;
        stopWatch.stopTime = 3687;

        // get the string representation of the split time on the stopwatch
        String splitTimeString = stopWatch.toSplitString();

        // check if the string representation is correct
        Assert.assertEquals("0:0:3.687", splitTimeString);
    }

}