import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        // Typical use case
        assertEquals("2:30:00.000", DurationFormatUtils.formatDurationHMS(9000000));
        // Edge case: Minimum duration
        assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0));
        // Error scenario: Negative duration
        assertEquals("", DurationFormatUtils.formatDurationHMS(-1000));
    }

    @Test
    public void testFormatDurationISO() {
        // Typical use case
        assertEquals("P1970Y1M18DT3H25M45.678S", DurationFormatUtils.formatDurationISO(160000000565678L));
        // Edge case: Minimum duration
        assertEquals("P1970Y1M1DT0H0M0.000S", DurationFormatUtils.formatDurationISO(1000));
        // Error scenario: Negative duration
        assertEquals("", DurationFormatUtils.formatDurationISO(-5000));
    }

    @Test
    public void testFormatDuration() {
        // Typical use case
        assertEquals("1 hour, 30 minutes, 15 seconds", DurationFormatUtils.formatDuration(5415000, "H 'hour', m 'minutes', s 'seconds'"));
        // Edge case: Minimum duration
        assertEquals("0 hour, 0 minutes, 0 seconds", DurationFormatUtils.formatDuration(0, "H 'hour', m 'minutes', s 'seconds'"));
        // Error scenario: Invalid format
        assertEquals("", DurationFormatUtils.formatDuration(1000000, "abcd"));
    }

    @Test
    public void testFormatDurationWords() {
        // Typical use case
        assertEquals("1 day 2 hours 20 minutes 10 seconds", DurationFormatUtils.formatDurationWords(974201000, false, false));
        // Edge case: Minimum duration
        assertEquals("0 seconds", DurationFormatUtils.formatDurationWords(0, false, false));
        // Error scenario: Suppress leading zero elements
        assertEquals("2 hours 20 minutes 10 seconds", DurationFormatUtils.formatDurationWords(8410000, true, false));
    }

    @Test
    public void testFormatPeriodISO() {
        // Typical use case
        assertEquals("P0001Y1M29DT22H29M5.000S", DurationFormatUtils.formatPeriodISO(0, 37044054005L));
        // Edge case: Minimum period
        assertEquals("P0000Y0M0DT0H0M1.000S", DurationFormatUtils.formatPeriodISO(1000, 1000));
        // Error scenario: Negative duration
        assertEquals("", DurationFormatUtils.formatPeriodISO(-1000, 5000));
    }

    @Test
    public void testFormatPeriod() {
        // Typical use case
        assertEquals("1 year, 2 months, 25 days, 12 hours, 30 minutes, 15 seconds", DurationFormatUtils.formatPeriod(382174665000L, 5415000, "y 'year', M 'months', d 'days', H 'hours', m 'minutes', s 'seconds'"));
        // Edge case: Minimum period
        assertEquals("0 year, 0 months, 0 days, 0 hours, 0 minutes, 0 seconds", DurationFormatUtils.formatPeriod(0, 0, "y 'year', M 'months', d 'days', H 'hours', m 'minutes', s 'seconds'"));
        // Error scenario: Invalid format
        assertEquals("", DurationFormatUtils.formatPeriod(1000000, 2000000, "abcd"));
    }
}