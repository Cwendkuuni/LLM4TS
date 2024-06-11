import org.joda.time.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod period;

    @Before
    public void setUp() {
        period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
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
        assertNull(period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        assertNull(period.getPeriodType());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeOutOfBounds() {
        period.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueOutOfBounds() {
        period.getValue(1);
    }

    @Test
    public void testGet() {
        assertEquals(5, period.get(null));
    }

    @Test
    public void testIsSupported() {
        assertTrue(period.isSupported(null));
    }

    @Test
    public void testToPeriod() {
        Period expectedPeriod = Period.ZERO.withFields(period);
        assertEquals(expectedPeriod, period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod expectedPeriod = new MutablePeriod();
        expectedPeriod.add(period);
        assertEquals(expectedPeriod, period.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod otherPeriod = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertTrue(period.equals(otherPeriod));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod otherPeriod = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(period.hashCode(), otherPeriod.hashCode());
    }

    @Test
    public void testCompareTo_SameClass() {
        BaseSingleFieldPeriod smallerPeriod = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod greaterPeriod = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        assertTrue(period.compareTo(smallerPeriod) > 0);
        assertTrue(period.compareTo(greaterPeriod) < 0);
        assertTrue(period.compareTo(period) == 0);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareTo_DifferentClass() {
        BaseSingleFieldPeriod otherPeriod = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        period.compareTo(otherPeriod);
    }
}