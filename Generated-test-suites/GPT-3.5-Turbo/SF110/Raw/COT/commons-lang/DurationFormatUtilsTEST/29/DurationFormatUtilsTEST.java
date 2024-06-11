
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1000;
        String expected = "0:00:01.000";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1000;
        String expected = "P1970Y1M1DT0H0M1.000S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1000;
        String format = "H:mm:ss.SSS";
        String expected = "0:00:01.000";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationPadWithZeros() {
        long durationMillis = 1000;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "0:00:01.000";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1000;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "0 days 0 hours 0 minutes 1 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0;
        long endMillis = 1000;
        String expected = "P1970Y1M1DT0H0M1.000S";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 0;
        long endMillis = 1000;
        String format = "H:mm:ss.SSS";
        String expected = "0:00:01.000";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodPadWithZeros() {
        long startMillis = 0;
        long endMillis = 1000;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "0:00:01.000";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        
        assertEquals(expected, result);
    }

}
