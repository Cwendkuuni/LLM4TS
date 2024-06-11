package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MutablePeriodTest {
    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3W4D");
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());

        MutablePeriod period2 = MutablePeriod.parse("PT1H2M3S", ISOPeriodFormat.alternateExtended());
        assertEquals(0, period2.getYears());
        assertEquals(0, period2.getMonths());
        assertEquals(0, period2.getWeeks());
        assertEquals(0, period2.getDays());
        assertEquals(1, period2.getHours());
        assertEquals(2, period2.getMinutes());
        assertEquals(3, period2.getSeconds());
        assertEquals(0, period2.getMillis());
    }
        
    @Test
    public void testConstructor() {
        MutablePeriod period1 = new MutablePeriod();
        assertEquals(0, period1.getYears());
        assertEquals(0, period1.getMonths());
        assertEquals(0, period1.getWeeks());
        assertEquals(0, period1.getDays());
        assertEquals(0, period1.getHours());
        assertEquals(0, period1.getMinutes());
        assertEquals(0, period1.getSeconds());
        assertEquals(0, period1.getMillis());

        MutablePeriod period2 = new MutablePeriod(1, 2, 3, 4);
        assertEquals(1, period2.getYears());
        assertEquals(2, period2.getMonths());
        assertEquals(3, period2.getWeeks());
        assertEquals(4, period2.getDays());
        assertEquals(0, period2.getHours());
        assertEquals(0, period2.getMinutes());
        assertEquals(0, period2.getSeconds());
        assertEquals(0, period2.getMillis());
    }
    
    @Test
    public void testSetters() {
        MutablePeriod period = new MutablePeriod();

        period.setYears(1);
        period.setMonths(2);
        period.setWeeks(3);
        period.setDays(4);
        period.setHours(5);
        period.setMinutes(6);
        period.setSeconds(7);
        period.setMillis(8);

        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
        assertEquals(8, period.getMillis());
    }

    @Test
    public void testAdd() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);

        period.addYears(1);
        period.addMonths(2);
        period.addWeeks(3);
        period.addDays(4);
        period.addHours(5);
        period.addMinutes(6);
        period.addSeconds(7);
        period.addMillis(8);

        assertEquals(2, period.getYears());
        assertEquals(4, period.getMonths());
        assertEquals(6, period.getWeeks());
        assertEquals(8, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
        assertEquals(8, period.getMillis());
    }

    @Test
    public void testMergePeriod() {
        Period otherPeriod = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        MutablePeriod period = new MutablePeriod(9, 10, 11, 12);

        period.mergePeriod(otherPeriod);

        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
        assertEquals(8, period.getMillis());
    }

    @Test
    public void testCopy() {
        MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        MutablePeriod period2 = period1.copy();

        assertEquals(period1.getYears(), period2.getYears());
        assertEquals(period1.getMonths(), period2.getMonths());
        assertEquals(period1.getWeeks(), period2.getWeeks());
        assertEquals(period1.getDays(), period2.getDays());
        assertEquals(period1.getHours(), period2.getHours());
        assertEquals(period1.getMinutes(), period2.getMinutes());
        assertEquals(period1.getSeconds(), period2.getSeconds());
        assertEquals(period1.getMillis(), period2.getMillis());
    }

    // Add more test cases as needed...

}