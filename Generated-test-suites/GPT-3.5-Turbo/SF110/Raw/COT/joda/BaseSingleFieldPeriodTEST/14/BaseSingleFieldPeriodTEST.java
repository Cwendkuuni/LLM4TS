import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.MutablePeriod;
import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.class, fieldType.getClass());
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.class, periodType.getClass());
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        int size = period.size();
        assertEquals(1, size);
    }
    
    @Test
    public void testGetFieldTypeWithIndex0() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType(0);
        assertEquals(DurationFieldType.class, fieldType.getClass());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        period.getFieldType(1);
    }
    
    @Test
    public void testGetValueWithIndex0() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        int value = period.getValue(0);
        assertEquals(5, value);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        period.getValue(1);
    }
    
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        int value = period.get(period.getFieldType());
        assertEquals(5, value);
    }
    
    @Test
    public void testGetWithDifferentFieldType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        int value = period.get(DurationFieldType.months());
        assertEquals(0, value);
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        boolean supported = period.isSupported(period.getFieldType());
        assertTrue(supported);
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        Period p = period.toPeriod();
        assertNotNull(p);
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        MutablePeriod mp = period.toMutablePeriod();
        assertNotNull(mp);
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(5);
        boolean equals = period1.equals(period2);
        assertTrue(equals);
    }
    
    @Test
    public void testEqualsWithDifferentPeriodType() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(5) {
            public PeriodType getPeriodType() {
                return PeriodType.minutes();
            }
        };
        boolean equals = period1.equals(period2);
        assertFalse(equals);
    }
    
    @Test
    public void testEqualsWithDifferentValue() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(10);
        boolean equals = period1.equals(period2);
        assertFalse(equals);
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        int hashCode = period.hashCode();
        assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(10);
        int result = period1.compareTo(period2);
        assertTrue(result < 0);
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClass() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }
            public PeriodType getPeriodType() {
                return PeriodType.years();
            }
        };
        period1.compareTo(period2);
    }
    
    private class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
        public TestSingleFieldPeriod(int iPeriod) {
            super(iPeriod);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.minutes();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.minutes();
        }
    }
}