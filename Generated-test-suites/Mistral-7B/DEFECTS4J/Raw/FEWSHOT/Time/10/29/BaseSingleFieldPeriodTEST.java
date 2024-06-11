package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.joda.time.BaseJodaTest;
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

public class BaseSingleFieldPeriodTest extends BaseJodaTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        // Test with int argument
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(1, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());
        assertSame(period.getClass(), period.getFieldType(0));
        assertEquals(1, period.getValue(0));

        // Test with null argument
        try {
            new BaseSingleFieldPeriod(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test getFieldType.
     */
    @Test
    public void testGetFieldType() {
        assertEquals(DurationFieldType.DAYS, new BaseSingleFieldPeriod(1).getFieldType());
    }

    /**
     * Test getPeriodType.
     */
    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.standard(), new BaseSingleFieldPeriod(1).getPeriodType());
    }

    /**
     * Test size.
     */
    @Test
    public void testSize() {
        assertEquals(1, new BaseSingleFieldPeriod(1).size());
    }

    /**
     * Test getFieldType at index.
     */
    @Test
    public void testGetFieldTypeAtIndex() {
        assertEquals(DurationFieldType.DAYS, new BaseSingleFieldPeriod(1).getFieldType(0));
    }

    /**
     * Test getValue at index.
     */
    @Test
    public void testGetValueAtIndex() {
        assertEquals(1, new BaseSingleFieldPeriod(1).getValue(0));
    }

    /**
     * Test getValue.
     */
    @Test
    public void testGetValue() {
        assertEquals(1, new BaseSingleFieldPeriod(1).getValue());
    }

    /**
     * Test setValue.
     */
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.setValue(2);
        assertEquals(2, period.getValue());
    }

    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(2);
        assertEquals(p1, p1);
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }

    /**
     * Test hashCode.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(-1);
        assertEquals(p1.hashCode(), p1.hashCode());
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
    }

    /**
     * Test compareTo.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(2);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(1);
        assertEquals(0, p1.compareTo(p1));
        assertEquals(-1, p1.compareTo(p2));
        assertEquals(1, p2.compareTo(p1));
        assertEquals(0, p1.compareTo(p3));
    }

    /**
     * Test between.
     */
    @Test
    public void testBetween() {
        ReadableInstant start = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        ReadableInstant end = new DateTime(2022, 1, 1, 1, 0, 0, 0, ISOChronology.getInstanceUTC());
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        int value = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(1, value);

        ReadablePeriod zeroInstance = Period.ZERO;
        ReadablePartial startPartial = new LocalDate(2022, 1, 1);
        ReadablePartial endPartial = new LocalDate(2022, 1, 2);
        int valuePartial = BaseSingleFieldPeriod.between(startPartial, endPartial, zeroInstance);
        assertEquals(1, valuePartial);
    }

    /**
     * Test standardPeriodIn.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(1, 2, 3, 4, 5, 6, 7);
        long millisPerUnit = 1000L;
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(7*24*60*60 + 2*60*60 + 3*60 + 4*60 + 5*60 + 6*60 + 7, hours);
    }
}