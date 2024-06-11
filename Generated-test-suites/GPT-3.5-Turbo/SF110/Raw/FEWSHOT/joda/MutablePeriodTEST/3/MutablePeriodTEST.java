package org.joda.time;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MutablePeriodTest {

    private MutablePeriod mutablePeriod;

    @Before
    public void setUp() {
        mutablePeriod = new MutablePeriod();
    }

    @Test
    public void testParse() {
        MutablePeriod parsedPeriod = MutablePeriod.parse("P1Y2M3D");
        assertEquals(1, parsedPeriod.getYears());
        assertEquals(2, parsedPeriod.getMonths());
        assertEquals(3, parsedPeriod.getDays());
    }

    @Test
    public void testSetYears() {
        mutablePeriod.setYears(1);
        assertEquals(1, mutablePeriod.getYears());
    }

    @Test
    public void testAddYears() {
        mutablePeriod.setYears(1);
        mutablePeriod.addYears(2);
        assertEquals(3, mutablePeriod.getYears());
    }

    @Test
    public void testSetMonths() {
        mutablePeriod.setMonths(1);
        assertEquals(1, mutablePeriod.getMonths());
    }

    @Test
    public void testAddMonths() {
        mutablePeriod.setMonths(1);
        mutablePeriod.addMonths(2);
        assertEquals(3, mutablePeriod.getMonths());
    }

    @Test
    public void testSetWeeks() {
        mutablePeriod.setWeeks(1);
        assertEquals(1, mutablePeriod.getWeeks());
    }

    @Test
    public void testAddWeeks() {
        mutablePeriod.setWeeks(1);
        mutablePeriod.addWeeks(2);
        assertEquals(3, mutablePeriod.getWeeks());
    }

    @Test
    public void testSetDays() {
        mutablePeriod.setDays(1);
        assertEquals(1, mutablePeriod.getDays());
    }

    @Test
    public void testAddDays() {
        mutablePeriod.setDays(1);
        mutablePeriod.addDays(2);
        assertEquals(3, mutablePeriod.getDays());
    }

    @Test
    public void testSetHours() {
        mutablePeriod.setHours(1);
        assertEquals(1, mutablePeriod.getHours());
    }

    @Test
    public void testAddHours() {
        mutablePeriod.setHours(1);
        mutablePeriod.addHours(2);
        assertEquals(3, mutablePeriod.getHours());
    }

    @Test
    public void testSetMinutes() {
        mutablePeriod.setMinutes(1);
        assertEquals(1, mutablePeriod.getMinutes());
    }

    @Test
    public void testAddMinutes() {
        mutablePeriod.setMinutes(1);
        mutablePeriod.addMinutes(2);
        assertEquals(3, mutablePeriod.getMinutes());
    }

    @Test
    public void testSetSeconds() {
        mutablePeriod.setSeconds(1);
        assertEquals(1, mutablePeriod.getSeconds());
    }

    @Test
    public void testAddSeconds() {
        mutablePeriod.setSeconds(1);
        mutablePeriod.addSeconds(2);
        assertEquals(3, mutablePeriod.getSeconds());
    }

    @Test
    public void testSetMillis() {
        mutablePeriod.setMillis(1);
        assertEquals(1, mutablePeriod.getMillis());
    }

    @Test
    public void testAddMillis() {
        mutablePeriod.setMillis(1);
        mutablePeriod.addMillis(2);
        assertEquals(3, mutablePeriod.getMillis());
    }

    @Test
    public void testCopy() {
        mutablePeriod.setYears(1);
        MutablePeriod copiedPeriod = mutablePeriod.copy();
        assertEquals(1, copiedPeriod.getYears());
    }
}