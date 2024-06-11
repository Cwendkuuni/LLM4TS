package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.UnsupportedDateTimeField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    private static final long MONTH = 2629746000L; // Approximate number of milliseconds in a month
    
    private BasicChronology chronology;
    private BasicMonthOfYearDateTimeField field;
    
    @Before
    public void setUp() {
        chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 0);
    }
    
    @After
    public void tearDown() {
        chronology = null;
        field = null;
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        long millis = chronology.getYearMonthMillis(2019, 7, 20);
        assertEquals(7, field.get(millis));
    }
    
    @Test
    public void testAddWithZero() {
        long millis = chronology.getYearMonthMillis(2019, 7, 20);
        assertEquals(millis, field.add(millis, 0));
    }
    
    @Test
    public void testAddPositive() {
        long millis = chronology.getYearMonthMillis(2019, 7, 20);
        long newMillis = field.add(millis, 2);
        assertEquals(9, field.get(newMillis));
    }
    
    @Test
    public void testAddNegative() {
        long millis = chronology.getYearMonthMillis(2019, 7, 20);
        long newMillis = field.add(millis, -2);
        assertEquals(5, field.get(newMillis));
    }
    
    @Test
    public void testAddZeroFraction() {
        long millis = chronology.getYearMonthMillis(2019, 7, 20);
        long newMillis = field.add(millis, 2L * MONTH);
        assertEquals(7, field.get(newMillis));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddLargeMagnitude() {
        long millis = chronology.getYearMonthMillis(2019, 7, 20);
        field.add(millis, Long.MAX_VALUE);
    }
    
    @Test
    public void testAddWrapField() {
        long millis = chronology.getYearMonthMillis(2019, 7, 20);
        long newMillis = field.addWrapField(millis, 5);
        assertEquals(12, field.get(newMillis));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long millis1 = chronology.getYearMonthMillis(2019, 7, 20);
        long millis2 = chronology.getYearMonthMillis(2020, 5, 15);
        assertEquals(10, field.getDifferenceAsLong(millis1, millis2));
    }
    
    @Test
    public void testSet() {
        long millis = chronology.getYearMonthMillis(2019, 7, 20);
        long newMillis = field.set(millis, 11);
        assertEquals(11, field.get(newMillis));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidValue() {
        long millis = chronology.getYearMonthMillis(2019, 7, 20);
        field.set(millis, -5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidDayOfMonth() {
        long millis = chronology.getYearMonthMillis(2019, 7, 20);
        field.set(millis, 2);
    }
    
    @Test
    public void testGetRangeDurationField() {
        assertEquals(chronology.years(), field.getRangeDurationField());
    }
    
    @Test
    public void testIsLeap() {
        long millis1 = chronology.getYearMonthMillis(2019, 7, 20);
        long millis2 = chronology.getYearMonthMillis(2020, 7, 20);
        assertFalse(field.isLeap(millis1));
        assertTrue(field.isLeap(millis2));
    }
    
    @Test
    public void testGetLeapAmount() {
        long millis1 = chronology.getYearMonthMillis(2019, 7, 20);
        long millis2 = chronology.getYearMonthMillis(2020, 7, 20);
        assertEquals(0, field.getLeapAmount(millis1));
        assertEquals(1, field.getLeapAmount(millis2));
    }
    
    @Test
    public void testGetLeapDurationField() {
        assertEquals(chronology.days(), field.getLeapDurationField());
    }
    
    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        assertEquals(12, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        long millis = chronology.getYearMonthMillis(2019, 7, 20);
        assertEquals(2019, chronology.getYear(field.roundFloor(millis)));
        assertEquals(7, field.get(field.roundFloor(millis)));
    }
    
    @Test
    public void testRemainder() {
        long millis = chronology.getYearMonthMillis(2019, 7, 20);
        assertEquals(0L, field.remainder(millis));
    }
}