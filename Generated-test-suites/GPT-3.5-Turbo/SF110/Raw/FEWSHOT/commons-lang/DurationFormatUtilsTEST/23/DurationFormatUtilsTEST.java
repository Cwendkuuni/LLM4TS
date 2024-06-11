package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DurationFormatUtilsTest {
    @Test
    public void testFormatDurationHMS() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0));
        assertEquals("0:00:01.000", DurationFormatUtils.formatDurationHMS(1000));
        assertEquals("0:01:00.000", DurationFormatUtils.formatDurationHMS(60000));
    }
    
    @Test
    public void testFormatDurationISO() {
        assertEquals("P1970Y01M01DT00H00M00.000S", DurationFormatUtils.formatDurationISO(0));
        assertEquals("P0001Y00M00DT00H00M01.000S", DurationFormatUtils.formatDurationISO(60000));
    }
    
    @Test
    public void testFormatDuration() {
        assertEquals("0", DurationFormatUtils.formatDuration(0, "d"));
        assertEquals("1", DurationFormatUtils.formatDuration(86400000, "d"));
        assertEquals("00", DurationFormatUtils.formatDuration(0, "dd"));
        assertNull(DurationFormatUtils.formatDuration(0, null));
    }
    
    @Test
    public void testFormatDurationWords() {
        assertEquals("0 days 00 hours 00 minutes 00 seconds", DurationFormatUtils.formatDurationWords(0, false, false));
        assertEquals("1 day 00 hours 00 minutes 01 second", DurationFormatUtils.formatDurationWords(61000, false, false));
    }
    
    @Test
    public void testFormatPeriodISO() {
        assertEquals("P0001Y00M00DT00H00M00.000S", DurationFormatUtils.formatPeriodISO(0, 60000));
        assertEquals("P0000Y00M00DT01H01M01.000S", DurationFormatUtils.formatPeriodISO(3661000, 3667000));
    }
    
    @Test
    public void testFormatPeriod() {
        assertEquals("00", DurationFormatUtils.formatPeriod(0, 60000, "dd"));
        assertNull(DurationFormatUtils.formatPeriod(0, 60000, null));
    }
}