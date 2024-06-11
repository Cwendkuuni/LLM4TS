import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 3661000L;
        String expected = "1:01:01.000";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 3661000L;
        String expected = "PT1H1M1.000S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 3661000L;
        String format = "H:mm:ss.SSS";
        String expected = "1:01:01.000";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationPadWithZeros() {
        long durationMillis = 3661000L;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "01:01:01.000";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 3661000L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "0 days 1 hours 1 minutes 1 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 3661000L;
        long endMillis = 7321000L;
        String expected = "PT2H1M1.000S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 3661000L;
        long endMillis = 7321000L;
        String format = "H:mm:ss.SSS";
        String expected = "2:01:01.000";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodPadWithZeros() {
        long startMillis = 3661000L;
        long endMillis = 7321000L;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "02:01:01.000";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        assertEquals(expected, actual);
    }
}