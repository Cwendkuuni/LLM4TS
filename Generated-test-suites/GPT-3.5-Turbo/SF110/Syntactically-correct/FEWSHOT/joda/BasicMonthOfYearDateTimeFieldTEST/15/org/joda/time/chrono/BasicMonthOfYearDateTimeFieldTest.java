package org.joda.time.chrono;

import org.joda.time.field.*;
import org.joda.time.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 1234567L;
        int expected = 7;
        int result = field.get(n);
        assertEquals(expected, result);
    }

    @Test
    public void testAddInt() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        
        long n = 1234567L;
        int value = 5;
        long expected = 1234567L + (5 * field.iChronology.getAverageMillisPerMonth());
        long result = field.add(n, value);
        assertEquals(expected, result);
        
        n = 1234567L;
        value = 0;
        result = field.add(n, value);
        assertEquals(n, result);
        
        n = 1234567L;
        value = -5;
        expected = 1234567L - (5 * field.iChronology.getAverageMillisPerMonth());
        result = field.add(n, value);
        assertEquals(expected, result);
    }

    @Test
    public void testAddLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        
        long n = 1234567L;
        long value = 5L;
        long expected = 1234567L + (5L * field.iChronology.getAverageMillisPerMonth());
        long result = field.add(n, value);
        assertEquals(expected, result);
        
        n = 1234567L;
        value = 0L;
        result = field.add(n, value);
        assertEquals(n, result);
        
        n = 1234567L;
        value = -5L;
        expected = 1234567L - (5L * field.iChronology.getAverageMillisPerMonth());
        result = field.add(n, value);
        assertEquals(expected, result);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 1234567L;
        int value = 5;
        long expected = 1234567L + (5 * field.iChronology.getAverageMillisPerMonth());
        long result = field.addWrapField(n, value);
        assertEquals(expected, result);
        
        n = 1234567L;
        value = 0;
        result = field.addWrapField(n, value);
        assertEquals(n, result);
        
        n = 1234567L;
        value = -5;
        expected = 1234567L - (5 * field.iChronology.getAverageMillisPerMonth());
        result = field.addWrapField(n, value);
        assertEquals(expected, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n1 = 1234567L;
        long n2 = 9876543L;
        long expected = (1998L - 1234L) * 12L + 5L;
        long result = field.getDifferenceAsLong(n2, n1);
        assertEquals(expected, result);
        
        n1 = 9876543L;
        n2 = 1234567L;
        expected = -(1998L - 1234L) * 12L + 5L;
        result = field.getDifferenceAsLong(n2, n1);
        assertEquals(expected, result);
        
        n1 = 1234567L;
        n2 = 1234567L;
        expected = 0L;
        result = field.getDifferenceAsLong(n2, n1);
        assertEquals(expected, result);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 1234567L;
        int value = 5;
        long expected = field.iChronology.getYearMonthDayMillis(1234, value, 1) + field.iChronology.getMillisOfDay(n);
        long result = field.set(n, value);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        int expected = 1;
        int result = field.getMinimumValue();
        assertEquals(expected, result);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        int expected = field.iMax;
        int result = field.getMaximumValue();
        assertEquals(expected, result);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 1234567L;
        long expected = field.iChronology.getYearMonthMillis(1234, 5);
        long result = field.roundFloor(n);
        assertEquals(expected, result);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 1234567L;
        long expected = n - field.roundFloor(n);
        long result = field.remainder(n);
        assertEquals(expected, result);
    }
}