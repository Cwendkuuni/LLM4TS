import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    
    private BaseSingleFieldPeriod period;
    
    @Before
    public void setUp() {
        period = new BaseSingleFieldPeriodImpl(5);
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
        assertEquals(DurationFieldType.class, period.getFieldType().getClass());
    }
    
    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.class, period.getPeriodType().getClass());
    }
    
    @Test
    public void testSize() {
        assertEquals(1, period.size());
    }
    
    @Test
    public void testGetFieldTypeWithValidIndex() {
        assertEquals(DurationFieldType.class, period.getFieldType(0).getClass());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        period.getFieldType(1);
    }
    
    @Test
    public void testGetValueWithValidIndex() {
        assertEquals(5, period.getValue(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        period.getValue(1);
    }
    
    @Test
    public void testGetWithMatchingFieldType() {
        assertEquals(5, period.get(period.getFieldType()));
    }
    
    @Test
    public void testGetWithNonMatchingFieldType() {
        assertEquals(0, period.get(DurationFieldType.centuries()));
    }
    
    @Test
    public void testIsSupportedWithMatchingFieldType() {
        assertTrue(period.isSupported(period.getFieldType()));
    }
    
    @Test
    public void testIsSupportedWithNonMatchingFieldType() {
        assertFalse(period.isSupported(DurationFieldType.centuries()));
    }
    
    @Test
    public void testToPeriod() {
        Period result = period.toPeriod();
        
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(5, result.getMillis());
    }
    
    @Test
    public void testToMutablePeriod() {
        MutablePeriod result = period.toMutablePeriod();
        
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(5, result.getMillis());
    }
    
    @Test
    public void testEqualsWithEqualObject() {
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodImpl(5);
        assertTrue(period.equals(period2));
    }
    
    @Test
    public void testEqualsWithDifferentValue() {
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodImpl(10);
        assertFalse(period.equals(period2));
    }
    
    @Test
    public void testEqualsWithDifferentType() {
        assertFalse(period.equals(new Object()));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(5, period.hashCode());
    }
    
    @Test
    public void testCompareToWithEqualPeriod() {
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodImpl(5);
        assertEquals(0, period.compareTo(period2));
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentType() {
        period.compareTo(new BaseSingleFieldPeriodImpl(10));
    }
    
    private class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {

        protected BaseSingleFieldPeriodImpl(int iPeriod) {
            super(iPeriod);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.seconds();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.time();
        }
        
    }
}