package org.joda.time.base;

import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullReadableInstant() {
        BaseSingleFieldPeriod.between(null, null, DurationFieldType.seconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullReadablePartial() {
        BaseSingleFieldPeriod.between(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_SizeMismatch() {
        ReadablePartial rp1 = new ReadablePartial();
        ReadablePartial rp2 = new ReadablePartial();
        BaseSingleFieldPeriod.between(rp1, rp2, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_FieldTypeMismatch() {
        ReadablePartial rp1 = new ReadablePartial();
        ReadablePartial rp2 = new ReadablePartial();
        rp1.addField(null);
        BaseSingleFieldPeriod.between(rp1, rp2, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NotContiguous() {
        ReadablePartial rp1 = new ReadablePartial();
        ReadablePartial rp2 = new ReadablePartial();
        rp1.addField(DurationFieldType.days());
        rp2.addField(DurationFieldType.hours());
        BaseSingleFieldPeriod.between(rp1, rp2, null);
    }

    @Test
    public void testStandardPeriodIn_NullReadablePeriod() {
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_NonPreciseField() {
        ReadablePeriod rp = new ReadablePeriod();
        rp.add(DurationFieldType.years(), 1);
        BaseSingleFieldPeriod.standardPeriodIn(rp, 1);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertEquals(10, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        period.setValue(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertEquals(1, period.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_IndexOutOfBounds() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        period.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_IndexOutOfBounds() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        period.getValue(1);
    }

    @Test
    public void testGet_SameFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertEquals(10, period.get(DurationFieldType.days()));
    }

    @Test
    public void testGet_DifferentFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertEquals(0, period.get(DurationFieldType.hours()));
    }

    @Test
    public void testIsSupported_SameFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertTrue(period.isSupported(DurationFieldType.days()));
    }

    @Test
    public void testIsSupported_DifferentFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertEquals(Period.ZERO, period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertEquals((new MutablePeriod()).add(period), period.toMutablePeriod());
    }

    @Test
    public void testEquals_SameObject() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertTrue(period.equals(period));
    }

    @Test
    public void testEquals_NullObject() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertFalse(period.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertFalse(period.equals(new Object()));
    }

    @Test
    public void testEquals_DifferentPeriodType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.hours(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testEquals_DifferentValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertEquals(27 * (27 * 17 + 10) + DurationFieldType.days().hashCode(), period.hashCode());
    }

    @Test(expected = ClassCastException.class)
    public void testCompareTo_DifferentClass() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        period1.compareTo(period2);
    }
    
    @Test
    public void testCompareTo_GreaterValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(20) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertEquals(1, period1.compareTo(period2));
    }

    @Test
    public void testCompareTo_LesserValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20) {
            public DurationFieldType getFieldType() { return DurationFieldType.days(); }
            public PeriodType getPeriodType() { return null; }
        };
        assertEquals(-1, period1.compareTo(period2));
    }
}