package org.joda.time.base;

import org.joda.time.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenReadableInstant() {
        ReadableInstant start = new DateTime(2019, 1, 1, 0, 0);
        ReadableInstant end = new DateTime(2019, 1, 2, 0, 0);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(1, result);
    }

    @Test
    public void testBetweenReadablePartial() {
        ReadablePartial start = new LocalDate(2019, 1, 1);
        ReadablePartial end = new LocalDate(2019, 1, 2);
        int result = BaseSingleFieldPeriod.between(start, end, new Period(1, DurationFieldType.days()));
        assertEquals(1, result);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(1, 0, 0, 0);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 24 * 60 * 60 * 1000);
        assertEquals(1, result);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        int result = period.getValue();
        assertEquals(1, result);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        period.setValue(2);
        int result = period.getValue();
        assertEquals(2, result);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        DurationFieldType result = period.getFieldType();
        assertEquals(DurationFieldType.days(), result);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        PeriodType result = period.getPeriodType();
        assertEquals(PeriodType.standard(), result);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        int result = period.size();
        assertEquals(1, result);
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        DurationFieldType result = period.getFieldType(0);
        assertEquals(DurationFieldType.days(), result);
    }

    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        int result = period.getValue(0);
        assertEquals(1, result);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        int result = period.get(DurationFieldType.days());
        assertEquals(1, result);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        boolean result = period.isSupported(DurationFieldType.days());
        assertTrue(result);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        Period result = period.toPeriod();
        Period expected = new Period(1, 0, 0, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        MutablePeriod result = period.toMutablePeriod();
        MutablePeriod expected = new MutablePeriod();
        expected.add(period);
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodImpl(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodImpl(1);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriodImpl(2);
        boolean result1 = period1.equals(period2);
        boolean result2 = period1.equals(period3);
        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        int result = period.hashCode();
        int expected = 17 * 27 + 1;
        assertEquals(expected, result);
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodImpl(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodImpl(2);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriodImpl(3);
        int result1 = period1.compareTo(period2);
        int result2 = period2.compareTo(period1);
        int result3 = period2.compareTo(period3);
        assertEquals(-1, result1);
        assertEquals(1, result2);
        assertEquals(-1, result3);
    }
}

class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {

    protected BaseSingleFieldPeriodImpl(int period) {
        super(period);
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.days();
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.standard();
    }
}