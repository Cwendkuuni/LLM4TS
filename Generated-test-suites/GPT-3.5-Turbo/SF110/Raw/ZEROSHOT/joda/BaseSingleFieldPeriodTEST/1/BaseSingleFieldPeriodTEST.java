import org.joda.time.DateTimeUtils;
import org.joda.time.Period;
import org.joda.time.field.FieldUtils;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    private class TestPeriod extends BaseSingleFieldPeriod {

        public TestPeriod(int iPeriod) {
            super(iPeriod);
        }

        @Override
        public DurationFieldType getFieldType() {
            return null;
        }

        @Override
        public PeriodType getPeriodType() {
            return null;
        }
    }
    
    private TestPeriod testPeriod;
    
    @Before
    public void setUp() {
        testPeriod = new TestPeriod(5);
    }
    
    @Test
    public void testGetValue() {
        assertEquals(5, testPeriod.getValue());
    }
    
    @Test
    public void testSetValue() {
        testPeriod.setValue(10);
        assertEquals(10, testPeriod.getValue());
    }
    
    @Test
    public void testSize() {
        assertEquals(1, testPeriod.size());
    }
    
    @Test
    public void testGetFieldType() {
        assertNull(testPeriod.getFieldType());
    }
    
    @Test
    public void testGetPeriodType() {
        assertNull(testPeriod.getPeriodType());
    }
    
    @Test
    public void testGetSize() {
        assertEquals(1, testPeriod.size());
    }
    
    @Test
    public void testGetFieldTypeAtIndex() {
        assertNull(testPeriod.getFieldType(0));
    }
    
    @Test
    public void testGetValueAtIndex() {
        assertEquals(5, testPeriod.getValue(0));
    }
    
    @Test
    public void testGet() {
        assertEquals(5, testPeriod.get(null));
    }
    
    @Test
    public void testIsSupported() {
        assertTrue(testPeriod.isSupported(null));
    }
    
    @Test
    public void testToPeriod() {
        Period period = testPeriod.toPeriod();
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(5, period.getMillis());
    }
    
    @Test
    public void testToMutablePeriod() {
        MutablePeriod mutablePeriod = testPeriod.toMutablePeriod();
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(0, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(5, mutablePeriod.getMillis());
    }
    
    @Test
    public void testEquals() {
        assertTrue(testPeriod.equals(new TestPeriod(5)));
        assertFalse(testPeriod.equals(new TestPeriod(10)));
        assertFalse(testPeriod.equals(null));
        assertFalse(testPeriod.equals(""));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(27 * (27 * 17 + 5) + testPeriod.getFieldType().hashCode(), testPeriod.hashCode());
    }
    
    @Test
    public void testCompareTo() {
        TestPeriod period1 = new TestPeriod(5);
        TestPeriod period2 = new TestPeriod(10);
        TestPeriod period3 = new TestPeriod(5);
        
        assertEquals(0, period1.compareTo(period3));
        assertEquals(-1, period1.compareTo(period2));
        assertEquals(1, period2.compareTo(period1));
    }
    
}