import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    
    private BaseSingleFieldPeriod period;
    
    @Before
    public void setup() {
        period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
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
    public void testSize() {
        assertEquals(1, period.size());
    }
    
    @Test
    public void testGetFieldType() {
        assertNull(period.getFieldType());
    }
    
    @Test
    public void testGetPeriodType() {
        assertNull(period.getPeriodType());
    }
    
    @Test
    public void testGetFieldTypeWithIndex() {
        try {
            period.getFieldType(1);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }
    
    @Test
    public void testGetValueWithIndex() {
        try {
            period.getValue(1);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }
    
    @Test
    public void testGet() {
        assertEquals(0, period.get(null));
    }
    
    @Test
    public void testIsSupported() {
        assertFalse(period.isSupported(null));
    }
    
    @Test
    public void testToPeriod() {
        Period result = period.toPeriod();
        assertNotNull(result);
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getWeeks());
        assertEquals(0, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(10, result.getMillis());
    }
    
    @Test
    public void testToMutablePeriod() {
        MutablePeriod result = period.toMutablePeriod();
        assertNotNull(result);
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getWeeks());
        assertEquals(0, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(10, result.getMillis());
    }
    
    @Test
    public void testEquals() {
        assertTrue(period.equals(period));
        assertFalse(period.equals(null));
        assertFalse(period.equals(new Object()));
        assertFalse(period.equals(new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        }));
        assertFalse(period.equals(new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.SECONDS;
            }
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        }));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(270, period.hashCode());
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareToDifferentType() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        period.compareTo(other);
    }
    
    @Test
    public void testCompareToGreater() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(1, period.compareTo(other));
    }
    
    @Test
    public void testCompareToLess() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(15) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(-1, period.compareTo(other));
    }
    
    @Test
    public void testCompareToEqual() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(0, period.compareTo(other));
    }
}