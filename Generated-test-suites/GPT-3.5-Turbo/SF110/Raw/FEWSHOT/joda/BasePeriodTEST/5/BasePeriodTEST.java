package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.Duration;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.field.UnsupportedDateTimeField;
import org.junit.Before;
import org.junit.Test;

public class BasePeriodTest {
    private BasePeriod basePeriod;

    @Before
    public void setUp() {
        basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
    }

    @Test
    public void testConstructorWithIntegers() {
        assertEquals(1, basePeriod.getValue(0));
        assertEquals(2, basePeriod.getValue(1));
        assertEquals(3, basePeriod.getValue(2));
        assertEquals(4, basePeriod.getValue(3));
        assertEquals(5, basePeriod.getValue(4));
        assertEquals(6, basePeriod.getValue(5));
        assertEquals(7, basePeriod.getValue(6));
        assertEquals(8, basePeriod.getValue(7));
    }

    @Test
    public void testConstructorWithLongs() {
        basePeriod = new BasePeriod(1L, 2L, PeriodType.dayTime(), JulianChronology.getInstanceUTC());
        assertEquals(1, basePeriod.getValue(0));
        assertEquals(2, basePeriod.getValue(1));
    }

    @Test
    public void testConstructorWithReadableInstant() {
        basePeriod = new BasePeriod(ISOChronology.getInstanceUTC(), ISOChronology.getInstanceUTC());
        assertEquals(0, basePeriod.getValue(0));
        assertEquals(0, basePeriod.getValue(1));

        basePeriod = new BasePeriod(null, null, PeriodType.years());
        assertEquals(0, basePeriod.getValue(0));
        assertEquals(0, basePeriod.getValue(1));

        basePeriod = new BasePeriod(null, null, PeriodType.time().withMillisRemoved());
        assertEquals(0, basePeriod.getValue(0));
        assertEquals(0, basePeriod.getValue(1));
    }

    @Test
    public void testConstructorWithReadablePartial() {
        assertNull(basePeriod.getPeriodType());
        assertNull(basePeriod.getValue(0));
        assertNull(basePeriod.getValue(1));

        basePeriod = new BasePeriod(ISOChronology.getInstanceUTC(), LenientChronology.getInstanceUTC());
        assertEquals(0, basePeriod.getValue(0));
        assertEquals(0, basePeriod.getValue(1));

        basePeriod = new BasePeriod(null, null, PeriodType.time().withMillisRemoved());
        assertEquals(0, basePeriod.getValue(0));
        assertEquals(0, basePeriod.getValue(1));
    }

    @Test
    public void testConstructorWithReadableDuration() {
        basePeriod = new BasePeriod(new Duration(1000L), null, PeriodType.days());
        assertEquals(86400, basePeriod.getValue(0));
        assertEquals(0, basePeriod.getValue(1));
    }

    @Test
    public void testConstructorWithLong() {
        basePeriod = new BasePeriod(1000L);
        assertEquals(0, basePeriod.getValue(0));
        assertEquals(0, basePeriod.getValue(1));
        assertEquals(0, basePeriod.getValue(2));
        assertEquals(0, basePeriod.getValue(3));
        assertEquals(0, basePeriod.getValue(4));
        assertEquals(0, basePeriod.getValue(5));
        assertEquals(0, basePeriod.getValue(6));
        assertEquals(1000, basePeriod.getValue(7));
    }

    @Test
    public void testConstructorWithObject() {
        basePeriod = new BasePeriod(new Object(), null, JulianChronology.getInstanceUTC());
        assertEquals(0, basePeriod.getValue(0));
        assertEquals(0, basePeriod.getValue(1));
    }
}