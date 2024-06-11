import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {
    @Test
    public void testBetween_WithValidArguments_ReturnsDifference() {
        ReadableInstant readableInstant1 = DateTimeUtils.currentTimeMillis();
        ReadableInstant readableInstant2 = DateTimeUtils.currentTimeMillis() + 1000;
        int result = BaseSingleFieldPeriod.between(readableInstant1, readableInstant2, DurationFieldType.millis());
        assertEquals(1000, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_WithNullArguments_ThrowsIllegalArgumentException() {
        ReadableInstant readableInstant1 = null;
        ReadableInstant readableInstant2 = DateTimeUtils.currentTimeMillis() + 1000;
        BaseSingleFieldPeriod.between(readableInstant1, readableInstant2, DurationFieldType.millis());
    }

    @Test
    public void testBetween_WithDifferentPartialFields_ThrowsIllegalArgumentException() {
        ReadablePartial readablePartial1 = new Partial();
        ReadablePartial readablePartial2 = new Partial().withField(DateTimeFieldType.monthOfYear());
        ReadablePeriod readablePeriod = new Period();
        try {
            BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_WithPreciseDurationFieldMissing_ThrowsIllegalArgumentException() {
        ReadablePeriod readablePeriod = new Period().withFieldAdded(DurationFieldType.halfdays(), 2);
        BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 1);
    }

    @Test
    public void testGetValue_WithInitializedPeriod_ReturnsValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int result = baseSingleFieldPeriod.getValue();
        assertEquals(10, result);
    }

    @Test
    public void testSetValue_WithNewValue_SetsValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        baseSingleFieldPeriod.setValue(20);
        int result = baseSingleFieldPeriod.getValue();
        assertEquals(20, result);
    }

    @Test
    public void testSize_Always_ReturnsOne() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int result = baseSingleFieldPeriod.size();
        assertEquals(1, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_WithIndexOutOfBounds_ThrowsIndexOutOfBoundsException() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        baseSingleFieldPeriod.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_WithIndexOutOfBounds_ThrowsIndexOutOfBoundsException() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        baseSingleFieldPeriod.getValue(1);
    }

    @Test
    public void testGet_WithMatchingFieldType_ReturnsValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int result = baseSingleFieldPeriod.get(DurationFieldType.seconds());
        assertEquals(10, result);
    }

    @Test
    public void testGet_WithDifferentFieldType_ReturnsZero() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int result = baseSingleFieldPeriod.get(DurationFieldType.minutes());
        assertEquals(0, result);
    }
    
    @Test
    public void testIsSupported_WithMatchingFieldType_ReturnsTrue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        boolean result = baseSingleFieldPeriod.isSupported(DurationFieldType.seconds());
        assertTrue(result);
    }
    
    @Test
    public void testIsSupported_WithDifferentFieldType_ReturnsFalse() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        boolean result = baseSingleFieldPeriod.isSupported(DurationFieldType.minutes());
        assertFalse(result);
    }
    
    @Test
    public void testToPeriod_ReturnsEquivalentPeriod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        Period result = baseSingleFieldPeriod.toPeriod();
        assertEquals(new Period(0, 0, 0, 0, 0, 0, 10, 0), result);
    }
    
    @Test
    public void testToMutablePeriod_ReturnsEquivalentMutablePeriod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        MutablePeriod result = baseSingleFieldPeriod.toMutablePeriod();
        assertEquals(new MutablePeriod(0, 0, 0, 0, 0, 0, 10, 0), result);
    }
    
    @Test
    public void testEquals_WithSameInstant_ReturnsTrue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        boolean result = baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2);
        assertTrue(result);
    }
    
    @Test
    public void testEquals_WithDifferentInstant_ReturnsFalse() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        boolean result = baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2);
        assertFalse(result);
    }
    
    @Test
    public void testHashCode_WithSameInstant_ReturnsSameHashCode() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int hashCode1 = baseSingleFieldPeriod1.hashCode();
        int hashCode2 = baseSingleFieldPeriod2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testHashCode_WithDifferentInstant_ReturnsDifferentHashCode() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int hashCode1 = baseSingleFieldPeriod1.hashCode();
        int hashCode2 = baseSingleFieldPeriod2.hashCode();
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testCompareTo_WithSamePeriodTypeAndLargerValue_ReturnsPositiveValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompareTo_WithSamePeriodTypeAndSmallerValue_ReturnsNegativeValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        assertTrue(result < 0);
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareTo_WithDifferentPeriodType_ThrowsClassCastException() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
    }
}