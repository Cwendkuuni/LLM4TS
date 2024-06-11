import org.junit.Test;
import org.junit.Assert;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long duration = 3661000L; // 1 hour, 1 minute, 1 second
        String expected = "1:01:01.000";
        Assert.assertEquals(expected, DurationFormatUtils.formatDurationHMS(duration));
    }

    @Test
    public void testFormatDurationISO() {
        long duration = 3661000L; // 1 hour, 1 minute, 1 second
        String expected = "PT0H1M1S";
        Assert.assertEquals(expected, DurationFormatUtils.formatDurationISO(duration));
    }

    @Test
    public void testFormatDuration() {
        long duration = 3661000L; // 1 hour, 1 minute, 1 second
        String format = "H:mm:ss.SSS";
        String expected = "1:01:01.000";
        Assert.assertEquals(expected, DurationFormatUtils.formatDuration(duration, format));
    }

    @Test
    public void testFormatDurationWords() {
        long duration = 3661000L; // 1 hour, 1 minute, 1 second
        boolean suppressLeading = true;
        boolean suppressTrailing = true;
        String expected = "1 hour 1 minute 1 second";
        Assert.assertEquals(expected, DurationFormatUtils.formatDurationWords(duration, suppressLeading, suppressTrailing));
    }

    @Test
    public void testFormatPeriodISO() {
        long start = 1000000000L; // 2001-09-09 01:46:40
        long end = 1000100000L; // 2001-09-09 01:48:20
        String expected = "PT2M40S";
        Assert.assertEquals(expected, DurationFormatUtils.formatPeriodISO(start, end));
    }

    @Test
    public void testFormatPeriod() {
        long start = 1000000000L; // 2001-09-09 01:46:40
        long end = 1000100000L; // 2001-09-09 01:48:20
        String format = "H:mm:ss";
        String expected = "0:01:40";
        Assert.assertEquals(expected, DurationFormatUtils.formatPeriod(start, end, format));
    }
}