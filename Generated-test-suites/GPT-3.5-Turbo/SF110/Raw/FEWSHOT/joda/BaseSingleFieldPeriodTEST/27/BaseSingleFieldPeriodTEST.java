
package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.ReadOnlyPeriod;
import org.joda.time.field.FieldUtils;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    
    @Test
    public void testBetweenWithReadableInstant() {
        // Test null parameters
        try {
            BaseSingleFieldPeriod.between(null, null, DurationFieldType.seconds());
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        
        // Test normal case
        DateTime dt1 = new DateTime();
        DateTime dt2 = dt1.plusSeconds(10);
        assertEquals(10, BaseSingleFieldPeriod.between(dt1, dt2, DurationFieldType.seconds()));
    }
    
    @Test
    public void testBetweenWithReadablePartial() {
        // Test null parameters
        try {
            BaseSingleFieldPeriod.between(null, null, new Period());
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        
        // Test different sized partials
        try {
            LocalDateTime end = new LocalDateTime();
            LocalDateTime start = end.minusYears(1);
            
            BaseSingleFieldPeriod.between(start, end, new Period());
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        
        // Test non-contiguous partial
        try {
            LocalDateTime start = new LocalDateTime();
            LocalDateTime middle = start.plusMonths(1);
            LocalDateTime end = middle.plusMonths(1);
            
            BaseSingleFieldPeriod.between(start, end, new Period());
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        
        // Test normal case
        LocalDateTime start = new LocalDateTime();
        LocalDateTime end = start.plusYears(1);
        DurationFieldType fieldType = DurationFieldType.years();
        int[] values = new int[]{baseSingleFieldPeriod.get(end), baseSingleFieldPeriod.get(start)};
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, new Period()));
    }
    
    @Test
    public void testStandardPeriodIn() {
        // Test null parameter
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, 1));
        
        // Test normal case
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(1, BaseSingleFieldPeriod.standardPeriodIn(period, 1));
    }
    
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
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        assertEquals(DurationFieldType.seconds(), period.getFieldType());
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return null;
            }
            
            public PeriodType getPeriodType() {
                return PeriodType.time();
            }
        };
        
        assertEquals(PeriodType.time(), period.getPeriodType());
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(1, period.size());
    }
    
    @Test
    public void testGetFieldTypeByIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        assertEquals(DurationFieldType.seconds(), period.getFieldType(0));
    }
    
    @Test
    public void testGetFieldTypeByInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        try {
            period.getFieldType(1);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testGetValueByIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        assertEquals(10, period.getValue(0));
    }
    
    @Test
    public void testGetValueByInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        try {
            period.getValue(1);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        assertEquals(10, period.get(DurationFieldType.seconds()));
        assertEquals(0, period.get(DurationFieldType.minutes()));
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        assertTrue(period.isSupported(DurationFieldType.seconds()));
        assertFalse(period.isSupported(DurationFieldType.minutes()));
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        Period result = period.toPeriod();
        assertEquals(10, result.getSeconds());
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(10, result.getSeconds());
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        assertTrue(period1.equals(period1));
        assertFalse(period1.equals(null));
        assertFalse(period1.equals(period2));
        assertFalse(period1.equals(period3));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        assertEquals(2709, period.hashCode());
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(10) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }
            
            public PeriodType getPeriodType() {
                return null;
            }
        };
        
        assertTrue(period1.compareTo(period2) < 0);
        assertTrue(period1.compareTo(period3) > 0);
        assertEquals(0, period1.compareTo(period1));
        try {
            period1.compareTo(new String());
        } catch (ClassCastException e) {
            assertTrue(true);
        }
    }
}

