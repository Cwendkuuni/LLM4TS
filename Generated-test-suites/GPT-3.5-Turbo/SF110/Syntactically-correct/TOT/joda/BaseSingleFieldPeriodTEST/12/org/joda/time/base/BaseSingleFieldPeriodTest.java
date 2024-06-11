import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {
    
    @Test
    public void testBetween_withValidInputs_returnDifference() {
        ReadableInstant instant1 = new DateTime(2022, 1, 1, 0, 0);
        ReadableInstant instant2 = new DateTime(2022, 1, 1, 12, 0);
        DurationFieldType type = DurationFieldType.hours();
        
        int result = BaseSingleFieldPeriod.between(instant1, instant2, type);
        
        assertEquals(12, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_withNullInstant_throwIllegalArgumentException() {
        ReadableInstant instant1 = null;
        ReadableInstant instant2 = new DateTime(2022, 1, 1, 0, 0);
        DurationFieldType type = DurationFieldType.hours();
        
        BaseSingleFieldPeriod.between(instant1, instant2, type);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_withNullType_throwIllegalArgumentException() {
        ReadableInstant instant1 = new DateTime(2022, 1, 1, 0, 0);
        ReadableInstant instant2 = new DateTime(2022, 1, 1, 12, 0);
        DurationFieldType type = null;
        
        BaseSingleFieldPeriod.between(instant1, instant2, type);
    }
    
    @Test
    public void testBetween_withDifferentChronology_returnDifference() {
        ReadableInstant instant1 = new DateTime(2022, 1, 1, 0, 0);
        ReadableInstant instant2 = new DateTime(2022, 1, 2, 12, 0);
        DurationFieldType type = DurationFieldType.days();
        
        int result = BaseSingleFieldPeriod.between(instant1, instant2, type);
        
        assertEquals(2, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_withNotContiguousPartial_throwIllegalArgumentException() {
        ReadablePartial partial1 = new Partial(
            new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()},
            new int[] {2020, 1, 1}
        );
        ReadablePartial partial2 = new Partial(
            new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()},
            new int[] {2020, 2, 1}
        );
        ReadablePeriod period = Period.days(7);
        
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_withPartialsWithDifferentFields_throwIllegalArgumentException() {
        ReadablePartial partial1 = new Partial(
            new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()},
            new int[] {2020, 1, 1}
        );
        ReadablePartial partial2 = new Partial(
            new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.hourOfDay()},
            new int[] {2020, 1, 1, 12}
        );
        ReadablePeriod period = Period.hours(1);
        
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test
    public void testStandardPeriodIn_withValidInputs_returnPeriodInUnits() {
        ReadablePeriod period = Period.hours(2);
        long unitMillis = 60 * 60 * 1000;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, unitMillis);
        
        assertEquals(2, result);
    }
    
    @Test
    public void testStandardPeriodIn_withZeroPeriod_returnZero() {
        ReadablePeriod period = null;
        long unitMillis = 60 * 60 * 1000;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, unitMillis);
        
        assertEquals(0, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_withNonPreciseField_throwIllegalArgumentException() {
        ReadablePeriod period = Period.months(1);
        long unitMillis = 30 * 24 * 60 * 60 * 1000L;
        
        BaseSingleFieldPeriod.standardPeriodIn(period, unitMillis);
    }
    
    @Test
    public void testGetValue_withValidValue_returnValue() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        int result = period.getValue();
        
        assertEquals(10, result);
    }
    
    @Test
    public void testSetValue_withValidValue_setValue() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        period.setValue(20);
        
        assertEquals(20, period.getValue());
    }
    
    @Test
    public void testGetFieldType_withCustomFieldType_returnFieldType() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        DurationFieldType result = period.getFieldType();
        
        assertEquals(CustomFieldType.INSTANCE, result);
    }
    
    @Test
    public void testGetPeriodType_withCustomPeriodType_returnPeriodType() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        PeriodType result = period.getPeriodType();
        
        assertEquals(CustomPeriodType.INSTANCE, result);
    }
    
    @Test
    public void testSize_withBasePeriod_returnSize1() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        int result = period.size();
        
        assertEquals(1, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_withInvalidIndex_throwIndexOutOfBoundsException() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        period.getFieldType(1);
    }
    
    @Test
    public void testGetFieldType_withValidIndex_returnFieldType() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        DurationFieldType result = period.getFieldType(0);
        
        assertEquals(CustomFieldType.INSTANCE, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_withInvalidIndex_throwIndexOutOfBoundsException() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        period.getValue(1);
    }
    
    @Test
    public void testGetValue_withValidIndex_returnValue() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        int result = period.getValue(0);
        
        assertEquals(10, result);
    }
    
    @Test
    public void testGet_withMatchingFieldType_returnValue() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        DurationFieldType fieldType = CustomFieldType.INSTANCE;
        
        int result = period.get(fieldType);
        
        assertEquals(10, result);
    }
    
    @Test
    public void testGet_withNonMatchingFieldType_returnZero() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        DurationFieldType fieldType = DurationFieldType.secs();
        
        int result = period.get(fieldType);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testIsSupported_withMatchingFieldType_returnTrue() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        DurationFieldType fieldType = CustomFieldType.INSTANCE;
        
        boolean result = period.isSupported(fieldType);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsSupported_withNonMatchingFieldType_returnFalse() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        DurationFieldType fieldType = DurationFieldType.secs();
        
        boolean result = period.isSupported(fieldType);
        
        assertFalse(result);
    }
    
    @Test
    public void testToPeriod_withNonEmptyPeriod_returnPeriodWithField() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        Period result = period.toPeriod();
        
        assertTrue(result.containsField(CustomFieldType.INSTANCE));
        assertEquals(10, result.get(CustomFieldType.INSTANCE));
    }
    
    @Test
    public void testToMutablePeriod_withNonEmptyPeriod_returnMutablePeriodWithField() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        MutablePeriod result = period.toMutablePeriod();
        
        assertTrue(result.containsField(CustomFieldType.INSTANCE));
        assertEquals(10, result.get(CustomFieldType.INSTANCE));
    }
    
    @Test
    public void testEquals_withEqualPeriods_returnTrue() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new CustomSingleFieldPeriod(10);
        
        boolean result = period1.equals(period2);
        
        assertTrue(result);
    }
    
    @Test
    public void testEquals_withDifferentPeriods_returnFalse() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new CustomSingleFieldPeriod(20);
        
        boolean result = period1.equals(period2);
        
        assertFalse(result);
    }
    
    @Test
    public void testHashCode_withEqualPeriods_returnEqualHashCodes() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new CustomSingleFieldPeriod(10);
        
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testHashCode_withDifferentPeriods_returnDifferentHashCodes() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new CustomSingleFieldPeriod(20);
        
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        
        assertNotEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testCompareTo_withSamePeriod_returnZero() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new CustomSingleFieldPeriod(10);
        
        int result = period1.compareTo(period2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testCompareTo_withGreaterPeriod_returnPositiveNumber() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(20);
        BaseSingleFieldPeriod period2 = new CustomSingleFieldPeriod(10);
        
        int result = period1.compareTo(period2);
        
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompareTo_withSmallerPeriod_returnNegativeNumber() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new CustomSingleFieldPeriod(20);
        
        int result = period1.compareTo(period2);
        
        assertTrue(result < 0);
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareTo_withDifferentPeriodTypes_throwClassCastException() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new OtherCustomSingleFieldPeriod(10);
        
        period1.compareTo(period2);
    }
}