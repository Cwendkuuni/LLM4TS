import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_ReadableInstant_NullInstant1_ThrowsIllegalArgumentException() {
        try {
            BaseSingleFieldPeriod.between(null, new Instant(), DurationFieldType.years());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_ReadableInstant_NullInstant2_ThrowsIllegalArgumentException() {
        try {
            BaseSingleFieldPeriod.between(new Instant(), null, DurationFieldType.years());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_ValidInstants_ReturnsDifference() {
        ReadableInstant instant1 = new Instant(1000L);
        ReadableInstant instant2 = new Instant(2000L);
        
        int difference = BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.millis());
        
        assertEquals(1000, difference);
    }
    
    @Test
    public void testBetween_InvalidInstantChronology_ThrowsIllegalArgumentException() {
        ReadableInstant instant1 = new Instant(1000L);
        ReadableInstant instant2 = new Instant(2000L);
        
        try {
            BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.months());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot get chronology as Field is not supported: months", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_InvalidPartialFields_ThrowsIllegalArgumentException() {
        ReadablePartial partial1 = new LocalDate();
        ReadablePartial partial2 = new LocalDateTime();
        
        try {
            BaseSingleFieldPeriod.between(partial1, partial2, new Period());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_ValidPartials_ReturnsValue() {
        ReadablePartial partial1 = new LocalDate();
        ReadablePartial partial2 = new LocalDate(2020, 1, 1);
        
        int value = BaseSingleFieldPeriod.between(partial1, partial2, new Period());
        
        assertEquals(2020, value);
    }
    
    @Test
    public void testStandardPeriodIn_NullPeriod_ReturnsZero() {
        int result = BaseSingleFieldPeriod.standardPeriodIn(null, 1000L);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testStandardPeriodIn_PrecisionNotSupported_ThrowsIllegalArgumentException() {
        ReadablePeriod period = new Period(1, 2, 3, 4);
        
        try {
            BaseSingleFieldPeriod.standardPeriodIn(period, 1000L);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot convert period to duration as years is not precise in the period", e.getMessage());
        }
    }
    
    @Test
    public void testStandardPeriodIn_ValidPeriod_ReturnsValue() {
        ReadablePeriod period = new Period(2);
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000L);
        
        assertEquals(2000, result);
    }
    
    @Test
    public void testGetValue_ReturnsInitialValue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        int value = period.getValue();
        
        assertEquals(123, value);
    }
    
    @Test
    public void testSetValue_SetsValue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        period.setValue(456);
        
        assertEquals(456, period.getValue());
    }
    
    @Test
    public void testGetFieldType_ReturnsFieldType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        DurationFieldType fieldType = period.getFieldType();
        
        assertEquals(DurationFieldType.years(), fieldType);
    }
    
    @Test
    public void testGetPeriodType_ReturnsPeriodType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        PeriodType periodType = period.getPeriodType();
        
        assertEquals(PeriodType.years(), periodType);
    }
    
    @Test
    public void testSize_ReturnsOne() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        int size = period.size();
        
        assertEquals(1, size);
    }
    
    @Test
    public void testGetFieldType_IndexOutOfBounds_ThrowsIndexOutOfBoundsException() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        try {
            period.getFieldType(1);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }
    
    @Test
    public void testGetFieldType_ValidIndex_ReturnsFieldType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        DurationFieldType fieldType = period.getFieldType(0);
        
        assertEquals(DurationFieldType.years(), fieldType);
    }
    
    @Test
    public void testGetValue_IndexOutOfBounds_ThrowsIndexOutOfBoundsException() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        try {
            period.getValue(1);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }
    
    @Test
    public void testGetValue_ValidIndex_ReturnsValue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        int value = period.getValue(0);
        
        assertEquals(123, value);
    }
    
    @Test
    public void testGet_SameFieldType_ReturnsValue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        int value = period.get(DurationFieldType.years());
        
        assertEquals(123, value);
    }
    
    @Test
    public void testGet_DifferentFieldType_ReturnsZero() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        int value = period.get(DurationFieldType.months());
        
        assertEquals(0, value);
    }
    
    @Test
    public void testIsSupported_SameFieldType_ReturnsTrue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        boolean supported = period.isSupported(DurationFieldType.years());
        
        assertTrue(supported);
    }
    
    @Test
    public void testIsSupported_DifferentFieldType_ReturnsFalse() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        boolean supported = period.isSupported(DurationFieldType.months());
        
        assertFalse(supported);
    }
    
    @Test
    public void testToPeriod_ReturnsPeriod() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        Period result = period.toPeriod();
        
        assertEquals(Period.ZERO.withYears(123), result);
    }
    
    @Test
    public void testToMutablePeriod_ReturnsMutablePeriod() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        MutablePeriod result = period.toMutablePeriod();
        
        assertEquals(new MutablePeriod().withYears(123), result);
    }
    
    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        boolean equals = period.equals(period);
        
        assertTrue(equals);
    }
    
    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        
        boolean equals = period.equals(null);
        
        assertFalse(equals);
    }
    
    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(123);
        Object obj = new Object();
        
        boolean equals = period.equals(obj);
        
        assertFalse(equals);
    }
    
    @Test
    public void testEquals_DifferentPeriodType_ReturnsFalse() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod2(123);
        
        boolean equals = period1.equals(period2);
        
        assertFalse(equals);
    }
    
    @Test
    public void testEquals_DifferentValue_ReturnsFalse() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(456);
        
        boolean equals = period1.equals(period2);
        
        assertFalse(equals);
    }
    
    @Test
    public void testHashCode_SameValueAndFieldType_ReturnsSameHashCode() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(123);
        
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testHashCode_DifferentValue_ReturnsDifferentHashCode() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(456);
        
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        
        assertNotEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testHashCode_DifferentFieldType_ReturnsDifferentHashCode() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod2(123);
        
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        
        assertNotEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testCompareTo_DifferentClass_ThrowsClassCastException() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod2(123);
        
        try {
            period1.compareTo(period2);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            assertEquals("class BaseSingleFieldPeriod cannot be compared to class TestSingleFieldPeriod2", e.getMessage());
        }
    }
    
    @Test
    public void testCompareTo_LessThan_ReturnsNegativeValue() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(456);
        
        int result = period1.compareTo(period2);
        
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompareTo_GreaterThan_ReturnsPositiveValue() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(456);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(123);
        
        int result = period1.compareTo(period2);
        
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompareTo_Equal_ReturnsZero() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(123);
        
        int result = period1.compareTo(period2);
        
        assertEquals(0, result);
    }
    
    // Helper class for testing
    private static class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
        public TestSingleFieldPeriod(int value) {
            super(value);
        }
        
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.years();
        }
        
        @Override
        public PeriodType getPeriodType() {
            return PeriodType.years();
        }
    }
    
    // Helper class for testing
    private static class TestSingleFieldPeriod2 extends BaseSingleFieldPeriod {
        public TestSingleFieldPeriod2(int value) {
            super(value);
        }
        
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.months();
        }
        
        @Override
        public PeriodType getPeriodType() {
            return PeriodType.months();
        }
    }
}