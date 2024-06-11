import org.joda.time.chrono.BasicChronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private static BasicChronology basicChronology;
    private static BasicMonthOfYearDateTimeField basicMonthOfYearDateTimeField;
    private static long testDateTime;
    
    @BeforeClass
    public static void setUp() {
        basicChronology = new BasicChronology();
        basicMonthOfYearDateTimeField = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        testDateTime = basicChronology.getYearMonthDayMillis(2021, 6, 15);
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(basicMonthOfYearDateTimeField.isLenient());
    }
    
    @Test
    public void testGet() {
        assertEquals(6, basicMonthOfYearDateTimeField.get(testDateTime));
    }
    
    @Test
    public void testAddIntZero() {
        assertEquals(testDateTime, basicMonthOfYearDateTimeField.add(testDateTime, 0));
    }
    
    @Test
    public void testAddIntPositive() {
        long expectedDateTime = basicChronology.getYearMonthDayMillis(2022, 4, 15);
        assertEquals(expectedDateTime, basicMonthOfYearDateTimeField.add(testDateTime, 10));
    }
    
    @Test
    public void testAddIntNegative() {
        long expectedDateTime = basicChronology.getYearMonthDayMillis(2020, 10, 15);
        assertEquals(expectedDateTime, basicMonthOfYearDateTimeField.add(testDateTime, -8));
    }
    
    @Test
    public void testAddLong() {
        long expectedDateTime = basicChronology.getYearMonthDayMillis(2022, 4, 15);
        assertEquals(expectedDateTime, basicMonthOfYearDateTimeField.add(testDateTime, 10L));
    }
    
    @Test
    public void testAddWrapField() {
        long expectedDateTime = basicChronology.getYearMonthDayMillis(2021, 8, 15);
        assertEquals(expectedDateTime, basicMonthOfYearDateTimeField.addWrapField(testDateTime, 2));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long testDateTime2 = basicChronology.getYearMonthDayMillis(2023, 6, 15);
        assertEquals(24, basicMonthOfYearDateTimeField.getDifferenceAsLong(testDateTime2, testDateTime));
    }
    
    @Test
    public void testSet() {
        long expectedDateTime = basicChronology.getYearMonthDayMillis(2021, 10, 15);
        assertEquals(expectedDateTime, basicMonthOfYearDateTimeField.set(testDateTime, 10));
    }
    
    @Test
    public void testIsLeap() {
        assertFalse(basicMonthOfYearDateTimeField.isLeap(testDateTime));
    }
    
    @Test
    public void testGetLeapAmount() {
        assertEquals(0, basicMonthOfYearDateTimeField.getLeapAmount(testDateTime));
    }
    
    @Test
    public void testGetMinimumValue() {
        assertEquals(1, basicMonthOfYearDateTimeField.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        assertEquals(12, basicMonthOfYearDateTimeField.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        long expectedDateTime = basicChronology.getYearMonthDayMillis(2021, 6, 1);
        assertEquals(expectedDateTime, basicMonthOfYearDateTimeField.roundFloor(testDateTime));
    }
    
    @Test
    public void testRemainder() {
        assertEquals(14, basicMonthOfYearDateTimeField.remainder(testDateTime));
    }
}