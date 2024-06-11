import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        // Test typical scenario
        long durationMillis = 3661000L;
        String expected = "1:01:01.000";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);

        // Test edge case with negative durationMillis
        durationMillis = -1000L;
        expected = "0:00:01.000";
        result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);

        // Test edge case with large durationMillis
        durationMillis = Long.MAX_VALUE;
        expected = "256204778:46:39.807";
        result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        // Test exception scenario with null format
        long durationMillis = 3661000L;
        String format = null;
        boolean padWithZeros = true;
        String expected = "1:01:01.000";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        // Test exception scenario with null timezone
        long startMillis = 0L;
        long endMillis = 3661000L;
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        boolean padWithZeros = true;
        TimeZone timezone = null;
        String expected = "0Y0M0DT1H1M1.000S";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros, timezone);
        assertEquals(expected, result);

        // Test edge case with format containing invalid token
        format = "INVALID";
        expected = "1:01:01.000";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros, TimeZone.getDefault());
        assertEquals(expected, result);
    }
}