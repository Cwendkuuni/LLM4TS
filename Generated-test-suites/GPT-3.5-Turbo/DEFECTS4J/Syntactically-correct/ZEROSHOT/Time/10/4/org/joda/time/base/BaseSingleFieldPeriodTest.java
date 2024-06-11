import org.joda.time.*;

public class BaseSingleFieldPeriodTest {

    BaseSingleFieldPeriod period;
    
    @Before
    public void setUp() {
        period = new BaseSingleFieldPeriodImpl(1);
    }
    
    @Test
    public void testGetFieldType() {
        DurationFieldType fieldType = period.getFieldType();
        assertNotNull(fieldType);
        assertEquals("The duration field type should be equal", DurationFieldType.class, fieldType.getClass());
    }
    
    @Test
    public void testGetPeriodType() {
        PeriodType periodType = period.getPeriodType();
        assertNotNull(periodType);
        assertEquals("The period type should be equal", PeriodType.class, periodType.getClass());
    }
    
    @Test
    public void testSize() {
        int size = period.size();
        assertEquals("The size should be 1", 1, size);
    }
    
    @Test
    public void testGetFieldTypeIndex() {
        DurationFieldType fieldType = period.getFieldType(0);
        assertNotNull(fieldType);
        assertEquals("The duration field type at index 0 should be equal", DurationFieldType.class, fieldType.getClass());
    }
    
    @Test
    public void testGetValueIndex() {
        int value = period.getValue(0);
        assertEquals("The value at index 0 should be 1", 1, value);
    }
    
    @Test
    public void testGet() {
        int value = period.get(period.getFieldType());
        assertEquals("The value of the duration field type should be 1", 1, value);
    }
    
    @Test
    public void testIsSupported() {
        boolean supported = period.isSupported(period.getFieldType());
        assertTrue("The duration field type should be supported", supported);
    }
    
    @Test
    public void testToPeriod() {
        Period p = period.toPeriod();
        assertNotNull(p);
        assertEquals("The period should have 1 day", 1, p.getDays());
    }
    
    @Test
    public void testToMutablePeriod() {
        MutablePeriod mp = period.toMutablePeriod();
        assertNotNull(mp);
        assertEquals("The mutable period should have 1 day", 1, mp.getDays());
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriodImpl(1);
        boolean equals = period.equals(p);
        assertTrue("The periods should be equal", equals);
    }
    
    @Test
    public void testHashCode() {
        int hashCode = period.hashCode();
        assertEquals("The hashcodes should be equal", period.getValue(), hashCode);
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriodImpl(2);
        int compare = period.compareTo(p);
        assertEquals("The periods should be compared correctly", -1, compare);
    }
    
    private class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {
        
        public BaseSingleFieldPeriodImpl(int period) {
            super(period);
        }
        
        public DurationFieldType getFieldType() {
            return DurationFieldType.days();
        }
        
        public PeriodType getPeriodType() {
            return PeriodType.dayTime();
        }
    }
}