package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test constructor with int argument.
     */
    @Test
    public void testConstructorInt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    /**
     * Test getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    /**
     * Test getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.dayTime(), periodType);
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int size = period.size();
        assertEquals(1, size);
    }

    /**
     * Test getFieldType method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(1);
    }

    /**
     * Test getValue method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(1);
    }

    /**
     * Test get method with valid DurationFieldType.
     */
    @Test
    public void testGetValid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int value = period.get(DurationFieldType.DAYS);
        assertEquals(5, value);
    }

    /**
     * Test get method with invalid DurationFieldType.
     */
    @Test
    public void testGetInvalid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int value = period.get(DurationFieldType.HOURS);
        assertEquals(0, value);
    }

    /**
     * Test isSupported method with valid DurationFieldType.
     */
    @Test
    public void testIsSupportedValid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        boolean supported = period.isSupported(DurationFieldType.DAYS);
        assertTrue(supported);
    }

    /**
     * Test isSupported method with invalid DurationFieldType.
     */
    @Test
    public void testIsSupportedInvalid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        boolean supported = period.isSupported(DurationFieldType.HOURS);
        assertFalse(supported);
    }

    /**
     * Test toPeriod method.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(5, result.getHours());
    }

    /**
     * Test toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(5, result.getHours());
    }

    /**
     * Test equals method with null object.
     */
    @Test
    public void testEqualsNull() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertFalse(period.equals(null));
    }

    /**
     * Test equals method with different classes.
     */
    @Test
    public void testEqualsDifferentClass() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertFalse(period.equals(new Object()));
    }

    /**
     * Test equals method with same value.
     */
    @Test
    public void testEqualsSameValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
    }

    /**
     * Test equals method with different values.
     */
    @Test
    public void testEqualsDifferentValues() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        assertFalse(period1.equals(period2));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int hashCode = period.hashCode();
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int hashCode2 = period2.hashCode();
        assertEquals(hashCode, hashCode2);
    }

    /**
     * Test compareTo method with same value.
     */
    @Test
    public void testCompareToSameValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int result = period1.compareTo(period2);
        assertEquals(0, result);
    }

    /**
     * Test compareTo method with different values.
     */
    @Test
    public void testCompareToDifferentValues() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        int result = period1.compareTo(period2);
        assertTrue(result > 0);
    }

    /**
     * Test between method with valid arguments.
     */
    @Test
    public void testBetweenValid() {
        ReadableInstant start = DateTimeUtils.currentTime();
        ReadableInstant end = start.plus(Duration.standardHours(2));
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS);
        assertEquals(2, result);
    }

    /**
     * Test between method with invalid start argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidStart() {
        ReadableInstant start = null;
        ReadableInstant end = DateTimeUtils.currentTime();
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS);
    }

    /**
     * Test between method with invalid end argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidEnd() {
        ReadableInstant start = DateTimeUtils.currentTime();
        ReadableInstant end = null;
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS);
    }

    /**
     * Test between method with null zeroInstance argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullZeroInstance() {
        ReadablePartial start = new LocalDate();
        ReadablePartial end = new LocalDate();
        BaseSingleFieldPeriod.between(start, end, null);
    }

    /**
     * Test standardPeriodIn method with valid arguments.
     */
    @Test
    public void testStandardPeriodInValid() {
        ReadablePeriod period = Period. parse("PT1H30M");
        long millisPerUnit = 1000 * 60 * 60;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(3, result);
    }

    /**
     * Test standardPeriodIn method with null period argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNullPeriod() {
        long millisPerUnit = 1000 * 60 * 60;
        ReadablePeriod period = null;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }

}