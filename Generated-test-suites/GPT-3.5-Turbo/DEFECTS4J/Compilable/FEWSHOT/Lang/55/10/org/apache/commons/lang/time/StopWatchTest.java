package org.apache.commons.lang.time;
import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {
    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        assertTrue(stopWatch.getTime() >= 0);
    }
    
    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        
        assertTrue(stopWatch.getTime() >= 0);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
    }
    
    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        
        assertEquals(0, stopWatch.getTime());
    }
    
    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        
        assertTrue(stopWatch.getSplitTime() >= 0);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }
    
    @Test
    public void testSuspend() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(1000);
        stopWatch.suspend();
        
        assertTrue(stopWatch.getTime() < 1000);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }
    
    @Test
    public void testResume() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(1000);
        stopWatch.suspend();
        Thread.sleep(1000);
        stopWatch.resume();
        
        assertTrue(stopWatch.getTime() >= 1000);
    }
    
    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        
        assertTrue(stopWatch.getTime() >= 0);
    }
    
    @Test(expected = RuntimeException.class)
    public void testGetTimeIllegalState() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getTime();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getSplitTime();
    }
    
    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        
        assertNotNull(stopWatch.toString());
    }
    
    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        
        assertNotNull(stopWatch.toSplitString());
    }
}