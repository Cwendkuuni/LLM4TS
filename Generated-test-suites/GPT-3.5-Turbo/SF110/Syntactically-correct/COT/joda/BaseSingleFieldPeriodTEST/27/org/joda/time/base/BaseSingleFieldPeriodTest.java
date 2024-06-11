package org.joda.time.base;

import org.joda.time.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        assertEquals(DurationFieldType.days(), period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeWithValidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        assertEquals(DurationFieldType.days(), period.getFieldType(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(1);
    }

    @Test
    public void testGetValueWithValidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(1);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        assertEquals(5, period.get(DurationFieldType.days()));
        assertEquals(0, period.get(DurationFieldType.hours()));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        assertTrue(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        Period expectedPeriod = new Period(0, 0, 0, 5);
        assertEquals(expectedPeriod, period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        MutablePeriod expectedPeriod = new MutablePeriod();
        expectedPeriod.add(period);
        assertEquals(expectedPeriod, period.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        int expectedHashCode = 27 * (27 * 17 + 5) + DurationFieldType.days().hashCode();
        assertEquals(expectedHashCode, period.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        assertEquals(-1, period1.compareTo(period2));
        assertEquals(1, period2.compareTo(period1));
        assertEquals(0, period1.compareTo(period1));
    }
}