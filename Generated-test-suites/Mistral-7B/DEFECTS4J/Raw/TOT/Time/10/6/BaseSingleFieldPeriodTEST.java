package org.joda.time.base;

import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link BaseSingleFieldPeriod}.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Tests the BaseSingleFieldPeriod class.
     */
    @Test
    public void testBaseSingleFieldPeriod() {
        // Test instant-instant calculation
        Chronology iso = ISOChronology.getInstanceUTC();
        ReadableInstant instant1 = new BaseDateTime(0, iso).withZoneRetainFields(iso);
        ReadableInstant instant2 = new BaseDateTime(1, iso).withZoneRetainFields(iso);
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.DAYS));
        assertEquals(1, period.getValue());
        assertSame(DurationFieldType.DAYS, period.getFieldType());
        assertSame(PeriodType.standard(), period.getPeriodType());
        assertEquals(PeriodType.standard(), period.toPeriod().getPeriodType());
        assertTrue(period.equals(period.toPeriod()));
        assertFalse(period.equals(null));
        assertFalse(period.equals(new Object()));
        assertEquals(period.hashCode(), period.toPeriod().hashCode());

        // Test partial-partial calculation
        ReadablePartial partial1 = new BaseDateTime(0, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        ReadablePartial partial2 = new BaseDateTime(3, 1, 1, 2, 3, 4, 5, ISOChronology.getInstanceUTC());
        BaseSingleFieldPeriod periodPartial = new BaseSingleFieldPeriod(BaseSingleFieldPeriod.between(partial1, partial2, PeriodType.standard()));
        assertEquals(2, periodPartial.getValue());
        assertSame(DurationFieldType.DAYS, periodPartial.getFieldType());
        assertSame(PeriodType.standard(), periodPartial.getPeriodType());
        assertEquals(PeriodType.standard(), periodPartial.toPeriod().getPeriodType());
        assertTrue(periodPartial.equals(periodPartial.toPeriod()));
        assertFalse(periodPartial.equals(null));
        assertFalse(periodPartial.equals(new Object()));
        assertEquals(periodPartial.hashCode(), periodPartial.toPeriod().hashCode());

        // Test imprecise period calculation
        ReadablePeriod imprecisePeriod = new Period(1, DurationFieldType.DAYS, PeriodType.standard());
        assertFalse(BaseSingleFieldPeriod.class.isInstance(imprecisePeriod));

        // Test mutable period calculation
        MutablePeriod mutablePeriod = new MutablePeriod(1, DurationFieldType.DAYS, PeriodType.standard());
        BaseSingleFieldPeriod periodMutable = new BaseSingleFieldPeriod(mutablePeriod.toPeriod());
        assertEquals(1, periodMutable.getValue());
        assertSame(DurationFieldType.DAYS, periodMutable.getFieldType());
        assertSame(PeriodType.standard(), periodMutable.getPeriodType());
        assertEquals(PeriodType.standard(), periodMutable.toPeriod().getPeriodType());
        assertTrue(periodMutable.equals(periodMutable.toPeriod()));
        assertFalse(periodMutable.equals(null));
        assertFalse(periodMutable.equals(new Object()));
        assertEquals(periodMutable.hashCode(), periodMutable.toPeriod().hashCode());

        // Test standard period calculation
        BaseSingleFieldPeriod periodStandard = new BaseSingleFieldPeriod(BaseSingleFieldPeriod.standardPeriodIn(new Period(1, DurationFieldType.HOURS, PeriodType.standard()), 1000L * 60 * 60 * 24));
        assertEquals(1, periodStandard.getValue());
        assertSame(DurationFieldType.HOURS, periodStandard.getFieldType());
        assertSame(PeriodType.standard(), periodStandard.getPeriodType());
        assertEquals(PeriodType.standard(), periodStandard.toPeriod().getPeriodType());
        assertTrue(periodStandard.equals(periodStandard.toPeriod()));
        assertFalse(periodStandard.equals(null));
        assertFalse(periodStandard.equals(new Object()));
        assertEquals(periodStandard.hashCode(), periodStandard.toPeriod().hashCode());
    }

}