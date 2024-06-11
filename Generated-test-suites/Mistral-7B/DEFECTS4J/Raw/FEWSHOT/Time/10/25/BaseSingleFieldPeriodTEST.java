package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.BasePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        // Test with instant objects
        ReadableInstant instant1 = ISOChronology.getInstanceUTC().millisOfEpochMillis(0);
        ReadableInstant instant2 = ISOChronology.getInstanceUTC().millisOfEpochMillis(7 * 24 * 60 * 60 * 1000L); // 7 days
        int daysBetween = BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.DAYS);
        assertEquals(7, daysBetween);

        // Test with partial objects
        ReadablePartial partial1 = new MutablePeriod(new Period(1, 0, 0, 0, 0, 0, 0, PeriodType.standard()), PeriodType.standard()).toMutableBasePeriod();
        ReadablePartial partial2 = new MutablePeriod(new Period(6, 23, 59, 59, 59, 59, 59, PeriodType.standard()), PeriodType.standard()).toMutableBasePeriod();
        int hoursBetween = BaseSingleFieldPeriod.between(partial1, partial2, BaseSingleFieldPeriod.class);
        assertEquals(6 * 24 + 23, hoursBetween);
    }

    @Test
    public void testStandardPeriodIn() {
        // Test with a Period that only contains precise duration values
        ReadablePeriod period = new MutablePeriod(new Period(7, 0, 0, 0, 0, 0, 0, PeriodType.standard()), PeriodType.standard()).toImmutable();
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, 60 * 60 * 1000);
        assertEquals(168, hours); // 7 days * 24 hours

        // Test with a Period that contains imprecise duration values
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.setFields(new Period(2, 1, 0, 0, 1, 1, 1, PeriodType.standard()));
        try {
            int hoursFromImprecisePeriod = BaseSingleFieldPeriod.standardPeriodIn(mutablePeriod, 60 * 60 * 1000);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBaseSingleFieldPeriodConstructor() {
        // Test with a positive integer value
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        assertEquals(7, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());

        // Test with a negative integer value
        try {
            new BaseSingleFieldPeriod(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        int value = period.getValue();
        assertEquals(7, value);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        period.setValue(5);
        int value = period.getValue();
        assertEquals(5, value);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        int size = period.size();
        assertEquals(1, size);
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        DurationFieldType fieldType = period.getFieldType(0);
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        int value = period.getValue(1);
        fail("Expected IndexOutOfBoundsException");
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        int value = period.getValue();
        assertEquals(7, value);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        DurationFieldType fieldType = DurationFieldType.HOURS;
        boolean isSupported = period.isSupported(fieldType);
        assertFalse(isSupported);

        fieldType = DurationFieldType.DAYS;
        isSupported = period.isSupported(fieldType);
        assertTrue(isSupported);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        Period result = period.toPeriod();
        assertEquals(new Period(7, 0, 0, 0, 0, 0, 0, PeriodType.standard()), result);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(new MutablePeriod(new Period(7, 0, 0, 0, 0, 0, 0, PeriodType.standard()), PeriodType.standard()), result);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7);
        assertTrue(period1.equals(period2));

        period2 = new BaseSingleFieldPeriod(5);
        assertFalse(period1.equals(period2));

        Object other = new Object();
        assertFalse(period1.equals(other));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        int hashCode1 = period1.hashCode();

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7);
        int hashCode2 = period2.hashCode();

        assertEquals(hashCode1, hashCode2);

        period2 = new BaseSingleFieldPeriod(5);
        int hashCode3 = period2.hashCode();

        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);

        int comparisonResult = period1.compareTo(period2);
        assertEquals(2, comparisonResult);

        Comparable<BaseSingleFieldPeriod> period3 = new BaseSingleFieldPeriod(5);
        comparisonResult = period1.compareTo(period3);
        assertEquals(1, comparisonResult);

        period3 = new BaseSingleFieldPeriod(7);
        comparisonResult = period1.compareTo(period3);
        assertEquals(0, comparisonResult);
    }
}