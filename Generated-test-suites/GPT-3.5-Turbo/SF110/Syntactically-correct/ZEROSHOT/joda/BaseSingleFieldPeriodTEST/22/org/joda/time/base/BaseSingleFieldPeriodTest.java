import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.*;
import org.joda.time.base.*;

public class BaseSingleFieldPeriodTest {
    
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodTestImpl(1);
        assertEquals(DurationFieldType.years(), period.getFieldType());
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodTestImpl(1);
        assertEquals(PeriodType.years(), period.getPeriodType());
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodTestImpl(1);
        assertEquals(1, period.size());
    }
    
    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodTestImpl(1);
        assertEquals(DurationFieldType.years(), period.getFieldType(0));
    }
    
    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodTestImpl(1);
        assertEquals(1, period.getValue(0));
    }
    
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodTestImpl(1);
        assertEquals(1, period.get(DurationFieldType.years()));
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodTestImpl(1);
        assertTrue(period.isSupported(DurationFieldType.years()));
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodTestImpl(1);
        Period expected = Period.ZERO.withYears(1);
        assertEquals(expected, period.toPeriod());
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodTestImpl(1);
        MutablePeriod expected = new MutablePeriod();
        expected.addYears(1);
        assertEquals(expected, period.toMutablePeriod());
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodTestImpl(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodTestImpl(2);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriodTestImpl(1);
        
        assertTrue(period1.equals(period3));
        assertFalse(period1.equals(period2));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodTestImpl(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodTestImpl(2);
        
        assertEquals(1311, period1.hashCode());
        assertEquals(1312, period2.hashCode());
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodTestImpl(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodTestImpl(2);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriodTestImpl(1);
        
        assertTrue(period1.compareTo(period2) < 0);
        assertTrue(period2.compareTo(period1) > 0);
        assertEquals(0, period1.compareTo(period3));
    }
    
    private class BaseSingleFieldPeriodTestImpl extends BaseSingleFieldPeriod {
        
        public BaseSingleFieldPeriodTestImpl(int iPeriod) {
            super(iPeriod);
        }
        
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.years();
        }
        
        @Override
        public PeriodType getPeriodType() {
            return PeriodType.years();
        }
    }
}