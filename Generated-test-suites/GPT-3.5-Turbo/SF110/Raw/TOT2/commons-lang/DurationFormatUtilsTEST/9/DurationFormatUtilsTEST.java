import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        assertEquals("1:02:03.456", DurationFormatUtils.formatDurationHMS(3723456));
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals("P1970Y1M1DT0H2M3.456S", DurationFormatUtils.formatDurationISO(3156198363456L));
    }

    @Test
    public void testFormatDuration() {
        assertEquals("1:02:03.456", DurationFormatUtils.formatDuration(3723456, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals("1 day 1 hour 2 minutes 3 seconds", DurationFormatUtils.formatDurationWords(93723000, false, false));
    }

    @Test
    public void testFormatPeriodISO() {
        assertEquals("P1970Y1M1DT0H2M3.456S", DurationFormatUtils.formatPeriodISO(315619836000L, 3156198363456L));
    }

    @Test
    public void testFormatPeriod() {
        assertEquals("1:02:03.456", DurationFormatUtils.formatPeriod(0, 3723456, "H:mm:ss.SSS", true, TimeZone.getDefault()));
    }

    @Test
    public void testLexx() {
        DurationFormatUtils.Token[] tokens = DurationFormatUtils.lexx("'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'");
        assertEquals(11, tokens.length);
    }

    @Test
    public void testFormat() {
        DurationFormatUtils.Token[] tokens = DurationFormatUtils.lexx("H:mm:ss");
        assertEquals("1:02:03", DurationFormatUtils.format(tokens, 0L, 0L, 0L, 1L, 2L, 3L, 0L, true));
    }

    @Test
    public void testPaddedValue() {
        assertEquals("01", DurationFormatUtils.paddedValue(1, true, 2));
    }
}