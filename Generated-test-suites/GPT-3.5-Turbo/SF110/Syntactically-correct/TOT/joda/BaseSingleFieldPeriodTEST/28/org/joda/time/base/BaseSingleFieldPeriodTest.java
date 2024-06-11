import org.joda.time.DurationFieldType;
import org.joda.time.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod period;
    
    @Before
    public void setUp() {
        period = new BaseSingleFieldPeriodImpl(10);
    }
    
    @Test
    public void testGetValue() {
        assertEquals(10, period.getValue());
    }
    
    @Test
    public void testSetValue() {
        period.setValue(20);
        assertEquals(20, period.getValue());
    }
    
    @Test
    public void testGetFieldType() {
        assertEquals(DurationFieldType.YEARS_TYPE, period.getFieldType());
    }
    
    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.years(), period.getPeriodType());
    }
    
    @Test
    public void testSize() {
        assertEquals(1, period.size());
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
        assertEquals(10, period.get(DurationFieldType.YEARS_TYPE));
        assertEquals(0, period.get(DurationFieldType.MONTHS_TYPE));
    }
    
    @Test
    public void testIsSupported() {
        assertTrue(period.isSupported(DurationFieldType.YEARS_TYPE));
        assertFalse(period.isSupported(DurationFieldType.MONTHS_TYPE));
    }
    
    @Test
    public void testToPeriod() {
        Period p = period.toPeriod();
        assertEquals(10, p.getYears());
        assertEquals(0, p.getMonths());
        assertEquals(0, p.getWeeks());
        assertEquals(0, p.getDays());
        assertEquals(0, p.getHours());
        assertEquals(0, p.getMinutes());
        assertEquals(0, p.getSeconds());
        assertEquals(0, p.getMillis());
    }
    
    @Test
    public void testToMutablePeriod() {
        MutablePeriod mp = period.toMutablePeriod();
        assertEquals(10, mp.getYears());
        assertEquals(0, mp.getMonths());
        assertEquals(0, mp.getWeeks());
        assertEquals(0, mp.getDays());
        assertEquals(0, mp.getHours());
        assertEquals(0, mp.getMinutes());
        assertEquals(0, mp.getSeconds());
        assertEquals(0, mp.getMillis());
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriodImpl(20);
        
        assertTrue(period.equals(p1));
        assertTrue(p1.equals(period));
        assertTrue(p1.equals(p2));
        assertFalse(p1.equals(p3));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriodImpl(10);
        
        assertEquals(p1.hashCode(), p2.hashCode());
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod p1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod p2 = new BaseSingleFieldPeriodImpl(20);
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriodImpl(10);
        
        assertTrue(p2.compareTo(p1) > 0);
        assertTrue(p1.compareTo(p2) < 0);
        assertTrue(p3.compareTo(p1) == 0);
    }
    
    private class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {
    
        protected BaseSingleFieldPeriodImpl(int iPeriod) {
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