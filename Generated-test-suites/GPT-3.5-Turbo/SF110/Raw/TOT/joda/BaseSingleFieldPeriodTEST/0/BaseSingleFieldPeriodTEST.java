import org.joda.time.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_WithValidInputs_ReturnsCorrectValue(){
        ReadableInstant instant1 = new DateTime(2019, 1, 1, 0, 0);
        ReadableInstant instant2 = new DateTime(2020, 1, 1, 0, 0);
        DurationFieldType fieldType = DurationFieldType.days();
        
        int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
        
        assertEquals(365, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_WithNullReadableInstant_ThrowsIllegalArgumentException(){
        ReadableInstant instant1 = null;
        ReadableInstant instant2 = new DateTime(2020, 1, 1, 0, 0);
        DurationFieldType fieldType = DurationFieldType.days();
        
        BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_WithNullFieldType_ThrowsIllegalArgumentException(){
        ReadableInstant instant1 = new DateTime(2019, 1, 1, 0, 0);
        ReadableInstant instant2 = new DateTime(2020, 1, 1, 0, 0);
        DurationFieldType fieldType = null;
        
        BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
    }
    
    @Test
    public void testBetween_ForNonContiguousReadablePartial_ThrowsIllegalArgumentException(){
        ReadablePartial partial1 = new YearMonth(2019, 1);
        ReadablePartial partial2 = new YearMonth(2020, 1);
        ReadablePeriod period = Period.days(30);
        
        try {
            int result = BaseSingleFieldPeriod.between(partial1, partial2, period);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must be contiguous", e.getMessage());
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_WithNullReadablePartial_ThrowsIllegalArgumentException(){
        ReadablePartial partial1 = null;
        ReadablePartial partial2 = new YearMonth(2020, 1);
        ReadablePeriod period = Period.months(1);
        
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_WithDifferentFieldTypes_ThrowsIllegalArgumentException(){
        ReadablePartial partial1 = new YearMonth(2019, 1);
        ReadablePartial partial2 = new YearMonth(2020, 1);
        ReadablePeriod period = Period.years(1);
        
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test
    public void testStandardPeriodIn_WithValidInputs_ReturnsCorrectValue(){
        ReadablePeriod period = Period.weeks(2);
        long n = 7;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        
        assertEquals(2, result);
    }
    
    @Test
    public void testStandardPeriodIn_WithNullReadablePeriod_ReturnsZero(){
        ReadablePeriod period = null;
        long n = 7;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        
        assertEquals(0, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_WithInvalidFieldType_ThrowsIllegalArgumentException(){
        ReadablePeriod period = Period.years(1);
        long n = 7;
        
        BaseSingleFieldPeriod.standardPeriodIn(period, n);
    }
    
    @Test
    public void testGetValue_WithInitializedValue_ReturnsCorrectValue(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        
        int result = baseSingleFieldPeriod.getValue();
        
        assertEquals(5, result);
    }
    
    @Test
    public void testSetValue_WithValidValue_UpdatesValue(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        
        baseSingleFieldPeriod.setValue(10);
        
        assertEquals(10, baseSingleFieldPeriod.getValue());
    }
    
    @Test
    public void testGetFieldType_WithValidFieldType_ReturnsCorrectFieldType(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        
        DurationFieldType result = baseSingleFieldPeriod.getFieldType();
        
        assertEquals(DurationFieldType.days(), result);
    }
    
    @Test
    public void testGetPeriodType_WithValidPeriodType_ReturnsCorrectPeriodType(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        
        PeriodType result = baseSingleFieldPeriod.getPeriodType();
        
        assertEquals(PeriodType.standard(), result);
    }
    
    @Test
    public void testGetSize_ReturnsOne(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        
        int result = baseSingleFieldPeriod.size();
        
        assertEquals(1, result);
    }
    
    @Test
    public void testGetFieldType_WithValidIndex_ReturnsCorrectFieldType(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        
        DurationFieldType result = baseSingleFieldPeriod.getFieldType(0);
        
        assertEquals(DurationFieldType.days(), result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_WithInvalidIndex_ThrowsIndexOutOfBoundsException(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        
        baseSingleFieldPeriod.getFieldType(1);
    }
    
    @Test
    public void testGetValue_WithValidIndex_ReturnsCorrectValue(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        
        int result = baseSingleFieldPeriod.getValue(0);
        
        assertEquals(5, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_WithInvalidIndex_ThrowsIndexOutOfBoundsException(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        
        baseSingleFieldPeriod.getValue(1);
    }
    
    @Test
    public void testGet_WithExistingFieldType_ReturnsCorrectValue(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.days();
        
        int result = baseSingleFieldPeriod.get(fieldType);
        
        assertEquals(5, result);
    }
    
    @Test
    public void testGet_WithNonExistingFieldType_ReturnsZero(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.months();
        
        int result = baseSingleFieldPeriod.get(fieldType);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testIsSupported_WithExistingFieldType_ReturnsTrue(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.days();
        
        boolean result = baseSingleFieldPeriod.isSupported(fieldType);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsSupported_WithNonExistingFieldType_ReturnsFalse(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.months();
        
        boolean result = baseSingleFieldPeriod.isSupported(fieldType);
        
        assertFalse(result);
    }
    
    @Test
    public void testToPeriod_WithValidInputs_ReturnsPeriodWithCorrectValues(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        
        Period result = baseSingleFieldPeriod.toPeriod();
        
        assertEquals(Period.days(5), result);
    }
    
    @Test
    public void testToMutablePeriod_WithValidInputs_ReturnsMutablePeriodWithCorrectValues(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        
        MutablePeriod result = baseSingleFieldPeriod.toMutablePeriod();
        
        assertEquals(MutablePeriod.days(5), result);
    }
    
    @Test
    public void testEquals_WithSameObject_ReturnsTrue(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        
        boolean result = baseSingleFieldPeriod.equals(baseSingleFieldPeriod);
        
        assertTrue(result);
    }
    
    @Test
    public void testEquals_WithNullObject_ReturnsFalse(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        
        boolean result = baseSingleFieldPeriod.equals(null);
        
        assertFalse(result);
    }
    
    @Test
    public void testEquals_WithDifferentClass_ReturnsFalse(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        Object object = new Object();
        
        boolean result = baseSingleFieldPeriod.equals(object);
        
        assertFalse(result);
    }
    
    @Test
    public void testEquals_WithSamePeriodTypeAndSameValue_ReturnsTrue(){
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new MockSingleFieldPeriod(5);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new MockSingleFieldPeriod(5);
        
        boolean result = baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2);
        
        assertTrue(result);
    }
    
    @Test
    public void testEquals_WithSamePeriodTypeAndDifferentValue_ReturnsFalse(){
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new MockSingleFieldPeriod(5);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new MockSingleFieldPeriod(10);
        
        boolean result = baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2);
        
        assertFalse(result);
    }
    
    @Test
    public void testHashCode_WithSamePeriodTypeAndSameValue_ReturnsSameHashCode(){
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new MockSingleFieldPeriod(5);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new MockSingleFieldPeriod(5);
        
        int hashCode1 = baseSingleFieldPeriod1.hashCode();
        int hashCode2 = baseSingleFieldPeriod2.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testHashCode_WithSamePeriodTypeAndDifferentValue_ReturnsDifferentHashCodes(){
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new MockSingleFieldPeriod(5);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new MockSingleFieldPeriod(10);
        
        int hashCode1 = baseSingleFieldPeriod1.hashCode();
        int hashCode2 = baseSingleFieldPeriod2.hashCode();
        
        assertNotEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testCompareTo_WithDifferentClass_ThrowsClassCastException(){
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
        Object object = new Object();
        
        try {
            baseSingleFieldPeriod.compareTo(object);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            assertEquals("class org.joda.time.base.MockSingleFieldPeriod cannot be compared to class java.lang.Object", e.getMessage());
        }
    }
    
    @Test
    public void testCompareTo_WithDifferentClass_ReturnsCorrectComparisonResult(){
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new MockSingleFieldPeriod(5);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new MockSingleFieldPeriod(10);
        
        int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        
        assertTrue(result < 0);
    }

}