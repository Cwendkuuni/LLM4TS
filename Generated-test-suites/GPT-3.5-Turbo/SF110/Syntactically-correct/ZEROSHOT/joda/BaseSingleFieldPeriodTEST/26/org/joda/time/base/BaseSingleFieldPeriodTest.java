package org.joda.time.base;

import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.MutablePeriod;
import java.io.Serializable;
import java.lang.ClassCastException;
import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalArgumentException;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadableInstant() {
        BaseSingleFieldPeriod.between(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadablePartial() {
        BaseSingleFieldPeriod.between(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithDifferentSizes() {
        ReadablePartial readablePartial = new DateTime();
        ReadablePartial readablePartial2 = new DateTime().plusDays(1);
        ReadablePeriod readablePeriod = new Period();
        BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithDifferentFieldTypes() {
        ReadablePartial readablePartial = new DateTime();
        ReadablePartial readablePartial2 = new DateTime().plusHours(1);
        ReadablePeriod readablePeriod = new Period();
        BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNonContiguousReadablePartial() {
        ReadablePartial readablePartial = new DateTime().withYear(2000);
        ReadablePartial readablePartial2 = new DateTime().withYear(2002);
        ReadablePeriod readablePeriod = new Period();
        BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
    }

    @Test
    public void testStandardPeriodInWithNullReadablePeriod() {
        int result = BaseSingleFieldPeriod.standardPeriodIn(null, 1);
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithUnpreciseField() {
        ReadablePeriod readablePeriod = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 1);
    }

    @Test
    public void testBaseSingleFieldPeriodConstructorAndGetters() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(1, baseSingleFieldPeriod.getValue());
        baseSingleFieldPeriod.setValue(2);
        assertEquals(2, baseSingleFieldPeriod.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFieldTypeIndexOutOfBoundsException() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        baseSingleFieldPeriod.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValueIndexOutOfBoundsException() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        baseSingleFieldPeriod.getValue(1);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(1, baseSingleFieldPeriod.get(baseSingleFieldPeriod.getFieldType()));
        assertEquals(0, baseSingleFieldPeriod.get(null));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertTrue(baseSingleFieldPeriod.isSupported(baseSingleFieldPeriod.getFieldType()));
        assertFalse(baseSingleFieldPeriod.isSupported(null));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        Period period = baseSingleFieldPeriod.toPeriod();
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
        assertEquals(0, mutablePeriod.getMillis());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertTrue(baseSingleFieldPeriod.equals(new Period(1)));
        assertFalse(baseSingleFieldPeriod.equals(null));
        assertFalse(baseSingleFieldPeriod.equals(""));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(27 * (27 * 17 + 1), baseSingleFieldPeriod.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod3 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(1, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod2));
        assertEquals(-1, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod3));
        try {
            baseSingleFieldPeriod.compareTo(new Period());
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            assertEquals(BaseSingleFieldPeriod.class + " cannot be compared to " + Period.class, e.getMessage());
        }
    }

}