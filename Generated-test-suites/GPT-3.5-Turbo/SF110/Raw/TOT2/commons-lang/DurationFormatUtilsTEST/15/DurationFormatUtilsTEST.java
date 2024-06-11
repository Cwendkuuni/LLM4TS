import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 3661000;
        String expected = "1:01:01.000";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 3661000;
        String expected = "P1970Y0M0DT1H1M1.000S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 3661000;
        String format = "H:mm:ss.SSS";
        String expected = "1:01:01.000";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 3661000;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = false;
        String expected = " 1 hour 1 minute 1 second";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0;
        long endMillis = 3661000;
        String expected = "P1970Y0M0DT1H1M1.000S";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 0;
        long endMillis = 3661000;
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        String expected = "P1970Y0M0DT1H1M1.000S";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, result);
    }
}