package org.joda.time.base;

import org.joda.time.chrono.ISOChronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {
    @Test
    public void testBetween_WithValidParameters_ShouldReturnDifference() {
        ReadableInstant instant1 = new DateTime(2021, 1, 1, 0, 0, 0, 0);
        ReadableInstant instant2 = new DateTime(2022, 1, 1, 0, 0, 0, 0);
        DurationFieldType fieldType = DurationFieldType.years();

        int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);

        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_WithNullInstant_ShouldThrowIllegalArgumentException() {
        ReadableInstant instant1 = null;
        ReadableInstant instant2 = new DateTime(2022, 1, 1, 0, 0, 0, 0);
        DurationFieldType fieldType = DurationFieldType.years();

        BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_WithSameFieldTypes_ShouldThrowIllegalArgumentException() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1);
        ReadablePartial partial2 = new LocalDate(2022, 1, 1);
        ReadablePeriod period = Period.years(1);

        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_WithNonContiguousReadablePartial_ShouldThrowIllegalArgumentException() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1);
        ReadablePartial partial2 = new LocalDate(2022, 1, 1);
        ReadablePeriod period = Period.years(1);

        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test
    public void testStandardPeriodIn_WithValidReadablePeriodAndN_ShouldReturnPeriodInN() {
        ReadablePeriod period = Period.years(4);
        long n = 1;

        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);

        assertEquals(4, result);
    }

    @Test
    public void testStandardPeriodIn_WithNullReadablePeriod_ShouldReturnZero() {
        ReadablePeriod period = null;
        long n = 1;

        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);

        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_WithNonPreciseFieldType_ShouldThrowIllegalArgumentException() {
        ReadablePeriod period = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        long n = 1;

        BaseSingleFieldPeriod.standardPeriodIn(period, n);
    }

    @Test
    public void testGetValue_WithValidPeriod_ShouldReturnValue() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(5);

        int result = basePeriod.getValue();

        assertEquals(5, result);
    }

    @Test
    public void testSetValue_WithValidPeriod_ShouldSetPeriodValue() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(0);

        basePeriod.setValue(10);

        assertEquals(10, basePeriod.getValue());
    }

    @Test
    public void testSize_ShouldReturnOne() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(0);

        int result = basePeriod.size();

        assertEquals(1, result);
    }

    @Test
    public void testGetFieldType_ShouldReturnNotNull() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(0);

        DurationFieldType result = basePeriod.getFieldType();

        assertNotNull(result);
    }

    @Test
    public void testGetPeriodType_ShouldReturnNotNull() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(0);

        PeriodType result = basePeriod.getPeriodType();

        assertNotNull(result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_WithInvalidIndex_ShouldThrowIndexOutOfBoundsException() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(0);

        basePeriod.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_WithInvalidIndex_ShouldThrowIndexOutOfBoundsException() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(0);

        basePeriod.getValue(1);
    }

    @Test
    public void testGet_WithSameFieldType_ShouldReturnValue() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(5);
        DurationFieldType fieldType = new DurationFieldTypeStub();

        int result = basePeriod.get(fieldType);

        assertEquals(5, result);
    }

    @Test
    public void testGet_WithDifferentFieldType_ShouldReturnZero() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(5);
        DurationFieldType fieldType = new DurationFieldTypeStub();

        int result = basePeriod.get(DurationFieldType.days());

        assertEquals(0, result);
    }

    @Test
    public void testIsSupported_WithSameFieldType_ShouldReturnTrue() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(0);
        DurationFieldType fieldType = new DurationFieldTypeStub();

        boolean result = basePeriod.isSupported(fieldType);

        assertTrue(result);
    }

    @Test
    public void testIsSupported_WithDifferentFieldType_ShouldReturnFalse() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(0);
        DurationFieldType fieldType = new DurationFieldTypeStub();

        boolean result = basePeriod.isSupported(DurationFieldType.days());

        assertFalse(result);
    }

    @Test
    public void testToPeriod_ShouldReturnPeriodWithValues() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(5);

        Period result = basePeriod.toPeriod();

        assertEquals(5, result.getYears());
    }

    @Test
    public void testToMutablePeriod_ShouldReturnMutablePeriodWithValues() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(5);

        MutablePeriod result = basePeriod.toMutablePeriod();

        assertEquals(5, result.getYears());
    }

    @Test
    public void testEquals_WithSameInstance_ShouldReturnTrue() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(5);

        boolean result = basePeriod.equals(basePeriod);

        assertTrue(result);
    }

    @Test
    public void testEquals_WithDifferentClass_ShouldReturnFalse() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(5);
        Object obj = new Object();

        boolean result = basePeriod.equals(obj);

        assertFalse(result);
    }

    @Test
    public void testEquals_WithDifferentPeriod_ShouldReturnFalse() {
        BaseSingleFieldPeriod basePeriod1 = new BaseSingleFieldPeriodStub(5);
        BaseSingleFieldPeriod basePeriod2 = new BaseSingleFieldPeriodStub(10);

        boolean result = basePeriod1.equals(basePeriod2);

        assertFalse(result);
    }

    @Test
    public void testEquals_WithEqualPeriod_ShouldReturnTrue() {
        BaseSingleFieldPeriod basePeriod1 = new BaseSingleFieldPeriodStub(5);
        BaseSingleFieldPeriod basePeriod2 = new BaseSingleFieldPeriodStub(5);

        boolean result = basePeriod1.equals(basePeriod2);

        assertTrue(result);
    }

    @Test
    public void testHashCode_WithSamePeriod_ShouldReturnEqualHashCodes() {
        BaseSingleFieldPeriod basePeriod1 = new BaseSingleFieldPeriodStub(5);
        BaseSingleFieldPeriod basePeriod2 = new BaseSingleFieldPeriodStub(5);

        int hashCode1 = basePeriod1.hashCode();
        int hashCode2 = basePeriod2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_WithDifferentPeriods_ShouldReturnDifferentHashCodes() {
        BaseSingleFieldPeriod basePeriod1 = new BaseSingleFieldPeriodStub(5);
        BaseSingleFieldPeriod basePeriod2 = new BaseSingleFieldPeriodStub(10);

        int hashCode1 = basePeriod1.hashCode();
        int hashCode2 = basePeriod2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testCompareTo_WithEqualPeriods_ShouldReturnZero() {
        BaseSingleFieldPeriod basePeriod1 = new BaseSingleFieldPeriodStub(5);
        BaseSingleFieldPeriod basePeriod2 = new BaseSingleFieldPeriodStub(5);

        int result = basePeriod1.compareTo(basePeriod2);

        assertEquals(0, result);
    }

    @Test
    public void testCompareTo_WithLessThanPeriod_ShouldReturnNegativeValue() {
        BaseSingleFieldPeriod basePeriod1 = new BaseSingleFieldPeriodStub(5);
        BaseSingleFieldPeriod basePeriod2 = new BaseSingleFieldPeriodStub(10);

        int result = basePeriod1.compareTo(basePeriod2);

        assertTrue(result < 0);
    }

    @Test
    public void testCompareTo_WithGreaterThanPeriod_ShouldReturnPositiveValue() {
        BaseSingleFieldPeriod basePeriod1 = new BaseSingleFieldPeriodStub(10);
        BaseSingleFieldPeriod basePeriod2 = new BaseSingleFieldPeriodStub(5);

        int result = basePeriod1.compareTo(basePeriod2);

        assertTrue(result > 0);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareTo_WithDifferentClass_ShouldThrowClassCastException() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodStub(5);
        Object obj = new Object();

        basePeriod.compareTo(obj);
    }
}

class BaseSingleFieldPeriodStub extends BaseSingleFieldPeriod {
    public BaseSingleFieldPeriodStub(int iPeriod) {
        super(iPeriod);
    }

    @Override
    public DurationFieldType getFieldType() {
        return null;
    }

    @Override
    public PeriodType getPeriodType() {
        return null;
    }
}

class DurationFieldTypeStub extends DurationFieldType {
    protected DurationFieldTypeStub() {
        super("Stub");
    }

    @Override
    public DurationField getField(Chronology chronology) {
        return null;
    }
}