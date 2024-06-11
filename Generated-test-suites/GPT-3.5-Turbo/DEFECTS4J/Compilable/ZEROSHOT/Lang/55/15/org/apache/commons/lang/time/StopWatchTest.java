package org.apache.commons.lang.time;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {

    private StopWatch watch;

    @Before
    public void setUp() {
        watch = new StopWatch();
    }

    @Test
    public void testStart() {
        assertEquals(0, watch.getTime());
        assertEquals(0, watch.getSplitTime());
        watch.start();
        assertTrue(watch.getTime() > 0);
        assertEquals(0, watch.getSplitTime());
        watch.start();
        assertTrue(watch.getTime() > 0);
        assertEquals(0, watch.getSplitTime());
    }

    @Test
    public void testStop() {
        watch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.stop();
        assertTrue(watch.getTime() > 0);
        assertEquals(0, watch.getSplitTime());
    }

    @Test
    public void testReset() {
        watch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.stop();
        assertTrue(watch.getTime() > 0);
        assertEquals(0, watch.getSplitTime());
        watch.reset();
        assertEquals(0, watch.getTime());
        assertEquals(0, watch.getSplitTime());
    }

    @Test
    public void testSplit() {
        watch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.split();
        assertTrue(watch.getTime() > 0);
        assertTrue(watch.getSplitTime() > 0);
    }

    @Test
    public void testUnsplit() {
        watch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.split();
        assertTrue(watch.getTime() > 0);
        assertTrue(watch.getSplitTime() > 0);
        watch.unsplit();
        assertTrue(watch.getTime() > 0);
        assertEquals(0, watch.getSplitTime());
    }

    @Test
    public void testSuspend() {
        watch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.suspend();
        assertTrue(watch.getTime() > 0);
        assertEquals(0, watch.getSplitTime());
    }

    @Test
    public void testResume() {
        watch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.suspend();
        assertTrue(watch.getTime() > 0);
        assertEquals(0, watch.getSplitTime());
        watch.resume();
        assertTrue(watch.getTime() > 0);
        assertEquals(0, watch.getSplitTime());
    }

    @Test
    public void testToString() {
        watch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.stop();
        String timeString = watch.toString();
        assertNotNull(timeString);
        assertTrue(timeString.matches("\\d{2}:\\d{2}:\\d{2}.\\d{1,3}"));
    }

    @Test
    public void testToSplitString() {
        watch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.split();
        String splitTimeString = watch.toSplitString();
        assertNotNull(splitTimeString);
        assertTrue(splitTimeString.matches("\\d{2}:\\d{2}:\\d{2}.\\d{1,3}"));
    }

}