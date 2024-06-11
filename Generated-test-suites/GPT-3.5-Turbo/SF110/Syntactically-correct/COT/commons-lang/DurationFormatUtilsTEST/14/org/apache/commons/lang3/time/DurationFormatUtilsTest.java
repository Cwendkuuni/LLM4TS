import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 3723000;
        String expected = "1:02:03.000";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 12345678;
        String expected = "P0Y0M0DT3H25M45.678S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 60000;
        String format = "mm:ss";
        String expected = "01:00";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithPadding() {
        long durationMillis = 1000;
        String format = "mm:ss.SSS";
        String expected = "00:01.000";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 86400000;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expected = "1 day";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0;
        long endMillis = 60000;
        String expected = "P0Y0M0DT0H1M0.000S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 0;
        long endMillis = 12345678;
        String format = "d 'days' H 'hours' m 'minutes' s 'seconds'";
        String expected = "142 days 21 hours 21 minutes 6 seconds";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithPadding() {
        long startMillis = 0;
        long endMillis = 1000;
        String format = "H 'hours' m 'minutes' s 'seconds' S 'milliseconds'";
        String expected = "0 hours 0 minutes 1 seconds 000 milliseconds";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
        assertEquals(expected, actual);
    }
}