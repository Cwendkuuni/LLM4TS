import static org.junit.Assert.*;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0));
        assertEquals("1:01:01.001", DurationFormatUtils.formatDurationHMS(3661001));
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals("P1970Y0M0DT0H0M0.001S", DurationFormatUtils.formatDurationISO(1));
    }

    @Test
    public void testFormatDuration() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatDuration(0, "H:mm:ss.SSS"));
        assertEquals("0:00:01.000", DurationFormatUtils.formatDuration(1000, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals(" 0 days 0 hours 0 minutes 0 seconds", DurationFormatUtils.formatDurationWords(0, false, false));
    }

    @Test
    public void testFormatPeriodISO() {
        assertEquals("P1970Y0M0DT0H0M0S", DurationFormatUtils.formatPeriodISO(0, 1));
    }

    @Test
    public void testFormatPeriod() {
        assertEquals("0:00:00.001", DurationFormatUtils.formatPeriod(0, 1, "H:mm:ss.SSS"));
        assertEquals("0:00:01.000", DurationFormatUtils.formatPeriod(0, 1000, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormat() {
        DurationFormatUtils.Token[] tokens = {new DurationFormatUtils.Token(DurationFormatUtils.H), new DurationFormatUtils.Token(DurationFormatUtils.m), new DurationFormatUtils.Token(DurationFormatUtils.s)};
        assertEquals("0011", DurationFormatUtils.format(tokens, 0, 0, 0, 1, 1, 0, 0, true));
    }

    @Test
    public void testPaddedValue() {
        assertEquals("001", DurationFormatUtils.paddedValue(1, true, 3));
        assertEquals("10", DurationFormatUtils.paddedValue(10, false, 2));
    }

    @Test
    public void testLexx() {
        DurationFormatUtils.Token[] tokens = DurationFormatUtils.lexx("H:mm:ss.SSS");
        assertEquals(3, tokens.length);
    }

}