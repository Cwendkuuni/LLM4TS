import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_withTwoNonNullReadablesAndValidFieldType_returnsDifference() {
        ReadableInstant instant1 = new Instant(10);
        ReadableInstant instant2 = new Instant(20);
        DurationFieldType fieldType = DurationFieldType.millis();
        
        int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
        
        assertEquals(10, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_withFirstReadableNull_throwsIllegalArgumentException() {
        ReadableInstant instant1 = null;
        ReadableInstant instant2 = new Instant(20);
        DurationFieldType fieldType = DurationFieldType.millis();
        
        BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_withSecondReadableNull_throwsIllegalArgumentException() {
        ReadableInstant instant1 = new Instant(10);
        ReadableInstant instant2 = null;
        DurationFieldType fieldType = DurationFieldType.millis();
        
        BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
    }

    @Test
    public void testBetween_withBothReadablesNull_returnsZero() {
        ReadableInstant instant1 = null;
        ReadableInstant instant2 = null;
        DurationFieldType fieldType = DurationFieldType.millis();
        
        int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
        
        assertEquals(0, result);
    }

    @Test
    public void testBetween_withDifferentFieldType_throwsIllegalArgumentException() {
        ReadableInstant instant1 = new Instant(10);
        ReadableInstant instant2 = new Instant(20);
        DurationFieldType fieldType = DurationFieldType.seconds();
        
        try {
            BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must have the same set of fields", e.getMessage());
        }
    }

    @Test
    public void testBetween_withTwoNonNullReadablesAndValidFieldType_returnsDifference() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1).toPartial(Constants.MILLIS_ONLY);
        ReadablePartial partial2 = new LocalDate(2021, 1, 2).toPartial(Constants.MILLIS_ONLY);
        ReadablePeriod period = BaseSingleFieldPeriod.days();

        int result = BaseSingleFieldPeriod.between(partial1, partial2, period);

        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_withFirstPartialNull_throwsIllegalArgumentException() {
        ReadablePartial partial1 = null;
        ReadablePartial partial2 = new LocalDate(2021, 1, 2).toPartial(Constants.MILLIS_ONLY);
        ReadablePeriod period = BaseSingleFieldPeriod.days();

        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_withSecondPartialNull_throwsIllegalArgumentException() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1).toPartial(Constants.MILLIS_ONLY);
        ReadablePartial partial2 = null;
        ReadablePeriod period = BaseSingleFieldPeriod.days();

        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test
    public void testBetween_withDifferentPeriodFields_throwsIllegalArgumentException() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1).toPartial(Constants.MILLIS_ONLY);
        ReadablePartial partial2 = new LocalDate(2021, 1, 2).toPartial(Constants.SECONDS_ONLY);
        ReadablePeriod period = BaseSingleFieldPeriod.days();

        try {
            BaseSingleFieldPeriod.between(partial1, partial2, period);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_withNonContiguousPartials_throwsIllegalArgumentException() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1).toPartial(Constants.MILLIS_ONLY);
        ReadablePartial partial2 = new LocalDate(2021, 1, 3).toPartial(Constants.MILLIS_ONLY);
        ReadablePeriod period = BaseSingleFieldPeriod.days();

        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test
    public void testStandardPeriodIn_withNonNullReadablePeriodAndValidN_returnsResult() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();

        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 24 * 60 * 60 * 1000);

        assertEquals(1, result);
    }

    @Test
    public void testStandardPeriodIn_withNullReadablePeriod_returnsZero() {
        ReadablePeriod period = null;

        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 24 * 60 * 60 * 1000);

        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_withInpreciseFieldType_throwsIllegalArgumentException() {
        ReadablePeriod period = BaseSingleFieldPeriod.seconds();

        BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
    }

    @Test
    public void testGetValue_withDefaultValue_returnsInitialValue() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();
        
        int result = BaseSingleFieldPeriod.getValue(period);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testGetValue_withSetNonDefaultValue_returnsSetValue() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();
        BaseSingleFieldPeriod.setValue(period, 5);
        
        int result = BaseSingleFieldPeriod.getValue(period);
        
        assertEquals(5, result);
    }

    @Test
    public void testGetFieldType_withDefaultValue_returnsInitialFieldType() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();

        DurationFieldType result = BaseSingleFieldPeriod.getFieldType(period);

        assertEquals(DurationFieldType.days(), result);
    }

    @Test
    public void testGetFieldType_withNonDefaultValue_returnsInitialFieldType() {
        ReadablePeriod period = BaseSingleFieldPeriod.years();

        DurationFieldType result = BaseSingleFieldPeriod.getFieldType(period);

        assertEquals(DurationFieldType.years(), result);
    }

    @Test
    public void testSize_withDefaultValue_returnsOne() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();

        int result = BaseSingleFieldPeriod.size(period);

        assertEquals(1, result);
    }

    @Test
    public void testGetFieldType_withIndexZero_returnsFieldType() {
        ReadablePeriod period = BaseSingleFieldPeriod.seconds();

        DurationFieldType result = BaseSingleFieldPeriod.getFieldType(period, 0);

        assertEquals(DurationFieldType.seconds(), result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_withInvalidIndex_throwsIndexOutOfBoundsException() {
        ReadablePeriod period = BaseSingleFieldPeriod.years();

        BaseSingleFieldPeriod.getFieldType(period, 1);
    }

    @Test
    public void testGetValue_withIndexZero_returnsValue() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();
        BaseSingleFieldPeriod.setValue(period, 5);

        int result = BaseSingleFieldPeriod.getValue(period, 0);

        assertEquals(5, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_withInvalidIndex_throwsIndexOutOfBoundsException() {
        ReadablePeriod period = BaseSingleFieldPeriod.minutes();

        BaseSingleFieldPeriod.getValue(period, 1);
    }

    @Test
    public void testGet_withMatchingFieldType_returnsValue() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();
        BaseSingleFieldPeriod.setValue(period, 15);

        int result = BaseSingleFieldPeriod.get(period, DurationFieldType.days());

        assertEquals(15, result);
    }

    @Test
    public void testGet_withNonMatchingFieldType_returnsZero() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();
        BaseSingleFieldPeriod.setValue(period, 15);

        int result = BaseSingleFieldPeriod.get(period, DurationFieldType.hours());

        assertEquals(0, result);
    }

    @Test
    public void testIsSupported_withMatchingFieldType_returnsTrue() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();

        boolean result = BaseSingleFieldPeriod.isSupported(period, DurationFieldType.days());

        assertTrue(result);
    }

    @Test
    public void testIsSupported_withNonMatchingFieldType_returnsFalse() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();

        boolean result = BaseSingleFieldPeriod.isSupported(period, DurationFieldType.hours());

        assertFalse(result);
    }

    @Test
    public void testToPeriod_withDefaultValue_returnsZeroPeriod() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();

        Period result = BaseSingleFieldPeriod.toPeriod(period);

        assertEquals(Period.ZERO, result);
    }

    @Test
    public void testToMutablePeriod_withDefaultValue_returnsZeroPeriod() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();

        MutablePeriod result = BaseSingleFieldPeriod.toMutablePeriod(period);

        assertEquals(Period.ZERO, result);
    }

    @Test
    public void testEquals_withSameObject_returnsTrue() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();

        boolean result = BaseSingleFieldPeriod.equals(period, period);

        assertTrue(result);
    }

    @Test
    public void testEquals_withDifferentClass_returnsFalse() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();
        Object object = new Object();

        boolean result = BaseSingleFieldPeriod.equals(period, object);

        assertFalse(result);
    }

    @Test
    public void testEquals_withDifferentPeriodType_returnsFalse() {
        ReadablePeriod period1 = BaseSingleFieldPeriod.days();
        ReadablePeriod period2 = BaseSingleFieldPeriod.minutes();

        boolean result = BaseSingleFieldPeriod.equals(period1, period2);

        assertFalse(result);
    }

    @Test
    public void testEquals_withDifferentValue_returnsFalse() {
        ReadablePeriod period1 = BaseSingleFieldPeriod.days();
        BaseSingleFieldPeriod.setValue(period1, 5);

        ReadablePeriod period2 = BaseSingleFieldPeriod.days();
        BaseSingleFieldPeriod.setValue(period2, 10);

        boolean result = BaseSingleFieldPeriod.equals(period1, period2);

        assertFalse(result);
    }

    @Test
    public void testEquals_withSameValue_returnsTrue() {
        ReadablePeriod period1 = BaseSingleFieldPeriod.days();
        BaseSingleFieldPeriod.setValue(period1, 5);

        ReadablePeriod period2 = BaseSingleFieldPeriod.days();
        BaseSingleFieldPeriod.setValue(period2, 5);

        boolean result = BaseSingleFieldPeriod.equals(period1, period2);

        assertTrue(result);
    }

    @Test
    public void testHashCode_withSameObject_returnsSameHashCode() {
        ReadablePeriod period = BaseSingleFieldPeriod.days();

        int hashCode1 = BaseSingleFieldPeriod.hashCode(period);
        int hashCode2 = BaseSingleFieldPeriod.hashCode(period);

        assertEquals(hashCode1, hashCode2);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareTo_withDifferentClasses_throwsClassCastException() {
        BaseSingleFieldPeriod period1 = BaseSingleFieldPeriod.days();
        BaseSingleFieldPeriod period2 = BaseSingleFieldPeriod.minutes();

        period1.compareTo(period2);
    }

    @Test
    public void testCompareTo_withLowerValue_returnsNegative() {
        BaseSingleFieldPeriod period1 = BaseSingleFieldPeriod.days();
        BaseSingleFieldPeriod period2 = BaseSingleFieldPeriod.days();
        period1.setValue(5);
        period2.setValue(10);

        int result = period1.compareTo(period2);

        assertTrue(result < 0);
    }

    @Test
    public void testCompareTo_withHigherValue_returnsPositive() {
        BaseSingleFieldPeriod period1 = BaseSingleFieldPeriod.days();
        BaseSingleFieldPeriod period2 = BaseSingleFieldPeriod.days();
        period1.setValue(10);
        period2.setValue(5);

        int result = period1.compareTo(period2);

        assertTrue(result > 0);
    }

    @Test
    public void testCompareTo_withSameValue_returnsZero() {
        BaseSingleFieldPeriod period1 = BaseSingleFieldPeriod.days();
        BaseSingleFieldPeriod period2 = BaseSingleFieldPeriod.days();
        period1.setValue(5);
        period2.setValue(5);

        int result = period1.compareTo(period2);

        assertEquals(0, result);
    }
}