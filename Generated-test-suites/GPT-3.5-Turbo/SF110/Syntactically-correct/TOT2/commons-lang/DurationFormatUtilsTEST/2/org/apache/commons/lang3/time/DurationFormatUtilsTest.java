package org.apache.commons.lang3.time;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {
    
    @Test
    public void testFormatDurationHMS() {
        assertEquals("0:00:00.000", DurationFormatUtils.formatDurationHMS(0));
        assertEquals("1:00:00.000", DurationFormatUtils.formatDurationHMS(3600000));
        assertEquals("1:30:20.123", DurationFormatUtils.formatDurationHMS(5420123));
    }
    
    @Test
    public void testFormatDurationISO() {
        assertEquals("P1970Y1M1DT0H0M0.000S", DurationFormatUtils.formatDurationISO(0));
        assertEquals("P1970Y1M1DT1H0M0.000S", DurationFormatUtils.formatDurationISO(3600000));
    }
    
    @Test
    public void testFormatDuration() {
        assertEquals("0", DurationFormatUtils.formatDuration(0, "s"));
        assertEquals("1", DurationFormatUtils.formatDuration(1000, "s"));
    }
    
    @Test
    public void testFormatDurationWords() {
        assertEquals("0 seconds", DurationFormatUtils.formatDurationWords(0, false, false));
        assertEquals("1 second", DurationFormatUtils.formatDurationWords(1000, false, false));
    }
    
    @Test
    public void testFormatPeriodISO() {
        assertEquals("P1970Y1M1DT0H0M0.000S", DurationFormatUtils.formatPeriodISO(0, 0));
        assertEquals("P1970Y1M1DT1H0M0.000S", DurationFormatUtils.formatPeriodISO(0, 3600000));
    }
    
    @Test
    public void testFormatPeriod() {
        assertEquals("0", DurationFormatUtils.formatPeriod(0, 0, "s"));
        assertEquals("1", DurationFormatUtils.formatPeriod(0, 1000, "s"));
    }

}