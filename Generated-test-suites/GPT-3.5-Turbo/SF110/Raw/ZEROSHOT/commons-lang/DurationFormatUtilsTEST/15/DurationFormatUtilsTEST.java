package org.apache.commons.lang3.time;

import org.junit.*;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0L));
        assertEquals("0:00:01.000", DurationFormatUtils.formatDurationHMS(1000L));
        assertEquals("0:01:00.000", DurationFormatUtils.formatDurationHMS(60000L));
        assertEquals("1:00:00.000", DurationFormatUtils.formatDurationHMS(3600000L));
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals("P1970Y1M1DT0H0M0.000S", DurationFormatUtils.formatDurationISO(0L));
        assertEquals("P1970Y1M1DT0H0M1.000S", DurationFormatUtils.formatDurationISO(1000L));
        assertEquals("P1970Y1M1DT0H1M0.000S", DurationFormatUtils.formatDurationISO(60000L));
        assertEquals("P1970Y1M1DT1H0M0.000S", DurationFormatUtils.formatDurationISO(3600000L));
    }

    @Test
    public void testFormatDuration() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatDuration(0L, "H:mm:ss.SSS"));
        assertEquals("0:00:01.000", DurationFormatUtils.formatDuration(1000L, "H:mm:ss.SSS"));
        assertEquals("0:01:00.000", DurationFormatUtils.formatDuration(60000L, "H:mm:ss.SSS"));
        assertEquals("1:00:00.000", DurationFormatUtils.formatDuration(3600000L, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals("0 days 0 hours 0 minutes 0 seconds", DurationFormatUtils.formatDurationWords(0L, false, false));
        assertEquals("0 days 0 hours 0 minutes 1 second", DurationFormatUtils.formatDurationWords(1000L, false, false));
        assertEquals("0 days 0 hours 1 minute 0 seconds", DurationFormatUtils.formatDurationWords(60000L, false, false));
        assertEquals("0 days 1 hour 0 minutes 0 seconds", DurationFormatUtils.formatDurationWords(3600000L, false, false));
        assertEquals("1 day 0 hours 0 minutes 0 seconds", DurationFormatUtils.formatDurationWords(86400000L, false, false));
    }

    @Test
    public void testFormatPeriodISO() {
        assertEquals("P1970Y1M1DT0H0M0.000S", DurationFormatUtils.formatPeriodISO(0L, 0L));
        assertEquals("P1970Y1M1DT0H0M1.000S", DurationFormatUtils.formatPeriodISO(0L, 1000L));
        assertEquals("P1970Y1M1DT0H1M0.000S", DurationFormatUtils.formatPeriodISO(0L, 60000L));
        assertEquals("P1970Y1M1DT1H0M0.000S", DurationFormatUtils.formatPeriodISO(0L, 3600000L));
    }

    @Test
    public void testFormatPeriod() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatPeriod(0L, 0L, "H:mm:ss.SSS", true, TimeZone.getDefault()));
        assertEquals("0:00:01.000", DurationFormatUtils.formatPeriod(0L, 1000L, "H:mm:ss.SSS", true, TimeZone.getDefault()));
        assertEquals("0:01:00.000", DurationFormatUtils.formatPeriod(0L, 60000L, "H:mm:ss.SSS", true, TimeZone.getDefault()));
        assertEquals("1:00:00.000", DurationFormatUtils.formatPeriod(0L, 3600000L, "H:mm:ss.SSS", true, TimeZone.getDefault()));
    }
}