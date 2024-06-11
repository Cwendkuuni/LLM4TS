import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {
    
    @Test
    public void testFormatDurationHMS() {
        long duration = 0;
        String expected = "0:00:00.000";
        String actual = DurationFormatUtils.formatDurationHMS(duration);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDurationISO() {
        long duration = 0;
        String expected = "P1970Y1M1DT0H0M0.0S";
        String actual = DurationFormatUtils.formatDurationISO(duration);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDuration() {
        long duration = 0;
        String format = "H:mm:ss.SSS";
        String expected = "0:00:00.000";
        String actual = DurationFormatUtils.formatDuration(duration, format);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDurationWithPadZeros() {
        long duration = 0;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "0:00:00.000";
        String actual = DurationFormatUtils.formatDuration(duration, format, padWithZeros);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long duration = 0;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "0 days 0 hours 0 minutes 0 seconds";
        String actual = DurationFormatUtils.formatDurationWords(duration, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0;
        long endMillis = 0;
        String expected = "P1970Y1M1DT0H0M0.0S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatPeriod() {
        long startMillis = 0;
        long endMillis = 0;
        String format = "H:mm:ss.SSS";
        String expected = "0:00:00.000";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatPeriodWithPadZeros() {
        long startMillis = 0;
        long endMillis = 0;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "0:00:00.000";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        assertEquals(expected, actual);
    }
    
}