package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.base.AbstractBaseSingleFieldPeriodTest;

public class BaseSingleFieldPeriodTest extends AbstractBaseSingleFieldPeriodTest {

    @Override
    protected BaseSingleFieldPeriod createPeriod(int value) {
        return new MySingleFieldPeriod(value);
    }

    @Test
    public void testBetween() {
        BaseDateTime start = new BaseDateTime(0, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        BaseDateTime end = new BaseDateTime(1, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullStart() {
        BaseDateTime end = new BaseDateTime(1, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        BaseSingleFieldPeriod.between(null, end, DurationFieldType.HOURS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullEnd() {
        BaseDateTime start = new BaseDateTime(0, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        BaseSingleFieldPeriod.between(start, null, DurationFieldType.HOURS);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(1, 1, 1, 1, 1, 1, PeriodType.standard());
        assertEquals(24 * 60 * 60 * 24 * 7, BaseSingleFieldPeriod.standardPeriodIn(period, Chronology.UTC.getMillisPerDay()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNullPeriod() {
        BaseSingleFieldPeriod.standardPeriodIn(null, 1);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(123);
        assertEquals(123, period.getValue());
    }

    @Test
    public void testEqualsWithSameInstance() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(123);
        assertTrue(period.equals(period));
    }

    @Test
    public void testEqualsWithNullObject() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(123);
        assertFalse(period.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(123);
        assertFalse(period.equals(new Object()));
    }

    @Test
    public void testEqualsWithOtherPeriod() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(456);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testEqualsWithOtherPeriodSameValue() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(123);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(456);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testCompareToSameInstance() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(123);
        assertEquals(0, period1.compareTo(period2));
    }

    @Test
    public void testCompareToDifferentInstanceWithLessValue() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(456);
        assertEquals(-1, period1.compareTo(period2));
    }

    @Test
    public void testCompareToDifferentInstanceWithGreaterValue() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(456);
        assertEquals(1, period2.compareTo(period1));
    }

    @Test
    public void testCompareToSameTypeSameValueButDifferentClass() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(123);
        AnotherSingleFieldPeriod period2 = new AnotherSingleFieldPeriod(123);
        assertEquals(0, period1.compareTo(period2));
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToDifferentType() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(123);
        AnotherSingleFieldPeriod period2 = new AnotherSingleFieldPeriod(456);
        period1.compareTo(period2);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(123);
        Period expected = new Period(1, 1, 1, 1, 1, 1, PeriodType.standard());
        Period actual = period.toPeriod();
        assertEquals(expected, actual);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(123);
        MutablePeriod expected = new MutablePeriod();
        expected.add(1, DurationFieldType.HOURS);
        MutablePeriod actual = period.toMutablePeriod();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(123);
        DurationFieldType fieldType = DurationFieldType.HOURS;
        assertTrue(period.isSupported(fieldType));
        fieldType = DurationFieldType.MINUTES;
        assertFalse(period.isSupported(fieldType));
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(123);
        DurationFieldType expected = DurationFieldType.HOURS;
        DurationFieldType actual = period.getFieldType();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(123);
        PeriodType expected = PeriodType.standard();
        PeriodType actual = period.getPeriodType();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetValueFromPeriod() {
        Period period = new Period(1, 1, 1, 1, 1, 1, PeriodType.standard());
        BaseSingleFieldPeriod singleFieldPeriod = new MySingleFieldPeriod(period.getHours());
        assertEquals(period.getHours(), singleFieldPeriod.getValue());
    }

    @Test
    public void testGetValueFromPeriodWithDifferentPeriodType() {
        Period period = new Period(1, 1, 1, 1, 1, 1, PeriodType.standard());
        BaseSingleFieldPeriod singleFieldPeriod = new MySingleFieldPeriod(period.getMillis());
        assertEquals(period.getHours(), singleFieldPeriod.getValue());
    }

    @Test
    public void testGetValueFromPeriodWithMillisPerUnitDifferentThanDefault() {
        Period period = new Period(1, 1, 1, 1, 1, 1, PeriodType.standard());
        BaseSingleFieldPeriod singleFieldPeriod = new MySingleFieldPeriod(period.getMillis(), 86400000);
        assertEquals(period.getHours(), singleFieldPeriod.getValue());
    }

    @Test
    public void testGetValueFromPeriodWithMillisPerUnitSameThanDefault() {
        Period period = new Period(1, 1, 1, 1, 1, 1, PeriodType.standard());
        BaseSingleFieldPeriod singleFieldPeriod = new MySingleFieldPeriod(period);
        assertEquals(period.getHours(), singleFieldPeriod.getValue());
    }

    @Test
    public void testGetValueFromPeriodWithMillisPerUnitNegative() {
        Period period = new Period(1, 1, 1, 1, 1, 1, PeriodType.standard());
        BaseSingleFieldPeriod singleFieldPeriod = new MySingleFieldPeriod(period.getMillis(), -86400000);
        assertEquals(period.getHours(), singleFieldPeriod.getValue());
    }

    @Test
    public void testChronologyAwareGetValueFromPeriod() {
        Period period = new Period(1, 1, 1, 1, 1, 1, PeriodType.standard());
        Chronology chrono = ISOChronology.getInstanceUTC();
        BaseSingleFieldPeriod singleFieldPeriod = new MySingleFieldPeriod(chrono, period);
        assertEquals(period.getHours(), singleFieldPeriod.getValue());
    }

    @Test
    public void testChronologyAwareGetValueFromPeriodWithDifferentChronology() {
        Period period = new Period(1, 1, 1, 1, 1, 1, PeriodType.standard());
        Chronology chrono1 = ISOChronology.getInstanceUTC();
        Chronology chrono2 = CALENDAR_SYSTEM_PROVIDER.getCalendarSystem().getCalendar(Locale.ENGLISH).getCalendar(Calendar.getInstance().getTimeZone().getId()).getTimeZone().getChronology();
        BaseSingleFieldPeriod singleFieldPeriod = new MySingleFieldPeriod(chrono2, period);
        assertEquals(period.getHours(), singleFieldPeriod.getValue());
    }

    @Test
    public void testChronologyAwareGetValueFromPeriodWithMillisPerUnit() {
        Period period = new Period(1, 1, 1, 1, 1, 1, PeriodType.standard());
        Chronology chrono = ISOChronology.getInstanceUTC();
        BaseSingleFieldPeriod singleFieldPeriod = new MySingleFieldPeriod(chrono, period, 86400000);
        assertEquals(period.getHours(), singleFieldPeriod.getValue());
    }

    @Test
    public void testChronologyAwareGetValueFromPeriodWithMillisPerUnitNegative() {
        Period period = new Period(1, 1, 1, 1, 1, 1, PeriodType.standard());
        Chronology chrono = ISOChronology.getInstanceUTC();
        BaseSingleFieldPeriod singleFieldPeriod = new MySingleFieldPeriod(chrono, period, -86400000);
        assertEquals(period.getHours(), singleFieldPeriod.getValue());
    }

    @Test
    public void testToString() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(123);
        assertEquals("P1Y1M1D1H1M1S1", period.toString());
    }

    private static class MySingleFieldPeriod extends BaseSingleFieldPeriod {
        public MySingleFieldPeriod(int value) {
            super(value);
        }

        public MySingleFieldPeriod(Chronology chronology, ReadablePeriod period) {
            super(period.getValue(0), chronology);
        }

        public MySingleFieldPeriod(Chronology chronology, ReadablePeriod period, long millisPerUnit) {
            super(standardPeriodIn(period, millisPerUnit), chronology);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.HOURS;
        }
    }

    private static class AnotherSingleFieldPeriod extends BaseSingleFieldPeriod {
        public AnotherSingleFieldPeriod(int value) {
            super(value);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.MINUTES;
        }
    }

}