import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {
    
    // Testing formatDurationHMS() method
    
    // Typical use case
    @Test
    public void testFormatDurationHMS_typical() {
        assertEquals("1:10:30.500", DurationFormatUtils.formatDurationHMS(4230500));
    }
    
    // Edge case
    @Test
    public void testFormatDurationHMS_edge() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0));
    }
    
    // Error scenario
    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationHMS_error() {
        DurationFormatUtils.formatDurationHMS(-1000);
    }
    
    // Testing formatDurationISO() method
    
    // Typical use case
    @Test
    public void testFormatDurationISO_typical() {
        assertEquals("P1970Y0M1DT0H1M30.450S", DurationFormatUtils.formatDurationISO(900450));
    }
    
    // Edge case
    @Test
    public void testFormatDurationISO_edge() {
        assertEquals("P0Y0M0DT0H0M0.000S", DurationFormatUtils.formatDurationISO(0));
    }
    
    // Error scenario
    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationISO_error() {
        DurationFormatUtils.formatDurationISO(-100);
    }
    
    // Testing formatDuration() method
    
    // Typical use case
    @Test
    public void testFormatDuration_typical() {
        assertEquals("1:10:30", DurationFormatUtils.formatDuration(4230000, "H:mm:ss"));
    }
    
    // Edge case
    @Test
    public void testFormatDuration_edge() {
        assertEquals("0:00:00", DurationFormatUtils.formatDuration(0, "H:mm:ss"));
    }
    
    // Error scenario
    @Test(expected = IllegalArgumentException.class)
    public void testFormatDuration_error() {
        DurationFormatUtils.formatDuration(-100, "H:mm:ss");
    }
    
    // Testing formatDurationWords() method
    
    // Typical use case
    @Test
    public void testFormatDurationWords_typical() {
        assertEquals("1 hour 10 minutes", DurationFormatUtils.formatDurationWords(4230000, false, false));
    }
    
    // Edge case
    @Test
    public void testFormatDurationWords_edge() {
        assertEquals("0 days 0 hours", DurationFormatUtils.formatDurationWords(0, false, false));
    }
    
    // Error scenario
    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWords_error() {
        DurationFormatUtils.formatDurationWords(-100, false, false);
    }
    
    // Testing formatPeriodISO() method
    
    // Typical use case
    @Test
    public void testFormatPeriodISO_typical() {
        assertEquals("P1970Y0M1DT0H1M30.450S", DurationFormatUtils.formatPeriodISO(0, 900450));
    }
    
    // Edge case
    @Test
    public void testFormatPeriodISO_edge() {
        assertEquals("P0Y0M0DT0H0M0.000S", DurationFormatUtils.formatPeriodISO(0, 0));
    }
    
    // Error scenario
    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodISO_error() {
        DurationFormatUtils.formatPeriodISO(900450, -100);
    }
    
    // Testing formatPeriod() method
    
    // Typical use case
    @Test
    public void testFormatPeriod_typical() {
        assertEquals("1:10:30", DurationFormatUtils.formatPeriod(0, 4230000, "H:mm:ss"));
    }
    
    // Edge case
    @Test
    public void testFormatPeriod_edge() {
        assertEquals("0:00:00", DurationFormatUtils.formatPeriod(0, 0, "H:mm:ss"));
    }
    
    // Error scenario
    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriod_error() {
        DurationFormatUtils.formatPeriod(-100, 4230000, "H:mm:ss");
    }
}