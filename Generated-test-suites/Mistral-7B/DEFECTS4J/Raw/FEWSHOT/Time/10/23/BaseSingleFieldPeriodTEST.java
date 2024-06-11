package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
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

public class BaseSingleFieldPeriodTest {

    private static BaseSingleFieldPeriod createPeriod(int iPeriod) {
        return new BaseSingleFieldPeriodWithDays(iPeriod);
    }

    @Test
    public void testBetween() {
        ReadableInstant start = new BaseDateTime().toInstant();
        ReadableInstant end = start.plus(1, DurationFieldType.days());

        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(1, period);

        start = new BaseDateTime().toInstant();
        end = start.plus(1, DurationFieldType.hours());

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.hours());
        assertEquals(1, period);

        start = new BaseDateTime().toInstant();
        end = start.plus(1, DurationFieldType.minutes());

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.minutes());
        assertEquals(1, period);

        start = new BaseDateTime().toInstant();
        end = start.plus(1, DurationFieldType.seconds());

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.seconds());
        assertEquals(1, period);

        start = new BaseDateTime().toInstant();
        end = start.plus(1, DurationFieldType.millis());

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.millis());
        assertEquals(1, period);

        start = new BaseDateTime().toInstant();
        end = start.plus(1, DurationFieldType.days().plus(1, DurationFieldType.hours()));

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(1, period);

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.hours());
        assertEquals(1, period);

        start = new BaseDateTime().toInstant();
        end = start.plus(1, DurationFieldType.days()).plus(1, DurationFieldType.hours());

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(1, period);

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.hours());
        assertEquals(2, period);

        start = new BaseDateTime().toInstant();
        end = start.plus(new Duration(1, DurationFieldType.weeks()));

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.weeks());
        assertEquals(1, period);

        start = new BaseDateTime().toInstant();
        end = start.plus(new Duration(1, DurationFieldType.days()).plus(new Duration(1, DurationFieldType.hours())));

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(1, period);

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.hours());
        assertEquals(2, period);

        start = new BaseDateTime().toInstant();
        end = start.plus(new Period(1, DurationFieldType.months()));

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertNull(period);

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.weeks());
        assertNull(period);

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.hours());
        assertNull(period);

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.minutes());
        assertNull(period);

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.seconds());
        assertNull(period);

        start = new BaseDateTime().toInstant();
        end = start.plus(new Period(1, DurationFieldType.months()).plus(new Period(1, DurationFieldType.weeks())));

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertNull(period);

        period = BaseSingleFieldType.between(start, end, DurationFieldType.weeks());
        assertNull(period);

        period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.hours());
        assertNull(period);
    }

    @Test
    public void testStandardPeriodIn() {
        int days = 7;
        int hours = 24 * days;
        int minutes = 60 * hours;
        int seconds = 1000 * minutes;
        int millis = seconds * 1000;

        BaseSingleFieldPeriod period = createPeriod(days);
        assertEquals(days, BaseSingleFieldPeriod.standardPeriodIn(period, 1000L * 24 * 60 * 60 * 7));

        period = createPeriod(hours);
        assertEquals(hours, BaseSingleFieldPeriod.standardPeriodIn(period, 1000L * 24 * 60 * 60));

        period = createPeriod(minutes);
        assertEquals(minutes, BaseSingleFieldPeriod.standardPeriodIn(period, 1000L * 60 * 60));

        period = createPeriod(seconds);
        assertEquals(seconds, BaseSingleFieldPeriod.standardPeriodIn(period, 1000L));

        period = createPeriod(millis);
        assertEquals(millis, BaseSingleFieldPeriod.standardPeriodIn(period, 1));

        period = createPeriod(2);
        int value = BaseSingleFieldPeriod.standardPeriodIn(period, 1000L * 24 * 60 * 60 * 7);
        assertNotEquals(days, value);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = createPeriod(123);
        assertEquals(123, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = createPeriod(123);
        period.setValue(456);
        assertEquals(456, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = createPeriod(123);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.days(), fieldType);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = createPeriod(123);
        PeriodType periodType = period.getPeriodType();
        assertTrue(periodType.isStandard());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = createPeriod(123);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = createPeriod(123);
        DurationFieldType fieldType = period.getFieldType(0);
        assertEquals(DurationFieldType.days(), fieldType);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeAtInvalidIndex() {
        BaseSingleFieldPeriod period = createPeriod(123);
        period.getFieldType(2);
    }

    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod period = createPeriod(123);
        int value = period.getValue(0);
        assertEquals(123, value);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAtInvalidIndex() {
        BaseSingleFieldPeriod period = createPeriod(123);
        period.getValue(2);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = createPeriod(123);
        int value = period.get(DurationFieldType.days());
        assertEquals(123, value);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = createPeriod(123);
        assertTrue(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = createPeriod(123);
        Period result = period.toPeriod();
        assertEquals(123, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = createPeriod(123);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(123, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = createPeriod(123);
        BaseSingleFieldPeriod period2 = createPeriod(123);
        assertTrue(period1.equals(period2));

        BaseSingleFieldPeriod period3 = createPeriod(456);
        assertFalse(period1.equals(period3));

        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriodWithDays(123);
        assertTrue(period1.equals(period4));

        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriodWithDays(456);
        assertFalse(period1.equals(period5));

        BaseSingleFieldPeriod period6 = new BaseSingleFieldPeriodWithDays(123);
        assertTrue(period1.equals(period6));

        BaseSingleFieldPeriod period7 = null;
        assertFalse(period1.equals(period7));

        Object obj = new Object();
        assertFalse(period1.equals(obj));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = createPeriod(123);
        BaseSingleFieldPeriod period2 = createPeriod(123);
        assertEquals(period1.hashCode(), period2.hashCode());

        BaseSingleFieldPeriod period3 = createPeriod(456);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period3.hashCode();
        assertNotEquals(hashCode1, hashCode2);

        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriodWithDays(123);
        assertEquals(period1.hashCode(), period4.hashCode());

        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriodWithDays(456);
        int hashCode3 = period1.hashCode();
        int hashCode4 = period5.hashCode();
        assertNotEquals(hashCode3, hashCode4);

        BaseSingleFieldPeriod period6 = null;
        int hashCode5 = period1.hashCode();
        int hashCode6 = period6.hashCode();
        assertNotEquals(hashCode5, hashCode6);

        Object obj = new Object();
        int hashCode7 = period1.hashCode();
        int hashCode8 = obj.hashCode();
        assertNotEquals(hashCode7, hashCode8);
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = createPeriod(123);
        BaseSingleFieldPeriod period2 = createPeriod(123);
        assertEquals(0, period1.compareTo(period2));

        BaseSingleFieldPeriod period3 = createPeriod(456);
        int compareTo1 = period1.compareTo(period3);
        assertEquals(-1, compareTo1);

        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriodWithDays(123);
        int compareTo2 = period1.compareTo(period4);
        assertEquals(0, compareTo2);

        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriodWithDays(456);
        int compareTo3 = period1.compareTo(period5);
        assertEquals(-1, compareTo3);

        BaseSingleFieldPeriod period6 = new BaseSingleFieldPeriodWithDays(123);
        int compareTo4 = period6.compareTo(period1);
        assertEquals(0, compareTo4);

        BaseSingleFieldPeriod period7 = new BaseSingleFieldPeriodWithDays(124);
        int compareTo5 = period1.compareTo(period7);
        assertEquals(-1, compareTo5);

        BaseSingleFieldPeriod period8 = null;
        int compareTo6 = period1.compareTo(period8);
        assertNull(compareTo6);

        Object obj = new Object();
        int compareTo7 = period1.compareTo(obj);
        assertNull(compareTo7);
    }
}