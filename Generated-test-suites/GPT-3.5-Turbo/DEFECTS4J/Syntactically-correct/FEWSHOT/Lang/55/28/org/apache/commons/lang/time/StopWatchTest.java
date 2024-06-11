package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {
    
    private StopWatch stopWatch;
    
    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }
    
    @Test
    public void testStart() {
        stopWatch.start();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStartWhenRunning() {
        stopWatch.start();
        stopWatch.start();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStartAfterStop() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.start();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStartAfterReset() {
        stopWatch.start();
        stopWatch.reset();
        stopWatch.start();
    }
    
    @Test
    public void testStop() {
        stopWatch.start();
        stopWatch.stop();
        assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStopWhenNotRunning() {
        stopWatch.stop();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStopAfterStop() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
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
    
    @Test(expected = IllegalStateException.class)
    public void testSplitWhenNotRunning() {
        stopWatch.split();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSplitAfterSplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUnsplitWhenNotSplit() {
        stopWatch.unsplit();
    }
    
    @Test
    public void testUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testResumeWhenNotSuspended() {
        stopWatch.resume();
    }
    
    @Test
    public void testResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testResumeAfterResume() {
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.resume();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWhenNotSplit() {
        stopWatch.getSplitTime();
    }
    
    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        stopWatch.split();
        assertEquals(stopWatch.getStopTime() - stopWatch.getStartTime(), stopWatch.getSplitTime());
    }
    
    @Test
    public void testToString() {
        stopWatch.start();
        assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getTime()), stopWatch.toString());
    }
    
    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime()), stopWatch.toSplitString());
    }
}