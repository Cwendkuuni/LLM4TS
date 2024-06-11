import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {
  
    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 3600000L;  // 1 hour
        String expected = "1:00:00.000";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDurationISO() {
        long durationMillis = 86400000L;  // 1 day
        String expected = "P0000Y00M01DT00H00M00.000S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDuration() {
        long durationMillis = 3600000L;  // 1 hour
        String format = "H:mm:ss.SSS";
        String expected = "1:00:00.000";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDurationPadWithZeros() {
        long durationMillis = 60000L;  // 1 minute
        String format = "m:ss";
        String expected = "01:00";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDurationWords() {
        long durationMillis = 3600000L;  // 1 hour
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 hour 0 minutes 0 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0L;
        long endMillis = 86400000L;  // 1 day
        String expected = "P0000Y00M01DT00H00M00.000S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatPeriod() {
        long startMillis = 0L;
        long endMillis = 3600000L;  // 1 hour
        String format = "H:mm:ss.SSS";
        String expected = "1:00:00.000";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatPeriodPadWithZeros() {
        long startMillis = 0L;
        long endMillis = 60000L;  // 1 minute
        String format = "m:ss";
        String expected = "01:00";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLexx() {
        String format = "H:mm:ss";
        DurationFormatUtils.Token[] expectedTokens = {
            new DurationFormatUtils.Token(DurationFormatUtils.H),
            new DurationFormatUtils.Token(":"),
            new DurationFormatUtils.Token(DurationFormatUtils.m),
            new DurationFormatUtils.Token(":"),
            new DurationFormatUtils.Token(DurationFormatUtils.s)
        };
        DurationFormatUtils.Token[] actualTokens = DurationFormatUtils.lexx(format);
        assertArrayEquals(expectedTokens, actualTokens);
    }
}