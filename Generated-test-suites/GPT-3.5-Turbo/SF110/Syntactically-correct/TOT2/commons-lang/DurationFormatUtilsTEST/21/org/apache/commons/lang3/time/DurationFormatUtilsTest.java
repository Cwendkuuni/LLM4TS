package org.apache.commons.lang3.time;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0));
        assertEquals("1:00:00.000", DurationFormatUtils.formatDurationHMS(3600000));
        // Add more test cases for different input values
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals("P1970Y0M0DT0H0M0.000S", DurationFormatUtils.formatDurationISO(0));
        // Add more test cases for different input values
    }

    @Test
    public void testFormatDuration() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatDuration(0, "H:mm:ss.SSS"));
        // Add more test cases for different input values and format strings
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals("0 seconds", DurationFormatUtils.formatDurationWords(0, false, false));
        // Add more test cases for different input values and suppression options
    }

    @Test
    public void testFormatPeriodISO() {
        assertEquals("P1970Y0M0DT0H0M0.000S", DurationFormatUtils.formatPeriodISO(0, 0));
        // Add more test cases for different input values
    }

    @Test
    public void testFormatPeriod() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatPeriod(0, 0, "H:mm:ss.SSS"));
        // Add more test cases for different input values and format strings
    }

    // Add more test methods to cover edge cases and exception scenarios

}