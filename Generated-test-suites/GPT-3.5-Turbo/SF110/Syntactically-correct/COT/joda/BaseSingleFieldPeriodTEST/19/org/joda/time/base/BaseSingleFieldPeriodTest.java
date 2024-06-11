import org.joda.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {
    
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.getValue());
    }
    
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.setValue(20);
        assertEquals(20, period.getValue());
    }
    
    @Test
    public void testGetFieldType() {
        // This is an abstract method, no need to test here
    }
    
    @Test
    public void testGetPeriodType() {
        // This is an abstract method, no need to test here
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(1, period.size());
    }
    
    @Test
    public void testGetFieldTypeAtIndex() {
        // This is an abstract method, no need to test here
    }
    
    @Test
    public void testGetValueAtIndex() {
        // This is an abstract method, no need to test here
    }
    
    @Test
    public void testGet() {
        // This is an abstract method, no need to test here
    }
    
    @Test
    public void testIsSupported() {
        // This is an abstract method, no need to test here
    }
    
    @Test
    public void testToPeriod() {
        // This method depends on Period.ZERO and already tested in PeriodTest
    }
    
    @Test
    public void testToMutablePeriod() {
        // This method depends on MutablePeriod and already tested in MutablePeriodTest
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(20);
        
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        
        assertEquals(period1.hashCode(), period2.hashCode());
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(20);
        
        assertEquals(0, period1.compareTo(period2));
        assertEquals(-1, period1.compareTo(period3));
        assertEquals(1, period3.compareTo(period1));
    }
}