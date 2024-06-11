package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        assertEquals("1:00:00.000", DurationFormatUtils.formatDurationHMS(3600000));
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals("P1970Y0M1DT1H0M0.000S", DurationFormatUtils.formatDurationISO(86400000));
    }

    @Test
    public void testFormatDuration() {
        assertEquals("0:00:01.000", DurationFormatUtils.formatDuration(1000, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWithPadZeros() {
        assertEquals("0:00:00.500", DurationFormatUtils.formatDuration(500, "H:mm:ss.SSS", true));
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals("1 second", DurationFormatUtils.formatDurationWords(1000, false, false));
    }

    @Test
    public void testFormatDurationWordsSuppressElements() {
        assertEquals("", DurationFormatUtils.formatDurationWords(0, true, true));
    }

    @Test
    public void testFormatPeriodISO() {
        assertEquals("P1970Y0M0DT1H0M0.000S", DurationFormatUtils.formatPeriodISO(0, 3600000));
    }

    @Test
    public void testFormatPeriod() {
        assertEquals("0:01:00.000", DurationFormatUtils.formatPeriod(0, 60000, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        assertEquals("0:01:00.000", DurationFormatUtils.formatPeriod(0, 60000, "H:mm:ss.SSS", true, TimeZone.getTimeZone("GMT")));
    }

    @Test
    public void testLeex() {
        DurationFormatUtils.Token[] tokens = DurationFormatUtils.lexx("H:mm:ss.SSS");
        assertEquals(7, tokens.length);
    }

}