package org.apache.commons.lang3.time;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        // Typical use case
        assertEquals("1:02:03.456", DurationFormatUtils.formatDurationHMS(3723456));
        // Edge cases
        assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0));
    }

    @Test
    public void testFormatDurationISO() {
        // Typical use case
        assertEquals("P0Y0M0DT1H2M3.456S", DurationFormatUtils.formatDurationISO(3723456));
        // Edge cases
        assertEquals("P0Y0M0DT0H0M0.000S", DurationFormatUtils.formatDurationISO(0));
    }

    @Test
    public void testFormatDuration() {
        // Typical use case
        assertEquals("1:02:03.456", DurationFormatUtils.formatDuration(3723456, "H:mm:ss.SSS"));
        // Edge cases
        assertEquals("0:00:00.000", DurationFormatUtils.formatDuration(0, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWords() {
        // Typical use case
        assertEquals(" 1 hour 2 minutes 3 seconds", DurationFormatUtils.formatDurationWords(3723456, false, false));
        // Edge cases
        assertEquals(" 1 hour 0 minutes 0 seconds", DurationFormatUtils.formatDurationWords(3600000, false, true));
    }

    @Test
    public void testFormatPeriodISO() {
        // Typical use case
        assertEquals("P0Y0M0DT1H2M3.456S", DurationFormatUtils.formatPeriodISO(0, 3723456));
        // Edge cases
        assertEquals("P0Y0M0DT0H0M0.000S", DurationFormatUtils.formatPeriodISO(0, 0));
    }

    @Test
    public void testFormatPeriod() {
        // Typical use case
        assertEquals("1:02:03.456", DurationFormatUtils.formatPeriod(0, 3723456, "H:mm:ss.SSS"));
        // Edge cases
        assertEquals("0:00:00.000", DurationFormatUtils.formatPeriod(0, 0, "H:mm:ss.SSS"));
    }

}