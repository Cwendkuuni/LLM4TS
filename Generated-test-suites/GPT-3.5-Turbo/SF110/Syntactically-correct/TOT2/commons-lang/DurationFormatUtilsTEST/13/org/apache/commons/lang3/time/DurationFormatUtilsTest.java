package org.apache.commons.lang3.time;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        assertEquals("2:30:15.123", DurationFormatUtils.formatDurationHMS(9015123));
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals("P1970Y0M0DT2H30M15.123S", DurationFormatUtils.formatDurationISO(9015123));
    }

    @Test
    public void testFormatDuration() {
        assertEquals("2:30:15.123", DurationFormatUtils.formatDuration(9015123, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWithZeroPadding() {
        assertEquals("02:30:15.123", DurationFormatUtils.formatDuration(9015123, "HH:mm:ss.SSS", true));
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals(" 2 hours 30 minutes 15 seconds", DurationFormatUtils.formatDurationWords(9015123, false, false));
    }

    @Test
    public void testFormatPeriodISO() {
        assertEquals("P1970Y0M0DT2H30M15.123S", DurationFormatUtils.formatPeriodISO(0, 9015123));
    }

    @Test
    public void testFormatPeriod() {
        assertEquals("0 years 0 months 0 days 2 hours 30 minutes 15 seconds", DurationFormatUtils.formatPeriod(0, 9015123, "y 'years' M 'months' d 'days' H 'hours' m 'minutes' s 'seconds'"));
    }

}