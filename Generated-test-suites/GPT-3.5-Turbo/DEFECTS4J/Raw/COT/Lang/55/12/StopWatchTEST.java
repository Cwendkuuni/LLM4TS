import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {
    
    private StopWatch stopWatch;
    
    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @Test
    public void startShouldInitializeStartTimeAndSetRunningState() {
        stopWatch.start();
        assertNotEquals(-1, stopWatch.startTime);
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void startShouldThrowExceptionIfAlreadyRunning() {
        stopWatch.runningState = StopWatch.STATE_RUNNING;
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void startShouldThrowExceptionIfAlreadyStopped() {
        stopWatch.runningState = StopWatch.STATE_STOPPED;
        stopWatch.start();
    }

    @Test
    public void stopShouldSetStopTimeAndSetStoppedState() {
        stopWatch.start();
        stopWatch.stop();
        assertNotEquals(-1, stopWatch.stopTime);
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void stopShouldThrowExceptionIfNotRunningOrSuspended() {
        stopWatch.stop();
    }

    @Test
    public void resetShouldClearInternalValuesAndResetToInitialState() {
        stopWatch.runningState = StopWatch.STATE_STOPPED;
        stopWatch.splitState = StopWatch.STATE_SPLIT;
        stopWatch.startTime = 123;
        stopWatch.stopTime = 456;
        
        stopWatch.reset();
        
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.runningState);
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
        assertEquals(-1, stopWatch.startTime);
        assertEquals(-1, stopWatch.stopTime);
    }

    @Test
    public void splitShouldSetStopTimeAndSetSplitState() {
        stopWatch.start();
        stopWatch.split();
        assertNotEquals(-1, stopWatch.stopTime);
        assertEquals(StopWatch.STATE_SPLIT, stopWatch.splitState);
    }

    @Test(expected = IllegalStateException.class)
    public void splitShouldThrowExceptionIfNotRunning() {
        stopWatch.split();
    }

    @Test
    public void unsplitShouldClearStopTimeAndResetSplitState() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(-1, stopWatch.stopTime);
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
    }

    @Test(expected = IllegalStateException.class)
    public void unsplitShouldThrowExceptionIfNotSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void suspendShouldSetStopTimeAndSetSuspendedState() {
        stopWatch.start();
        stopWatch.suspend();
        assertNotEquals(-1, stopWatch.stopTime);
        assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.runningState);
    }

    @Test(expected = IllegalStateException.class)
    public void suspendShouldThrowExceptionIfNotRunning() {
        stopWatch.suspend();
    }

    @Test
    public void resumeShouldAdjustStartTimeAndSetRunningState() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
        assertEquals(stopWatch.startTime + stopWatch.stopTime, System.currentTimeMillis(), 1);
    }

    @Test(expected = IllegalStateException.class)
    public void resumeShouldThrowExceptionIfNotSuspended() {
        stopWatch.resume();
    }

    @Test
    public void getTimeShouldReturnTotalTimeBetweenStartAndStopInMilliseconds() throws InterruptedException {
        stopWatch.start();
        Thread.sleep(100);
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= 100);
    }

    @Test
    public void getTimeShouldReturnZeroIfStopWatchNeverStarted() {
        assertEquals(0, stopWatch.getTime());
    }

    @Test
    public void getTimeShouldReturnTimeBetweenStartAndCurrentMomentIfStopWatchStillRunning() throws InterruptedException {
        stopWatch.start();
        Thread.sleep(100);
        assertTrue(stopWatch.getTime() >= 100);
    }

    @Test(expected = RuntimeException.class)
    public void getTimeShouldThrowExceptionIfIllegalRunningState() {
        stopWatch.runningState = -1;
        stopWatch.getTime();
    }

    @Test
    public void getSplitTimeShouldReturnTimeBetweenStartAndLatestSplitInMilliseconds() throws InterruptedException {
        stopWatch.start();
        Thread.sleep(100);
        stopWatch.split();
        Thread.sleep(100);
        assertTrue(stopWatch.getSplitTime() >= 200);
    }

    @Test(expected = IllegalStateException.class)
    public void getSplitTimeShouldThrowExceptionIfNoSplitYet() {
        stopWatch.getSplitTime();
    }

    @Test
    public void toStringShouldReturnFormattedStringOfTotalTime() throws InterruptedException {
        stopWatch.start();
        Thread.sleep(1234);
        stopWatch.stop();
        String expected = "00:00:01.234";
        assertEquals(expected, stopWatch.toString());
    }

    @Test
    public void toSplitStringShouldReturnFormattedStringOfSplitTime() throws InterruptedException {
        stopWatch.start();
        Thread.sleep(1234);
        stopWatch.split();
        Thread.sleep(1000);
        String expected = "00:00:02.234";
        assertEquals(expected, stopWatch.toSplitString());
    }

}