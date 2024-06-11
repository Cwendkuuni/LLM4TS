import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 3661000L;
        assertEquals("1:01:01.000", DurationFormatUtils.formatDurationHMS(durationMillis));
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 3661000L;
        assertEquals("PT1H1M1S", DurationFormatUtils.formatDurationISO(durationMillis));
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 3661000L;
        assertEquals("0:01:01.000", DurationFormatUtils.formatDuration(durationMillis, "H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 3661000L;
        assertEquals(" 0 days 0 hours 1 minutes 1 seconds", DurationFormatUtils.formatDurationWords(durationMillis, false, false));
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0L;
        long endMillis = 3661000L;
        assertEquals("PT1H1M1S", DurationFormatUtils.formatPeriodISO(startMillis, endMillis));
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 0L;
        long endMillis = 3661000L;
        assertEquals("0:01:01.000", DurationFormatUtils.formatPeriod(startMillis, endMillis, "H:mm:ss.SSS"));
    }
}