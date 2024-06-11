import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {
    
    @Test
    public void testFormatDurationHMS() {
        long duration = 3661000;
        String expected = "1:01:01.000";
        assertEquals(expected, DurationFormatUtils.formatDurationHMS(duration));
    }
    
    @Test
    public void testFormatDurationISO() {
        long duration = 3661000;
        String expected = "P1970Y0M0DT1H1M1.000S";
        assertEquals(expected, DurationFormatUtils.formatDurationISO(duration));
    }
    
    @Test
    public void testFormatDuration() {
        long duration = 3661000;
        String format = "H:mm:ss.SSS";
        String expected = "1:01:01.000";
        assertEquals(expected, DurationFormatUtils.formatDuration(duration, format));
    }
    
    @Test
    public void testFormatDurationWords() {
        long duration = 3661000;
        boolean suppressLeadingZero = true;
        boolean suppressTrailingZero = false;
        String expected = "1 hour 1 minute 1 second";
        assertEquals(expected, DurationFormatUtils.formatDurationWords(duration, suppressLeadingZero, suppressTrailingZero));
    }
    
    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1000;
        long endMillis = 1000000;
        String expected = "P0Y0M0DT0H16M40.0S";
        assertEquals(expected, DurationFormatUtils.formatPeriodISO(startMillis, endMillis));
    }
    
    @Test
    public void testFormatPeriod() {
        long startMillis = 1000;
        long endMillis = 1000000;
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        boolean padWithZeros = true;
        String expected = "P0Y0M0DT0H16M40.0S";
        assertEquals(expected, DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros));
    }
}