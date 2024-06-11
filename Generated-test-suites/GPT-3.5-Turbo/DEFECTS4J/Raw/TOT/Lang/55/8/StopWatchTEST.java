import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

    @Test
    public void testStart() {
        StopWatch watch = new StopWatch();
        
        // Test when stop watch is not running
        watch.start();
        assertEquals(watch.getTime(), 0);
        
        // Test when stop watch is already running
        try {
            watch.start();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testStop() {
        StopWatch watch = new StopWatch();
        
        // Test when stop watch is not running
        try {
            watch.stop();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
        
        // Test when stop watch is running
        watch.start();
        watch.stop();
        assertTrue(watch.getTime() > 0);
    }
    
    @Test
    public void testReset() {
        StopWatch watch = new StopWatch();
        
        // Test when stop watch is not running
        watch.reset();
        assertEquals(watch.getTime(), 0);
        
        // Test when stop watch is running
        watch.start();
        watch.reset();
        assertEquals(watch.getTime(), 0);
    }
    
    @Test
    public void testSplit() {
        StopWatch watch = new StopWatch();
        
        // Test when stop watch is not running
        try {
            watch.split();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
        
        // Test when stop watch is running
        watch.start();
        watch.split();
        assertTrue(watch.getSplitTime() > 0);
    }
    
    @Test
    public void testUnsplit() {
        StopWatch watch = new StopWatch();
        
        // Test when stop watch has not been split
        try {
            watch.unsplit();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
        
        // Test when stop watch has been split
        watch.start();
        watch.split();
        watch.unsplit();
        assertTrue(watch.getSplitTime() == 0);
    }
    
    @Test
    public void testSuspend() {
        StopWatch watch = new StopWatch();
        
        // Test when stop watch is not running
        try {
            watch.suspend();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
        
        // Test when stop watch is running
        watch.start();
        watch.suspend();
        assertTrue(watch.getTime() > 0);
    }
    
    @Test
    public void testResume() {
        StopWatch watch = new StopWatch();
        
        // Test when stop watch has not been suspended
        try {
            watch.resume();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
        
        // Test when stop watch has been suspended
        watch.start();
        watch.suspend();
        watch.resume();
        assertTrue(watch.getTime() > 0);
    }
    
    @Test
    public void testGetTime() {
        StopWatch watch = new StopWatch();
        
        // Test when stop watch is not running
        assertEquals(watch.getTime(), 0);
        
        // Test when stop watch is running
        watch.start();
        assertTrue(watch.getTime() > 0);
        
        // Test when stop watch is stopped
        watch.stop();
        assertTrue(watch.getTime() > 0);
        
        // Test when stop watch is suspended
        watch.start();
        watch.suspend();
        assertTrue(watch.getTime() > 0);
    }
    
    @Test
    public void testGetSplitTime() {
        StopWatch watch = new StopWatch();
        
        // Test when stop watch has not been split
        try {
            watch.getSplitTime();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
        
        // Test when stop watch has been split
        watch.start();
        watch.split();
        assertTrue(watch.getSplitTime() > 0);
    }
    
    @Test
    public void testToString() {
        StopWatch watch = new StopWatch();
        
        // Test when stop watch is not running
        assertEquals(watch.toString(), "00:00:00.000");
        
        // Test when stop watch is running
        watch.start();
        assertTrue(watch.toString().matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
        
        // Test when stop watch is stopped
        watch.stop();
        assertTrue(watch.toString().matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
        
        // Test when stop watch is suspended
        watch.start();
        watch.suspend();
        assertTrue(watch.toString().matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
    }
    
    @Test
    public void testToSplitString() {
        StopWatch watch = new StopWatch();
        
        // Test when stop watch has not been split
        try {
            watch.toSplitString();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
        
        // Test when stop watch has been split
        watch.start();
        watch.split();
        assertTrue(watch.toSplitString().matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
    }
}