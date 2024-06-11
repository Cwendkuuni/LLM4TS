package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch watch = new StopWatch();
        watch.start();
        // Do some work here...
        watch.stop();
        long actual = watch.getTime();
        long expected = TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - watch.getStartTime());
        assertEquals(expected, actual);
        assertEquals(DurationFormatUtils.formatDurationHMS(actual), watch.toString());
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split(); // Record split time
        // Do some work here...
        watch.stop();
        long actualTotal = watch.getTime();
        long expectedTotal = TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - watch.getStartTime());
        assertEquals(expectedTotal, actualTotal);

        watch.reset();
        watch.start();
        watch.split(); // Record split time
        watch.unsplit(); // Remove split
        // Do some work here...
        watch.stop();
        long actualSplit = watch.getSplitTime();
        long expectedSplit = TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - watch.getStartTime());
        assertEquals(expectedSplit, actualSplit);
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        // Do some work here...
        watch.resume();
        // Do some more work here...
        watch.stop();
        long actualTotal = watch.getTime();
        long expectedTotal = TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - watch.getStartTime()) + watch.getSuspendedTime();
        assertEquals(expectedTotal, actualTotal);
        assertFalse(watch.isRunning());
    }

    @Test
    public void testIllegalOperations() {
        StopWatch watch = new StopWatch();
        watch.start();
        assertThrows(IllegalStateException.class, () -> watch.split());
        watch.split();
        assertThrows(IllegalStateException.class, () -> watch.split());
        assertThrows(IllegalStateException.class, () -> watch.stop());
        assertThrows(IllegalStateException.class, () -> watch.resume());
        assertThrows(IllegalStateException.class, () -> watch.unsplit());
        watch.reset();
    }

    @Test
    public void testReset() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        watch.stop();
        watch.reset();
        assertNull(watch.getStartTime());
        assertNull(watch.getStopTime());
        assertNull(watch.getSplitTime());
    }
}