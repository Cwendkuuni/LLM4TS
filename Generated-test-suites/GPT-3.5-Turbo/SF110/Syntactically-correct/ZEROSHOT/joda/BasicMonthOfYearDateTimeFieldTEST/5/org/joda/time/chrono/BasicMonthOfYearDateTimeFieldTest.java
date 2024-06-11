package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTimeFieldType;
import org.joda.time.field.FieldUtils;
import org.joda.time.DurationField;
import org.joda.time.DateTimeFieldType;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;

    @Before
    public void setUp() {
        BasicChronology chronology = new BasicChronology();
        int leapMonth = 12;
        field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    }

    @Test
    public void testConstructor() {
        assertEquals(DateTimeFieldType.monthOfYear(), field.getType());
        assertEquals(1, field.getMinimumValue());
        assertEquals(field.getMaximumValue(), field.getMaximumValue());
    }

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        assertEquals(1, field.get(0));
        assertEquals(12, field.get(11));
    }

    @Test
    public void testAddZero() {
        assertEquals(0, field.add(0, 0));
        assertEquals(11, field.add(11, 0));
    }

    @Test
    public void testAdd() {
        assertEquals(1, field.add(0, 1));
        assertEquals(0, field.add(11, 1));
        assertEquals(2, field.add(1, 1));
        assertEquals(11, field.add(0, -1));
        assertEquals(0, field.add(11, -1));
        assertEquals(12, field.add(11, 2));
        assertEquals(1, field.add(0, -2));
    }

    @Test
    public void testAddLarge() {
        assertEquals(0, field.add(0, Integer.MAX_VALUE));
        assertEquals(11, field.add(11, Integer.MAX_VALUE));
        assertEquals(2, field.add(1, Integer.MAX_VALUE));
    }
    
    @Test
    public void testAddLong() {
        assertEquals(1, field.add(0, 1L));
        assertEquals(0, field.add(11, 1L));
        assertEquals(2, field.add(1, 1L));
        assertEquals(11, field.add(0, -1L));
        assertEquals(0, field.add(11, -1L));
        assertEquals(12, field.add(11, 2L));
    }

    @Test
    public void testAddWrapField() {
        assertEquals(1, field.addWrapField(0, 1));
        assertEquals(0, field.addWrapField(11, 1));
        assertEquals(2, field.addWrapField(1, 1));
        assertEquals(11, field.addWrapField(0, -1));
        assertEquals(0, field.addWrapField(11, -1));
        assertEquals(0, field.addWrapField(11, 2));
        assertEquals(1, field.addWrapField(0, -2));
    }

    @Test
    public void testGetDifferenceAsLong() {
        assertEquals(-1, field.getDifferenceAsLong(0, 1));
        assertEquals(1, field.getDifferenceAsLong(1, 0));
        assertEquals(12, field.getDifferenceAsLong(11, 0));
    }

    @Test
    public void testSet() {
        assertEquals(0, field.set(1, 1));
        assertEquals(11, field.set(0, 12));
        assertEquals(1, field.set(0, 2));
        assertEquals(0, field.set(11, 12));
        assertEquals(11, field.set(11, 13));
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField durationField = field.getRangeDurationField();
        assertEquals(field.iChronology.years(), durationField);
    }

    @Test
    public void testIsLeap() {
        assertTrue(field.isLeap(0));
        assertFalse(field.isLeap(1));
        assertTrue(field.isLeap(11));
    }

    @Test
    public void testGetLeapAmount() {
        assertEquals(1, field.getLeapAmount(0));
        assertEquals(0, field.getLeapAmount(1));
        assertEquals(1, field.getLeapAmount(11));
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField durationField = field.getLeapDurationField();
        assertEquals(field.iChronology.days(), durationField);
    }

    @Test
    public void testRoundFloor() {
        assertEquals(0, field.roundFloor(1));
        assertEquals(0, field.roundFloor(11));
    }

    @Test
    public void testRemainder() {
        assertEquals(1, field.remainder(1));
        assertEquals(0, field.remainder(11));
    }

    @Test
    public void testReadResolve() {
        Object result = field.readResolve();
        assertEquals(field.iChronology.monthOfYear(), result);
    }

}