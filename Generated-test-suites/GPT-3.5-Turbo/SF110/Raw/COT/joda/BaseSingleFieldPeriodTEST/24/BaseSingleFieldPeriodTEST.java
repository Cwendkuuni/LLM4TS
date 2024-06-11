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
                return DurationFieldType.SECONDS;
            }
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(DurationFieldType.SECONDS, period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.SECONDS;
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
    public void testGetFieldTypeByIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.SECONDS;
            }
        };
        assertEquals(DurationFieldType.SECONDS, period.getFieldType(0));
        try {
            period.getFieldType(1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testGetValueByIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue(0));
        try {
            period.getValue(1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.SECONDS;
            }
        };
        assertEquals(5, period.get(DurationFieldType.SECONDS));
        assertEquals(0, period.get(DurationFieldType.MINUTES));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.SECONDS;
            }
        };
        assertTrue(period.isSupported(DurationFieldType.SECONDS));
        assertFalse(period.isSupported(DurationFieldType.MINUTES));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.SECONDS;
            }
        };
        assertEquals(new Period(0, 0, 0, 0, 0, 5, 0, 0), period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.SECONDS;
            }
        };
        assertEquals(new MutablePeriod().withSeconds(5), period.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.SECONDS;
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.SECONDS;
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.SECONDS;
            }
        };
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }
        };

        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
        assertFalse(period1.equals(period4));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.SECONDS;
            }
        };
        assertEquals(949, period.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.SECONDS;
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.SECONDS;
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }
        };

        assertTrue(period1.compareTo(period2) < 0);
        assertTrue(period2.compareTo(period1) > 0);
        assertEquals(0, period1.compareTo(period1));
        try {
            period1.compareTo(period3);
            fail("ClassCastException expected");
        } catch (ClassCastException e) {
            // expected
        }
    }
}