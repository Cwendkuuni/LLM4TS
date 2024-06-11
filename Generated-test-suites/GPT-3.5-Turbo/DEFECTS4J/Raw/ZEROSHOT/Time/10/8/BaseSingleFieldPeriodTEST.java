import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod period;

    @Before
    public void setUp() {
        // Initialize period
        period = new BaseSingleFieldPeriod(10) {
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
        // Test getValue method
        assertEquals(10, period.getValue());
    }

    @Test
    public void testSetValue() {
        // Test setValue method
        period.setValue(20);
        assertEquals(20, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        // Test getFieldType method
        assertEquals(DurationFieldType.days(), period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        // Test getPeriodType method
        assertEquals(PeriodType.days(), period.getPeriodType());
    }

    @Test
    public void testSize() {
        // Test size method
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        // Test getFieldType method with index
        assertEquals(DurationFieldType.days(), period.getFieldType(0));
        try {
            period.getFieldType(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testGetValueAtIndex() {
        // Test getValue method with index
        assertEquals(10, period.getValue(0));
        try {
            period.getValue(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testGet() {
        // Test get method
        assertEquals(10, period.get(DurationFieldType.days()));
        assertEquals(0, period.get(DurationFieldType.hours()));
    }

    @Test
    public void testIsSupported() {
        // Test isSupported method
        assertTrue(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }

    @Test
    public void testToPeriod() {
        // Test toPeriod method
        Period p = period.toPeriod();
        assertEquals(10, p.getDays());
        assertEquals(0, p.getHours());
    }

    @Test
    public void testToMutablePeriod() {
        // Test toMutablePeriod method
        MutablePeriod mp = period.toMutablePeriod();
        assertEquals(10, mp.getDays());
        assertEquals(0, mp.getHours());
    }

    @Test
    public void testEquals() {
        // Test equals method
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertTrue(period.equals(period2));
        assertFalse(period.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        // Test hashCode method
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertEquals(period.hashCode(), period2.hashCode());
    }

    @Test
    public void testCompareTo() {
        // Test compareTo method
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertTrue(period.compareTo(period2) < 0);
        assertTrue(period2.compareTo(period) > 0);
        assertTrue(period.compareTo(period) == 0);
    }
}