package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.BaseDurationFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        // Test between two instants
        ReadableInstant start = ISOChronology.getInstanceUTC().parseInstant("2000-01-01T00:00:00.001Z");
        ReadableInstant end = ISOChronology.getInstanceUTC().parseInstant("2000-01-01T00:00:00.002Z");
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS));

        // Test between two partials
        ReadablePartial startPartial = ISOChronology.getInstanceUTC().getLocalDate("2000-01-01");
        ReadablePartial endPartial = ISOChronology.getInstanceUTC().getLocalDate("2000-01-02");
        assertEquals(1, BaseSingleFieldPeriod.between(startPartial, endPartial, Period.FIFTEEN_MINUTES));

        // Test invalid arguments
        assertNotNull(BaseSingleFieldPeriod.between(null, null, null));
        assertNotNull(BaseSingleFieldPeriod.between(null, null, DurationFieldType.HOURS));
        assertNotNull(BaseSingleFieldPeriod.between(ReadableInstant.class.cast(null), end, DurationFieldType.HOURS));
        assertNotNull(BaseSingleFieldPeriod.between(start, null, DurationFieldType.HOURS));
        assertNotNull(BaseSingleFieldPeriod.between(start, end, null));
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new MutablePeriod(1, DurationFieldType.DAYS, PeriodType.standard()).toImmutable();
        assertEquals(24 * 60 * 60 * 1000L, BaseSingleFieldPeriod.standardPeriodIn(period, 1000L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithInvalidPeriod() {
        ReadablePeriod period = new MutablePeriod(1, DurationFieldType.MONTHS, PeriodType.standard()).toImmutable();
        BaseSingleFieldPeriod.standardPeriodIn(period, 1000L);
    }

    @Test
    public void testCreateInstance() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);
        assertEquals(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), p.getFieldType());
        assertEquals(1, p.getValue());
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 2);
        assertEquals(2, p.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);
        p.setValue(3);
        assertEquals(3, p.getValue());
    }

    @Test
    public void testSize() {
        assertEquals(1, new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1).size());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);
        assertEquals(new BaseDurationFieldType("Test", 1), p.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);
        assertEquals(PeriodType.standard(), p.getPeriodType());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);
        p.getFieldType(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testgetValueInvalidIndex() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);
        p.getValue(2);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);
        assertTrue(p.isSupported(new BaseDurationFieldType("Test", 1)));
        assertFalse(p.isSupported(new BaseDurationFieldType("Test2", 1)));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);
        Period expected = new Period(1, 0, 0, 0, 0);
        Period actual = p.toPeriod();
        assertEquals(expected, actual);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);
        MutablePeriod expected = new MutablePeriod(1, 0, 0, 0, 0, PeriodType.standard());
        MutablePeriod actual = p.toMutablePeriod();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test2", 1).getField(ISOChronology.getInstanceUTC()), 1);

        assertTrue(p1.equals(p1));
        assertTrue(p1.equals(p2));
        assertFalse(p1.equals(p3));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test2", 1).getField(ISOChronology.getInstanceUTC()), 1);

        int p1Hashcode = p1.hashCode();
        int p2Hashcode = p2.hashCode();
        int p3Hashcode = p3.hashCode();

        assertTrue(p1.hashCode() == p2.hashCode());
        assertFalse(p1.hashCode() == p3.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 2);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(new BaseDurationFieldType("Test", 1).getField(ISOChronology.getInstanceUTC()), 1);

        assertEquals(0, p1.compareTo(p3));
        assertEquals(1, p1.compareTo(p2));
        assertEquals(-1, p2.compareTo(p1));
    }
}