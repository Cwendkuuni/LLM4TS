package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField field;
    private BasicChronology chronology;
    
    public BasicMonthOfYearDateTimeFieldTest() {
        chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 12);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(DateTimeFieldType.monthOfYear(), field.getType());
        assertEquals(chronology.getAverageMillisPerMonth(), field.getDurationField().getMillis());
        assertFalse(field.isLenient());
        assertEquals(1, field.getMinimumValue());
        assertEquals(12, field.getMaximumValue());
        assertNotNull(field.getRangeDurationField());
        assertNotNull(field.getLeapDurationField());
        assertTrue(field.getLeapDurationField() instanceof DurationField);
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        MutableDateTime dt = new MutableDateTime(2020, 7, 1, 0, 0, 0, 0);
        assertEquals(7, field.get(dt.getMillis()));
    }
    
    @Test
    public void testAdd_int() {
        MutableDateTime dt = new MutableDateTime(2020, 7, 1, 0, 0, 0, 0);
        assertEquals(dt.getMillis(), field.add(dt.getMillis(), 0));
        assertEquals(dt.plusMonths(1).getMillis(), field.add(dt.getMillis(), 1));
        assertEquals(dt.plusMonths(3).getMillis(), field.add(dt.getMillis(), 3));
        assertEquals(dt.plusMonths(11).getMillis(), field.add(dt.getMillis(), 11));
        assertEquals(dt.plusYears(1).minusMonths(3).getMillis(), field.add(dt.getMillis(), 9));
        assertEquals(dt.plusYears(-1).minusMonths(3).getMillis(), field.add(dt.getMillis(), -3));
    }
    
    @Test
    public void testAdd_long() {
        MutableDateTime dt = new MutableDateTime(2020, 7, 1, 0, 0, 0, 0);
        assertEquals(dt.getMillis(), field.add(dt.getMillis(), 0L));
        assertEquals(dt.plusMonths(1).getMillis(), field.add(dt.getMillis(), 1L));
        assertEquals(dt.plusMonths(3).getMillis(), field.add(dt.getMillis(), 3L));
        assertEquals(dt.plusMonths(11).getMillis(), field.add(dt.getMillis(), 11L));
        assertEquals(dt.plusYears(1).minusMonths(3).getMillis(), field.add(dt.getMillis(), 9L));
        assertEquals(dt.plusYears(-1).minusMonths(3).getMillis(), field.add(dt.getMillis(), -3L));
    }
    
    @Test
    public void testAddWrapField_long_int() {
        MutableDateTime dt = new MutableDateTime(2020, 7, 1, 0, 0, 0, 0);
        assertEquals(dt.getMillis(), field.addWrapField(dt.getMillis(), 0));
        assertEquals(dt.plusMonths(1).getMillis(), field.addWrapField(dt.getMillis(), 1));
        assertEquals(dt.plusMonths(3).getMillis(), field.addWrapField(dt.getMillis(), 3));
        assertEquals(dt.plusMonths(11).getMillis(), field.addWrapField(dt.getMillis(), 11));
        assertEquals(dt.plusYears(1).minusMonths(3).getMillis(), field.addWrapField(dt.getMillis(), 9));
        assertEquals(dt.plusYears(-1).minusMonths(3).getMillis(), field.addWrapField(dt.getMillis(), -3));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        MutableDateTime dt1 = new MutableDateTime(2020, 7, 1, 0, 0, 0, 0);
        MutableDateTime dt2 = new MutableDateTime(2021, 7, 1, 0, 0, 0, 0);
        assertEquals(12L, field.getDifferenceAsLong(dt2.getMillis(), dt1.getMillis()));
        assertEquals(-12L, field.getDifferenceAsLong(dt1.getMillis(), dt2.getMillis()));
        
        dt2 = new MutableDateTime(2020, 6, 1, 0, 0, 0, 0);
        assertEquals(-1L, field.getDifferenceAsLong(dt2.getMillis(), dt1.getMillis()));
        assertEquals(1L, field.getDifferenceAsLong(dt1.getMillis(), dt2.getMillis()));
    }
    
    @Test
    public void testSet_long_int() {
        MutableDateTime dt1 = new MutableDateTime(2020, 7, 1, 0, 0, 0, 0);
        MutableDateTime dt2 = new MutableDateTime(2020, 6, 1, 0, 0, 0, 0);
        assertEquals(dt1.getMillis(), field.set(dt2.getMillis(), 7));
        dt2 = new MutableDateTime(2020, 8, 1, 0, 0, 0, 0);
        assertEquals(dt2.getMillis(), field.set(dt1.getMillis(), 8));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSet_long_int_tooLow() {
        MutableDateTime dt = new MutableDateTime(2020, 7, 1, 0, 0, 0, 0);
        field.set(dt.getMillis(), 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSet_long_int_tooHigh() {
        MutableDateTime dt = new MutableDateTime(2020, 7, 1, 0, 0, 0, 0);
        field.set(dt.getMillis(), 13);
    }
    
    @Test
    public void testRoundFloor() {
        MutableDateTime dt = new MutableDateTime(2020, 7, 15, 12, 30, 0, 0);
        assertEquals(new MutableDateTime(2020, 7, 1, 0, 0, 0, 0).getMillis(), field.roundFloor(dt.getMillis()));
    }
    
    @Test
    public void testRemainder() {
        MutableDateTime dt = new MutableDateTime(2020, 7, 15, 12, 30, 0, 0);
        assertEquals(15L * DateTimeUtils.MILLIS_PER_DAY + 12L * DateTimeUtils.MILLIS_PER_HOUR + 30L * DateTimeUtils.MILLIS_PER_MINUTE, field.remainder(dt.getMillis()));
    }
    
    @Test
    public void testAddReadablePartial_int_intArray_int() {
        MutableDateTime dt1 = new MutableDateTime(2020, 7, 15, 0, 0, 0, 0);
        MutableDateTime dt2 = new MutableDateTime(2020, 7, 15, 0, 0, 0, 0);
        assertEquals(dt2, field.add(ReadablePartial.EMPTY, 0, dt1.toDateTime()));
    }
    
    // Additional test cases can be added here
    
}