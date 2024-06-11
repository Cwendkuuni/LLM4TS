import org.joda.time.*;
import org.joda.time.field.UnsupportedDurationField;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new TestPeriod(5);
        assertEquals(DurationFieldType.days(), period.getFieldType());
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new TestPeriod(5);
        assertEquals(1, period.size());
    }
    
    @Test
    public void testGetFieldTypeAtIndexZero() {
        BaseSingleFieldPeriod period = new TestPeriod(5);
        assertEquals(DurationFieldType.days(), period.getFieldType(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeAtIndexNonZero() {
        BaseSingleFieldPeriod period = new TestPeriod(5);
        period.getFieldType(1);
    }
    
    @Test
    public void testGetValueAtIndexZero() {
        BaseSingleFieldPeriod period = new TestPeriod(5);
        assertEquals(5, period.getValue(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAtIndexNonZero() {
        BaseSingleFieldPeriod period = new TestPeriod(5);
        period.getValue(1);
    }
    
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new TestPeriod(5);
        assertEquals(5, period.get(DurationFieldType.days()));
        assertEquals(0, period.get(DurationFieldType.months()));
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new TestPeriod(5);
        assertTrue(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.months()));
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new TestPeriod(5);
        Period expected = Period.days(5);
        assertEquals(expected, period.toPeriod());
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new TestPeriod(5);
        MutablePeriod expected = new MutablePeriod();
        expected.add(period);
        assertEquals(expected, period.toMutablePeriod());
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new TestPeriod(5);
        BaseSingleFieldPeriod period2 = new TestPeriod(5);
        BaseSingleFieldPeriod period3 = new TestPeriod(10);
        
        assertTrue(period1.equals(period1));
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
        
        assertFalse(period1.equals(null));
        assertFalse(period1.equals("String"));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new TestPeriod(5);
        int expected = 17 * 27 + 5;
        expected = 27 * expected + DurationFieldType.days().hashCode();
        assertEquals(expected, period.hashCode());
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new TestPeriod(5);
        BaseSingleFieldPeriod period2 = new TestPeriod(5);
        BaseSingleFieldPeriod period3 = new TestPeriod(10);
        
        assertEquals(0, period1.compareTo(period1));
        assertEquals(0, period1.compareTo(period2));
        assertEquals(1, period3.compareTo(period2));
        assertEquals(-1, period1.compareTo(period3));
        
        try {
            period1.compareTo(new TestPeriod2(5));
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            // expected
        }
    }

    private static class TestPeriod extends BaseSingleFieldPeriod {

        public TestPeriod(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.days();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.days();
        }

    }

    private static class TestPeriod2 extends BaseSingleFieldPeriod {

        public TestPeriod2(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.weeks();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.weeks();
        }

    }
}