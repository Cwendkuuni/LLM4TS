package org.joda.time;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {
    
    @Test
    public void testParse() {
        MutablePeriod expected = new MutablePeriod(1, 2, 3, 4);
        MutablePeriod result = MutablePeriod.parse("P1Y2M3W4D", ISOPeriodFormat.standard());
        assertEquals(expected, result);
    }
    
    @Test
    public void testConstructor() {
        MutablePeriod period = new MutablePeriod();
        assertNotNull(period);
    }
    
    @Test
    public void testClear() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        period.clear();
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
    }

    // Add more test cases for the remaining methods
    
}