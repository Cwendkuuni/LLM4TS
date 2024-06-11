import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        // Typical use case
        assertEquals("1:02:03.004", DurationFormatUtils.formatDurationHMS(3723004));
        // Edge case
        assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0));
        // Error scenario
        assertNull(DurationFormatUtils.formatDurationHMS(-100));
    }

    @Test
    public void testFormatDurationISO() {
        // Typical use case
        assertEquals("P1970Y1M2DT2H3M23.004S", DurationFormatUtils.formatDurationISO(2022003004));
        // Edge case
        assertEquals("P0Y0M0DT0H0M0.000S", DurationFormatUtils.formatDurationISO(0));
        // Error scenario
        assertNull(DurationFormatUtils.formatDurationISO(-100));
    }

    @Test
    public void testFormatDuration() {
        // Typical use case
        assertEquals("2:03:23.004", DurationFormatUtils.formatDuration(7403004, "H:mm:ss.SSS"));
        // Edge case
        assertEquals("0:00:00.000", DurationFormatUtils.formatDuration(0, "H:mm:ss.SSS"));
        // Error scenario
        assertNull(DurationFormatUtils.formatDuration(-100, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationPadWithZeros() {
        // Typical use case
        assertEquals("02:03:23.004", DurationFormatUtils.formatDuration(7403004, "HH:mm:ss.SSS", true));
        // Edge case
        assertEquals("00:00:00.000", DurationFormatUtils.formatDuration(0, "HH:mm:ss.SSS", true));
        // Error scenario
        assertNull(DurationFormatUtils.formatDuration(-100, "HH:mm:ss.SSS", true));
    }

    @Test
    public void testFormatDurationWords() {
        // Typical use case
        assertEquals("1 days 2 hours 3 minutes 23 seconds", DurationFormatUtils.formatDurationWords(2022003004, false, false));
        // Edge case
        assertEquals("0 days 0 hours 0 minutes 0 seconds", DurationFormatUtils.formatDurationWords(0, false, false));
        // Error scenario
        assertNull(DurationFormatUtils.formatDurationWords(-100, false, false));
    }

    @Test
    public void testFormatPeriodISO() {
        // Typical use case
        assertEquals("P1969Y11M29DT11H56M43.996S", DurationFormatUtils.formatPeriodISO(-1000004, 2022003000));
        // Edge case
        assertEquals("P0Y0M0DT0H0M0.000S", DurationFormatUtils.formatPeriodISO(0, 0));
    }

    @Test
    public void testFormatPeriod() {
        // Typical use case
        assertEquals("11 hours 56 minutes 43 seconds", DurationFormatUtils.formatPeriod(-1000004, 2022003000, "H 'hours' m 'minutes' s 'seconds'", false, TimeZone.getDefault()));
        // Edge case
        assertEquals("0 hours 0 minutes 0 seconds", DurationFormatUtils.formatPeriod(0, 0, "H 'hours' m 'minutes' s 'seconds'", false, TimeZone.getDefault()));
    }

}