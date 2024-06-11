import org.joda.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_ReadableInstant_NullReadableInstant() {
        try {
            BaseSingleFieldPeriod.between(null, new DateTime(), DurationFieldType.days());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_ReadableInstant_NullReadableInstant2() {
        try {
            BaseSingleFieldPeriod.between(new DateTime(), null, DurationFieldType.weeks());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_ReadableInstant_DifferentChronologies() {
        try {
            BaseSingleFieldPeriod.between(new DateTime(2020, 1, 1, 0, 0, 0), new DateTime(2020, 1, 1, 0, 0, 0, 0, BuddhistChronology.getInstance()), DurationFieldType.months());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_ReadablePartial_NullReadablePartial() {
        try {
            BaseSingleFieldPeriod.between(null, new LocalDate(2020, 1, 1), Period.years());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_ReadablePartial_NullReadablePartial2() {
        try {
            BaseSingleFieldPeriod.between(new LocalDate(2020, 1, 1), null, Period.months());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_ReadablePartial_DifferentSize() {
        try {
            BaseSingleFieldPeriod.between(new LocalDate(2020, 1, 1), new LocalDate(2020, 1, 1, 0, 0, 0), Period.weeks());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_ReadablePartial_DifferentFields() {
        try {
            BaseSingleFieldPeriod.between(new LocalDate(2020, 1, 1), new LocalDate(2020, 1, 1), Period.yearMonthDayTime());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_ReadablePartial_NotContiguous() {
        try {
            BaseSingleFieldPeriod.between(new LocalDate(2020, 1, 1), new LocalDate(2020, 1, 3), Period.days());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must be contiguous", e.getMessage());
        }
    }
    
    @Test
    public void testStandardPeriodIn_NullReadablePeriod() {
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, 1000));
    }
    
    @Test
    public void testStandardPeriodIn_InvalidValue() {
        try {
            Period period = new Period(new DurationFieldType() {
                @Override
                public String getName() {
                    return "InvalidField";
                }

                @Override
                public boolean isSupported() {
                    return true;
                }
            }, 5);
            BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot convert period to duration as InvalidField is not precise in the period PT0S", e.getMessage());
        }
    }
    
    @Test
    public void testConstructor_Getter_Setter() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(10, period.getValue());
        period.setValue(5);
        assertEquals(5, period.getValue());
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(1, period.size());
    }
    
    @Test
    public void testGetFieldType_ByIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(DurationFieldType.minutes(), period.getFieldType(0));
        
        try {
            period.getFieldType(1);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }
    
    @Test
    public void testGetValue_ByIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(10, period.getValue(0));
        
        try {
            period.getValue(1);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }
    
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(10, period.get(DurationFieldType.minutes()));
        
        assertEquals(0, period.get(DurationFieldType.seconds()));
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertTrue(period.isSupported(DurationFieldType.minutes()));
        
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        Period expectedPeriod = new Period().withMinutes(10);
        
        assertEquals(expectedPeriod, period.toPeriod());
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        MutablePeriod expectedMutablePeriod = new MutablePeriod().withMinutes(10);
        
        assertEquals(expectedMutablePeriod, period.toMutablePeriod());
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        assertEquals(period1, period2);
        assertNotEquals(period1, period3);
        assertNotEquals(period1, period4);
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        assertEquals(period1.hashCode(), period2.hashCode());
        assertNotEquals(period1.hashCode(), period3.hashCode());
        assertNotEquals(period1.hashCode(), period4.hashCode());
    }
    
    @Test
    public void testCompareTo_SameClass() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(15) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        assertEquals(-1, period1.compareTo(period2));
        assertEquals(1, period1.compareTo(period3));
        assertEquals(0, period1.compareTo(period1));
    }
    
    @Test
    public void testCompareTo_DifferentClass() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        BaseSingleFieldPeriod differentPeriod = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.minutes();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        
        try {
            period.compareTo(differentPeriod);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            assertEquals("class BaseSingleFieldPeriod cannot be compared to class BaseSingleFieldPeriod", e.getMessage());
        }
    }
}