package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.ParseException;

public class StopWatchTest {

    @Test
    public void testUnstartedStopWatch() {
        StopWatch stopWatch = new StopWatch();
        assertEquals(0, stopWatch.getTime());
        assertEquals("0:00:00.000", stopWatch.toString());
    }

    @Test
    public void testStartAndStopStopWatch() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertEquals(-1, time);
        assertEquals("0:00:00.000", stopWatch.toString());

        stopWatch.start();
        stopWatch.stop();
        time = stopWatch.getTime();
        assertFalse(time < 0);
        String formattedTime = DurationFormatUtils.formatDurationHMS(time);
        assertEquals("0:00:0?..", formattedTime); // Suffix may vary based on actual elapsed time
        assertEquals(time, Long.parseLong(formattedTime.substring(0, formattedTime.lastIndexOf(".")).replaceAll(":", "")) * 1000);
        assertEquals(formattedTime, stopWatch.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwiceWithoutReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplitStopWatch() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(100);
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        long time = stopWatch.getTime();
        assertFalse(time < 0);
        assertTrue(splitTime > 0);
        String formattedTime = DurationFormatUtils.formatDurationHMS(time);
        String formattedSplitTime = DurationFormatUtils.formatDurationHMS(splitTime);
        assertEquals("0:00:0?..", formattedTime.substring(0, formattedTime.lastIndexOf(".")));
        assertEquals("0:00:01", formattedSplitTime);

        stopWatch.stop();
        formattedTime = DurationFormatUtils.formatDurationHMS(time);
        assertEquals(formattedTime, formattedSplitTime + "." + formattedTime.substring(formattedTime.lastIndexOf(".")));
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResumeStopWatch() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(100);
        stopWatch.suspend();
        Thread.sleep(100);
        stopWatch.resume();
        long time = stopWatch.getTime();
        assertFalse(time < 0);
        String formattedTime = DurationFormatUtils.formatDurationHMS(time);
        assertEquals("0:00:0?..", formattedTime.substring(0, formattedTime.lastIndexOf(".")));
        try {
            long splitTime = stopWatch.getSplitTime();
            fail("Expecting IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRun() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testResetStopWatch() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        long time = stopWatch.getTime();
        assertEquals(0, time);
        String formattedTime = stopWatch.toString();
        assertEquals("0:00:00.000", formattedTime);
    }
}