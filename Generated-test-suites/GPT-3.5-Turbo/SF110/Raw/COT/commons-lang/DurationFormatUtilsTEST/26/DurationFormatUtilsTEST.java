import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Assert;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 3600000; // 1 hour
        String expected = "1:00:00.000";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 123456789;
        String expected = "P0019Y02M03DT10H17M36.789S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 123456789;
        String format = "H:mm:ss.SSS";
        String expected = "34:17:36.789";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationPadWithZeros() {
        long durationMillis = 1000;
        String format = "ss.SS";
        String expected = "01.00";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, true);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 123456789;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 days 10 hours 17 minutes 36 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0;
        long endMillis = 123456789;
        String expected = "P0000Y00M01DT10H17M36.789S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 0;
        long endMillis = 123456789;
        String format = "H:mm:ss.SS";
        String expected = "10:17:36.78";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodPadWithZeros() {
        long startMillis = 0;
        long endMillis = 123456789;
        String format = "ss.SSS";
        String expected = "36.789";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 0;
        long endMillis = 123456789;
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = false;
        String timeZone = "America/New_York";
        String expected = "05:17:36.789";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros, TimeZone.getTimeZone(timeZone));
        Assert.assertEquals(expected, actual);
    }

}