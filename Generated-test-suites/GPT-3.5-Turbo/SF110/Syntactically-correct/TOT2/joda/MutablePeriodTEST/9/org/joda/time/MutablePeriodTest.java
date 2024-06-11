package org.joda.time;
import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.MutablePeriod;

public class MutablePeriodTest {

    @Test
    public void testConstructorNoArgs() {
        MutablePeriod period = new MutablePeriod();
        assertNotNull(period);
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testSetYears() {
        MutablePeriod period = new MutablePeriod();
        period.setYears(5);
        assertEquals(5, period.getYears());
    }

    @Test
    public void testAddYears() {
        MutablePeriod period = new MutablePeriod();
        period.addYears(3);
        assertEquals(3, period.getYears());
    }

    // Add more test cases for other methods as needed

}