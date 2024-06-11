package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.field.ImpreciseDateTimeField;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BasicMonthOfYearDateTimeFieldTest {

    @Mock
    private BasicChronology mockChronology;

    private BasicMonthOfYearDateTimeField monthOfYearField;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        monthOfYearField = new BasicMonthOfYearDateTimeField(mockChronology, 6);
    }

    @Test
    public void testConstructor() {
        assertEquals(DateTimeFieldType.monthOfYear(), monthOfYearField.getType());
        assertEquals(30_000L, monthOfYearField.getAverageMillis());
        assertFalse(monthOfYearField.isLenient());
    }

    @Test
    public void testGet() {
        long input = 1_550_000_000L;
        int expected = 5;
        
        assertEquals(expected, monthOfYearField.get(input));
    }

    @Test
    public void testAdd_long_int_wrap() {
        long input = 1_550_000_000L;
        int amount = 6;
        long expected = 1_730_000_000L;
        
        assertEquals(expected, monthOfYearField.add(input, amount));
    }

    @Test
    public void testAdd_long_long_wrap() {
        long input = 1_550_000_000L;
        long amount = 6L;
        long expected = 1_730_000_000L;
        
        assertEquals(expected, monthOfYearField.add(input, amount));
    }

    @Test
    public void testAddWrapField_long_int_wrap() {
        long input = 1_550_000_000L;
        int amount = 6;
        long expected = 1_730_000_000L;
        
        assertEquals(expected, monthOfYearField.addWrapField(input, amount));
    }

    @Test
    public void testGetDifferenceAsLong_long_long() {
        long minuend = 1_600_000_000L;
        long subtrahend = 1_550_000_000L;
        long expected = 5;
        
        assertEquals(expected, monthOfYearField.getDifferenceAsLong(minuend , subtrahend));
    }

    @Test
    public void testSet_long_int() {
        long input = 1_550_000_000L;
        int value = 8;
        long expected = 1_624_000_000L;
        
        assertEquals(expected, monthOfYearField.set(input, value));
    }

    @Test
    public void testIsLeap() {
        long input = 1_550_000_000L;
        boolean expected = false;
        
        assertEquals(expected, monthOfYearField.isLeap(input));
    }

    @Test
    public void testGetLeapAmount() {
        long input = 1_550_000_000L;
        int expected = 0;
        
        assertEquals(expected, monthOfYearField.getLeapAmount(input));
    }

    @Test
    public void testGetMinimumValue() {
        int expected = 1;
        
        assertEquals(expected, monthOfYearField.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        int expected = 12;
        
        assertEquals(expected, monthOfYearField.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long input = 1_550_000_000L;
        long expected = 1_544_640_000L;
        
        assertEquals(expected, monthOfYearField.roundFloor(input));
    }

    @Test
    public void testRemainder() {
        long input = 1_550_000_000L;
        long expected = 5_360_000L;
        
        assertEquals(expected, monthOfYearField.remainder(input));
    }

    @Test
    public void testIsLenient() {
        assertFalse(monthOfYearField.isLenient());
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField expected = mockChronology.years();
        
        assertEquals(expected, monthOfYearField.getRangeDurationField());
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField expected = mockChronology.days();
        
        assertEquals(expected, monthOfYearField.getLeapDurationField());
    }
}