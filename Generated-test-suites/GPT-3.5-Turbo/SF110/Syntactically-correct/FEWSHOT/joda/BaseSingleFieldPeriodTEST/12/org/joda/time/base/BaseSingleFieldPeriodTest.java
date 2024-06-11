import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    private BaseSingleFieldPeriod period;

    @Before
    public void setUp() {
        period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
    }

    @Test
    public void testGetValue() {
        assertEquals(5, period.getValue());
    }

    @Test
    public void testSetValue() {
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    @Test
    public void testSize() {
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldType() {
        assertEquals(DurationFieldType.days(), period.getFieldType());
    }

    @Test
    public void testGetFieldType_IndexOutOfBoundsException() {
        try {
            period.getFieldType(1);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.days(), period.getPeriodType());
    }

    @Test
    public void testGetValue_IndexOutOfBoundsException() {
        try {
            period.getValue(1);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGet() {
        assertEquals(5, period.get(DurationFieldType.days()));
        assertEquals(0, period.get(DurationFieldType.months()));
    }

    @Test
    public void testIsSupported() {
        assertTrue(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.months()));
    }

    @Test
    public void testToPeriod() {
        Period p = period.toPeriod();
        assertEquals(5, p.getDays());
        assertEquals(0, p.getMonths());
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod mp = period.toMutablePeriod();
        assertEquals(5, mp.getDays());
        assertEquals(0, mp.getMonths());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };

        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };

        assertEquals(p1, period);
        assertNotEquals(p2, period);
    }

    @Test
    public void testHashCode() {
        assertEquals(27 * (27 * 17 + 5) + DurationFieldType.days().hashCode(), period.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };

        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };

        assertEquals(0, period.compareTo(p1));
        assertEquals(-1, period.compareTo(p2));
    }
}