import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {
    
    @Test
    public void formatDurationHMSTest() {
        assertEquals("01:00:00.000", DurationFormatUtils.formatDurationHMS(3600000));
    }
    
    @Test
    public void formatDurationISOTest() {
        assertEquals("P1970Y0M0DT00H00M01.000S", DurationFormatUtils.formatDurationISO(1000));
    }
    
    @Test
    public void formatDurationTest() {
        assertEquals("0 days 1 hours 0 minutes 0 seconds", DurationFormatUtils.formatDuration(3600000, "d' days 'H' hours 'm' minutes 's' seconds'"));
    }
    
    @Test
    public void formatDurationWordsTest() {
        assertEquals("1 hour", DurationFormatUtils.formatDurationWords(3600000, false, false));
    }
    
    @Test
    public void formatPeriodISOTest() {
        assertEquals("P1970Y0M0DT00H00M01.000S", DurationFormatUtils.formatPeriodISO(0, 1000));
    }
    
    @Test
    public void formatPeriodTest() {
        assertEquals("0 days 1 hours 0 minutes 0 seconds", DurationFormatUtils.formatPeriod(0, 3600000, "d' days 'H' hours 'm' minutes 's' seconds'", false, TimeZone.getDefault()));
    }
    
    @Test
    public void formatTest() {
        DurationFormatUtils.Token[] tokens = DurationFormatUtils.lexx("H'H'm'M's.S'S'");
        assertEquals("01", DurationFormatUtils.format(tokens, 0, 0, 0, 1, 0, 0, 0, false));
    }
    
    @Test
    public void paddedValueTest() {
        assertEquals("01", DurationFormatUtils.paddedValue(1, true, 2));
    }
    
    @Test
    public void lexxTest() {
        DurationFormatUtils.Token[] tokens = DurationFormatUtils.lexx("H'H'm'M's.S'S'");
        assertEquals(3, tokens.length);
    }
}