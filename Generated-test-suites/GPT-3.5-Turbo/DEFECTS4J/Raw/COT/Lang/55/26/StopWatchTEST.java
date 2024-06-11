
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StopWatchTest {
    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @Test
    public void testStart() {
        stopWatch.start();
        assertTrue(stopWatch.getTime() == 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutStart() {
        stopWatch.stop();
    }

    @Test
    public void testStop() {
        stopWatch.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= 2000);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopTwice() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.stop();
    }
    
    @Test
    public void testReset() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertTrue(stopWatch.getTime() == 0);
    }

    @Test
    public void testSplit() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        assertTrue(stopWatch.getSplitTime() >= 1000);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        stopWatch.unsplit();
        assertTrue(stopWatch.getSplitTime() == 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        stopWatch.unsplit();
    }

    @Test
    public void testSuspend() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.suspend();
        assertTrue(stopWatch.getTime() < 1000);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutStart() {
        stopWatch.suspend();
    }

    @Test
    public void testResume() {
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
        assertTrue(stopWatch.getTime() >= 2000);
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        stopWatch.resume();
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        assertTrue(stopWatch.getTime() >= 1000);
    }

    @Test
    public void testToString() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String expected = "0:00:01.000";
        assertEquals(expected, stopWatch.toString());
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String expected = "0:00:01.000";
        assertEquals(expected, stopWatch.toSplitString());
    }
}

