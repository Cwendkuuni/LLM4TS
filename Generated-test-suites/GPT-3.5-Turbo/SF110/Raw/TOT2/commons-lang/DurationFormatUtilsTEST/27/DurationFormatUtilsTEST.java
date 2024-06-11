import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DurationFormatUtilsTest {
    
    @Test
    public void testFormatDurationHMS() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0));
        assertEquals("1:01:01.001", DurationFormatUtils.formatDurationHMS(3661001));
    }
    
    @Test
    public void testFormatDurationISO() {
        assertEquals("P0Y0M0DT0H0M0.000S", DurationFormatUtils.formatDurationISO(0));
        assertEquals("P0Y0M0DT1H1M1.001S", DurationFormatUtils.formatDurationISO(3661001));
    }
    
    @Test
    public void testFormatDuration() {
        assertEquals("0", DurationFormatUtils.formatDuration(0, "s", false));
        assertEquals("1 day 1 hour 1 minute 1 second", DurationFormatUtils.formatDuration(90061000, "d 'day' H 'hour' m 'minute' s 'second'", false));
    }
    
    @Test
    public void testFormatDurationWords() {
        assertEquals("1 second", DurationFormatUtils.formatDurationWords(1000, false, false));
        assertEquals("1 day 1 hour 1 minute 1 second", DurationFormatUtils.formatDurationWords(90061000, false, false));
    }
    
    @Test
    public void testFormatPeriodISO() {
        assertEquals("P0Y0M0DT0H0M0.000S", DurationFormatUtils.formatPeriodISO(0, 0));
        assertEquals("P0Y0M0DT1H1M1.000S", DurationFormatUtils.formatPeriodISO(3661000, 0));
    }
    
    @Test
    public void testFormatPeriod() {
        assertEquals("0", DurationFormatUtils.formatPeriod(0, 0, "s", false, TimeZone.getDefault()));
        assertEquals("1 day 1 hour 1 minute 1 second", DurationFormatUtils.formatPeriod(90061000, 90061000, "d 'day' H 'hour' m 'minute' s 'second'", false, TimeZone.getDefault()));
    }
}