import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        // Test cases for formatDurationHMS method
        assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0));
        assertEquals("0:00:01.000", DurationFormatUtils.formatDurationHMS(1000));
        assertEquals("0:01:00.000", DurationFormatUtils.formatDurationHMS(60000));
        assertEquals("1:00:00.000", DurationFormatUtils.formatDurationHMS(3600000));
    }

    @Test
    public void testFormatDurationISO() {
        // Test cases for formatDurationISO method
        assertEquals("P1970Y01M01DT00H00M00.000S", DurationFormatUtils.formatDurationISO(0));
        assertEquals("P1970Y01M01DT00H00M01.000S", DurationFormatUtils.formatDurationISO(1000));
        assertEquals("P1970Y01M01DT00H01M00.000S", DurationFormatUtils.formatDurationISO(60000));
        assertEquals("P1970Y01M01DT01H00M00.000S", DurationFormatUtils.formatDurationISO(3600000));
    }

    @Test
    public void testFormatDuration() {
        // Test cases for formatDuration method
        assertEquals("0", DurationFormatUtils.formatDuration(0, ""));
        assertEquals("0", DurationFormatUtils.formatDuration(1000, ""));
        assertEquals("0", DurationFormatUtils.formatDuration(60000, ""));
        assertEquals("0", DurationFormatUtils.formatDuration(3600000, ""));
    }

    @Test
    public void testFormatDurationWords() {
        // Test cases for formatDurationWords method
        assertEquals("0 seconds", DurationFormatUtils.formatDurationWords(0, false, false));
        assertEquals("1 second", DurationFormatUtils.formatDurationWords(1000, false, false));
        assertEquals("1 minute", DurationFormatUtils.formatDurationWords(60000, false, false));
        assertEquals("1 hour", DurationFormatUtils.formatDurationWords(3600000, false, false));
    }

    @Test
    public void testFormatPeriodISO() {
        // Test cases for formatPeriodISO method
        assertEquals("P1970Y01M01DT00H00M00.000S", DurationFormatUtils.formatPeriodISO(0, 0));
        assertEquals("P1970Y01M01DT00H00M01.000S", DurationFormatUtils.formatPeriodISO(0, 1000));
        assertEquals("P1970Y01M01DT00H01M00.000S", DurationFormatUtils.formatPeriodISO(0, 60000));
        assertEquals("P1970Y01M01DT01H00M00.000S", DurationFormatUtils.formatPeriodISO(0, 3600000));
    }

    @Test
    public void testFormatPeriod() {
        // Test cases for formatPeriod method
        assertEquals("0", DurationFormatUtils.formatPeriod(0, 0, ""));
        assertEquals("0", DurationFormatUtils.formatPeriod(0, 1000, ""));
        assertEquals("0", DurationFormatUtils.formatPeriod(0, 60000, ""));
        assertEquals("0", DurationFormatUtils.formatPeriod(0, 3600000, ""));
    }
}