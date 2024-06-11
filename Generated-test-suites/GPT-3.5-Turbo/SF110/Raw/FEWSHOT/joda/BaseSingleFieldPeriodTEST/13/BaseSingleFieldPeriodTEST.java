package org.joda.time.base;

import org.joda.time.chrono.ISOChronology;
import org.joda.time.DurationField;
import org.joda.time.field.FieldUtils;
import org.joda.time.DateTimeUtils;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Chronology;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class BaseSingleFieldPeriodTest {

    @Test(expected=IllegalArgumentException.class)
    public void testBetweenWithNullReadableInstant() {
        BaseSingleFieldPeriod.between(null, null, DurationFieldType.year());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBetweenWithNullReadablePeriod() {
        BaseSingleFieldPeriod.between(new ReadablePartial(), new ReadablePartial(), null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBetweenWithDifferentPartialSizes() {
        ReadablePartial rp1 = new ReadablePartial();
        ReadablePartial rp2 = new ReadablePartial();
        rp1.addField(DateTimeFieldType.year(), 1);
        rp2.addField(DateTimeFieldType.monthOfYear(), 1);
        BaseSingleFieldPeriod.between(rp1, rp2, Period.ZERO);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBetweenWithDifferentFieldTypes() {
        ReadablePartial rp1 = new ReadablePartial();
        ReadablePartial rp2 = new ReadablePartial();
        rp1.addField(DateTimeFieldType.year(), 1);
        rp2.addField(DateTimeFieldType.yearOfEra(), 1);
        BaseSingleFieldPeriod.between(rp1, rp2, Period.ZERO);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBetweenWithNonContiguousPartial() {
        ReadablePartial rp1 = new ReadablePartial();
        rp1.addField(DateTimeFieldType.year(), 1);
        rp1.addField(DateTimeFieldType.dayOfMonth(), 1);
        BaseSingleFieldPeriod.between(rp1, rp1, Period.ZERO);
    }

    @Test
    public void testBetween() {
        ReadablePartial rp1 = new ReadablePartial();
        rp1.addField(DateTimeFieldType.year(), 1);
        ReadablePartial rp2 = new ReadablePartial();
        rp2.addField(DateTimeFieldType.year(), 2);
        assertEquals(1, BaseSingleFieldPeriod.between(rp1, rp2, Period.ZERO));
    }

    @Test
    public void testStandardPeriod() {
        ReadablePeriod period = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(1, BaseSingleFieldPeriod.standardPeriodIn(period, 1));
    }

    @Test
    public void testStandardPeriodWithNull() {
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, 1));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFieldNotPrecise() {
        ReadablePeriod period = new Period(1, 0, 0, 0, 0, 0, 0, 0);
        BaseSingleFieldPeriod.standardPeriodIn(period, 2);
    }

    @Test
    public void testBaseSingleFieldPeriodConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(1, period.getValue());
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(1, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        period.setValue(2);
        assertEquals(2, period.getValue());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(DurationFieldType.years(), period.getFieldType());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(DurationFieldType.years(), period.getFieldType(0));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testGetFieldTypeAtInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        period.getFieldType(1);
    }

    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(1, period.getValue(0));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testGetValueAtInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        period.getValue(1);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(1, period.get(DurationFieldType.years()));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertTrue(period.isSupported(DurationFieldType.years()));
        assertFalse(period.isSupported(DurationFieldType.months()));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertNotNull(period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertNotNull(period.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.months();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.time();
            }
        };

        assertEquals(period1, period1);
        assertNotEquals(period1, null);
        assertNotEquals(period1, "string");
        assertNotEquals(period1, period2);
        assertNotEquals(period1, period3);
        assertNotEquals(period1, period4);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(1343406129, period.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.months();
            }

            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(1) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            public PeriodType getPeriodType() {
                return PeriodType.time();
            }
        };

        assertEquals(0, period1.compareTo(period1));
        assertEquals(-1, period1.compareTo(period2));
        assertEquals(1, period2.compareTo(period1));
        assertEquals(-1, period1.compareTo(period3));
        assertEquals(1, period3.compareTo(period1));
        assertEquals(-1, period1.compareTo(period4));
        assertEquals(1, period4.compareTo(period1));
    }

}