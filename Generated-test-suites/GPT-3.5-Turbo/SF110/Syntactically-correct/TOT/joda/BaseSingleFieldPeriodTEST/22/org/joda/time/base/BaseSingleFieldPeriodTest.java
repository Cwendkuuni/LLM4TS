
package org.joda.time.base;

import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.Period;
import org.joda.time.MutablePeriod;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullReadableInstant() {
        BaseSingleFieldPeriod.between(null, ISOChronology.getInstanceUTC().now(), DurationFieldType.minutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullFieldType() {
        BaseSingleFieldPeriod.between(ISOChronology.getInstanceUTC().now(), ISOChronology.getInstanceUTC().now(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullReadablePartial() {
        BaseSingleFieldPeriod.between(null, ISOChronology.getInstanceUTC().now(), Period.hours());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ReadablePartialSizeMismatch() {
        BaseSingleFieldPeriod.between(ISOChronology.getInstanceUTC().now().toLocalDate(), ISOChronology.getInstanceUTC().now(), Period.hours());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ReadablePartialFieldsMismatch() {
        BaseSingleFieldPeriod.between(ISOChronology.getInstanceUTC().monthOfYear(), ISOChronology.getInstanceUTC().secondOfMinute(), Period.months());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NonContiguousReadablePartial() {
        BaseSingleFieldPeriod.between(ISOChronology.getInstanceUTC().monthOfYear(), ISOChronology.getInstanceUTC().yearOfCentury(), Period.years());
    }

    @Test
    public void testBetween_Valid() {
        ReadablePartial readablePartial1 = ISOChronology.getInstanceUTC().monthOfYear().set(ISOChronology.getInstanceUTC().toDateTime(), 1);
        ReadablePartial readablePartial2 = ISOChronology.getInstanceUTC().monthOfYear().set(ISOChronology.getInstanceUTC().toDateTime(), 2);
        int result = BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, Period.months());
        assertEquals(1, result);
    }

    @Test
    public void testStandardPeriodIn_NullReadablePeriod() {
        int result = BaseSingleFieldPeriod.standardPeriodIn(null, 1000);
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_NonPreciseFieldType() {
        ReadablePeriod nonPrecisePeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod.standardPeriodIn(nonPrecisePeriod, 1000);
    }

    @Test
    public void testStandardPeriodIn_Valid() {
        ReadablePeriod readablePeriod = new BaseSingleFieldPeriod(1000) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.millis();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 100);
        assertEquals(10, result);
    }

    @Test
    public void testGetValue_Valid() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int result = singleFieldPeriod.getValue();
        assertEquals(25, result);
    }

    @Test
    public void testSetValue_Valid() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        singleFieldPeriod.setValue(50);
        int result = singleFieldPeriod.getValue();
        assertEquals(50, result);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int result = singleFieldPeriod.size();
        assertEquals(1, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_InvalidIndex() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        singleFieldPeriod.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_InvalidIndex() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        singleFieldPeriod.getValue(1);
    }

    @Test
    public void testGet_Valid() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int result = singleFieldPeriod.get(DurationFieldType.minutes());
        assertEquals(25, result);
    }

    @Test
    public void testGet_Invalid() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int result = singleFieldPeriod.get(DurationFieldType.seconds());
        assertEquals(0, result);
    }

    @Test
    public void testIsSupported_Valid() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        boolean result = singleFieldPeriod.isSupported(DurationFieldType.minutes());
        assertTrue(result);
    }

    @Test
    public void testIsSupported_Invalid() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        boolean result = singleFieldPeriod.isSupported(DurationFieldType.seconds());
        assertFalse(result);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        Period result = singleFieldPeriod.toPeriod();
        assertEquals(25, result.getMinutes());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        MutablePeriod result = singleFieldPeriod.toMutablePeriod();
        assertEquals(25, result.getMinutes());
    }

    @Test
    public void testEquals_True() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        boolean result = singleFieldPeriod1.equals(singleFieldPeriod2);
        assertTrue(result);
    }

    @Test
    public void testEquals_False() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriod(30) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        boolean result = singleFieldPeriod1.equals(singleFieldPeriod2);
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int result = singleFieldPeriod.hashCode();
        assertEquals(67107025, result);
    }

    @Test
    public void testCompareTo_Greater() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriod(30) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        int result = singleFieldPeriod1.compareTo(singleFieldPeriod2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareTo_Less() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        int result = singleFieldPeriod1.compareTo(singleFieldPeriod2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareTo_Equal() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriod(25) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        int result = singleFieldPeriod1.compareTo(singleFieldPeriod2);
        assertEquals(0, result);
    }

}

