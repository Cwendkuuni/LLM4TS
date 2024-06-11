import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        assertEquals("1:00:00.000", DurationFormatUtils.formatDurationHMS(3600000));
        assertEquals("0:05:30.000", DurationFormatUtils.formatDurationHMS(330000));
        assertEquals("0:00:01.000", DurationFormatUtils.formatDurationHMS(1000));
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals("P1970Y01M01DT01H00M00S.000", DurationFormatUtils.formatDurationISO(3600000));
        assertEquals("P1970Y01M01DT00H05M30S.000", DurationFormatUtils.formatDurationISO(330000));
        assertEquals("P1970Y01M01DT00H00M01S.000", DurationFormatUtils.formatDurationISO(1000));
    }

    @Test
    public void testFormatDuration() {
        assertEquals("1:00:00.000", DurationFormatUtils.formatDuration(3600000, "H:mm:ss.SSS"));
        assertEquals("0:05:30.000", DurationFormatUtils.formatDuration(330000, "H:mm:ss.SSS"));
        assertEquals("0:00:01.000", DurationFormatUtils.formatDuration(1000, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWithPadZeros() {
        assertEquals("01:00:00.000", DurationFormatUtils.formatDuration(3600000, "HH:mm:ss.SSS", true));
        assertEquals("00:05:30.000", DurationFormatUtils.formatDuration(330000, "HH:mm:ss.SSS", true));
        assertEquals("00:00:01.000", DurationFormatUtils.formatDuration(1000, "HH:mm:ss.SSS", true));
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals("1 hour", DurationFormatUtils.formatDurationWords(3600000, false, false));
        assertEquals("5 minutes", DurationFormatUtils.formatDurationWords(330000, false, false));
        assertEquals("1 second", DurationFormatUtils.formatDurationWords(1000, false, false));
    }

    @Test
    public void testFormatPeriodISO() {
        assertEquals("P1970Y01M01DT01H00M00S.000", DurationFormatUtils.formatPeriodISO(0, 3600000));
        assertEquals("P1970Y01M01DT00H05M30S.000", DurationFormatUtils.formatPeriodISO(0, 330000));
        assertEquals("P1970Y01M01DT00H00M01S.000", DurationFormatUtils.formatPeriodISO(0, 1000));
    }

    @Test
    public void testFormatPeriod() {
        assertEquals("1:00:00.000", DurationFormatUtils.formatPeriod(0, 3600000, "H:mm:ss.SSS"));
        assertEquals("0:05:30.000", DurationFormatUtils.formatPeriod(0, 330000, "H:mm:ss.SSS"));
        assertEquals("0:00:01.000", DurationFormatUtils.formatPeriod(0, 1000, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatPeriodWithPadZeros() {
        assertEquals("01:00:00.000", DurationFormatUtils.formatPeriod(0, 3600000, "HH:mm:ss.SSS", true, TimeZone.getDefault()));
        assertEquals("00:05:30.000", DurationFormatUtils.formatPeriod(0, 330000, "HH:mm:ss.SSS", true, TimeZone.getDefault()));
        assertEquals("00:00:01.000", DurationFormatUtils.formatPeriod(0, 1000, "HH:mm:ss.SSS", true, TimeZone.getDefault()));
    }

    @Test
    public void testFormatMethod() {
        DurationFormatUtils.Token[] tokens = DurationFormatUtils.lexx("HH:mm:ss.SSS");
        assertEquals("01", DurationFormatUtils.format(tokens, 0, 0, 0, 1, 0, 0, 0, true));
        assertEquals("02", DurationFormatUtils.format(tokens, 0, 0, 0, 2, 0, 0, 0, true));
        assertEquals("03", DurationFormatUtils.format(tokens, 0, 0, 0, 3, 0, 0, 0, true));
    }
}