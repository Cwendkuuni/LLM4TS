
package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullReadableInstant() {
        BaseSingleFieldPeriod.between(null, new DateTime(), DurationFieldType.seconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullReadableInstant2() {
        BaseSingleFieldPeriod.between(new DateTime(), null, DurationFieldType.seconds());
    }

    @Test
    public void testBetween_ValidData() {
        DateTime dt1 = new DateTime(2020, 1, 1, 0, 0, 0, 0);
        DateTime dt2 = new DateTime(2020, 1, 1, 0, 0, 10, 0);
        int seconds = BaseSingleFieldPeriod.between(dt1, dt2, DurationFieldType.seconds());
        assertEquals(10, seconds);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullReadablePartial() {
        BaseSingleFieldPeriod.between(null, new DateTime(), new Period());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullReadablePartial2() {
        BaseSingleFieldPeriod.between(new DateTime(), null, new Period());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_InvalidPartialSize() {
        BaseSingleFieldPeriod.between(new LocalDate(), new DateTime(), new Period());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_DifferentFieldTypes() {
        Period period1 = new Period(1, 0, 0, 0, 0, 0, 0, 0);
        Period period2 = new Period(0, 1, 0, 0, 0, 0, 0, 0);
        BaseSingleFieldPeriod.between(period1, period2, new Period());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NonContiguousReadablePartial() {
        LocalDate ld1 = new LocalDate(2020, 1, 1);
        LocalDate ld2 = new LocalDate(2020, 1, 3);
        BaseSingleFieldPeriod.between(ld1, ld2, new Period());
    }
    
    @Test
    public void testBetween_ValidPartial() {
        LocalDate ld1 = new LocalDate(2020, 1, 1);
        LocalDate ld2 = new LocalDate(2020, 3, 1);
        Period period = new Period(0, 2, 0, 0, 0, 0, 0, 0);
        int months = BaseSingleFieldPeriod.between(ld1, ld2, period);
        assertEquals(2, months);
    }

    @Test
    public void testStandardPeriodIn_NullReadablePeriod() {
        int result = BaseSingleFieldPeriod.standardPeriodIn(null, 1000);
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_NonPrecisePeriod() {
        Period period = new Period(0, 0, 0, 0, 0, 0, 1, 0);
        BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
    }

    @Test
    public void testStandardPeriodIn_ValidPeriod() {
        Period period = new Period(0, 0, 0, 1, 0, 0, 0, 0);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
        assertEquals(1, result);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod fieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(10, fieldPeriod.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod fieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        fieldPeriod.setValue(20);
        assertEquals(20, fieldPeriod.getValue());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod fieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(1, fieldPeriod.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_InvalidIndex() {
        BaseSingleFieldPeriod fieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        fieldPeriod.getFieldType(1);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod fieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(DurationFieldType.minutes(), fieldPeriod.getFieldType(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_InvalidIndex() {
        BaseSingleFieldPeriod fieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        fieldPeriod.getValue(1);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod fieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(10, fieldPeriod.getValue(0));
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod fieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(10, fieldPeriod.get(DurationFieldType.minutes()));
        assertEquals(0, fieldPeriod.get(DurationFieldType.hours()));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod fieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertTrue(fieldPeriod.isSupported(DurationFieldType.minutes()));
        assertFalse(fieldPeriod.isSupported(DurationFieldType.hours()));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod fieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Period period = fieldPeriod.toPeriod();
        assertEquals(10, period.getMinutes());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod fieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        MutablePeriod mutablePeriod = fieldPeriod.toMutablePeriod();
        assertEquals(10, mutablePeriod.getMinutes());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod fieldPeriod1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod fieldPeriod2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod fieldPeriod3 = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertTrue(fieldPeriod1.equals(fieldPeriod2));
        assertFalse(fieldPeriod1.equals(fieldPeriod3));
        assertFalse(fieldPeriod1.equals(null));
        assertFalse(fieldPeriod1.equals("not a period"));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod fieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        int expectedHashCode = 27 * (27 * 17 + 10) + DurationFieldType.minutes().hashCode();
        assertEquals(expectedHashCode, fieldPeriod.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod fieldPeriod1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod fieldPeriod2 = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod fieldPeriod3 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertTrue(fieldPeriod1.compareTo(fieldPeriod2) < 0);
        assertTrue(fieldPeriod2.compareTo(fieldPeriod1) > 0);
        assertTrue(fieldPeriod1.compareTo(fieldPeriod3) == 0);
        try {
            fieldPeriod1.compareTo(null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            // expected
        }
        try {
            fieldPeriod1.compareTo("not a period");
            fail("ClassCastException should be thrown");
        } catch (ClassCastException e) {
            // expected
        }
    }
}
