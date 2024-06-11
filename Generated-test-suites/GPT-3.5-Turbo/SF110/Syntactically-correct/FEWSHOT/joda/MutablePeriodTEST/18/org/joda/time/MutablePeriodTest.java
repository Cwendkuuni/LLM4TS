package org.joda.time;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        String s = "P1Y2M3W4DT5H6M7S";
        MutablePeriod mp = MutablePeriod.parse(s);
        assertEquals(mp.getYears(), 1);
        assertEquals(mp.getMonths(), 2);
        assertEquals(mp.getWeeks(), 3);
        assertEquals(mp.getDays(), 4);
        assertEquals(mp.getHours(), 5);
        assertEquals(mp.getMinutes(), 6);
        assertEquals(mp.getSeconds(), 7);
        assertNull(mp.getMillis());

        s = "PT10H5M30S";
        mp = MutablePeriod.parse(s);
        assertNull(mp.getYears());
        assertNull(mp.getMonths());
        assertNull(mp.getWeeks());
        assertNull(mp.getDays());
        assertEquals(mp.getHours(), 10);
        assertEquals(mp.getMinutes(), 5);
        assertEquals(mp.getSeconds(), 30);
        assertNull(mp.getMillis());
    }
    
    @Test
    public void testParseWithFormatter() {
        String s = "P1Y2M3W4DT5H6M7S";
        PeriodFormatter pf = ISOPeriodFormat.standard();
        MutablePeriod mp = MutablePeriod.parse(s, pf);
        assertEquals(mp.getYears(), 1);
        assertEquals(mp.getMonths(), 2);
        assertEquals(mp.getWeeks(), 3);
        assertEquals(mp.getDays(), 4);
        assertEquals(mp.getHours(), 5);
        assertEquals(mp.getMinutes(), 6);
        assertEquals(mp.getSeconds(), 7);
        assertNull(mp.getMillis());

        s = "PT10H5M30S";
        mp = MutablePeriod.parse(s, pf);
        assertNull(mp.getYears());
        assertNull(mp.getMonths());
        assertNull(mp.getWeeks());
        assertNull(mp.getDays());
        assertEquals(mp.getHours(), 10);
        assertEquals(mp.getMinutes(), 5);
        assertEquals(mp.getSeconds(), 30);
        assertNull(mp.getMillis());
    }

    @Test
    public void testDefaultConstructor() {
        MutablePeriod mp = new MutablePeriod();
        assertEquals(mp.getYears(), 0);
        assertEquals(mp.getMonths(), 0);
        assertEquals(mp.getWeeks(), 0);
        assertEquals(mp.getDays(), 0);
        assertEquals(mp.getHours(), 0);
        assertEquals(mp.getMinutes(), 0);
        assertEquals(mp.getSeconds(), 0);
        assertNull(mp.getMillis());
    }

    @Test
    public void testConstructorWithPeriodType() {
        PeriodType pt = PeriodType.standard();
        MutablePeriod mp = new MutablePeriod(pt);
        assertEquals(mp.getYears(), 0);
        assertEquals(mp.getMonths(), 0);
        assertEquals(mp.getWeeks(), 0);
        assertEquals(mp.getDays(), 0);
        assertEquals(mp.getHours(), 0);
        assertEquals(mp.getMinutes(), 0);
        assertEquals(mp.getSeconds(), 0);
        assertNull(mp.getMillis());
    }

    @Test
    public void testConstructorWithInts() {
        MutablePeriod mp = new MutablePeriod(1, 2, 3, 4);
        assertEquals(mp.getYears(), 1);
        assertEquals(mp.getMonths(), 2);
        assertEquals(mp.getWeeks(), 3);
        assertEquals(mp.getDays(), 4);
        assertEquals(mp.getHours(), 0);
        assertEquals(mp.getMinutes(), 0);
        assertEquals(mp.getSeconds(), 0);
        assertNull(mp.getMillis());

        mp = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(mp.getYears(), 1);
        assertEquals(mp.getMonths(), 2);
        assertEquals(mp.getWeeks(), 3);
        assertEquals(mp.getDays(), 4);
        assertEquals(mp.getHours(), 5);
        assertEquals(mp.getMinutes(), 6);
        assertEquals(mp.getSeconds(), 7);
        assertEquals(mp.getMillis(), 8);
    }

    // Add more test methods to comprehensively test all methods in MutablePeriod class

}