package org.joda.time.base;

import static org.junit.Assert.assertEquals;

import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

public class BasePeriodTest {

    private BasePeriod basePeriod;

    @Before
    public void setUp() {
        basePeriod = new BasePeriod();
    }

    @Test
    public void testGetPeriodType() {
        PeriodType periodType = basePeriod.getPeriodType();
        assertEquals(PeriodType.time(), periodType);
    }

    @Test
    public void testGetValue() {
        int value = basePeriod.getValue(0);
        assertEquals(0, value);
    }

    @Test
    public void testToDurationFrom() {
        //TODO: Test implementation
    }

    @Test
    public void testToDurationTo() {
        //TODO: Test implementation
    }

    @Test
    public void testCheckPeriodType() {
        //TODO: Test implementation
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPeriodNull() {
        basePeriod.setPeriod(null);
    }

    @Test
    public void testSetPeriodNotNull() {
        //TODO: Test implementation
    }

    @Test
    public void testSetPeriodInternal() {
        //TODO: Test implementation
    }

    @Test
    public void testSetPeriod() {
        //TODO: Test implementation
    }

    @Test
    public void testSetPeriodInternalIntegers() {
        //TODO: Test implementation
    }

    @Test
    public void testSetField() {
        //TODO: Test implementation
    }

    @Test
    public void testSetFieldInto() {
        //TODO: Test implementation
    }

    @Test
    public void testAddField() {
        //TODO: Test implementation
    }

    @Test
    public void testAddFieldInto() {
        //TODO: Test implementation
    }

    @Test
    public void testMergePeriod() {
        //TODO: Test implementation
    }

    @Test
    public void testMergePeriodInto() {
        //TODO: Test implementation
    }

    @Test
    public void testAddPeriod() {
        //TODO: Test implementation
    }

    @Test
    public void testAddPeriodInto() {
        //TODO: Test implementation
    }

    @Test
    public void testSetValue() {
        //TODO: Test implementation
    }

    @Test
    public void testSetValues() {
        //TODO: Test implementation
    }
}