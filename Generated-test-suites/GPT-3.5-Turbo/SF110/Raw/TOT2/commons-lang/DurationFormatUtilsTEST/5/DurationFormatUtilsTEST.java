import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        // Test typical case
        assertEquals("1:02:03.456", DurationFormatUtils.formatDurationHMS(3723456));
        // Test edge case
        assertEquals("0:00:00.001", DurationFormatUtils.formatDurationHMS(1));
        // Test error scenario
        assertNull(DurationFormatUtils.formatDurationHMS(-1000));
    }

    @Test
    public void testFormatDurationISO() {
        // Test typical case
        assertEquals("P1970Y0M0DT1H2M3.456S", DurationFormatUtils.formatDurationISO(3723456));
        // Test edge case
        assertEquals("P1970Y0M0DT0H0M0.001S", DurationFormatUtils.formatDurationISO(1));
        // Test error scenario
        assertNull(DurationFormatUtils.formatDurationISO(-1000));
    }

    @Test
    public void testFormatDuration() {
        // Test typical case
        assertEquals("1:02:03.456", DurationFormatUtils.formatDuration(3723456, "H:mm:ss.SSS"));
        // Test edge case
        assertEquals("0:00:00.001", DurationFormatUtils.formatDuration(1, "H:mm:ss.SSS"));
        // Test error scenario
        assertNull(DurationFormatUtils.formatDuration(-1000, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWords() {
        // Test typical case
        assertEquals("1 day 2 hours 3 minutes 4 seconds", DurationFormatUtils.formatDurationWords(93784400, false, false));
        // Test edge case
        assertEquals("1 day 0 hours 0 minutes 0 seconds", DurationFormatUtils.formatDurationWords(86400000, false, false));
        // Test error scenario
        assertNull(DurationFormatUtils.formatDurationWords(-1000, false, false));
    }

    @Test
    public void testFormatPeriodISO() {
        // Test typical case
        assertEquals("P1970Y0M1DT1H2M3.456S", DurationFormatUtils.formatPeriodISO(86400000, 3723456));
        // Test edge case
        assertEquals("P1970Y0M0DT23H59M59.999S", DurationFormatUtils.formatPeriodISO(86400000, 86399999));
        // Test error scenario
        assertNull(DurationFormatUtils.formatPeriodISO(3723456, 86400000));
    }

    @Test
    public void testFormatPeriod() {
        // Test typical case
        assertEquals("1:02:03.456", DurationFormatUtils.formatPeriod(0, 3723456, "H:mm:ss.SSS", true, TimeZone.getDefault()));
        // Test edge case
        assertEquals("0:59:59.999", DurationFormatUtils.formatPeriod(0, 3599999, "H:mm:ss.SSS", true, TimeZone.getDefault()));
        // Test error scenario
        assertNull(DurationFormatUtils.formatPeriod(3723456, 0, "H:mm:ss.SSS", true, TimeZone.getDefault()));
    }
}