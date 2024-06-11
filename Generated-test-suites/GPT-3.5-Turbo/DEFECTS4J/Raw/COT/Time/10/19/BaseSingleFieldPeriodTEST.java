
import org.joda.time.*;
import org.joda.time.base.BaseSingleFieldPeriod;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new DummySingleFieldPeriod(10);
        assertEquals(10, period.getValue());
    }
    
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new DummySingleFieldPeriod();
        period.setValue(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new DummySingleFieldPeriod();
        DurationFieldType fieldType = period.getFieldType();
        assertNotNull(fieldType);
        assertEquals(DurationFieldType.class, fieldType.getClass());
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new DummySingleFieldPeriod();
        PeriodType periodType = period.getPeriodType();
        assertNotNull(periodType);
        assertEquals(PeriodType.class, periodType.getClass());
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new DummySingleFieldPeriod();
        assertEquals(1, period.size());
    }
    
    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = new DummySingleFieldPeriod();
        DurationFieldType fieldType = period.getFieldType(0);
        assertNotNull(fieldType);
        assertEquals(DurationFieldType.class, fieldType.getClass());
    }
    
    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod period = new DummySingleFieldPeriod(5);
        int value = period.getValue(0);
        assertEquals(5, value);
    }
    
    @Test
    public void testGetDurationField() {
        BaseSingleFieldPeriod period = new DummySingleFieldPeriod();
        DurationFieldType fieldType = period.getFieldType();
        DurationField field = fieldType.getField(ISOChronology.getInstanceUTC());
        assertNotNull(field);
        assertEquals(DurationField.class, field.getClass());
    }
    
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new DummySingleFieldPeriod(5);
        int value = period.get(period.getFieldType());
        assertEquals(5, value);
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new DummySingleFieldPeriod();
        assertTrue(period.isSupported(period.getFieldType()));
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new DummySingleFieldPeriod(5);
        Period p = period.toPeriod();
        assertNotNull(p);
        assertEquals(Period.class, p.getClass());
        assertEquals(5, p.get(period.getFieldType()));
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new DummySingleFieldPeriod(5);
        MutablePeriod p = period.toMutablePeriod();
        assertNotNull(p);
        assertEquals(MutablePeriod.class, p.getClass());
        assertEquals(5, p.get(period.getFieldType()));
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new DummySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new DummySingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
    }
    
    @Test
    public void testNotEquals() {
        BaseSingleFieldPeriod period1 = new DummySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new DummySingleFieldPeriod(10);
        assertFalse(period1.equals(period2));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new DummySingleFieldPeriod(5);
        int hashCode = period.getFieldType().hashCode() + 27 * 5;
        assertEquals(hashCode, period.hashCode());
    }
    
    @Test
    public void testCompareToEquals() {
        BaseSingleFieldPeriod period1 = new DummySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new DummySingleFieldPeriod(5);
        assertEquals(0, period1.compareTo(period2));
    }
    
    @Test
    public void testCompareToGreaterThan() {
        BaseSingleFieldPeriod period1 = new DummySingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new DummySingleFieldPeriod(5);
        assertEquals(1, period1.compareTo(period2));
    }
    
    @Test
    public void testCompareToLessThan() {
        BaseSingleFieldPeriod period1 = new DummySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new DummySingleFieldPeriod(10);
        assertEquals(-1, period1.compareTo(period2));
    }
    
    private static class DummySingleFieldPeriod extends BaseSingleFieldPeriod {
        public DummySingleFieldPeriod() {
            super(0);
        }
        
        public DummySingleFieldPeriod(int period) {
            super(period);
        }
        
        public DurationFieldType getFieldType() {
            return DurationFieldType.days();
        }

        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }

}
