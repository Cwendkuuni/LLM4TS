
package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MutablePeriodTest {
    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3W4DT5H6M7S");
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
        assertEquals(5, period.getHours());
        assertEquals(6, period.getMinutes());
        assertEquals(7, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testSettersAndGetters() {
        MutablePeriod period = new MutablePeriod();

        period.setYears(1);
        assertEquals(1, period.getYears());
        period.addYears(2);
        assertEquals(3, period.getYears());

        period.setMonths(4);
        assertEquals(4, period.getMonths());
        period.addMonths(5);
        assertEquals(9, period.getMonths());

        period.setWeeks(6);
        assertEquals(6, period.getWeeks());
        period.addWeeks(7);
        assertEquals(13, period.getWeeks());

        period.setDays(8);
        assertEquals(8, period.getDays());
        period.addDays(9);
        assertEquals(17, period.getDays());

        period.setHours(10);
        assertEquals(10, period.getHours());
        period.addHours(11);
        assertEquals(21, period.getHours());

        period.setMinutes(12);
        assertEquals(12, period.getMinutes());
        period.addMinutes(13);
        assertEquals(25, period.getMinutes());

        period.setSeconds(14);
        assertEquals(14, period.getSeconds());
        period.addSeconds(15);
        assertEquals(29, period.getSeconds());

        period.setMillis(16);
        assertEquals(16, period.getMillis());
        period.addMillis(17);
        assertEquals(33, period.getMillis());
    }

    @Test
    public void testAdd() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        period.add(1, 2, 3, 4, 5, 6, 7, 8);
        
        assertEquals(2, period.getYears());
        assertEquals(4, period.getMonths());
        assertEquals(6, period.getWeeks());
        assertEquals(8, period.getDays());
        assertEquals(10, period.getHours());
        assertEquals(12, period.getMinutes());
        assertEquals(14, period.getSeconds());
        assertEquals(16, period.getMillis());
    }

    @Test
    public void testSetPeriod() {
        MutablePeriod period = new MutablePeriod();
        period.setPeriod(1, 2, 3, 4, 5, 6, 7, 8);

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
    public void testMergePeriod() {
        MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        MutablePeriod period2 = new MutablePeriod(9, 10, 11, 12, 13, 14, 15, 16);
        period1.mergePeriod(period2);

        assertEquals(10, period1.getYears());
        assertEquals(12, period1.getMonths());
        assertEquals(14, period1.getWeeks());
        assertEquals(16, period1.getDays());
        assertEquals(18, period1.getHours());
        assertEquals(20, period1.getMinutes());
        assertEquals(22, period1.getSeconds());
        assertEquals(24, period1.getMillis());
    }

    @Test
    public void testCopy() {
        MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        MutablePeriod period2 = period1.copy();

        assertEquals(1, period2.getYears());
        assertEquals(2, period2.getMonths());
        assertEquals(3, period2.getWeeks());
        assertEquals(4, period2.getDays());
        assertEquals(5, period2.getHours());
        assertEquals(6, period2.getMinutes());
        assertEquals(7, period2.getSeconds());
        assertEquals(8, period2.getMillis());
    }
}
