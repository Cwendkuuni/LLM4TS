import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long duration = 3661000L; // 1 hour, 1 minute, 1 second
        assertEquals("01:01:01.000", DurationFormatUtils.formatDurationHMS(duration));
    }

    @Test
    public void testFormatDurationISO() {
        long duration = 3672000L; // 1 hour, 1 minute, 12 seconds
        assertEquals("PT1H1M12.000S", DurationFormatUtils.formatDurationISO(duration));
    }

    @Test
    public void testFormatDuration() {
        long duration = 3661000L; // 1 hour, 1 minute, 1 second
        assertEquals("1:01:01.000", DurationFormatUtils.formatDuration(duration, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWords() {
        long duration = 900600000L; // 10 days, 6 hours
        assertEquals("10 days 6 hours", DurationFormatUtils.formatDurationWords(duration, false, false));
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1611154800000L; // January 20, 2021 12:00:00 AM UTC
        long endMillis = 1616809200000L; // March 27, 2021 12:00:00 AM UTC
        assertEquals("P0Y2M7DT23H0M", DurationFormatUtils.formatPeriodISO(startMillis, endMillis));
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1611154800000L; // January 20, 2021 12:00:00 AM UTC
        long endMillis = 1616809200000L; // March 27, 2021 12:00:00 AM UTC
        assertEquals("2 months 7 days 23 hours", DurationFormatUtils.formatPeriod(startMillis, endMillis, "M 'months' d 'days' H 'hours'"));
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1611154800000L; // January 20, 2021 12:00:00 AM UTC
        long endMillis = 1616809200000L; // March 27, 2021 12:00:00 AM UTC
        assertEquals("2 months 8 days", DurationFormatUtils.formatPeriod(startMillis, endMillis, "M 'months' d 'days'", true, TimeZone.getTimeZone("America/New_York")));
    }

}