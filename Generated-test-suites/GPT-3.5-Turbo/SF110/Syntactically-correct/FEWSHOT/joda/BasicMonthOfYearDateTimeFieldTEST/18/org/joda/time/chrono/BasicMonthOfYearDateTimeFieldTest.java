package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicChronology chronology;
    private BasicMonthOfYearDateTimeField monthOfYearField;

    @Before
    public void setUp() {
        chronology = new BasicChronology();
        monthOfYearField = new BasicMonthOfYearDateTimeField(chronology, 1);
    }

    @Test
    public void testIsLenient() {
        assertFalse(monthOfYearField.isLenient());
    }

    @Test
    public void testGet() {
        DateTime dt = new DateTime(2019, 1, 15, 0, 0, 0);
        assertEquals(1, monthOfYearField.get(dt.getMillis()));
    }

    @Test
    public void testAdd() {
        DateTime dt = new DateTime(2019, 1, 15, 0, 0, 0);
        long newDateTime = monthOfYearField.add(dt.getMillis(), 2);
        DateTime expected = new DateTime(2019, 3, 15, 0, 0, 0);
        assertEquals(expected.getMillis(), newDateTime);
    }

    @Test
    public void testAddWrapField() {
        DateTime dt = new DateTime(2019, 12, 15, 0, 0, 0);
        long newDateTime = monthOfYearField.addWrapField(dt.getMillis(), 2);
        DateTime expected = new DateTime(2019, 2, 15, 0, 0, 0);
        assertEquals(expected.getMillis(), newDateTime);
    }

    @Test
    public void testGetDifferenceAsLong() {
        DateTime dt1 = new DateTime(2019, 1, 15, 0, 0, 0);
        DateTime dt2 = new DateTime(2020, 3, 15, 0, 0, 0);
        long difference = monthOfYearField.getDifferenceAsLong(dt2.getMillis(), dt1.getMillis());
        assertEquals(14, difference);
    }

    @Test
    public void testSet() {
        DateTime dt = new DateTime(2019, 1, 15, 0, 0, 0);
        long newDateTime = monthOfYearField.set(dt.getMillis(), 5);
        DateTime expected = new DateTime(2019, 5, 15, 0, 0, 0);
        assertEquals(expected.getMillis(), newDateTime);
    }

    @Test
    public void testIsLeap() {
        DateTime dt = new DateTime(2020, 2, 15, 0, 0, 0);
        assertTrue(monthOfYearField.isLeap(dt.getMillis()));
    }

    @Test
    public void testGetLeapAmount() {
        DateTime dt = new DateTime(2020, 2, 15, 0, 0, 0);
        assertEquals(1, monthOfYearField.getLeapAmount(dt.getMillis()));
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(1, monthOfYearField.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(12, monthOfYearField.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        DateTime dt = new DateTime(2019, 1, 15, 12, 30, 0);
        long rounded = monthOfYearField.roundFloor(dt.getMillis());
        DateTime expected = new DateTime(2019, 1, 1, 0, 0, 0);
        assertEquals(expected.getMillis(), rounded);
    }

    @Test
    public void testRemainder() {
        DateTime dt = new DateTime(2019, 1, 15, 12, 30, 0);
        long remainder = monthOfYearField.remainder(dt.getMillis());
        assertEquals(15 * 60 * 60 * 1000, remainder);
    }

    // Additional test cases can be added here.

}