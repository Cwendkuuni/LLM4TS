import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }
    
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }
    
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertNotNull(period.getFieldType());
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertNotNull(period.getPeriodType());
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }
    
    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(period.getFieldType(), period.getFieldType(0));
    }
    
    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(period.getValue(), period.getValue(0));
    }
    
    @Test
    public void testGetFieldByType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertEquals(5, period.get(period.getFieldType()));
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertTrue(period.isSupported(period.getFieldType()));
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertNotNull(period.toPeriod());
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertNotNull(period.toMutablePeriod());
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(5);
        assertNotNull(period.hashCode());
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(10);
        assertTrue(period1.compareTo(period2) < 0);
    }
    
    private class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
        
        TestSingleFieldPeriod(int period) {
            super(period);
        }
        
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.days();
        }
        
        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }

}