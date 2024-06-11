package org.joda.time.chrono;

import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeFieldType;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.joda.time.BasicChronology;

import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    @InjectMocks
    BasicMonthOfYearDateTimeField basicMonthOfYearDateTimeField;

    @Mock
    BasicChronology iChronology;

    @org.junit.Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsLenient() {
        // Test Case 1
        assertTrue(basicMonthOfYearDateTimeField.isLenient());
    }

    @Test
    public void testGet() {
        // Test Case 1
        long n = 2022;
        assertEquals(7, basicMonthOfYearDateTimeField.get(n));

        // Test Case 2
        n = 2023;
        assertEquals(12, basicMonthOfYearDateTimeField.get(n));

        // Test Case 3
        n = 2024;
        assertEquals(2, basicMonthOfYearDateTimeField.get(n));
    }

    @Test
    public void testAddInt() {
        // Test Case 1
        long n = 2022;
        int n2 = 3;
        assertEquals(2022, basicMonthOfYearDateTimeField.add(n, n2));

        // Test Case 2
        n = 2023;
        n2 = 6;
        assertEquals(2023, basicMonthOfYearDateTimeField.add(n, n2));

        // Test Case 3
        n = 2024;
        n2 = 9;
        assertEquals(2024, basicMonthOfYearDateTimeField.add(n, n2));
    }

    @Test
    public void testAddLong() {
        // Test Case 1
        long n = 2022;
        long n2 = 3;
        assertEquals(2022, basicMonthOfYearDateTimeField.add(n, n2));

        // Test Case 2
        n = 2023;
        n2 = 6;
        assertEquals(2023, basicMonthOfYearDateTimeField.add(n, n2));

        // Test Case 3
        n = 2024;
        n2 = 9;
        assertEquals(2024, basicMonthOfYearDateTimeField.add(n, n2));
    }

    @Test
    public void testAddReadablePartial() {
        // Test Case 1
        ReadablePartial readablePartial = Mockito.mock(ReadablePartial.class);
        int n = 3;
        int[] array = {4, 5, 6};
        int n2 = 5;
        int[] result = {9, 10, 11};

        when(readablePartial.size()).thenReturn(3);
        when(readablePartial.getFieldType(0)).thenReturn(DateTimeFieldType.monthOfYear());
        when(readablePartial.getValue(0)).thenReturn(4);
        when(readablePartial.getFieldType(1)).thenReturn(DateTimeFieldType.monthOfYear());
        when(readablePartial.getValue(1)).thenReturn(5);
        when(readablePartial.getFieldType(2)).thenReturn(DateTimeFieldType.monthOfYear());
        when(readablePartial.getValue(2)).thenReturn(6);

        // Test Case 1
        assertEquals(result, basicMonthOfYearDateTimeField.add(readablePartial, n, array, n2));
    }

    @Test
    public void testAddWrapField() {
        // Test Case 1
        long n = 2022;
        int n2 = 5;
        assertEquals(2022, basicMonthOfYearDateTimeField.addWrapField(n, n2));

        // Test Case 2
        n = 2023;
        n2 = 8;
        assertEquals(2023, basicMonthOfYearDateTimeField.addWrapField(n, n2));

        // Test Case 3
        n = 2024;
        n2 = 11;
        assertEquals(2024, basicMonthOfYearDateTimeField.addWrapField(n, n2));
    }

    @Test
    public void testGetDifferenceAsLong() {
        // Test Case 1
        long n = 2022;
        long set = 2023;
        assertEquals(-11, basicMonthOfYearDateTimeField.getDifferenceAsLong(n, set));

        // Test Case 2
        n = 2023;
        set = 2024;
        assertEquals(-10, basicMonthOfYearDateTimeField.getDifferenceAsLong(n, set));

        // Test Case 3
        n = 2024;
        set = 2025;
        assertEquals(-9, basicMonthOfYearDateTimeField.getDifferenceAsLong(n, set));
    }

    @Test
    public void testSet() {
        // Test Case 1
        long n = 2022;
        int n2 = 3;
        assertEquals(2022, basicMonthOfYearDateTimeField.set(n, n2));

        // Test Case 2
        n = 2023;
        n2 = 6;
        assertEquals(2023, basicMonthOfYearDateTimeField.set(n, n2));

        // Test Case 3
        n = 2024;
        n2 = 9;
        assertEquals(2024, basicMonthOfYearDateTimeField.set(n, n2));
    }

    @Test
    public void testIsLeap() {
        // Test Case 1
        long n = 2022;
        assertTrue(basicMonthOfYearDateTimeField.isLeap(n));

        // Test Case 2
        n = 2023;
        assertTrue(basicMonthOfYearDateTimeField.isLeap(n));

        // Test Case 3
        n = 2024;
        assertFalse(basicMonthOfYearDateTimeField.isLeap(n));
    }

    @Test
    public void testGetLeapAmount() {
        // Test Case 1
        long n = 2022;
        assertEquals(1, basicMonthOfYearDateTimeField.getLeapAmount(n));

        // Test Case 2
        n = 2023;
        assertEquals(1, basicMonthOfYearDateTimeField.getLeapAmount(n));

        // Test Case 3
        n = 2024;
        assertEquals(0, basicMonthOfYearDateTimeField.getLeapAmount(n));
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
        // Test Case 1
        long n = 2022;
        assertEquals(2022, basicMonthOfYearDateTimeField.roundFloor(n));

        // Test Case 2
        n = 2023;
        assertEquals(2023, basicMonthOfYearDateTimeField.roundFloor(n));

        // Test Case 3
        n = 2024;
        assertEquals(2024, basicMonthOfYearDateTimeField.roundFloor(n));
    }

    @Test
    public void testRemainder() {
        // Test Case 1
        long n = 2022;
        assertEquals(0, basicMonthOfYearDateTimeField.remainder(n));

        // Test Case 2
        n = 2023;
        assertEquals(0, basicMonthOfYearDateTimeField.remainder(n));

        // Test Case 3
        n = 2024;
        assertEquals(0, basicMonthOfYearDateTimeField.remainder(n));
    }
}