package org.joda.time.chrono;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.joda.time.field.*;
import org.joda.time.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;
    private BasicChronology chronology;

    @Before
    public void setUp() {
        chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 12);
    }

    @Test
    public void testIsLenient() {
        Assert.assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long n = 1;
        Assert.assertEquals(1, field.get(n));

        n = 12345;
        Assert.assertEquals(9, field.get(n));
    }

    @Test
    public void testAddInt() {
        long n = 1;
        int n2 = 2;
        Assert.assertEquals(3, field.add(n, n2));
    }

    @Test
    public void testAddLong() {
        long n = 1;
        long n2 = 2;
        Assert.assertEquals(3, field.add(n, n2));
    }

    @Test
    public void testAddReadablePartial() {
        ReadablePartial readablePartial = new BasicReadablePartial();
        int n = 2;
        int[] array = new int[1];
        int n2 = 3;
        int[] expected = new int[1];
        expected[0] = 3;

        Assert.assertArrayEquals(expected, field.add(readablePartial, n, array, n2));
    }

    @Test
    public void testAddWrapField() {
        long n = 1;
        int n2 = 2;
        Assert.assertEquals(3, field.addWrapField(n, n2));
    }

    @Test
    public void testGetDifferenceAsLong() {
        long n = 1;
        long set = 2;
        Assert.assertEquals(-1, field.getDifferenceAsLong(n, set));
    }

    @Test
    public void testSet() {
        long n = 1;
        int n2 = 2;
        Assert.assertEquals(2, field.set(n, n2));
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField durationField = field.getRangeDurationField();
        Assert.assertNotNull(durationField);
        Assert.assertEquals(chronology.years(), durationField);
    }

    @Test
    public void testIsLeap() {
        long n = 1;
        Assert.assertFalse(field.isLeap(n));

        n = 12345;
        Assert.assertFalse(field.isLeap(n));
    }

    @Test
    public void testGetLeapAmount() {
        long n = 1;
        Assert.assertEquals(0, field.getLeapAmount(n));

        n = 12345;
        Assert.assertEquals(0, field.getLeapAmount(n));
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField durationField = field.getLeapDurationField();
        Assert.assertNotNull(durationField);
        Assert.assertEquals(chronology.days(), durationField);
    }

    @Test
    public void testGetMinimumValue() {
        Assert.assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        Assert.assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long n = 1;
        Assert.assertEquals(1, field.roundFloor(n));
    }

    @Test
    public void testRemainder() {
        long n = 1;
        Assert.assertEquals(0, field.remainder(n));
    }

}