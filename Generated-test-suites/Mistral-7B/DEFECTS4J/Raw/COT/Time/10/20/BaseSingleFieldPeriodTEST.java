package org.joda.time.base.test;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        assertEquals(42, period.getValue());
    }

    /**
     * Test getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays();
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    /**
     * Test getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays();
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays();
        int size = period.size();
        assertEquals(1, size);
    }

    /**
     * Test getValue method.
     */
    @Test
    public void testgetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays(42);
        int value = period.getValue();
        assertEquals(42, value);
    }

    /**
     * Test get method with valid DurationFieldType.
     */
    @Test
    public void testGetValid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays(42);
        int value = period.get(DurationFieldType.DAYS);
        assertEquals(42, value);
    }

    /**
     * Test get method with invalid DurationFieldType.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetInvalid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays(42);
        period.get(DurationFieldType.MONTHS);
    }

    /**
     * Test isSupported method.
     */
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays();
        boolean supported = period.isSupported(DurationFieldType.DAYS);
        assertTrue(supported);
        boolean notSupported = period.isSupported(DurationFieldType.MONTHS);
        assertFalse(notSupported);
    }

    /**
     * Test toPeriod method.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays(42);
        Period expectedPeriod = new Period(42, PeriodUnit.DAYS);
        Period actualPeriod = period.toPeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }

    /**
     * Test toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays(42);
        MutablePeriod expectedPeriod = new MutablePeriod().setDays(42);
        MutablePeriod actualPeriod = period.toMutablePeriod();
        assertEquals(expectedPeriod, actualPeriod);
    }

    /**
     * Test equals method with equal object.
     */
    @Test
    public void testEqualsSame() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodDays(42);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodDays(42);
        assertTrue(period1.equals(period2));
    }

    /**
     * Test equals method with null object.
     */
    @Test
    public void testEqualsNull() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays(42);
        assertFalse(period.equals(null));
    }

    /**
     * Test equals method with different class.
     */
    @Test
    public void testEqualsDifferentClass() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays(42);
        assertFalse(period.equals(new Object()));
    }

    /**
     * Test equals method with different period type.
     */
    @Test
    public void testEqualsDifferentType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodDays(42);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodHours(42);
        assertFalse(period1.equals(period2));
    }

    /**
     * Test equals method with different value.
     */
    @Test
    public void testEqualsDifferentValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodDays(42);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodDays(43);
        assertFalse(period1.equals(period2));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodDays(42);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodDays(42);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    /**
     * Test compareTo method with equal objects.
     */
    @Test
    public void testCompareToEqual() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodDays(42);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodDays(42);
        int comparison = period1.compareTo(period2);
        assertEquals(0, comparison);
    }

    /**
     * Test compareTo method with greater object.
     */
    @Test
    public void testCompareToGreater() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodDays(43);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodDays(42);
        int comparison = period1.compareTo(period2);
        assertEquals(1, comparison);
    }

    /**
     * Test compareTo method with lesser object.
     */
    @Test
    public void testCompareToLesser() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodDays(42);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodDays(43);
        int comparison = period1.compareTo(period2);
        assertEquals(-1, comparison);
    }

    /**
     * Test between method with valid arguments.
     */
    @Test
    public void testBetweenValid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays(42);
        long startMillis = System.currentTimeMillis();
        long endMillis = startMillis + 24 * 60 * 60 * 1000; // one day
        int value = BaseSingleFieldPeriod.between(DateTimeUtils.asReadableInstant(startMillis), DateTimeUtils.asReadableInstant(endMillis), period.getFieldType());
        assertEquals(42, value);
    }

    /**
     * Test between method with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNull() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays(42);
        ReadableInstant start = null;
        int value = BaseSingleFieldPeriod.between(start, DateTimeUtils.asReadableInstant(System.currentTimeMillis()), period.getFieldType());
    }

    /**
     * Test between method with invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays(42);
        long startMillis = System.currentTimeMillis() - 24 * 60 * 60 * 1000; // one day ago
        int value = BaseSingleFieldPeriod.between(DateTimeUtils.asReadableInstant(startMillis), DateTimeUtils.asReadableInstant(System.currentTimeMillis()), period.getFieldType());
        assertEquals(-43, value); // should be -43, but Joda-Time returns -42
    }

    /**
     * Test standardPeriodIn method with valid arguments.
     */
    @Test
    public void testStandardPeriodInValid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays(42);
        long millisPerUnit = 1000 * 60 * 60 * 24; // one day in milliseconds
        int value = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(42, value);
    }

    /**
     * Test standardPeriodIn method with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNull() {
        int value = BaseSingleFieldPeriod.standardPeriodIn(null, 1000 * 60 * 60 * 24);
    }

    /**
     * Test standardPeriodIn method with invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInInvalid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodDays(42);
        long millisPerUnit = 1000; // invalid unit
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit));
    }
}