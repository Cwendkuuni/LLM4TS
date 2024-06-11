package org.apache.commons.lang3.time;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long duration = 3661000; // 1 hour, 1 minute, 1 second
        String expected = "1:01:01.000";
        String actual = DurationFormatUtils.formatDurationHMS(duration);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long duration = 3661000; // 1 hour, 1 minute, 1 second
        String expected = "PT1H1M1S";
        String actual = DurationFormatUtils.formatDurationISO(duration);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long duration = 3661000; // 1 hour, 1 minute, 1 second
        String format = "H:mm:ss.SSS";
        String expected = "1:01:01.000";
        String actual = DurationFormatUtils.formatDuration(duration, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long duration = 172800000; // 2 days
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expected = "2 days";
        String actual = DurationFormatUtils.formatDurationWords(duration, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0;
        long endMillis = 3661000; // 1 hour, 1 minute, 1 second
        String expected = "PT1H1M1S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 0;
        long endMillis = 3661000; // 1 hour, 1 minute, 1 second
        String format = "'H' hours 'm' minutes 's' seconds'";
        String expected = "1 hours 1 minutes 1 seconds";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, actual);
    }

}