import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0));
        assertEquals("1:00:01.001", DurationFormatUtils.formatDurationHMS(3601001));
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals("P1970Y0M0DT0H0M0.000S", DurationFormatUtils.formatDurationISO(0));
        assertEquals("P1970Y0M0DT1H0M1.001S", DurationFormatUtils.formatDurationISO(3601001));
    }

    @Test
    public void testFormatDuration() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatDuration(0, "H:mm:ss.SSS"));
        assertEquals("1:00:01.001", DurationFormatUtils.formatDuration(3601001, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals("1 day 1 minute 1 second", DurationFormatUtils.formatDurationWords(90061000, false, false));
        assertEquals("1 hour 1 minute 1 second", DurationFormatUtils.formatDurationWords(3661000, false, false));
    }

    @Test
    public void testFormatPeriodISO() {
        assertEquals("P1970Y0M0DT0H0M0.000S", DurationFormatUtils.formatPeriodISO(0, 0));
        assertEquals("P1970Y0M0DT1H0M1.001S", DurationFormatUtils.formatPeriodISO(0, 3601001));
    }

    @Test
    public void testFormatPeriod() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatPeriod(0, 0, "H:mm:ss.SSS", true, TimeZone.getDefault()));
        assertEquals("1:00:01.001", DurationFormatUtils.formatPeriod(0, 3601001, "H:mm:ss.SSS", true, TimeZone.getDefault()));
    }

    // Add more test cases to cover edge cases and exceptions

}