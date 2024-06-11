import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long duration = 3600000; // 1 hour in milliseconds
        String expected = "1:00:00.000";
        assertEquals(expected, DurationFormatUtils.formatDurationHMS(duration));
    }

    @Test
    public void testFormatDurationISO() {
        long duration = 86400000; // 1 day in milliseconds
        String expected = "P1970Y0M1DT0H0M0.0S";
        assertEquals(expected, DurationFormatUtils.formatDurationISO(duration));
    }

    @Test
    public void testFormatDuration() {
        long duration = 3661000; // 1 hour, 1 minute and 1 second in milliseconds
        String format = "H:mm:ss.SSS";
        String expected = "1:01:01.0";
        assertEquals(expected, DurationFormatUtils.formatDuration(duration, format));
    }

    @Test
    public void testFormatDurationWords() {
        long duration = 365000000; // 4 days, 4 hours and 20 minutes in milliseconds
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = " 4 days 4 hours 20 minutes 0 seconds";
        assertEquals(expected, DurationFormatUtils.formatDurationWords(duration, suppressLeadingZeroElements, suppressTrailingZeroElements));
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0;
        long endMillis = 31556952000; // 1 year in milliseconds
        String expected = "P1970Y1M0DT0H0M0.0S";
        assertEquals(expected, DurationFormatUtils.formatPeriodISO(startMillis, endMillis));
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 0;
        long endMillis = 90000000; // 1 day and 1 hour in milliseconds
        String format = "H:mm:ss.SSS";
        String expected = "24:00:00.0";
        assertEquals(expected, DurationFormatUtils.formatPeriod(startMillis, endMillis, format));
    }
}