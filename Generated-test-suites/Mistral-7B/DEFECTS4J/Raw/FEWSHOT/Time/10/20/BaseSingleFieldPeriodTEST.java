package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.joda.time.BaseJodaTimeTest;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;

public class BaseSingleFieldPeriodTest extends BaseJodaTimeTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        // Test basic functionality
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(2);
        assertEquals(2, p2.compareTo(p1));
        assertEquals(1, p1.compareTo(p2));
        assertEquals(0, p1.compareTo(p1));
        assertEquals(p1.getClass(), p1.getClass());
        assertEquals(p2.getClass(), p2.getClass());

        // Test equality
        assertEquals(p1, p1);
        assertNotEquals(p1, null);
        assertNotEquals(p1, new Object());
        assertNotEquals(p1, p2);

        // Test hash code
        int h1 = p1.hashCode();
        int h2 = p2.hashCode();
        assertNotEquals(h1, h2);

        // Test getters
        assertEquals(DurationFieldType.DAYS, p1.getFieldType());
        assertEquals(DurationFieldType.DAYS, p2.getFieldType());
        assertEquals(1, p1.getValue());
        assertEquals(2, p2.getValue());
        assertEquals(PeriodType.standard(), p1.getPeriodType());
        assertEquals(PeriodType.standard(), p2.getPeriodType());

        // Test toPeriod()
        Period p3 = p1.toPeriod();
        assertEquals(1, p3.getDays());
        assertEquals(PeriodType.standard(), p3.getPeriodType());

        // Test toMutablePeriod()
        MutablePeriod m1 = p1.toMutablePeriod();
        assertEquals(1, m1.getDays());
        assertEquals(PeriodType.standard(), m1.getPeriodType());

        // Test equals()
        BaseSingleFieldPeriod p4 = new BaseSingleFieldPeriod(1);
        assertEquals(p1, p4);

        // Test int conversion
        assertEquals(1, p1.intValue());
        assertEquals(2, p2.intValue());

        // Test long conversion
        assertEquals(1L, p1.longValue());
        assertEquals(2L, p2.longValue());

        // Test add()
        BaseSingleFieldPeriod p5 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod p6 = p1.add(p2);
        assertEquals(5, p6.compareTo(p1));
        assertEquals(3, p5.compareTo(p6));
        assertEquals(1, p5.compareTo(p1));

        // Test subtract()
        BaseSingleFieldPeriod p7 = p1.subtract(p2);
        assertEquals(-1, p7.compareTo(p1));
        assertEquals(1, p7.compareTo(p2));

        // Test getValue() with different types
        assertEquals(0, new BaseSingleFieldPeriod(0).getValue(DurationFieldType.MINUTES));
        assertEquals(1, p1.getValue(DurationFieldType.MINUTES));
        assertEquals(2, p2.getValue(DurationFieldType.MINUTES));
        assertEquals(0, new BaseSingleFieldPeriod(0).getValue(DurationFieldType.HOURS));
        assertEquals(24 * 60, p1.getValue(DurationFieldType.MINUTES));
        assertEquals(24 * 60 * 60, p1.getValue(DurationFieldType.HOURS));

        // Test getValue() with non-zero value
        assertEquals(1, p1.getValue(DurationFieldType.DAYS));
        assertEquals(7, new BaseSingleFieldPeriod(7).getValue(DurationFieldType.DAYS));

        // Test getValue() with negative value
        BaseSingleFieldPeriod p8 = new BaseSingleFieldPeriod(-1);
        assertEquals(-1, p8.compareTo(p1));

        // Test getValue() with zero value
        assertEquals(0, new BaseSingleFieldPeriod(0).compareTo(p1));

        // Test getValue() with different period types
        BaseSingleFieldPeriod p9 = new BaseSingleFieldPeriod(7, PeriodType.weeks());
        assertEquals(7, p9.getValue(DurationFieldType.DAYS));
        assertEquals(7 * 7, p9.getValue(DurationFieldType.WEEKS));
        assertEquals(7 * 7 * 7, p9.getValue(DurationFieldType.DAYS));

        // Test getValue() with ISOChronology
        BaseSingleFieldPeriod p10 = new BaseSingleFieldPeriod(1, ISOChronology.getInstance());
        assertEquals(1, p10.getValue(DurationFieldType.DAYS));
        assertEquals(86400000L, p10.getValue(DurationFieldType.MILLIS));
        assertEquals(1, p10.getValue(DurationFieldType.SECONDS));
        assertEquals(1, p10.getValue(DurationFieldType.MINUTES));
        assertEquals(1, p10.getValue(DurationFieldType.HOURS));
    }

    @Test
    public void testBetween() {
        // Test between() with null instants
        try {
            BaseSingleFieldPeriod.between(null, null, DurationFieldType.DAYS);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test between() with null chronology
        try {
            BaseSingleFieldPeriod.between(new Instant(), null, DurationFieldType.DAYS);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test between() with invalid field type
        try {
            BaseSingleFieldPeriod.between(new Instant(), new Instant(), null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test between() with valid instants and field type
        ReadableInstant start = new Instant(123456789000L);
        ReadableInstant end = new Instant(123456789123L);
        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(1, period);

        // Test between() with valid partials and zero instance
        ReadablePartial startPartial = new LocalDate(2012, 1, 1);
        ReadablePartial endPartial = new LocalDate(2012, 1, 2);
        ReadablePeriod zeroInstance = Period.ZERO;
        int period2 = BaseSingleFieldPeriod.between(startPartial, endPartial, zeroInstance);
        assertEquals(1, period2);

        // Test between() with valid partials and different zero instance
        ReadablePeriod zeroInstance2 = Period.ONE.withFieldAdded(DurationFieldType.DAYS, -1);
        int period3 = BaseSingleFieldPeriod.between(startPartial, endPartial, zeroInstance2);
        assertEquals(-1, period3);

        // Test between() with partials of different types
        try {
            BaseSingleFieldPeriod.between(new LocalDate(), new LocalDateTime(), DurationFieldType.DAYS);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test between() with partials of different chronologies
        LocalDateTime localDateTime = new LocalDateTime(2012, 1, 1, 12, 0, 0, 0, DateTimeUtils.forOffsetHours(1));
        LocalDate localDate = new LocalDate(2012, 1, 1);
        try {
            BaseSingleFieldPeriod.between(localDateTime, localDate, DurationFieldType.DAYS);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test between() with partials of different sizes
        try {
            ReadablePeriod zeroInstance3 = Period.ONE.withFieldAdded(DurationFieldType.HOURS, -1);
            BaseSingleFieldPeriod.between(startPartial, endPartial, zeroInstance3);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testStandardPeriodIn() {
        // Test standardPeriodIn() with null period
        try {
            BaseSingleFieldPeriod.standardPeriodIn(null, 1000L);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test standardPeriodIn() with zero period
        BaseSingleFieldPeriod p11 = Period.ZERO;
        int period4 = BaseSingleFieldPeriod.standardPeriodIn(p11, 1000L);
        assertEquals(0, period4);

        // Test standardPeriodIn() with non-zero period
        BaseSingleFieldPeriod p12 = Period.ONE;
        int period5 = BaseSingleFieldPeriod.standardPeriodIn(p12, 1000L);
        assertEquals(1, period5);

        // Test standardPeriodIn() with period containing months and years
        Period p13 = new Period(1, 2, 3);
        try {
            BaseSingleFieldPeriod.standardPeriodIn(p13, 1000L);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test standardPeriodIn() with period containing weeks
        Period p14 = new Period(2, 0, 0).withWeeks(1);
        int period6 = BaseSingleFieldPeriod.standardPeriodIn(p14, 1000L);
        assertEquals(7, period6);

        // Test standardPeriodIn() with period containing days
        Period p15 = new Period(0, 23, 59, 59, 59, 59, 59);
        int period7 = BaseSingleFieldPeriod.standardPeriodIn(p15, 1000L);
        assertEquals(24 * 60 * 60 * 60 * 1000, period7);

        // Test standardPeriodIn() with period containing hours
        Period p16 = new Period(0, 23);
        int period8 = BaseSingleFieldPeriod.standardPeriodIn(p16, 1000L);
        assertEquals(24 * 60 * 60 * 1000, period8);

        // Test standardPeriodIn() with period containing minutes
        Period p17 = new Period(0, 59);
        int period9 = BaseSingleFieldPeriod.standardPeriodIn(p17, 1000L);
        assertEquals(60 * 60 * 1000, period9);

        // Test standardPeriodIn() with period containing seconds
        Period p18 = new Period(0, 0, 59);
        int period10 = BaseSingleFieldPeriod.standardPeriodIn(p18, 1000L);
        assertEquals(1000, period10);

        // Test standardPeriodIn() with period containing milliseconds
        Period p19 = new Period(0, 0, 0, 599);
        int period11 = BaseSingleFieldPeriod.standardPeriodIn(p19, 1000L);
        assertEquals(599, period11);

        // Test standardPeriodIn() with period containing microseconds
        Period p20 = new Period(0, 0, 0, 0, 599_000);
        int period12 = BaseSingleFieldPeriod.standardPeriodIn(p20, 1000L);
        assertEquals(599_000, period12);

        // Test standardPeriodIn() with period containing nanoseconds
        Period p21 = new Period(0, 0, 0, 0, 0, 599_000_000);
        int period13 = BaseSingleFieldPeriod.standardPeriodIn(p21, 1000L);
        assertEquals(599_000_000, period13);
    }

    // Test constructor with wrong argument type
    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodWrongArgumentType() {
        new BaseSingleFieldPeriod("foo");
    }
}