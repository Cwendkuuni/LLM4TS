package org.joda.time.base;

import java.io.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_WithNullInputs_ThrowsIllegalArgumentException() {
        try {
            BaseSingleFieldPeriod.between(null, null, null);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testBetween_ReadableInstantReturnsDifference() {
        ReadableInstant instant1 = new DateTime(2020, 1, 1, 0, 0, 0);
        ReadableInstant instant2 = new DateTime(2021, 1, 1, 0, 0, 0);
        DurationFieldType fieldType = DurationFieldType.years();
        
        int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testBetween_ReadablePartialWithDifferentSetOfFields_ThrowsIllegalArgumentException() {
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDateTime(2021, 1, 1, 0, 0, 0);
        ReadablePeriod period = Period.months();
        
        try {
            BaseSingleFieldPeriod.between(partial1, partial2, period);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testBetween_ReadablePartialWithContiguousFields_ReturnsDifference() {
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDate(2021, 1, 1);
        ReadablePeriod period = Period.years();
        
        int result = BaseSingleFieldPeriod.between(partial1, partial2, period);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testStandardPeriodIn_WithNullReadablePeriod_ReturnsZero() {
        long n = 1;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(null, n);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testStandardPeriodIn_WithPreciseFields_ReturnsExpectedResult() {
        ReadablePeriod period = Period.years().withYears(2).withMonths(3);
        long n = 12;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        
        assertEquals(2, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_WithImpreciseFields_ThrowsIllegalArgumentException() {
        ReadablePeriod period = Period.weeks().withWeeks(2).withDays(3);
        long n = 7;
        
        BaseSingleFieldPeriod.standardPeriodIn(period, n);
    }
    
    @Test
    public void testGetValue_ReturnsInitialValue() {
        int iPeriod = 10;
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(iPeriod);
        
        int result = baseSingleFieldPeriod.getValue();
        
        assertEquals(iPeriod, result);
    }
    
    @Test
    public void testSetValue_SetsNewValue() {
        int iPeriod = 10;
        int newPeriod = 20;
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(iPeriod);
        
        baseSingleFieldPeriod.setValue(newPeriod);
        int result = baseSingleFieldPeriod.getValue();
        
        assertEquals(newPeriod, result);
    }
    
    @Test
    public void testSize_Returns1() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.size();
        
        assertEquals(1, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_WithInvalidIndex_ThrowsIndexOutOfBoundsException() {
        int n = 1;
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        baseSingleFieldPeriod.getFieldType(n);
    }
    
    @Test
    public void testGetFieldType_ReturnsExpectedFieldType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        DurationFieldType result = baseSingleFieldPeriod.getFieldType(0);
        
        assertEquals(DurationFieldType.years(), result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_WithInvalidIndex_ThrowsIndexOutOfBoundsException() {
        int n = 1;
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        baseSingleFieldPeriod.getValue(n);
    }
    
    @Test
    public void testGetValue_ReturnsExpectedValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.getValue(0);
        
        assertEquals(10, result);
    }
    
    @Test
    public void testGet_ReturnsExpectedValue() {
        DurationFieldType fieldType = DurationFieldType.years();
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.get(fieldType);
        
        assertEquals(10, result);
    }
    
    @Test
    public void testGet_WithDifferentType_ReturnsZero() {
        DurationFieldType fieldType = DurationFieldType.months();
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.get(fieldType);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testIsSupported_WithSameFieldType_ReturnsTrue() {
        DurationFieldType fieldType = DurationFieldType.years();
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        boolean result = baseSingleFieldPeriod.isSupported(fieldType);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsSupported_WithDifferentFieldType_ReturnsFalse() {
        DurationFieldType fieldType = DurationFieldType.months();
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        boolean result = baseSingleFieldPeriod.isSupported(fieldType);
        
        assertFalse(result);
    }
    
    @Test
    public void testToPeriod_ReturnsZeroPeriodWithFields() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        Period result = baseSingleFieldPeriod.toPeriod();
        
        assertEquals(Period.ZERO, result);
    }
    
    @Test
    public void testToMutablePeriod_ReturnsMutablePeriodWithFields() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        MutablePeriod result = baseSingleFieldPeriod.toMutablePeriod();
        
        assertNotNull(result);
        assertEquals(10, result.get(DurationFieldType.years()));
    }
    
    @Test
    public void testEquals_WithSameObject_ReturnsTrue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        boolean result = baseSingleFieldPeriod.equals(baseSingleFieldPeriod);
        
        assertTrue(result);
    }
    
    @Test
    public void testEquals_WithDifferentType_ReturnsFalse() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        Object obj = new Object();
        
        boolean result = baseSingleFieldPeriod.equals(obj);
        
        assertFalse(result);
    }
    
    @Test
    public void testEquals_WithDifferentReadablePeriod_ReturnsFalse() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        ReadablePeriod readablePeriod = Period.years().withYears(5);
        
        boolean result = baseSingleFieldPeriod.equals(readablePeriod);
        
        assertFalse(result);
    }
    
    @Test
    public void testEquals_WithSameReadablePeriod_ReturnsTrue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        ReadablePeriod readablePeriod = Period.years().withYears(10);
        
        boolean result = baseSingleFieldPeriod.equals(readablePeriod);
        
        assertTrue(result);
    }
    
    @Test
    public void testHashCode_ReturnsExpectedHashCode() {
        int iPeriod = 10;
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(iPeriod);
        
        int result = baseSingleFieldPeriod.hashCode();
        
        assertEquals(27 * (27 * 17 + iPeriod), result);
    }
    
    @Test
    public void testCompareTo_WithDifferentClass_ThrowsClassCastException() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod() {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        try {
            baseSingleFieldPeriod.compareTo(other);
            fail("Expected ClassCastException not thrown");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testCompareTo_WithGreaterValue_ReturnsPositive() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriodImpl(5);
        
        int result = baseSingleFieldPeriod.compareTo(other);
        
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompareTo_WithSmallerValue_ReturnsNegative() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(5);
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.compareTo(other);
        
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompareTo_WithSameValue_ReturnsZero() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.compareTo(other);
        
        assertEquals(0, result);
    }

    private class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {
        public BaseSingleFieldPeriodImpl(int iPeriod) {
            super(iPeriod);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.years();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }        
    }
}