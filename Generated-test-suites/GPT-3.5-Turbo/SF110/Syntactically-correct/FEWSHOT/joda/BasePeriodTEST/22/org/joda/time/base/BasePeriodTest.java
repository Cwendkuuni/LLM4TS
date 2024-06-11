package org.joda.time.base;

import org.junit.Test;

public class BasePeriodTest {
    @Test
    public void testConstructor1() {
        // Test constructor with all zero values
        BasePeriod period = new BasePeriod(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals(0, period.getValue(0));
        assertEquals(0, period.getValue(1));
        assertEquals(0, period.getValue(2));
        assertEquals(0, period.getValue(3));
        assertEquals(0, period.getValue(4));
        assertEquals(0, period.getValue(5));
        assertEquals(0, period.getValue(6));
        assertEquals(0, period.getValue(7));
    }

    @Test
    public void testConstructor2() {
        // Test constructor with non-zero values
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(1, period.getValue(0));
        assertEquals(2, period.getValue(1));
        assertEquals(3, period.getValue(2));
        assertEquals(4, period.getValue(3));
        assertEquals(5, period.getValue(4));
        assertEquals(6, period.getValue(5));
        assertEquals(7, period.getValue(6));
        assertEquals(8, period.getValue(7));
    }

    @Test
    public void testConstructor3() {
        // Test constructor with null readablePeriod
        BasePeriod period = new BasePeriod(null);
        assertEquals(0, period.getValue(0));
        assertEquals(0, period.getValue(1));
        assertEquals(0, period.getValue(2));
        assertEquals(0, period.getValue(3));
        assertEquals(0, period.getValue(4));
        assertEquals(0, period.getValue(5));
        assertEquals(0, period.getValue(6));
        assertEquals(0, period.getValue(7));
    }
}