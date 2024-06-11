package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.ISOChronology;

/**
 * Test class for {@link BaseSingleFieldPeriod}.
 */
public class BaseSingleFieldPeriodTest {

    /** Test for a period with a single day field. */
    public static class DayPeriod extends BaseSingleFieldPeriod {
        private static final long serialVersionUID = 1L;

        public DayPeriod(int days) {
            super(days);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.DAYS;
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }

    @Test
    public void testBetween() {
        // Test between two Instants
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = new BaseDateTime(1972, 1, 1, 0, 0, 0, 0, chrono).toInstant();
        ReadableInstant end = new BaseDateTime(1972, 1, 3, 0, 0, 0, 0, chrono).toInstant();
        int daysBetween = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(3, daysBetween);

        // Test between two Partial objects
        ReadablePartial startPartial = new MutablePeriod().withDays(1).toReadOnly();
        ReadablePartial endPartial = new MutablePeriod().withDays(3).toReadOnly();
        int daysBetweenPartial = BaseSingleFieldPeriod.between(startPartial, endPartial, Period.INSTANCE);
        assertEquals(3, daysBetweenPartial);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new DayPeriod(3);
        long millisPerUnit = 1000 * 60 * 60 * 24; // 1 day
        int hoursBetween = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(3, hoursBetween);
    }

    @Test
    public void testInstantFields() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant instant = new BaseDateTime(2013, 1, 1, 1, 2, 3, 4, chrono).toInstant();
        BaseSingleFieldPeriod period = new DayPeriod(3).withInstant(instant);

        assertEquals(3, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testComparison() {
        BaseSingleFieldPeriod period1 = new DayPeriod(3);
        BaseSingleFieldPeriod period2 = new DayPeriod(5);
        BaseSingleFieldPeriod period3 = new DayPeriod(3);

        assertTrue(period1.compareTo(period3) == 0);
        assertTrue(period1.compareTo(period2) < 0);
        assertTrue(period2.compareTo(period1) > 0);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        BaseSingleFieldPeriod period = new DayPeriod(3);
        BaseSingleFieldPeriod clone = (BaseSingleFieldPeriod) period.clone();
        assertEquals(period, clone);
        assertNotNull(clone.getClass());
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new DayPeriod(3);
        assertEquals(3, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new DayPeriod(3);
        period.setValue(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testNull() {
        assertNull(BaseSingleFieldPeriod.between(null, null, null));
        assertNull(BaseSingleFieldPeriod.standardPeriodIn(null, 0));
    }

    @Test
    public void testInvalidPeriodType() {
        ReadablePeriod period = new MutablePeriod();
        assertFalse(period.isSupported(DurationFieldType.HOURS));
    }

    @Test
    public void testInvalidFieldType() {
        ReadablePeriod period = new DayPeriod(1);
        assertFalse(period.isSupported(DurationFieldType.MONTHS));
    }

    @Test
    public void testToString() {
        BaseSingleFieldPeriod period = new DayPeriod(3);
        String expected = "P3D";
        String actual = period.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new DayPeriod(3);
        BaseSingleFieldPeriod period2 = new DayPeriod(3);
        BaseSingleFieldPeriod period3 = new DayPeriod(5);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        int hashCode3 = period3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }
}