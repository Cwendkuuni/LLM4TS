package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    @Test
    public void testBetween() {
        // Test null inputs
        try {
            BaseSingleFieldPeriod.between(null, DateTime.now(), DurationFieldType.days());
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
        try {
            BaseSingleFieldPeriod.between(DateTime.now(), null, DurationFieldType.days());
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
        
        // Test different set of fields
        try {
            BaseSingleFieldPeriod.between(new LocalDateTime(), new LocalDate(), DurationFieldType.days());
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
        }
        
        // Test non-contiguous partials
        try {
            BaseSingleFieldPeriod.between(new LocalTime(), new LocalTime().withHourOfDay(1), DurationFieldType.hours());
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must be contiguous", e.getMessage());
        }
        
        // Test valid inputs
        // Add more test cases here...
        
    }
    
    @Test
    public void testStandardPeriodIn() {
        // Test null input
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, 1000));
        
        // Test non-precise field
        Period period = new Period(1, 0, 0, 0, 0, 0, 0, 0);
        try {
            BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot convert period to duration as years is not precise in the period " + period, e.getMessage());
        }
        
        // Test valid input
        // Add more test cases here...
        
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
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertEquals(DurationFieldType.days(), period.getFieldType());
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertEquals(PeriodType.days(), period.getPeriodType());
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(1, period.size());
    }
    
    @Test
    public void testGetFieldTypeByIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertEquals(DurationFieldType.days(), period.getFieldType(0));
        try {
            period.getFieldType(1);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }
    
    @Test
    public void testGetValueByIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertEquals(10, period.getValue(0));
        try {
            period.getValue(1);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }
    
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertEquals(10, period.get(DurationFieldType.days()));
        assertEquals(0, period.get(DurationFieldType.weeks()));
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertTrue(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.weeks()));
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        Period result = period.toPeriod();
        assertEquals(period.getValue(), result.get(DurationFieldType.days()));
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(period.getValue(), result.get(DurationFieldType.days()));
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertEquals(27 * (27 * 17 + 10) + DurationFieldType.days().hashCode(), period.hashCode());
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertEquals(-1, period1.compareTo(period2));
        assertEquals(0, period1.compareTo(period1));
        assertEquals(1, period2.compareTo(period1));
        try {
            period1.compareTo(new BaseSingleFieldPeriod(10) {
                @Override
                public DurationFieldType getFieldType() {
                    return DurationFieldType.hours();
                }
                
                @Override
                public PeriodType getPeriodType() {
                    return PeriodType.hours();
                }
            });
        } catch (ClassCastException e) {
            assertEquals("class org.joda.time.base.BaseSingleFieldPeriodTest$1 cannot be compared to class org.joda.time.base.BaseSingleFieldPeriodTest$2", e.getMessage());
        }
    }
}