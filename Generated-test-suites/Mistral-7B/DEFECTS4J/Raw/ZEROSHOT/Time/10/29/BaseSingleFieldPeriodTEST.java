package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalFields;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for BaseSingleFieldPeriod.java
 */
public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod period;
    private static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;

    @Before
    public void setUp() {
        period = new Days(3);
    }

    @Test
    public void testGetFieldType() {
        DurationFieldType durationFieldType = period.getFieldType();
        assertNotNull(durationFieldType);
        assertEquals(DurationFieldType.DAYS, durationFieldType);
    }

    @Test
    public void testGetPeriodType() {
        PeriodType periodType = period.getPeriodType();
        assertNotNull(periodType);
        assertEquals(PeriodType.standard(), periodType);
    }

    @Test
    public void testSize() {
        int size = period.size();
        assertEquals(1, size);
    }

    @Test
    public void testGetValue() {
        int value = period.getValue();
        assertEquals(3, value);
    }

    @Test
    public void testGetValue_zero() {
        period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int value = period.getValue();
        assertEquals(0, value);
    }

    @Test
    public void testIsSupported() {
        DurationFieldType durationFieldType = DurationFieldType.DAYS;
        assertTrue(period.isSupported(durationFieldType));
        DurationFieldType durationFieldType2 = DurationFieldType.HOURS;
        assertFalse(period.isSupported(durationFieldType2));
    }

    @Test
    public void testToPeriod() {
        Period toPeriod = period.toPeriod();
        assertEquals(3, toPeriod.getDays());
        assertEquals(0, toPeriod.getHours());
        assertEquals(0, toPeriod.getMinutes());
        assertEquals(0, toPeriod.getSeconds());
        assertEquals(0, toPeriod.getMillis());
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod toMutablePeriod = period.toMutablePeriod();
        assertEquals(3, toMutablePeriod.getDays());
        assertEquals(0, toMutablePeriod.getHours());
        assertEquals(0, toMutablePeriod.getMinutes());
        assertEquals(0, toMutablePeriod.getSeconds());
        assertEquals(0, toMutablePeriod.getMillis());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period2 = new Days(3);
        assertTrue(period.equals(period2));
        assertFalse(period.equals(null));
        assertFalse(period.equals(""));
        assertFalse(period.equals(new Object()));

        BaseSingleFieldPeriod period3 = new Days(5);
        assertFalse(period.equals(period3));
    }

    @Test
    public void testHashCode() {
        int hashCode1 = period.hashCode();
        BaseSingleFieldPeriod period2 = new Days(3);
        assertEquals(hashCode1, period2.hashCode());
        BaseSingleFieldPeriod period3 = new Days(5);
        int hashCode2 = period3.hashCode();
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testCompareTo_same_instance() {
        int compareTo = period.compareTo(period);
        assertEquals(0, compareTo);
    }

    @Test
    public void testCompareTo_this_less() {
        BaseSingleFieldPeriod period2 = new Days(5);
        int compareTo = period.compareTo(period2);
        assertLess(compareTo, 0);
    }

    @Test
    public void testCompareTo_this_greater() {
        BaseSingleFieldPeriod period2 = new Days(2);
        int compareTo = period.compareTo(period2);
        assertGreater(compareTo, 0);
    }

    @Test
    public void testBetween() {
        Instant start = Instant.now();
        Instant end = Instant.now().plus(Duration.standardDays(3));
        int between = BaseSingleFieldPeriod.between(BaseDateTime.fromInstant(start), BaseDateTime.fromInstant(end), DurationFieldType.DAYS);
        assertEquals(3, between);
    }

    @Test
    public void testBetween_partials() {
        ReadablePartial start = new BaseLocalDateTime(2023, 2, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).withZoneRetainFields(ZoneId.systemDefault());
        ReadablePartial end = new BaseLocalDateTime(2023, 2, 3, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).withZoneRetainFields(ZoneId.systemDefault());
        int between = BaseSingleFieldPeriod.between(start, end, Period.getInstance(3, PeriodUnit.DAYS()));
        assertEquals(3, between);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.instance(3, TemporalFields.DAYS, 2, TemporalFields.HOURS, 1, TemporalFields.MINUTES, 30, TemporalFields.SECONDS);
        assertSame(Days.DAYS, period.getFieldType(0));
        int standardPeriodIn = BaseSingleFieldPeriod.standardPeriodIn(period, MILLIS_PER_DAY);
        assertEquals(7, standardPeriodIn);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_imprecise_period() {
        ReadablePeriod period = Period.instance(3, TemporalFields.MONTHS, 2, TemporalFields.YEARS);
        BaseSingleFieldPeriod.standardPeriodIn(period, MILLIS_PER_DAY);
    }

    private static void assertGreater(int i, int j) {
        assertTrue(i > j);
    }

    private static void assertLess(int i, int j) {
        assertTrue(i < j);
    }

    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Test
    public void testToString() {
        String toString = period.toString();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("P[d]D");
        String expectedToString = formatter.print(Period.instance(3, PeriodUnit.DAYS));
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testGetDurationField() {
        DurationField field = period.getField(ISOChronology.getInstanceUTC(), DurationFieldType.DAYS);
        assertNotNull(field);
        assertEquals(DurationField.FIELD_DAYS, field.getCode());
        assertEquals(3, field.getCount());
    }
}