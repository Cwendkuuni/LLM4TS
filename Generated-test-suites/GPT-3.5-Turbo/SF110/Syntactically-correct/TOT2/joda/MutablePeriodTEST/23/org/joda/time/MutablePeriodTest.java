package org.joda.time;
import static org.junit.Assert.*;
import org.junit.Test;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P2Y");
        assertEquals(2, period.getYears());
    }
    
    @Test
    public void testConstructorWithDuration() {
        MutablePeriod period = new MutablePeriod(86400000L);
        assertEquals(1, period.getDays());
    }
    
    @Test
    public void testSetYears() {
        MutablePeriod period = new MutablePeriod(1, 1, 1, 1, 1, 1, 1, 1);
        period.setYears(5);
        assertEquals(5, period.getYears());
    }
    
    @Test
    public void testAddMonths() {
        MutablePeriod period = new MutablePeriod(1, 1, 1, 1, 1, 1, 1, 1);
        period.addMonths(6);
        assertEquals(7, period.getMonths());
    }
}