import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {
    
    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1000;
        String expected = "0:00:01.000";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1000;
        String expected = "P1970Y1M1DT0H0M1.000S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDuration() {
        long durationMillis = 1000;
        String format = "H:mm:ss.SSS";
        String expected = "0:00:01.000";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDurationWithPad() {
        long durationMillis = 1000;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "0:00:01.000";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatDurationWords() {
        long durationMillis = 86400000;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 days 0 hours 0 minutes 0 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0;
        long endMillis = 1000;
        String expected = "P1970Y1M1DT0H0M1.000S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatPeriod() {
        long startMillis = 0;
        long endMillis = 1000;
        String format = "H:mm:ss.SSS";
        String expected = "0:00:01.000";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatPeriodWithPad() {
        long startMillis = 0;
        long endMillis = 1000;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "0:00:01.000";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormat() {
        DurationFormatUtils.Token[] tokens = DurationFormatUtils.lexx("H:mm:ss.SSS");
        long years = 0;
        long months = 0;
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 1;
        long milliseconds = 0;
        boolean padWithZeros = true;
        String expected = "0:00:01.000";
        String actual = DurationFormatUtils.format(tokens, years, months, days, hours, minutes, seconds, milliseconds, padWithZeros);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testPaddedValue() {
        long value = 1;
        boolean padWithZeros = true;
        int count = 2;
        String expected = "01";
        String actual = DurationFormatUtils.paddedValue(value, padWithZeros, count);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLexx() {
        String format = "H:mm:ss.SSS";
        DurationFormatUtils.Token[] expected = { new DurationFormatUtils.Token(DurationFormatUtils.H), new DurationFormatUtils.Token(":"), new DurationFormatUtils.Token(DurationFormatUtils.m), new DurationFormatUtils.Token(":"), new DurationFormatUtils.Token(DurationFormatUtils.s), new DurationFormatUtils.Token("."), new DurationFormatUtils.Token(DurationFormatUtils.S) };
        DurationFormatUtils.Token[] actual = DurationFormatUtils.lexx(format);
        assertArrayEquals(expected, actual);
    }
    
}