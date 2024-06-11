import static org.junit.Assert.*;
import org.junit.*;

public class BaseSingleFieldPeriodTest {
    
    // Test getValue method
    
    @Test
    public void testGetValue_WithPositiveValue_ReturnsCorrectValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }
    
    // Test setValue method
    
    @Test
    public void testSetValue_WithPositiveValue_SetsCorrectValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }
    
    // Test getFieldType method
    
    @Test
    public void testGetFieldType_ReturnsCorrectFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        assertEquals(DurationFieldType.days(), period.getFieldType());
    }
    
    // Test getPeriodType method
    
    @Test
    public void testGetPeriodType_ReturnsCorrectPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }
    
    // Test size method
    
    @Test
    public void testSize_ReturnsOne() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0);
        assertEquals(1, period.size());
    }
    
    // Test getFieldType(int) method
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_WithInvalidIndex_ThrowsIndexOutOfBoundsException() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0);
        period.getFieldType(1);
    }
    
    @Test
    public void testGetFieldType_WithValidIndex_ReturnsCorrectFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        assertEquals(DurationFieldType.days(), period.getFieldType(0));
    }
    
    // Test getValue(int) method
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_WithInvalidIndex_ThrowsIndexOutOfBoundsException() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0);
        period.getValue(1);
    }
    
    @Test
    public void testGetValue_WithValidIndex_ReturnsCorrectValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.getValue(0));
    }
    
    // Test get method
    
    @Test
    public void testGet_WithMatchingDurationFieldType_ReturnsCorrectValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        assertEquals(10, period.get(DurationFieldType.days()));
    }
    
    @Test
    public void testGet_WithNonMatchingDurationFieldType_ReturnsZero() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        assertEquals(0, period.get(DurationFieldType.weeks()));
    }
    
    // Test isSupported method
    
    @Test
    public void testIsSupported_WithMatchingDurationFieldType_ReturnsTrue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        assertTrue(period.isSupported(DurationFieldType.days()));
    }
    
    @Test
    public void testIsSupported_WithNonMatchingDurationFieldType_ReturnsFalse() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        assertFalse(period.isSupported(DurationFieldType.weeks()));
    }
    
    // Test toPeriod method
    
    @Test
    public void testToPeriod_ReturnsCorrectPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        Period expectedPeriod = new Period(10, PeriodType.standard());
        
        assertEquals(expectedPeriod, period.toPeriod());
    }
    
    // Test toMutablePeriod method
    
    @Test
    public void testToMutablePeriod_ReturnsCorrectMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        MutablePeriod expectedMutablePeriod = new MutablePeriod();
        expectedMutablePeriod.addDays(10);
        
        assertEquals(expectedMutablePeriod, period.toMutablePeriod());
    }
    
    // Test equals method
    
    @Test
    public void testEquals_WithEqualObjects_ReturnsTrue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        assertTrue(period1.equals(period2));
    }
    
    @Test
    public void testEquals_WithDifferentType_ReturnsFalse() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        assertFalse(period.equals("5"));
    }
    
    @Test
    public void testEquals_WithDifferentPeriodType_ReturnsFalse() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        
        assertFalse(period1.equals(period2));
    }
    
    @Test
    public void testEquals_WithDifferentValue_ReturnsFalse() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        assertFalse(period1.equals(period2));
    }
    
    // Test hashCode method
    
    @Test
    public void testHashCode_ReturnsCorrectHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        int expectedHashCode = 27 * (27 * 17 + 5) + DurationFieldType.days().hashCode();
        
        assertEquals(expectedHashCode, period.hashCode());
    }
    
    // Test compareTo method
    
    @Test
    public void testCompareTo_WithEqualValue_ReturnsZero() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        assertEquals(0, period1.compareTo(period2));
    }
    
    @Test
    public void testCompareTo_WithGreaterValue_ReturnsPositive() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        assertTrue(period1.compareTo(period2) > 0);
    }
    
    @Test
    public void testCompareTo_WithSmallerValue_ReturnsNegative() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        assertTrue(period1.compareTo(period2) < 0);
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareTo_WithDifferentType_ThrowsClassCastException() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.hours();
            }
        };
        
        period1.compareTo(period2);
    }
}