
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 100;
        String expected = "0:00:00.100";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1000;
        String expected = "PT1S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 60000;
        String format = "m:ss";
        String expected = "1:00";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration_padWithZeros() {
        long durationMillis = 6000;
        String format = "ss.SS";
        String expected = "06.00";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 3600000;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 hours 0 minutes 0 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords_suppressLeadingZeroElements() {
        long durationMillis = 86400000;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 days";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords_suppressTrailingZeroElements() {
        long durationMillis = 1800000;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = true;
        String expected = "0 hours 30 minutes";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0;
        long endMillis = 86400000;
        String expected = "P1D";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 0;
        long endMillis = 60000;
        String format = "m:ss";
        String expected = "1:00";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod_padWithZeros() {
        long startMillis = 0;
        long endMillis = 6000;
        String format = "ss.SS";
        String expected = "06.00";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
        assertEquals(expected, actual);
    }
}

