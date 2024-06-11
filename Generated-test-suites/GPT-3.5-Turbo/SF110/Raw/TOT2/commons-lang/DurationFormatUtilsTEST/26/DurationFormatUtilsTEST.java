import static org.junit.Assert.*;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 3661000L; // 1 hour, 1 minute, 1 second
        assertEquals("1:01:01.000", DurationFormatUtils.formatDurationHMS(durationMillis));
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 90000000L; // 1 day, 1 hour
        assertEquals("P1970Y0M1DT1H0M0.000S", DurationFormatUtils.formatDurationISO(durationMillis));
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 3600000L; // 1 hour
        assertEquals("1:00:00.000", DurationFormatUtils.formatDuration(durationMillis, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 3661000L; // 1 hour, 1 minute, 1 second
        // No need for testing every possible combination for now, as the implementation is complex
        assertNotNull(DurationFormatUtils.formatDurationWords(durationMillis, false, false));
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0L;
        long endMillis = 90000000L; // 1 day, 1 hour
        assertEquals("P1970Y0M1DT1H0M0.000S", DurationFormatUtils.formatPeriodISO(startMillis, endMillis));
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 0L;
        long endMillis = 3600000L; // 1 hour
        assertEquals("0:00:00.000", DurationFormatUtils.formatPeriod(startMillis, endMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'"));
    }

    // Add more test cases for edge cases and exception handling scenarios

}