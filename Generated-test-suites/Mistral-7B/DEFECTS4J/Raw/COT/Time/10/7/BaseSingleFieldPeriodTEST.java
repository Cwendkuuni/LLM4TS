package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseJodaTest;
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
import org.joda.time.field.IntegerFields;
import org.joda.time.field.ISODateFields;
import org.joda.time.field.LocalDateFields;

import java.io.Serializable;
import java.text.ParseException;
import java.util.TimeZone;

public class BaseSingleFieldPeriodTest extends BaseJodaTest {

    public void testBaseSingleFieldPeriodConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertNotNull(period);
        assertEquals(5, period.getValue());
    }

    public void testBaseSingleFieldPeriodGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertNotNull(fieldType);
        assertEquals(IntegerFields.DAYS, fieldType);
    }

    public void testBaseSingleFieldPeriodGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        PeriodType periodType = period.getPeriodType();
        assertNotNull(periodType);
        assertEquals(PeriodType.standard(), periodType);
    }

    public void testBaseSingleFieldPeriodSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int size = period.size();
        assertEquals(1, size);
    }

    public void testBaseSingleFieldPeriodGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int value = period.getValue();
        assertEquals(5, value);
    }

    public void testBaseSingleFieldPeriodSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    public void testBaseSingleFieldPeriodToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(5, result.getDays());
        assertEquals(PeriodType.standard(), result.getPeriodType());
    }

    public void testBaseSingleFieldPeriodToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(5, result.getDays());
    }

    public void testBaseSingleFieldPeriodEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(10);
        assertFalse(period1.equals(period3));

        Object obj = new Object();
        assertFalse(period1.equals(obj));

        BaseSingleFieldPeriod period4 = null;
        assertFalse(period1.equals(period4));
    }

    public void testBaseSingleFieldPeriodHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        int hashCode1 = period1.hashCode();

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int hashCode2 = period2.hashCode();

        assertEquals(hashCode1, hashCode2);

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(10);
        int hashCode3 = period3.hashCode();

        assertNotEquals(hashCode1, hashCode3);
    }

    public void testBaseSingleFieldPeriodCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5);

        int compareResult1 = period1.compareTo(period2);
        assertEquals(-5, compareResult1);

        int compareResult2 = period1.compareTo(period3);
        assertEquals(0, compareResult2);
    }

    public void testBetween_Instant() {
        ReadableInstant start = DateTimeUtils.currentInstant();
        ReadableInstant end = DateTimeUtils.currentInstant().plusMillis(1000);

        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.milliseconds());
        assertEquals(1, period);
    }

    public void testBetween_Instant_Null() {
        ReadableInstant start = DateTimeUtils.currentInstant();
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(start, null, DurationFieldType.milliseconds()));
    }

    public void testBetween_Instant_NullFieldType() {
        ReadableInstant start = DateTimeUtils.currentInstant();
        ReadableInstant end = DateTimeUtils.currentInstant().plusMillis(1000);

        int period = BaseSingleFieldPeriod.between(start, end, null);
        assertEquals(0, period);
    }

    public void testBetween_Partial() throws ParseException {
        ReadablePartial start = new LocalDate(2011, 1, 1).toDateTimeAtStartOfDay().toLocalDate();
        ReadablePartial end = new LocalDate(2011, 1, 3).toDateTimeAtStartOfDay().toLocalDate();

        Chronology chrono = ISOChronology.getInstanceUTC();
        int period = BaseSingleFieldPeriod.between(start, end, null);
        assertEquals(3, period);
    }

    public void testBetween_Partial_Null() {
        ReadablePartial start = null;
        ReadablePartial end = new LocalDate(2011, 1, 1).toDateTimeAtStartOfDay().toLocalDate();

        int period = BaseSingleFieldPeriod.between(start, end, null);
        assertEquals(0, period);
    }

    public void testBetween_Partial_NullFieldType() {
        ReadablePartial start = new LocalDate(2011, 1, 1).toDateTimeAtStartOfDay().toLocalDate();
        ReadablePartial end = new LocalDate(2011, 1, 3).toDateTimeAtStartOfDay().toLocalDate();

        int period = BaseSingleFieldPeriod.between(start, end, null);
        assertEquals(3, period);

        int period2 = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(3, period2);

        int period3 = BaseSingleFieldPeriod.between(start, end, DurationFieldType.milliseconds());
        assertEquals(261600001, period3);
    }

    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.parse("PT1H30M");
        long millisPerUnit = 1000L;

        int standardPeriod = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(9100000, standardPeriod);
    }

    public void testStandardPeriodIn_NullPeriod() {
        long millisPerUnit = 1000L;

        int standardPeriod = BaseSingleFieldPeriod.standardPeriodIn(null, millisPerUnit);
        assertEquals(0, standardPeriod);
    }

    public void testStandardPeriodIn_ImprecisePeriod() {
        ReadablePeriod period = Period.parse("P1M");
        long millisPerUnit = 1000L;

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit));
        assertTrue(ex.getMessage().contains("Cannot convert period to duration as " + ISODateFields.MONTH_OF_YEAR.getField().getName() + " is not precise in the period " + period));
    }
}