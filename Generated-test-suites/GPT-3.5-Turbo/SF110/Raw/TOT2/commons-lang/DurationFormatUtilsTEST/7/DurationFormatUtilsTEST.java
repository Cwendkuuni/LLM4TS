import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {
    
    @Test
    public void testFormatDurationHMS() {
        long duration = 3661000L; // 1 hour, 1 minute, 1 second
        String expected = "1:01:01.000";
        assertEquals(expected, DurationFormatUtils.formatDurationHMS(duration));
    }
    
    @Test
    public void testFormatDurationISO() {
        long duration = 172800000L; // 2 days
        String expected = "P0000Y0M2DT00H00M00.000S";
        assertEquals(expected, DurationFormatUtils.formatDurationISO(duration));
    }
    
    @Test
    public void testFormatDuration() {
        long duration = 3661000L; // 1 hour, 1 minute, 1 second
        String format = "H:mm:ss.SSS";
        String expected = "1:01:01.000";
        assertEquals(expected, DurationFormatUtils.formatDuration(duration, format));
    }
    
    @Test
    public void testFormatDurationWords() {
        long duration = 3661000L; // 1 hour, 1 minute, 1 second
        String expected = " 1 hour 1 minute 1 second";
        assertEquals(expected, DurationFormatUtils.formatDurationWords(duration, false, false));
    }
    
    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0L;
        long endMillis = 86400000L; // 1 day
        String expected = "P0000Y0M1DT00H00M00.000S";
        assertEquals(expected, DurationFormatUtils.formatPeriodISO(startMillis, endMillis));
    }
    
    @Test
    public void testFormatPeriod() {
        long startMillis = 0L;
        long endMillis = 3672000L; // 1 hour, 1 minute, 12 seconds
        String format = "H:mm:ss.SSS";
        String expected = "1:01:12.000";
        assertEquals(expected, DurationFormatUtils.formatPeriod(startMillis, endMillis, format));
    }
    
    @Test
    public void testLexx() {
        String format = "H:mm:ss.SSS";
        DurationFormatUtils.Token[] tokens = DurationFormatUtils.lexx(format);
        assertNotNull(tokens);
        assertEquals(7, tokens.length);
    }
    
    @Test
    public void testPaddedValue() {
        long value = 5L;
        boolean padWithZeros = true;
        int count = 2;
        String expected = "05";
        assertEquals(expected, DurationFormatUtils.paddedValue(value, padWithZeros, count));
    }
}