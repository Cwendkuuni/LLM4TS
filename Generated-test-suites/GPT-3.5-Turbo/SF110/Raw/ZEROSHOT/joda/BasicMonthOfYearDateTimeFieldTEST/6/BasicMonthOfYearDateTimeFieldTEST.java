package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.field.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 0; // replace with actual value
        int result = field.get(n);
        // replace with assertions
    }

    @Test
    public void testAddLongInt() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 0; // replace with actual value
        int n2 = 0; // replace with actual value
        long result = field.add(n, n2);
        // replace with assertions
    }

    @Test
    public void testAddLongLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 0; // replace with actual value
        long n2 = 0; // replace with actual value
        long result = field.add(n, n2);
        // replace with assertions
    }

    @Test
    public void testAddReadablePartialIntArrayInt() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        ReadablePartial readablePartial = null; // replace with actual value
        int n = 0; // replace with actual value
        int[] array = null; // replace with actual value
        int n2 = 0; // replace with actual value
        int[] result = field.add(readablePartial, n, array, n2);
        // replace with assertions
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 0; // replace with actual value
        int n2 = 0; // replace with actual value
        long result = field.addWrapField(n, n2);
        // replace with assertions
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 0; // replace with actual value
        long set = 0; // replace with actual value
        long result = field.getDifferenceAsLong(n, set);
        // replace with assertions
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 0; // replace with actual value
        int n2 = 0; // replace with actual value
        long result = field.set(n, n2);
        // replace with assertions
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        DurationField result = field.getRangeDurationField();
        // replace with assertions
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 0; // replace with actual value
        boolean result = field.isLeap(n);
        // replace with assertions
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 0; // replace with actual value
        int result = field.getLeapAmount(n);
        // replace with assertions
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        DurationField result = field.getLeapDurationField();
        // replace with assertions
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        int result = field.getMinimumValue();
        // replace with assertions
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        int result = field.getMaximumValue();
        // replace with assertions
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 0; // replace with actual value
        long result = field.roundFloor(n);
        // replace with assertions
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = 0; // replace with actual value
        long result = field.remainder(n);
        // replace with assertions
    }

    @Test
    public void testReadResolve() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        Object result = field.readResolve();
        // replace with assertions
    }
}