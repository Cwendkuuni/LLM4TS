package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    private BaseSingleFieldPeriod period;

    @Before
    public void setUp() {
        period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.seconds();
            }
        };
    }

    @Test
    public void testGetValue() {
        assertEquals(10, period.getValue());
    }

    @Test
    public void testSetValue() {
        period.setValue(20);
        assertEquals(20, period.getValue());
    }

    @Test
    public void testSize() {
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldType() {
        assertEquals(DurationFieldType.seconds(), period.getFieldType());
    }

    @Test
    public void testGetFieldType_InvalidIndex() {
        try {
            period.getFieldType(1);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetValue_InvalidIndex() {
        try {
            period.getValue(1);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGet() {
        assertEquals(10, period.get(DurationFieldType.seconds()));
        assertEquals(0, period.get(DurationFieldType.minutes()));
    }

    @Test
    public void testIsSupported() {
        assertTrue(period.isSupported(DurationFieldType.seconds()));
        assertFalse(period.isSupported(DurationFieldType.minutes()));
    }

    @Test
    public void testToPeriod() {
        Period expected = new Period(0, 0, 0, 10, 0, 0, 0, 0);
        assertEquals(expected, period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        assertEquals(new Period(0, 0, 0, 10, 0, 0, 0, 0), period.toMutablePeriod());
    }

    @Test
    public void testEquals_Itself() {
        assertTrue(period.equals(period));
    }

    @Test
    public void testEquals_Null() {
        assertFalse(period.equals(null));
    }

    @Test
    public void testEquals_DifferentType() {
        assertFalse(period.equals(new Object()));
    }

    @Test
    public void testEquals_EqualPeriod() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.seconds();
            }
        };

        assertTrue(period.equals(other));
    }

    @Test
    public void testEquals_NotEqualPeriod() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.seconds();
            }
        };

        assertFalse(period.equals(other));
    }

    @Test
    public void testHashCode_EqualPeriod() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.seconds();
            }
        };

        assertEquals(period.hashCode(), other.hashCode());
    }

    @Test
    public void testHashCode_NotEqualPeriod() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.seconds();
            }
        };

        assertNotEquals(period.hashCode(), other.hashCode());
    }

    @Test
    public void testCompareTo_EqualPeriod() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.seconds();
            }
        };

        assertEquals(0, period.compareTo(other));
    }

    @Test
    public void testCompareTo_SmallerPeriod() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.seconds();
            }
        };

        assertEquals(1, period.compareTo(other));
    }

    @Test
    public void testCompareTo_LargerPeriod() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.seconds();
            }
        };

        assertEquals(-1, period.compareTo(other));
    }

    @Test(expected = ClassCastException.class)
    public void testCompareTo_DifferentType() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.millis();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.millis();
            }
        };

        period.compareTo(other);
    }
}