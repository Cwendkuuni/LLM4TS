import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetValue() {
        int value = 10;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(value);
        assertEquals(value, period.getValue());
    }

    @Test
    public void testSetValue() {
        int value = 10;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0);
        period.setValue(value);
        assertEquals(value, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(DurationFieldType.years(), period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            public DurationFieldType getFieldType() {
                return null;
            }
            public PeriodType getPeriodType() {
                return PeriodType.years();
            }
        };
        assertEquals(PeriodType.years(), period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeInt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            public DurationFieldType getFieldType() {
                return null;
            }
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(null, period.getFieldType(1));
    }

    @Test
    public void testGetValueInt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return null;
            }
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(10, period.getValue(1));
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(10, period.get(DurationFieldType.years()));
        assertEquals(0, period.get(DurationFieldType.months()));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertTrue(period.isSupported(DurationFieldType.years()));
        assertFalse(period.isSupported(DurationFieldType.months()));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }
            public PeriodType getPeriodType() {
                return null;
            }
        };
        Period expectedPeriod = new Period(10);
        assertEquals(expectedPeriod, period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }
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
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return null;
            }
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
        assertFalse(period1.equals(null));
        assertFalse(period1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(8541, period.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(15) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(1, period1.compareTo(period2));
        assertEquals(-1, period2.compareTo(period1));
        assertEquals(0, period1.compareTo(period3));
    }

}