package org.joda.time.base;

import static org.junit.Assert.assertEquals;

import org.joda.time.BaseDateTime;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.junit.Test;

/**
 * Test cases for the BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test case for between method with ReadableInstant.
     */
    @Test
    public void testBetweenInstants() {
        BaseSingleFieldPeriod period = new Days(5);
        long instant1 = BaseDateTime.UTC.getMillis();
        long instant2 = instant1 + 5L * 24L * 60L * 60L * 1000L;
        int value = BaseSingleFieldPeriod.between(new BaseDateTime(instant1), new BaseDateTime(instant2), DurationFieldType.days());
        assertEquals(5, value);
    }

    /**
     * Test case for between method with ReadablePartial.
     */
    @Test
    public void testBetweenPartials() {
        BaseSingleFieldPeriod period = new Days(5);
        long instant = BaseDateTime.UTC.getMillis();
        ReadablePeriod zeroInstance = Period.ZERO;
        ReadablePartial start = new BasePartial(new BaseDateTime(instant), new PeriodType() {
            @Override
            public int getPrecision() {
                return PeriodType.DAYS.getPrecision();
            }

            @Override
            public String getID() {
                return PeriodType.DAYS.getName();
            }

            @Override
            public DurationFieldType[] getFieldTypes() {
                return new DurationFieldType[]{DurationFieldType.days()};
            }

            @Override
            public int getValue(int i) {
                return i == 0 ? 1 : 0;
            }

            @Override
            public long getMillis(int i) {
                return i == 0 ? 1L * 24L * 60L * 60L * 1000L : 0L;
            }

            @Override
            public void add(int field, long amount) {
                // Do nothing
            }

            @Override
            public void set(int field, long value) {
                // Do nothing
            }

            @Override
            public void clear() {
                // Do nothing
            }
        });
        ReadablePartial end = new BasePartial(new BaseDateTime(instant + 5L * 24L * 60L * 60L * 1000L), zeroInstance);
        int value = BaseSingleFieldPeriod.between(start, end, zeroInstance);
        assertEquals(5, value);
    }

    /**
     * Test case for standardPeriodIn method.
     */
    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod period = new Hours(12);
        long millisPerUnit = 1000L * 60L * 60L;
        int value = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(43200, value);
    }

    /**
     * Test case for getValue method.
     */
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new Days(5);
        int value = period.getValue();
        assertEquals(5, value);
    }

    /**
     * Test case for setValue method.
     */
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new Days(5);
        period.setValue(10);
        int value = period.getValue();
        assertEquals(10, value);
    }

    /**
     * Test case for getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new Days(5);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.days(), fieldType);
    }

    /**
     * Test case for getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new Days(5);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.DAYS, periodType);
    }

    /**
     * Test case for size method.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new Days(5);
        int size = period.size();
        assertEquals(1, size);
    }

    /**
     * Test case for getFieldType method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod period = new Days(5);
        period.getFieldType(2);
    }

    /**
     * Test case for getValue method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueInvalidIndex() {
        BaseSingleFieldPeriod period = new Days(5);
        period.getValue(2);
    }

    /**
     * Test case for get method with unsupported field type.
     */
    @Test
    public void testGetUnsupportedFieldType() {
        BaseSingleFieldPeriod period = new Days(5);
        int value = period.get(DurationFieldType.weeks());
        assertEquals(0, value);
    }

    /**
     * Test case for isSupported method with unsupported field type.
     */
    @Test
    public void testIsSupportedUnsupportedFieldType() {
        BaseSingleFieldPeriod period = new Days(5);
        boolean supported = period.isSupported(DurationFieldType.weeks());
        assertFalse(supported);
    }

    /**
     * Test case for toPeriod method.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new Days(5);
        Period result = period.toPeriod();
        assertTrue(result.isEqual(new Period(5, PeriodType.DAYS)));
    }

    /**
     * Test case for toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new Days(5);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(period.getValue(), result.getBasePeriod().getValue(0));
    }

    /**
     * Test case for equals method with null argument.
     */
    @Test
    public void testEqualsNull() {
        BaseSingleFieldPeriod period = new Days(5);
        assertFalse(period.equals(null));
    }

    /**
     * Test case for equals method with different class argument.
     */
    @Test
    public void testEqualsDifferentClass() {
        BaseSingleFieldPeriod period = new Days(5);
        assertFalse(period.equals(new Object()));
    }

    /**
     * Test case for equals method with equal object.
     */
    @Test
    public void testEqualsEqual() {
        BaseSingleFieldPeriod period1 = new Days(5);
        BaseSingleFieldPeriod period2 = new Days(5);
        assertTrue(period1.equals(period2));
    }

    /**
     * Test case for equals method with different object.
     */
    @Test
    public void testEqualsDifferent() {
        BaseSingleFieldPeriod period1 = new Days(5);
        BaseSingleFieldPeriod period2 = new Hours(10);
        assertFalse(period1.equals(period2));
    }

    /**
     * Test case for hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new Days(5);
        BaseSingleFieldPeriod period2 = new Days(5);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    /**
     * Test case for compareTo method with equal object.
     */
    @Test
    public void testCompareToEqual() {
        BaseSingleFieldPeriod period1 = new Days(5);
        BaseSingleFieldPeriod period2 = new Days(5);
        int comparison = period1.compareTo(period2);
        assertEquals(0, comparison);
    }

    /**
     * Test case for compareTo method with greater object.
     */
    @Test
    public void testCompareToGreater() {
        BaseSingleFieldPeriod period1 = new Days(5);
        BaseSingleFieldPeriod period2 = new Days(10);
        int comparison = period1.compareTo(period2);
        assertEquals(-1, comparison);
    }

    /**
     * Test case for compareTo method with less object.
     */
    @Test
    public void testCompareToLess() {
        BaseSingleFieldPeriod period1 = new Days(5);
        BaseSingleFieldPeriod period2 = new Days(3);
        int comparison = period1.compareTo(period2);
        assertEquals(1, comparison);
    }
}