import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {
    
    @Before
    public void setUp() {
        // Set up any necessary objects or data before each test
    }
    
    @Test
    public void testFormatDurationHMS() {
        // Test typical use case
        long durationMillis = 3661000L; // 1 hour, 1 minute, 1 second
        String expected = "1:01:01.000";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
        
        // Test edge case
        durationMillis = 0L; // 0 milliseconds
        expected = "0:00:00.000";
        result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
        
        // Test error scenario
        durationMillis = -1000L; // Negative duration
        expected = "-0:00:01.000";
        result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatDurationISO() {
        // Test typical use case
        long durationMillis = 3661000L; // 1 hour, 1 minute, 1 second
        String expected = "PT1H1M1.001S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
        
        // Test edge case
        durationMillis = 0L; // 0 milliseconds
        expected = "PT0S";
        result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
        
        // Test error scenario
        durationMillis = -1000L; // Negative duration
        expected = "-PT0.001S";
        result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatDuration() {
        // Test typical use case
        long durationMillis = 3661000L; // 1 hour, 1 minute, 1 second
        String format = "H:mm:ss.SSS";
        String expected = "1:01:01.001";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);
        
        // Test edge case
        durationMillis = 0L; // 0 milliseconds
        expected = "0:00:00.000";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);
        
        // Test error scenario
        durationMillis = -1000L; // Negative duration
        expected = "-0:00:01.000";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);
    }
    
    // Add test case for formatDuration with padWithZeros
    
    @Test
    public void testFormatDurationWords() {
        // Test typical use case
        long durationMillis = 3661000L; // 1 hour, 1 minute, 1 second
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "0 days 1 hours 1 minutes 1 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);
        
        // Test edge case
        durationMillis = 0L; // 0 milliseconds
        expected = "0 days 0 hours 0 minutes 0 seconds";
        result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);
        
        // Test error scenario
        durationMillis = -1000L; // Negative duration
        expected = "-0 days 0 hours 0 minutes 1 seconds";
        result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);
    }
    
    // Add test case for formatPeriodISO
    
    // Add test case for formatPeriod with padWithZeros and timezone
    
}