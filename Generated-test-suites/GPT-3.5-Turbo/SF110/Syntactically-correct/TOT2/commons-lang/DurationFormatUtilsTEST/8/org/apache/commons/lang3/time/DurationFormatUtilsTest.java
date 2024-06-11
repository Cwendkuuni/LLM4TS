package org.apache.commons.lang3.time;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.TimeZone;

import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        assertEquals(DurationFormatUtils.formatDurationHMS(3661000), "1:01:01.000");
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals(DurationFormatUtils.formatDurationISO(3661000), "P1970Y1M1DT1H1M1.000S");
    }

    @Test
    public void testFormatDuration() {
        assertEquals(DurationFormatUtils.formatDuration(3661000, "H:mm:ss.SSS"), "1:01:01.000");
    }

    @Test
    public void testFormatDurationPadWithZeros() {
        assertEquals(DurationFormatUtils.formatDuration(60000, "H:mm:ss.SSS", true), "0:01:00.000");
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals(DurationFormatUtils.formatDurationWords(3661000, false, false), " 1 days 1 hours 1 minutes 1 seconds");
    }

    @Test
    public void testFormatPeriodISO() {
        assertEquals(DurationFormatUtils.formatPeriodISO(0, 60000), "P1970Y0M0DT0H1M0.000S");
    }

    @Test
    public void testFormatPeriod() {
        assertEquals(DurationFormatUtils.formatPeriod(0, 60000, "d' days 'H' hours 'm' minutes 's' seconds'"), "0 days 0 hours 1 minutes 0 seconds");
    }

    @Test
    public void testFormatPeriodPadWithZeros() {
        assertEquals(DurationFormatUtils.formatPeriod(0, 86400000, "d' days 'H' hours 'm' minutes 's' seconds'", true, TimeZone.getDefault()), "1 days 0 hours 0 minutes 0 seconds");
    }

    @Test
    public void testLexx() {
        DurationFormatUtils.Token[] tokens = DurationFormatUtils.lexx("H:mm:ss.SSS");
        assertEquals(tokens.length, 7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLexxUnmatchedQuote() {
        DurationFormatUtils.lexx("H:mm:ss.SSS'");
    }
}