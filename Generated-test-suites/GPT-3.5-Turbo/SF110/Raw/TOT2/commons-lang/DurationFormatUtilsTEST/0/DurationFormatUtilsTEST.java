import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class DurationFormatUtilsTest {
    
    @Test
    public void testFormatDurationHMS() {
        long duration = 3661000L; // 1 hour, 1 minute, 1 second
        assertEquals("1:01:01.000", DurationFormatUtils.formatDurationHMS(duration));
    }
    
    @Test
    public void testFormatDurationISO() {
        long duration = 3661000L; // 1 hour, 1 minute, 1 second
        assertEquals("P1970Y1M1DT1H1M1.000S", DurationFormatUtils.formatDurationISO(duration));
    }
    
    @Test
    public void testFormatDuration() {
        long duration = 3661000L; // 1 hour, 1 minute, 1 second
        assertEquals("1:01:01.000", DurationFormatUtils.formatDuration(duration, "H:mm:ss.SSS"));
    }
    
    @Test
    public void testFormatDurationWords() {
        long duration = 3661000L; // 1 hour, 1 minute, 1 second
        assertEquals("0 days 1 hours 1 minutes 1 seconds", DurationFormatUtils.formatDurationWords(duration, false, false));
    }
    
    @Test
    public void testFormatPeriodISO() {
        long start = 0L;
        long end = 3661000L; // 1 hour, 1 minute, 1 second
        assertEquals("P1970Y1M1DT1H1M1.000S", DurationFormatUtils.formatPeriodISO(start, end));
    }
    
    @Test
    public void testFormatPeriod() {
        long start = 0L;
        long end = 3661000L; // 1 hour, 1 minute, 1 second
        assertEquals("0 days 1 hours 1 minutes 1 seconds", DurationFormatUtils.formatPeriod(start, end, "d' days 'H' hours 'm' minutes 's' seconds'"));
    }
    
    @Test
    public void testFormatMethodExceptionHandling() {
        long duration = -3661000L; // Negative duration
        try {
            DurationFormatUtils.formatDuration(duration, "H:mm:ss.SSS");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
}