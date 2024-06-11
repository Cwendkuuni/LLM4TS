package org.joda.time.chrono;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.junit.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private static BasicMonthOfYearDateTimeField field;
    private static BasicChronology chronology;
    
    @BeforeClass
    public static void setUp() {
        chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 1);
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        long millis = new DateTime(2020, 5, 15, 0, 0, chronology).getMillis();
        assertEquals(5, field.get(millis));
    }
    
    @Test
    public void testAddWithZeroAmount() {
        long millis = new DateTime(2021, 10, 15, 0, 0, chronology).getMillis();
        assertEquals(millis, field.add(millis, 0));
    }
    
    @Test
    public void testAddWithPositiveAmount() {
        long millis = new DateTime(2021, 10, 15, 0, 0, chronology).getMillis();
        long expected = new DateTime(2022, 4, 15, 0, 0, chronology).getMillis();
        assertEquals(expected, field.add(millis, 6));
    }
    
    @Test
    public void testAddWithNegativeAmount() {
        long millis = new DateTime(2021, 10, 15, 0, 0, chronology).getMillis();
        long expected = new DateTime(2021, 4, 15, 0, 0, chronology).getMillis();
        assertEquals(expected, field.add(millis, -6));
    }
    
    @Test
    public void testAddWithLargeAmount() {
        long millis = new DateTime(2021, 10, 15, 0, 0, chronology).getMillis();
        try {
            field.add(millis, 50);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Magnitude of add amount is too large: 50", e.getMessage());
        }
    }
    
    @Test
    public void testAddAdditionalTypes() {
        DateTime dateTime = new DateTime(2021, 10, 15, 0, 0, chronology);
        LocalDate date = new LocalDate(dateTime);
        LocalDateTime dateTime2 = new LocalDateTime(dateTime);
        
        long millis = dateTime.getMillis();
        int[] values = {10, 5, 2022};
        int[] expected = {1, 5, 2023};
        assertArrayEquals(expected, field.add(date, 0, values, 12));
        assertArrayEquals(expected, field.add(dateTime2, 0, values, 12));
    }
    
    @Test
    public void testAddWrapField() {
        long millis = new DateTime(2021, 10, 15, 0, 0, chronology).getMillis();
        long expected = new DateTime(2021, 11, 15, 0, 0, chronology).getMillis();
        assertEquals(expected, field.addWrapField(millis, 1));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long millis1 = new DateTime(2021, 10, 15, 0, 0, chronology).getMillis();
        long millis2 = new DateTime(2022, 4, 15, 0, 0, chronology).getMillis();
        assertEquals(6, field.getDifferenceAsLong(millis2, millis1));
    }
    
    @Test
    public void testSet() {
        long millis = new DateTime(2021, 10, 15, 0, 0, chronology).getMillis();
        long expected = new DateTime(2021, 4, 15, 0, 0, chronology).getMillis();
        assertEquals(expected, field.set(millis, 4));
    }
    
    @Test
    public void testGetRangeDurationField() {
        assertEquals(chronology.years(), field.getRangeDurationField());
    }
    
    @Test
    public void testIsLeap() {
        long millis1 = new DateTime(2020, 2, 15, 0, 0, chronology).getMillis();
        long millis2 = new DateTime(2021, 2, 15, 0, 0, chronology).getMillis();
        long millis3 = new DateTime(2021, 1, 15, 0, 0, chronology).getMillis();
        assertTrue(field.isLeap(millis1));
        assertFalse(field.isLeap(millis2));
        assertFalse(field.isLeap(millis3));
    }
    
    @Test
    public void testGetLeapAmount() {
        long millis1 = new DateTime(2020, 2, 15, 0, 0, chronology).getMillis();
        long millis2 = new DateTime(2021, 2, 15, 0, 0, chronology).getMillis();
        assertEquals(1, field.getLeapAmount(millis1));
        assertEquals(0, field.getLeapAmount(millis2));
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
        long millis = new DateTime(2021, 10, 15, 0, 0, chronology).getMillis();
        long expected = new DateTime(2021, 10, 1, 0, 0, chronology).getMillis();
        assertEquals(expected, field.roundFloor(millis));
    }
    
    @Test
    public void testRemainder() {
        long millis = new DateTime(2021, 10, 15, 0, 0, chronology).getMillis();
        assertEquals(15, field.remainder(millis));
    }
    
    @Test
    public void testReadResolve() {
        assertSame(chronology.monthOfYear(), field.readResolve());
    }
    
}