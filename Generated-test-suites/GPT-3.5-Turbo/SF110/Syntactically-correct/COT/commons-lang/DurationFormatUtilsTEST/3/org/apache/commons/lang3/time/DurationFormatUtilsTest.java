
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 3661000;
        String expected = "1:01:01.000";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 3661000;
        String expected = "PT1H1M1.000S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 3661000;
        String format = "H:mm:ss.SSS";
        String expected = "1:01:01.000";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithPadding() {
        long durationMillis = 3661000;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "01:01:01.000";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 3690000;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 hours 1 minutes 30 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1584902400000L;
        long endMillis = 1584906000000L;
        String expected = "P0Y0M0DT1H0M0.000S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1584902400000L;
        long endMillis = 1584906000000L;
        String format = "H:mm:ss.SSS";
        String expected = "1:00:00.000";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithPadding() {
        long startMillis = 1584902400000L;
        long endMillis = 1584906000000L;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "01:00:00.000";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        assertEquals(expected, actual);
    }

}
