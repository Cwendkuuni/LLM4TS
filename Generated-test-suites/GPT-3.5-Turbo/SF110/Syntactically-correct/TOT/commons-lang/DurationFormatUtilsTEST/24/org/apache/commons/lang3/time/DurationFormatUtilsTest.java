import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long duration = 3600000; // 1 hour
        String expected = "1:00:00.000";
        assertEquals(expected, DurationFormatUtils.formatDurationHMS(duration));
    }
    
    @Test
    public void testFormatDurationISO() {
        long duration = 86400000; // 1 day
        String expected = "P0Y0M1DT00H00M00S";
        assertEquals(expected, DurationFormatUtils.formatDurationISO(duration));
    }
    
    @Test
    public void testFormatDuration() {
        long duration = 60000; // 1 minute
        String format = "m:ss";
        String expected = "1:00";
        assertEquals(expected, DurationFormatUtils.formatDuration(duration, format));
    }
    
    @Test
    public void testFormatDurationPadWithZeros() {
        long duration = 60000; // 1 minute
        String format = "m:ss";
        boolean padWithZeros = true;
        String expected = "01:00";
        assertEquals(expected, DurationFormatUtils.formatDuration(duration, format, padWithZeros));
    }
    
    @Test
    public void testFormatDurationWords() {
        long duration = 1000000; // 16 minutes and 40 seconds
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = " 0 days 0 hours 16 minutes 40 seconds";
        assertEquals(expected, DurationFormatUtils.formatDurationWords(duration, suppressLeadingZeroElements, suppressTrailingZeroElements));
    }
    
    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0;
        long endMillis = 63072000000L; // 2 years
        String expected = "P2Y0M0DT00H00M00S";
        assertEquals(expected, DurationFormatUtils.formatPeriodISO(startMillis, endMillis));
    }
    
    @Test
    public void testFormatPeriod() {
        long startMillis = 0;
        long endMillis = 86400000; // 1 day
        String format = "H:mm:ss";
        String expected = "24:00:00";
        assertEquals(expected, DurationFormatUtils.formatPeriod(startMillis, endMillis, format));
    }
    
    @Test
    public void testFormatPeriodPadWithZeros() {
        long startMillis = 0;
        long endMillis = 60000; // 1 minute
        String format = "H:mm:ss";
        boolean padWithZeros = true;
        String expected = "00:01:00";
        assertEquals(expected, DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros));
    }
    
    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 0;
        long endMillis = 63072000000L; // 2 years
        String format = "H:mm:ss";
        boolean padWithZeros = true;
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        String expected = "16:00:00";
        assertEquals(expected, DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros, timeZone));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testLexxWithUnmatchedQuote() {
        String format = "'P";
        DurationFormatUtils.lexx(format);
    }
    
    @Test
    public void testTokenContainsTokenWithValue() {
        DurationFormatUtils.Token[] tokens = {new DurationFormatUtils.Token("H"), new DurationFormatUtils.Token("m")};
        Object value = "H";
        assertTrue(DurationFormatUtils.Token.containsTokenWithValue(tokens, value));
    }
}