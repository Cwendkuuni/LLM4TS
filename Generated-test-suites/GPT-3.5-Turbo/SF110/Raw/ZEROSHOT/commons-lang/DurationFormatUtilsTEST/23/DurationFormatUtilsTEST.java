import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 10000;
        String expectedResult = "0:00:10.000";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 10000;
        String expectedResult = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 10000;
        String format = "H:mm:ss.SSS";
        String expectedResult = "0:00:10.000";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationPadWithZeros() {
        long durationMillis = 10000;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String expectedResult = "0:00:10.000";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationWordsSuppressLeadingZeroElements() {
        long durationMillis = 10000;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = false;
        String expectedResult = "10 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationWordsSuppressTrailingZeroElements() {
        long durationMillis = 10000;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = true;
        String expectedResult = "0 days 0 hours 0 minutes 10 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 10000;
        long endMillis = 20000;
        String expectedResult = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 10000;
        long endMillis = 20000;
        String format = "H:mm:ss.SSS";
        String expectedResult = "0:00:10.000";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodPadWithZeros() {
        long startMillis = 10000;
        long endMillis = 20000;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String expectedResult = "0:00:10.000";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        assertEquals(expectedResult, result);
    }
    
}