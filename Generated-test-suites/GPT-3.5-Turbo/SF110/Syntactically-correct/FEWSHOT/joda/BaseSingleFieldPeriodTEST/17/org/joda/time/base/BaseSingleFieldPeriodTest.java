
package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_ReadableInstant_ValidParameters_ReturnsCorrectValue() {
        DateTime startDateTime = new DateTime(1972, 1, 1, 0, 0, 0);
        DateTime endDateTime = new DateTime(1972, 1, 1, 0, 0, 5);
        int duration = BaseSingleFieldPeriod.between(startDateTime, endDateTime, DurationFieldType.seconds());
        assertEquals(5, duration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ReadableInstant_NullStartDateTime_ThrowsIllegalArgumentException() {
        DateTime endDateTime = new DateTime(1972, 1, 1, 0, 0, 5);
        BaseSingleFieldPeriod.between(null, endDateTime, DurationFieldType.seconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ReadableInstant_NullEndDateTime_ThrowsIllegalArgumentException() {
        DateTime startDateTime = new DateTime(1972, 1, 1, 0, 0, 0);
        BaseSingleFieldPeriod.between(startDateTime, null, DurationFieldType.seconds());
    }

    @Test
    public void testBetween_ReadablePartial_ValidParameters_ReturnsCorrectValue() {
        LocalDate startDate = new LocalDate(1972, 1, 1);
        LocalDate endDate = new LocalDate(1972, 1, 5);
        int duration = BaseSingleFieldPeriod.between(startDate, endDate, Period.years());
        assertEquals(4, duration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ReadablePartial_NullStartPartial_ThrowsIllegalArgumentException() {
        LocalDate endDate = new LocalDate(1972, 1, 5);
        BaseSingleFieldPeriod.between(null, endDate, Period.years());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ReadablePartial_NullEndPartial_ThrowsIllegalArgumentException() {
        LocalDate startDate = new LocalDate(1972, 1, 1);
        BaseSingleFieldPeriod.between(startDate, null, Period.years());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ReadablePartial_DifferentSizePartials_ThrowsIllegalArgumentException() {
        LocalDate startDate = new LocalDate(1972, 1, 1);
        LocalTime endTime = new LocalTime(0, 0, 0);
        BaseSingleFieldPeriod.between(startDate, endTime, Period.years());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ReadablePartial_DifferentTypePartials_ThrowsIllegalArgumentException() {
        LocalDate startDate = new LocalDate(1972, 1, 1);
        LocalDateTime endDate = new LocalDateTime(1972, 1, 5, 0, 0, 0);
        BaseSingleFieldPeriod.between(startDate, endDate, Period.years());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ReadablePartial_NonContiguousPartials_ThrowsIllegalArgumentException() {
        LocalDate startDate = new LocalDate(1972, 1, 1);
        LocalDate endDate = new LocalDate(1972, 1, 3);
        BaseSingleFieldPeriod.between(startDate, endDate, Period.years());
    }

    @Test
    public void testStandardPeriodIn_NullReadablePeriod_ReturnsZero() {
        int standardPeriod = BaseSingleFieldPeriod.standardPeriodIn(null, 1000);
        assertEquals(0, standardPeriod);
    }

    @Test
    public void testStandardPeriodIn_ValidReadablePeriod_ReturnsCorrectValue() {
        Period period = Period.years(2).withMonths(3).withWeeks(1).withDays(5);
        int standardPeriod = BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
        assertEquals(621, standardPeriod);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_PreciseFieldWithUnsafePeriod_ThrowsIllegalArgumentException() {
        Period period = Period.years(2).withMonths(3).withWeeks(1).withDays(5).withHours(2);
        BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
    }

    @Test
    public void testGetValue_ValidPeriod_ReturnsCorrectValue() {
        BaseSingleFieldPeriod singleFieldPeriod = new Years(5);
        int value = singleFieldPeriod.getValue();
        assertEquals(5, value);
    }

    @Test
    public void testSetValue_ValidValue_SetsValueCorrectly() {
        BaseSingleFieldPeriod singleFieldPeriod = new Years(5);
        singleFieldPeriod.setValue(10);
        int value = singleFieldPeriod.getValue();
        assertEquals(10, value);
    }

    @Test
    public void testGetFieldType_CorrectFieldTypeReturned() {
        BaseSingleFieldPeriod singleFieldPeriod = new Years(5);
        DurationFieldType fieldType = singleFieldPeriod.getFieldType();
        assertEquals(DurationFieldType.years(), fieldType);
    }

    @Test
    public void testGetPeriodType_CorrectPeriodTypeReturned() {
        BaseSingleFieldPeriod singleFieldPeriod = new Years(5);
        PeriodType periodType = singleFieldPeriod.getPeriodType();
        assertEquals(PeriodType.years(), periodType);
    }

    @Test
    public void testSize_ReturnsOne() {
        BaseSingleFieldPeriod singleFieldPeriod = new Years(5);
        int size = singleFieldPeriod.size();
        assertEquals(1, size);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_InvalidIndex_ThrowsIndexOutOfBoundsException() {
        BaseSingleFieldPeriod singleFieldPeriod = new Years(5);
        singleFieldPeriod.getFieldType(1);
    }

    @Test
    public void testGetValue_InvalidIndex_ThrowsIndexOutOfBoundsException() {
        BaseSingleFieldPeriod singleFieldPeriod = new Years(5);
        try {
            singleFieldPeriod.getValue(1);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void testGet_CorrectValueReturnedForMatchingFieldType() {
        BaseSingleFieldPeriod singleFieldPeriod = new Years(5);
        int value = singleFieldPeriod.get(DurationFieldType.years());
        assertEquals(5, value);
    }

    @Test
    public void testGet_ZeroReturnedForNonMatchingFieldType() {
        BaseSingleFieldPeriod singleFieldPeriod = new Years(5);
        int value = singleFieldPeriod.get(DurationFieldType.months());
        assertEquals(0, value);
    }

    @Test
    public void testIsSupported_TrueReturnedForMatchingFieldType() {
        BaseSingleFieldPeriod singleFieldPeriod = new Years(5);
        boolean supported = singleFieldPeriod.isSupported(DurationFieldType.years());
        assertTrue(supported);
    }

    @Test
    public void testIsSupported_FalseReturnedForNonMatchingFieldType() {
        BaseSingleFieldPeriod singleFieldPeriod = new Years(5);
        boolean supported = singleFieldPeriod.isSupported(DurationFieldType.months());
        assertFalse(supported);
    }

    @Test
    public void testToPeriod_CorrectPeriodReturned() {
        BaseSingleFieldPeriod singleFieldPeriod = new Years(5);
        Period period = singleFieldPeriod.toPeriod();
        assertEquals(Period.years(5), period);
    }

    @Test
    public void testToMutablePeriod_CorrectMutablePeriodReturned() {
        BaseSingleFieldPeriod singleFieldPeriod = new Years(5);
        MutablePeriod mutablePeriod = singleFieldPeriod.toMutablePeriod();
        assertEquals(Period.years(5), mutablePeriod.toPeriod());
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        BaseSingleFieldPeriod singleFieldPeriod = new Years(5);
        boolean result = singleFieldPeriod.equals(singleFieldPeriod);
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObjectsWithSameValue_ReturnsTrue() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new Years(5);
        BaseSingleFieldPeriod singleFieldPeriod2 = new Years(5);
        boolean result = singleFieldPeriod1.equals(singleFieldPeriod2);
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObjectsWithDifferentValue_ReturnsFalse() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new Years(5);
        BaseSingleFieldPeriod singleFieldPeriod2 = new Years(10);
        boolean result = singleFieldPeriod1.equals(singleFieldPeriod2);
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentObjectsWithDifferentFieldType_ReturnsFalse() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new Years(5);
        BaseSingleFieldPeriod singleFieldPeriod2 = new Months(5);
        boolean result = singleFieldPeriod1.equals(singleFieldPeriod2);
        assertFalse(result);
    }

    @Test
    public void testHashCode_SameValue_ReturnsEqualHashCodes() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new Years(5);
        BaseSingleFieldPeriod singleFieldPeriod2 = new Years(5);
        assertEquals(singleFieldPeriod1.hashCode(), singleFieldPeriod2.hashCode());
    }

    @Test
    public void testHashCode_DifferentValue_ReturnsDifferentHashCodes() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new Years(5);
        BaseSingleFieldPeriod singleFieldPeriod2 = new Years(10);
        assertNotEquals(singleFieldPeriod1.hashCode(), singleFieldPeriod2.hashCode());
    }

    @Test(expected = ClassCastException.class)
    public void testCompareTo_DifferentType_ThrowsClassCastException() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new Years(5);
        BaseSingleFieldPeriod singleFieldPeriod2 = new Months(5);
        singleFieldPeriod1.compareTo(singleFieldPeriod2);
    }

    @Test
    public void testCompareTo_SmallerValue_ReturnsNegative() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new Years(5);
        BaseSingleFieldPeriod singleFieldPeriod2 = new Years(10);
        int result = singleFieldPeriod1.compareTo(singleFieldPeriod2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareTo_GreaterValue_ReturnsPositive() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new Years(10);
        BaseSingleFieldPeriod singleFieldPeriod2 = new Years(5);
        int result = singleFieldPeriod1.compareTo(singleFieldPeriod2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareTo_SameValue_ReturnsZero() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new Years(5);
        BaseSingleFieldPeriod singleFieldPeriod2 = new Years(5);
        int result = singleFieldPeriod1.compareTo(singleFieldPeriod2);
        assertEquals(0, result);
    }
}
