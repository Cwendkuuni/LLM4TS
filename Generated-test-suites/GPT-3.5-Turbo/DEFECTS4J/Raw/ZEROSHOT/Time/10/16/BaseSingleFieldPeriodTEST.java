import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.*;

public class BaseSingleFieldPeriodTest {

    // Test getValue()
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        assertEquals(10, period.getValue());
    }

    // Test setValue()
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        period.setValue(20);
        assertEquals(20, period.getValue());
    }

    // Test getFieldType()
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        assertEquals(DurationFieldType.class, period.getFieldType().getClass());
    }

    // Test getPeriodType()
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        assertEquals(PeriodType.class, period.getPeriodType().getClass());
    }

    // Test size()
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        assertEquals(1, period.size());
    }

    // Test getFieldType(int index)
    @Test
    public void testGetFieldTypeIndex() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        assertEquals(DurationFieldType.class, period.getFieldType(0).getClass());
    }

    // Test getValue(int index)
    @Test
    public void testGetValueIndex() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        assertEquals(10, period.getValue(0));
    }

    // Test get(DurationFieldType type)
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        assertEquals(10, period.get(period.getFieldType()));
    }

    // Test isSupported(DurationFieldType type)
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        assertTrue(period.isSupported(period.getFieldType()));
        assertFalse(period.isSupported(DurationFieldType.weeks()));
    }

    // Test toPeriod()
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        Period p = period.toPeriod();
        assertEquals(10, p.get(period.getFieldType()));
    }

    // Test toMutablePeriod()
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        MutablePeriod mp = period.toMutablePeriod();
        assertEquals(10, mp.get(period.getFieldType()));
    }

    // Test equals(Object period)
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(10);
        BaseSingleFieldPeriod period3 = new TestSingleFieldPeriod(20);

        assertTrue(period1.equals(period1));
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
        assertFalse(period1.equals(null));
        assertFalse(period1.equals("test"));
    }

    // Test hashCode()
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(10);
        assertEquals(2290, period.hashCode());
    }

    // Test compareTo(BaseSingleFieldPeriod other)
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(20);
        BaseSingleFieldPeriod period3 = new TestSingleFieldPeriod(10);

        assertTrue(period1.compareTo(period1) == 0);
        assertTrue(period1.compareTo(period2) < 0);
        assertTrue(period2.compareTo(period1) > 0);
        assertTrue(period1.compareTo(period3) == 0);

        try {
            period1.compareTo(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            period1.compareTo(new BaseSingleFieldPeriod(5){});
            fail("ClassCastException expected");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    private class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
        public TestSingleFieldPeriod(int period) {
            super(period);
        }
        public DurationFieldType getFieldType() {
            return DurationFieldType.years();
        }
        public PeriodType getPeriodType() {
            return PeriodType.yearMonthDayTime();
        }
    }

}