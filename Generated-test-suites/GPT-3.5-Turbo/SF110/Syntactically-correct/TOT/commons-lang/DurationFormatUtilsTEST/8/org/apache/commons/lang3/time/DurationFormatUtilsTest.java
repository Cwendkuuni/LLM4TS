import org.junit.*;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 3678000L;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals("1:01:18.000", result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 3678000L;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals("PT1H1M18.000S", result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 3678000L;
        String format = "H:mm:ss.SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals("1:01:18.000", result);
    }

    @Test
    public void testFormatDurationWithPadding() {
        long durationMillis = 3678L;
        String format = "H:mm:ss.SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals("0:00:03.678", result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 300000L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = false;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals("5 minutes", result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1588099500000L;
        long endMillis = 1588149600000L;
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals("P0Y0M1DT6H5M0.000S", result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1588099500000L;
        long endMillis = 1588149600000L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds'";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals("1 days 6 hours 5 minutes 0 seconds", result);
    }

    @Test
    public void testFormatPeriodWithPadding() {
        long startMillis = 1588099500000L;
        long endMillis = 1588149600000L;
        String format = "MMM d, HH:mm:ss:SSS";
        boolean padWithZeros = true;
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros, TimeZone.getDefault());
        assertEquals("Apr 29, 23:11:40:000", result);
    }

    @Test
    public void testFormatPeriodWithDifferentTimezone() {
        long startMillis = 1588099500000L;
        long endMillis = 1588149600000L;
        String format = "MMM d, HH:mm:ss:SSS";
        boolean padWithZeros = false;
        TimeZone timezone = TimeZone.getTimeZone("Europe/London");
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros, timezone);
        assertEquals("Apr 30, 06:11:40:000", result);
    }

}