import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DurationFormatUtilsTest {
    @Test
    public void testFormatDurationHMS() {
        assertEquals("0:01:02.003", DurationFormatUtils.formatDurationHMS(62003));
        assertEquals("1:00:00.000", DurationFormatUtils.formatDurationHMS(3600000));
        assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0));
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals("P0Y0M0DT0H1M2.003S", DurationFormatUtils.formatDurationISO(62003));
        assertEquals("P0Y0M0DT1H0M0.000S", DurationFormatUtils.formatDurationISO(3600000));
        assertEquals("P0Y0M0DT0H0M0.000S", DurationFormatUtils.formatDurationISO(0));
    }

    @Test
    public void testFormatDuration() {
        assertEquals("0:01:02.003", DurationFormatUtils.formatDuration(62003, "H:mm:ss.SSS"));
        assertEquals("1:00:00.000", DurationFormatUtils.formatDuration(3600000, "H:mm:ss.SSS"));
        assertEquals("0:00:00.000", DurationFormatUtils.formatDuration(0, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals("0 days 1 hours 2 minutes 3 seconds", DurationFormatUtils.formatDurationWords(3723000, false, false));
        assertEquals("0 days", DurationFormatUtils.formatDurationWords(0, false, false));
        assertEquals("1 second", DurationFormatUtils.formatDurationWords(1000, true, true));
    }

    @Test
    public void testFormatPeriodISO() {
        assertEquals("P0Y0M0DT1H2M3.004S", DurationFormatUtils.formatPeriodISO(3723004, 0));
        assertEquals("P0Y0M0DT0H0M0.000S", DurationFormatUtils.formatPeriodISO(0, 0));
    }

    @Test
    public void testFormatPeriod() {
        assertEquals("0:01:02.003", DurationFormatUtils.formatPeriod(3723003, 0, "H:mm:ss.SSS", true));
        assertEquals("0:00:00.000", DurationFormatUtils.formatPeriod(0, 0, "H:mm:ss.SSS", true));
    }
}