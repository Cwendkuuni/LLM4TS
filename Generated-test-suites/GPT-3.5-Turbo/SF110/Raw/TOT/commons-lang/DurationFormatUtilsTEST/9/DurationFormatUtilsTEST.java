import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {
    
    @Test
    public void testFormatDurationHMS_typical() {
        long durationMillis = 3661000;
        String expected = "1:01:01.000";
        
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatDurationISO_typical() {
        long durationMillis = 3661000;
        String expected = "P0Y0M0DT1H1M1.000S";
        
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatDuration_typical() {
        long durationMillis = 3661000;
        String format = "H:mm:ss.SSS";
        String expected = "1:01:01.000";
        
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatDuration_padWithZeros() {
        long durationMillis = 3661000;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "01:01:01.000";
        
        String result = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatDurationWords_typical() {
        long durationMillis = 90061000;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 days 1 hours 1 minutes 1 seconds";
        
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatPeriodISO_typical() {
        long startMillis = 1577836800000L;
        long endMillis = 1577923199000L;
        String expected = "P0Y0M1DT23H59M59.000S";
        
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatPeriod_typical() {
        long startMillis = 1577836800000L;
        long endMillis = 1577923199000L;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "23:59:59.000";
        
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        
        assertEquals(expected, result);
    }
}