package org.apache.commons.lang3.time;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 3661000L;
        String expected = "1:01:01.000";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 3661000L;
        String expected = "PT1H1M1.000S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }
    
    // Add more test cases for other methods as necessary

}