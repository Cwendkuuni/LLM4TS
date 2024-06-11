import org.junit.Test;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BaseSingleFieldPeriodTest {

    /**
     * Test constructor with positive integer.
     */
    @Test
    public void testConstructor_int() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12);
        assertEquals(12, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Test constructor with negative integer.
     */
    @Test
    public void testConstructor_int_negative() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(-12);
        assertEquals(-12, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Test between method with Instant.
     */
    @Test
    public void testBetween_Instant_Instant_DurationFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        ReadableInstant start = DateTimeUtils.parseInstant("2022-01-01T00:00:00.000Z");
        ReadableInstant end = DateTimeUtils.parseInstant("2022-01-02T00:00:00.000Z");
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(6, result);
        result = period.getValue();
        assertEquals(5, result);
    }

    /**
     * Test between method with Instant and IllegalArgumentException.
     */
    @Test
    public void testBetween_Instant_null_DurationFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        ReadableInstant start = DateTimeUtils.parseInstant("2022-01-01T00:00:00.000Z");
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(start, null, DurationFieldType.DAYS));
    }

    /**
     * Test between method with Instant and IllegalArgumentException (invalid DurationFieldType).
     */
    @Test
    public void testBetween_Instant_Instant_invalid_DurationFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        ReadableInstant start = DateTimeUtils.parseInstant("2022-01-01T00:00:00.000Z");
        ReadableInstant end = DateTimeUtils.parseInstant("2022-01-02T00:00:00.000Z");
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(start, end, DurationFieldType.MONTHS));
    }

    /**
     * Test between method with Partial.
     */
    @Test
    public void testBetween_Partial_Partial_ReadablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        ReadablePartial start = new org.joda.time.LocalDate(2022, 1, 1);
        ReadablePartial end = new org.joda.time.LocalDate(2022, 1, 6);
        int result = BaseSingleFieldPeriod.between(start, end, period);
        assertEquals(6, result);
        result = period.getValue();
        assertEquals(5, result);
    }

    /**
     * Test between method with Partial and IllegalArgumentException.
     */
    @Test
    public void testBetween_Partial_null_ReadablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        ReadablePartial start = new org.joda.time.LocalDate(2022, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(start, null, period));
    }

    /**
     * Test between method with Partial and IllegalArgumentException (invalid period).
     */
    @Test
    public void testBetween_Partial_Partial_invalid_ReadablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        ReadablePartial start = new org.joda.time.LocalDate(2022, 1, 1);
        ReadablePartial end = new org.joda.time.readable.LocalDate(2022, 1, 6, null);
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(start, end, period));
    }

    /**
     * Test standardPeriodIn method with Period.
     */
    @Test
    public void testStandardPeriodIn_ReadablePeriod_long() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        ReadablePeriod testPeriod = new Period(1, 2, 3, 4, 5, 6, 7);
        long millisPerUnit = 86400000;
        int result = BaseSingleFieldPeriod.standardPeriodIn(testPeriod, millisPerUnit);
        assertEquals(32, result);
        result = period.getValue();
        assertEquals(5, result);
    }

    /**
     * Test standardPeriodIn method with null Period.
     */
    @Test
    public void testStandardPeriodIn_null_long() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertNull(BaseSingleFieldPeriod.standardPeriodIn(null, 86400000));
        result = period.getValue();
        assertEquals(5, result);
    }

    /**
     * Test standardPeriodIn method with imprecise Period.
     */
    @Test
    public void testStandardPeriodIn_ReadablePeriod_invalid_long() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        ReadablePeriod testPeriod = new Period(1, 2, new Period(3, 4));
        long millisPerUnit = 86400000;
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.standardPeriodIn(testPeriod, millisPerUnit));
        result = period.getValue();
        assertEquals(5, result);
    }

    /**
     * Test value getter and setter.
     */
    @Test
    public void testValue_int() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int getValue = period.getValue();
        assertEquals(5, getValue);
        period.setValue(10);
        getValue = period.getValue();
        assertEquals(10, getValue);
    }

    /**
     * Test value setter and immutability.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testValue_int_immutable() {
        final BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(10);
    }

    /**
     * Test getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    /**
     * Test getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    /**
     * Test getFieldType method with index out of bounds exception.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_int_IndexOutOfBoundsException() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(2);
    }

    /**
     * Test getValue method with index out of bounds exception.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testValue_int_IndexOutOfBoundsException() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(2);
    }

    /**
     * Test get method.
     */
    @Test
    public void testGet_DurationFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int result = period.get(DurationFieldType.DAYS);
        assertEquals(5, result);
    }

    /**
     * Test get method with invalid DurationFieldType.
     */
    @Test
    public void testGet_DurationFieldType_invalid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int result = period.get(DurationFieldType.MONTHS);
        assertEquals(0, result);
    }

    /**
     * Test isSupported method.
     */
    @Test
    public void testIsSupported_DurationFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertTrue(period.isSupported(DurationFieldType.DAYS));
        assertFalse(period.isSupported(DurationFieldType.MONTHS));
    }

    /**
     * Test toPeriod method.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(new Period(5, 0, 0, 0, 0, 0, 0), result);
    }

    /**
     * Test toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(new MutablePeriod(5, 0, 0, 0, 0, 0, 0), result);
    }

    /**
     * Test equals method with same object.
     */
    @Test
    public void testEquals_Object_same() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
    }

    /**
     * Test equals method with different object.
     */
    @Test
    public void testEquals_Object_different() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        Object other = new Object();
        assertFalse(period1.equals(other));
    }

    /**
     * Test equals method with different class.
     */
    @Test
    public void testEquals_Object_different_class() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        Object other = new String("test");
        assertFalse(period1.equals(other));
    }

    /**
     * Test equals method with null object.
     */
    @Test
    public void testEquals_Object_null() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        assertFalse(period1.equals(null));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertEquals(period1.hashCode(), period2.hashCode());
    }

    /**
     * Test compareTo method with null object.
     */
    @Test
    public void testCompareTo_null() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        assertNull(period1.compareTo(null));
    }

    /**
     * Test compareTo method with same object.
     */
    @Test
    public void testCompareTo_same() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertEquals(0, period1.compareTo(period2));
    }

    /**
     * Test compareTo method with different object.
     */
    @Test
    public void testCompareTo_different() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertEquals(-5, period1.compareTo(period2));
    }
}