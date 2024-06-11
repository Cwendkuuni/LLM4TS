import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {
    
    @Test
    public void testFormatDurationHMS() {
        // Typical use case
        assertEquals("1:01:01.500", DurationFormatUtils.formatDurationHMS(3661500));
        
        // Edge case - 0 milliseconds
        assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0));
        
        // Error scenario - negative duration
        assertEquals("-1:30:10.000", DurationFormatUtils.formatDurationHMS(-5410000));
    }
    
    @Test
    public void testFormatDurationISO() {
        // Typical use case
        assertEquals("P1970Y01M01DT01H01M01.500S", DurationFormatUtils.formatDurationISO(3661501500L));
        
        // Edge case - 0 milliseconds
        assertEquals("P1970Y01M01DT00H00M00.000S", DurationFormatUtils.formatDurationISO(0));
        
        // Error scenario - negative duration
        assertEquals("P1969Y11M30DT22H59M50.000S", DurationFormatUtils.formatDurationISO(-5410000));
    }
    
    @Test
    public void testFormatDuration() {
        // Typical use case
        assertEquals("0 days 1:01:01.500", DurationFormatUtils.formatDuration(3661500, "d' days 'H:mm:ss.SSS"));
        
        // Edge case - 0 milliseconds
        assertEquals("0 days 0:00:00.000", DurationFormatUtils.formatDuration(0, "d' days 'H:mm:ss.SSS"));
        
        // Error scenario - negative duration
        assertEquals("-1 days 1:30:10.000", DurationFormatUtils.formatDuration(-5410000, "d' days 'H:mm:ss.SSS"));
    }
    
    @Test
    public void testFormatDurationWords() {
        // Typical use case
        assertEquals(" 1 hour 1 minute 1 second", DurationFormatUtils.formatDurationWords(3661500, false, false));
        
        // Edge case - 0 milliseconds
        assertEquals(" 0 seconds", DurationFormatUtils.formatDurationWords(0, false, false));
        
        // Error scenario - negative duration
        assertEquals("-1 hour 30 minutes 10 seconds", DurationFormatUtils.formatDurationWords(-5410000, false, false));
    }
    
    @Test
    public void testFormatPeriodISO() {
        // Typical use case
        assertEquals("P1970Y01M01DT01H01M01.500S", DurationFormatUtils.formatPeriodISO(0, 3661501500L));
        
        // Edge case - 0 milliseconds
        assertEquals("P1970Y01M01DT00H00M00.000S", DurationFormatUtils.formatPeriodISO(0, 0));
        
        // Error scenario - negative duration
        assertEquals("P1969Y11M30DT22H59M50.000S", DurationFormatUtils.formatPeriodISO(-5410000, 0));
    }
    
    @Test
    public void testFormatPeriod() {
        // Typical use case
        assertEquals("0 years 0 months 1 days 1:01:01.500", DurationFormatUtils.formatPeriod(0, 3661500, "yyyy' years 'MM' months 'd' days 'H:mm:ss.SSS"));
        
        // Edge case - 0 milliseconds
        assertEquals("0 years 0 months 0 days 0:00:00.000", DurationFormatUtils.formatPeriod(0, 0, "yyyy' years 'MM' months 'd' days 'H:mm:ss.SSS"));
        
        // Error scenario - negative duration
        assertEquals("-1 years 0 months -30 days 1:30:10.000", DurationFormatUtils.formatPeriod(-5410000, 0, "yyyy' years 'MM' months 'd' days 'H:mm:ss.SSS"));
    }
}