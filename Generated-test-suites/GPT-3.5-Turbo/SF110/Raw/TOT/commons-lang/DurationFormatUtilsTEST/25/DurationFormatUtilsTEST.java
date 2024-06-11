import org.junit.Test;
import static org.junit.Assert.*;

import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long duration = 3600000;
        String expectedResult = "1:00:00.000";
        String actualResult = DurationFormatUtils.formatDurationHMS(duration);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationISO() {
        long duration = 86400000;
        String expectedResult = "P1DT0H0M0S";
        String actualResult = DurationFormatUtils.formatDurationISO(duration);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDuration() {
        long duration = 120000;
        String format = "H:mm:ss.SSS";
        String expectedResult = "0:02:00.000";
        String actualResult = DurationFormatUtils.formatDuration(duration, format);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWithPaddingZeros() {
        long duration = 120000;
        String format = "H:mm:ss.S";
        String expectedResult = "0:02:00.1";
        String actualResult = DurationFormatUtils.formatDuration(duration, format, true);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFormatDurationWords() {
        long duration = 86401000;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expectedResult = "1 day 1 second";
        String actualResult = DurationFormatUtils.formatDurationWords(duration, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0;
        long endMillis = 86401000;
        String expectedResult = "P0Y0M1DT0H0M1.000S";
        String actualResult = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFormatPeriod() {
        long startMillis = 0;
        long endMillis = 36001000;
        String format = "y years M months d days H hours m minutes s seconds";
        TimeZone timezone = TimeZone.getDefault();
        String expectedResult = "0 years 0 months 0 days 10 hours 0 minutes 1 seconds";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timezone);
        assertEquals(expectedResult, actualResult);
    }

}