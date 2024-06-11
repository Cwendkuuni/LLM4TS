import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        assertEquals("1:01:01.001", DurationFormatUtils.formatDurationHMS(3661001));
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals("P1970Y1M1DT1H1M1.001S", DurationFormatUtils.formatDurationISO(3661001));
    }

    @Test
    public void testFormatDuration() {
        assertEquals("1:01:01.001", DurationFormatUtils.formatDuration(3661001, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationPadWithZeros() {
        assertEquals("01:01:01.001", DurationFormatUtils.formatDuration(3661001, "HH:mm:ss.SSS", true));
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals("1 day 1 hour 1 minute 1 second", DurationFormatUtils.formatDurationWords(90061000, false, false));
    }

    @Test
    public void testFormatPeriodISO() {
        assertEquals("P1970Y0M0DT1H1M1.001S", DurationFormatUtils.formatPeriodISO(0, 3661001));
    }

    @Test
    public void testFormatPeriod() {
        assertEquals("0:01:01.001", DurationFormatUtils.formatPeriod(0, 3661001, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatPeriodPadWithZeros() {
        assertEquals("00:01:01.001", DurationFormatUtils.formatPeriod(0, 3661001, "HH:mm:ss.SSS", true, TimeZone.getDefault()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        DurationFormatUtils.formatDuration(3661001, "xyz", true);
    }
}