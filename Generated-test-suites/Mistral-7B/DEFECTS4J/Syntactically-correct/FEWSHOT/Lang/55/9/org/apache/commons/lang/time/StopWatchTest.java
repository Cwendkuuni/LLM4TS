package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.ParseException;

public class StopWatchTest {

    @Test
    public void testStartStop() {
        StopWatch watch = new StopWatch();

        watch.start();
        watch.stop();
        assertEquals(0, watch.getTime());

        watch.start();
        long startTime = System.currentTimeMillis();
        watch.stop();
        long elapsedTime = watch.getTime();
        assertTrue(elapsedTime > 0);
        assertEquals(elapsedTime, startTime - watch.getStartTime());

        watch.reset();
        watch.start();
        assertFalse(watch.isRunning());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        StopWatch watch = new StopWatch();
        watch.stop();
    }

    @Test
    public void testSplitUnsplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        long splitTime = watch.splitTime();
        watch.unsplit();
        assertNull(watch.getSplitTime());
        assertEquals(splitTime, watch.getTime());

        watch.reset();
        watch.start();
        watch.split();
        watch.split();
        assertEquals(0, watch.getSplitTime());

        watch.reset();
        watch.start();
        watch.split();
        long splitTime2 = watch.splitTime();
        watch.unsplit();
        assertEquals(splitTime2, watch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        StopWatch watch = new StopWatch();
        watch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitBeforeSplit() {
        StopWatch watch = new StopWatch();
        watch.unsplit();
    }

    @Test
    public void testSuspendResume() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        long pauseTime = System.currentTimeMillis();
        watch.resume();
        watch.stop();
        long elapsedTime = watch.getTime();
        long pauseDuration = System.currentTimeMillis() - pauseTime;
        assertTrue(elapsedTime > pauseDuration);
        assertEquals(elapsedTime - pauseDuration, watch.getStartTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() {
        StopWatch watch = new StopWatch();
        watch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeBeforeSuspend() {
        StopWatch watch = new StopWatch();
        watch.resume();
    }

    @Test
    public void testFormatStrings() {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.stop();

        String formattedTime = watch.toString();
        String formattedSplitTime = watch.toSplitString();

        try {
            long time = Long.parseLong(formattedTime.substring(0, formattedTime.indexOf(".")));
            long splitTime = Long.parseLong(formattedSplitTime.substring(0, formattedSplitTime.indexOf(".")));

            long totalTime = time + splitTime;
            String expectedTime = DurationFormatUtils.formatDurationHMS(totalTime);
            assertEquals(expectedTime, formattedTime);
            assertEquals(expectedTime, formattedSplitTime.substring(0, formattedSplitTime.indexOf(".")));

            long seconds = totalTime / 1000 % 60;
            long minutes = totalTime / (60 * 1000) % 60;
            long hours = totalTime / (60 * 60 * 1000);

            String expectedFormat = String.format("%d:%02d:%02d.%03d", hours, minutes, seconds, totalTime % 1000);
            assertEquals(expectedFormat, formattedTime);
            assertEquals(expectedFormat.substring(0, expectedFormat.length() - 3), formattedSplitTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}