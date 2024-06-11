import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {
    
    private BaseSingleFieldPeriod period;
    
    @Before
    public void setUp() {
        period = new BaseSingleFieldPeriodStub(5);
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
    public void testGetFieldType() {
        assertEquals(DurationFieldType.years(), period.getFieldType());
    }
    
    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }
    
    @Test
    public void testSize() {
        assertEquals(1, period.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        period.getFieldType(1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        period.getValue(1);
    }
    
    @Test
    public void testGet() {
        assertEquals(5, period.get(DurationFieldType.years()));
        assertEquals(0, period.get(DurationFieldType.months()));
    }
    
    @Test
    public void testIsSupported() {
        assertTrue(period.isSupported(DurationFieldType.years()));
        assertFalse(period.isSupported(DurationFieldType.months()));
    }
    
    @Test
    public void testToPeriod() {
        Period result = period.toPeriod();
        assertEquals(5, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getDays());
        assertEquals(0, result.getHours());
    }
    
    @Test
    public void testToMutablePeriod() {
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(5, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getDays());
        assertEquals(0, result.getHours());
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod samePeriod = new BaseSingleFieldPeriodStub(5);
        BaseSingleFieldPeriod differentPeriod = new BaseSingleFieldPeriodStub(10);
        
        assertTrue(period.equals(samePeriod));
        assertFalse(period.equals(differentPeriod));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod samePeriod = new BaseSingleFieldPeriodStub(5);
        
        assertEquals(period.hashCode(), samePeriod.hashCode());
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod smallerPeriod = new BaseSingleFieldPeriodStub(2);
        BaseSingleFieldPeriod biggerPeriod = new BaseSingleFieldPeriodStub(10);
        
        assertTrue(period.compareTo(smallerPeriod) > 0);
        assertTrue(period.compareTo(biggerPeriod) < 0);
        assertTrue(period.compareTo(period) == 0);
    }
    
    public class BaseSingleFieldPeriodStub extends BaseSingleFieldPeriod {
        public BaseSingleFieldPeriodStub(int iPeriod) {
            super(iPeriod);
        }
        
        public DurationFieldType getFieldType() {
            return DurationFieldType.years();
        }
        
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }
}