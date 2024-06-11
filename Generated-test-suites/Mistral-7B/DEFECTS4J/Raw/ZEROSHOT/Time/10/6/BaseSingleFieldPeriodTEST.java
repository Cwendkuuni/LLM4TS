package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.joda.time.BaseJodaTest;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
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

    public void testBaseSingleFieldPeriod() {
        // Test abstract methods
        DurationFieldType fieldType = DurationFieldType.standardInstances().get(DurationFieldType.HOURS);
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return fieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(5, period.getValue());
        assertEquals(fieldType, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());
        assertSame(period.getFieldType(), period.getFieldType(0));
        assertEquals(5, period.getValue(0));
        assertTrue(period.isSupported(fieldType));
        assertFalse(period.isSupported(DurationFieldType.MINUTES));
        assertEquals(Period.ZERO, period.toPeriod());
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period);
        assertEquals(Period.valueOf(Duration.standardHours(5)), mutablePeriod);
        assertEquals(period, period.toMutablePeriod().toPeriod());
        assertEquals(period, period.toPeriod().toMutablePeriod().toPeriod());

        // Test static methods
        ReadableInstant instant1 = ZonedDateTime.of(2012, 1, 1, 12, 0, 0, 0, ZoneId.systemDefault()).toInstant();
        ReadableInstant instant2 = ZonedDateTime.of(2012, 1, 1, 13, 0, 0, 0, ZoneId.systemDefault()).toInstant();
        Chronology chrono = DateTimeUtils.getInstantChronology(instant1);
        int hoursBetween = BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.HOURS);
        assertEquals(1, hoursBetween);
        ReadablePeriod periodFromStatic = BaseSingleFieldPeriod.parse(String.valueOf(hoursBetween), fieldType, PeriodType.standard());
        assertEquals(period, periodFromStatic);

        // Test standardPeriodIn method
        ReadablePeriod period2 = Period.valueOf(Duration.standardHours(60), DurationFieldType.HOURS, PeriodType.standard());
        assertEquals(24, BaseSingleFieldPeriod.standardPeriodIn(period2, fieldType.getUnitMillis()));

        // Test constructor with null argument
        try {
            new BaseSingleFieldPeriod(null);
        } catch (IllegalArgumentException expected) {
            assertEquals("ReadableInstant or ReadablePartial objects must not be null", expected.getMessage());
        }

        // Test constructor with invalid argument
        try {
            new BaseSingleFieldPeriod(-1);
        } catch (IllegalArgumentException expected) {
            assertEquals("The period must be zero or greater", expected.getMessage());
        }
    }

}