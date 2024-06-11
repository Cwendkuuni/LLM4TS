package org.apache.commons.lang3.time;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long duration = 3600000; // 1 hour in milliseconds
        String expected = "1:00:00.000";
        String result = DurationFormatUtils.formatDurationHMS(duration);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long duration = 86400000; // 1 day in milliseconds
        String expected = "P0000Y0M1DT00H00M00.000S";
        String result = DurationFormatUtils.formatDurationISO(duration);
        assertEquals(expected, result);
    }

    // More test cases for other public methods can be added here

}