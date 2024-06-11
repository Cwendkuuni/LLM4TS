import static org.junit.Assert.*;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = stopWatch.getTime();
        assertTrue(time > 0);
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
    }
    
    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertTrue(time >= 1000);
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.isStopped());
    }
    
    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        stopWatch.reset();
        assertEquals(0, stopWatch.getTime());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
    }
    
    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        assertTrue(splitTime >= 1000);
    }
    
    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        assertTrue(time >= 1000);
    }
    
    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.suspend();
        long time = stopWatch.getTime();
        assertTrue(time >= 1000);
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.isSuspended());
    }
    
    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.suspend();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.resume();
        long time = stopWatch.getTime();
        assertTrue(time >= 2000);
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
    }
    
    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time = stopWatch.getTime();
        assertTrue(time >= 1000);
    }
    
    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long splitTime = stopWatch.getSplitTime();
        assertTrue(splitTime >= 2000);
    }
    
    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String timeString = stopWatch.toString();
        assertNotNull(timeString);
        assertFalse(timeString.isEmpty());
    }
    
    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String splitTimeString = stopWatch.toSplitString();
        assertNotNull(splitTimeString);
        assertFalse(splitTimeString.isEmpty());
    }
}