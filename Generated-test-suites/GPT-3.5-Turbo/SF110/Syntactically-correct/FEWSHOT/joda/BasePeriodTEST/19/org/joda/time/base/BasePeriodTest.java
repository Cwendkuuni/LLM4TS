package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.joda.time.ReadablePeriod;
import org.junit.Before;
import org.junit.Test;

public class BasePeriodTest {
    private BasePeriod basePeriod;

    @Before
    public void setUp() {
        basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
    }

    @Test
    public void testGetPeriodType() {
        PeriodType periodType = basePeriod.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    @Test
    public void testGetValue() {
        int value1 = basePeriod.getValue(0);
        assertEquals(1, value1);

        int value2 = basePeriod.getValue(1);
        assertEquals(2, value2);

        int value3 = basePeriod.getValue(2);
        assertEquals(3, value3);

        int value4 = basePeriod.getValue(3);
        assertEquals(4, value4);

        int value5 = basePeriod.getValue(4);
        assertEquals(5, value5);

        int value6 = basePeriod.getValue(5);
        assertEquals(6, value6);

        int value7 = basePeriod.getValue(6);
        assertEquals(7, value7);

        int value8 = basePeriod.getValue(7);
        assertEquals(8, value8);
    }

    @Test
    public void testToDurationFrom() {
        Duration duration = basePeriod.toDurationFrom(new Instant(1000L));
        assertEquals(1000L, duration.getMillis());
    }

    @Test
    public void testToDurationTo() {
        Duration duration = basePeriod.toDurationTo(new Instant(2000L));
        assertEquals(2000L, duration.getMillis());
    }

    @Test
    public void testSetField() {
        basePeriod.setField(DurationFieldType.years(), 10);
        assertEquals(10, basePeriod.getValue(0));
    }

    @Test
    public void testAddField() {
        basePeriod.addField(DurationFieldType.years(), 5);
        assertEquals(6, basePeriod.getValue(0));
    }

    @Test
    public void testMergePeriod() {
        Period period = new Period(0, 1, 2, 3, 4, 5, 6, 7);
        basePeriod.mergePeriod(period);
        assertEquals(1, basePeriod.getValue(0));
        assertEquals(3, basePeriod.getValue(2));
        assertEquals(5, basePeriod.getValue(4));
        assertEquals(7, basePeriod.getValue(6));
    }

    @Test
    public void testAddPeriod() {
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        basePeriod.addPeriod(period);
        assertEquals(2, basePeriod.getValue(0));
        assertEquals(4, basePeriod.getValue(2));
        assertEquals(6, basePeriod.getValue(4));
        assertEquals(8, basePeriod.getValue(6));
    }

    @Test
    public void testSetValue() {
        basePeriod.setValue(0, 10);
        assertEquals(10, basePeriod.getValue(0));
    }

    @Test
    public void testSetValues() {
        int[] values = {10, 20, 30, 40, 50, 60, 70, 80};
        basePeriod.setValues(values);
        assertEquals(10, basePeriod.getValue(0));
        assertEquals(20, basePeriod.getValue(1));
        assertEquals(30, basePeriod.getValue(2));
        assertEquals(40, basePeriod.getValue(3));
        assertEquals(50, basePeriod.getValue(4));
        assertEquals(60, basePeriod.getValue(5));
        assertEquals(70, basePeriod.getValue(6));
        assertEquals(80, basePeriod.getValue(7));
    }
}