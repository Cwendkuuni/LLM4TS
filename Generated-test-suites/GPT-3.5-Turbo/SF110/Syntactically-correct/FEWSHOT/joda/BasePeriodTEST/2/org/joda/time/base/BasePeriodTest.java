package org.joda.time.base;

import org.joda.time.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasePeriodTest {
    @Test
    public void testConstructor_with8Parameters() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertNotNull(period);
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
    public void testConstructor_with6Parameters() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, PeriodType.standard());
        assertNotNull(period);
        assertEquals(1, period.getValue(0));
        assertEquals(2, period.getValue(1));
        assertEquals(3, period.getValue(2));
        assertEquals(4, period.getValue(3));
        assertEquals(5, period.getValue(4));
        assertEquals(6, period.getValue(5));
        assertEquals(0, period.getValue(6));
        assertEquals(0, period.getValue(7));
    }

    @Test
    public void testConstructor_with4Parameters() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, PeriodType.standard());
        assertNotNull(period);
        assertEquals(1, period.getValue(0));
        assertEquals(2, period.getValue(1));
        assertEquals(3, period.getValue(2));
        assertEquals(4, period.getValue(3));
        assertEquals(0, period.getValue(4));
        assertEquals(0, period.getValue(5));
        assertEquals(0, period.getValue(6));
        assertEquals(0, period.getValue(7));
    }

    @Test
    public void testConstructor_with2Parameters() {
        BasePeriod period = new BasePeriod(1, 2, PeriodType.standard());
        assertNotNull(period);
        assertEquals(1, period.getValue(0));
        assertEquals(2, period.getValue(1));
        assertEquals(0, period.getValue(2));
        assertEquals(0, period.getValue(3));
        assertEquals(0, period.getValue(4));
        assertEquals(0, period.getValue(5));
        assertEquals(0, period.getValue(6));
        assertEquals(0, period.getValue(7));
    }

    @Test
    public void testConstructor_withReadableInstantAndDuration() {
        DateTime start = new DateTime(2020, 1, 1, 0, 0);
        DateTime end = new DateTime(2020, 1, 2, 0, 0);
        ReadablePeriod duration = new Period(start, end);
        
        BasePeriod period = new BasePeriod(start, duration, PeriodType.standard());
        assertNotNull(period);
        assertEquals(0, period.getValue(0));
        assertEquals(0, period.getValue(1));
        assertEquals(1, period.getValue(2));
        assertEquals(0, period.getValue(3));
        assertEquals(0, period.getValue(4));
        assertEquals(0, period.getValue(5));
        assertEquals(0, period.getValue(6));
        assertEquals(0, period.getValue(7));
    }

    @Test
    public void testConstructor_withReadableInstantAndReadableInstant() {
        DateTime start = new DateTime(2020, 1, 1, 0, 0);
        DateTime end = new DateTime(2020, 1, 2, 0, 0);
        
        BasePeriod period = new BasePeriod(start, end, PeriodType.standard());
        assertNotNull(period);
        assertEquals(0, period.getValue(0));
        assertEquals(0, period.getValue(1));
        assertEquals(1, period.getValue(2));
        assertEquals(0, period.getValue(3));
        assertEquals(0, period.getValue(4));
        assertEquals(0, period.getValue(5));
        assertEquals(0, period.getValue(6));
        assertEquals(0, period.getValue(7));
    }
}