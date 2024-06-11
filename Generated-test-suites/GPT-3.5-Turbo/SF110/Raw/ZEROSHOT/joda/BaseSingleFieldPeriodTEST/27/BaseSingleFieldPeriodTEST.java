import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    
    private BaseSingleFieldPeriod period;
    
    @Before
    public void setUp() {
        period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
    }
    
    @After
    public void tearDown() {
        period = null;
    }
    
    @Test
    public void testConstructor() {
        assertEquals(10, period.getValue());
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
        assertEquals(DurationFieldType.seconds(), period.getFieldType());
    }
    
    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }
    
    @Test
    public void testSize() {
        assertEquals(1, period.size());
    }
    
    @Test
    public void testGetFieldTypeAtIndex() {
        assertEquals(DurationFieldType.seconds(), period.getFieldType(0));
    }
    
    @Test
    public void testGetValueAtIndex() {
        assertEquals(10, period.getValue(0));
    }
    
    @Test
    public void testGet() {
        assertEquals(10, period.get(DurationFieldType.seconds()));
        assertEquals(0, period.get(DurationFieldType.minutes()));
    }
    
    @Test
    public void testIsSupported() {
        assertTrue(period.isSupported(DurationFieldType.seconds()));
        assertFalse(period.isSupported(DurationFieldType.minutes()));
    }
    
    @Test
    public void testToPeriod() {
        Period result = period.toPeriod();
        assertNotNull(result);
        assertEquals(10, result.get(DurationFieldType.seconds()));
    }
    
    @Test
    public void testToMutablePeriod() {
        MutablePeriod result = period.toMutablePeriod();
        assertNotNull(result);
        assertEquals(10, result.get(DurationFieldType.seconds()));
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertTrue(period.equals(period2));
        
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertFalse(period.equals(period3));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(period.hashCode(), period2.hashCode());
        
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertNotEquals(period.hashCode(), period3.hashCode());
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(0, period.compareTo(period2));
        
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertTrue(period.compareTo(period3) > 0);
        
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(15) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertTrue(period.compareTo(period4) < 0);
    }
    
}