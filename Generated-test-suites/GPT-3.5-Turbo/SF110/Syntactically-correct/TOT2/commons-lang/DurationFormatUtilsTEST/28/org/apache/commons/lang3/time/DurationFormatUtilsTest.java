package org.apache.commons.lang3.time;
import static org.junit.Assert.*;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS_basicCase() {
        long durationMillis = 3661000;
        String expected = "1:01:01.000";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO_basicCase() {
        long durationMillis = 3661000;
        String expected = "PT1H1M1S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration_basicCase() {
        long durationMillis = 3661000;
        String format = "H:mm:ss.SSS";
        String expected = "1:01:01.000";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords_basicCase() {
        long durationMillis = 90061000;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 days 1 hours 1 minutes 1 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO_basicCase() {
        long startMillis = 1554847200000L;
        long endMillis = 1554861600000L;
        String expected = "P0Y0M1DT4H0M0.000S";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod_basicCase() {
        long startMillis = 1554847200000L;
        long endMillis = 1554861600000L;
        String format = "d'T'days'H' hours 'm' minutes 's' seconds'";
        String expected = "1 days 4 hours 0 minutes 0 seconds";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod_invalidInput() {
        long startMillis = 1554861600000L;
        long endMillis = 1554847200000L;
        String format = "d'T'days'H' hours 'm' minutes 's' seconds'";
        assertThrows(IllegalArgumentException.class, () -> DurationFormatUtils.formatPeriod(startMillis, endMillis, format));
    }
}