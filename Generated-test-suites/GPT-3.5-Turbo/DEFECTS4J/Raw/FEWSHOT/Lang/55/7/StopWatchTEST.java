import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {
    @Test
    public void testStartStop() {
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sw.stop();
        
        long time = sw.getTime();
        assertTrue(time >= 1000 && time <= 1100);
    }
    
    @Test
    public void testReset() {
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sw.stop();
        
        sw.reset();
        
        long time = sw.getTime();
        assertEquals(0, time);
    }
    
    @Test
    public void testStartSplitStop() {
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sw.split();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sw.stop();
        
        long time = sw.getTime();
        long splitTime = sw.getSplitTime();
        assertTrue(time >= 1500 && time <= 1700);
        assertTrue(splitTime >= 500 && splitTime <= 700);
    }
    
    @Test
    public void testResume() {
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sw.suspend();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sw.resume();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sw.stop();
        
        long time = sw.getTime();
        assertTrue(time >= 1500 && time <= 1700);
    }
}