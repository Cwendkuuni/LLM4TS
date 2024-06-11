
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    // Test between method with null ReadableInstant arguments
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadableInstant() {
        ReadableInstant instant = null;
        ReadableInstant instant2 = null;
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod.between(instant, instant2, fieldType);
    }

    // Test between method with non-null ReadableInstant arguments
    @Test
    public void testBetweenWithReadableInstant() {
        ReadableInstant instant = new DateTime(2020, 1, 1, 0, 0);
        ReadableInstant instant2 = new DateTime(2020, 1, 2, 0, 0);
        DurationFieldType fieldType = DurationFieldType.days();
        int result = BaseSingleFieldPeriod.between(instant, instant2, fieldType);
        assertEquals(1, result);
    }

    // Test between method with null ReadablePartial arguments
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadablePartial() {
        ReadablePartial partial = null;
        ReadablePartial partial2 = null;
        ReadablePeriod period = new Period(1, 2, 3, 4);
        BaseSingleFieldPeriod.between(partial, partial2, period);
    }

    // Test between method with non-null ReadablePartial arguments
    @Test
    public void testBetweenWithReadablePartial() {
        ReadablePartial partial = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDate(2020, 1, 2);
        ReadablePeriod period = new Period(1, 2, 3, 4);
        int result = BaseSingleFieldPeriod.between(partial, partial2, period);
        assertEquals(1, result);
    }

    // Test standardPeriodIn method with null ReadablePeriod argument
    @Test
    public void testStandardPeriodInWithNullReadablePeriod() {
        ReadablePeriod period = null;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1);
        assertEquals(0, result);
    }

    // Test standardPeriodIn method with non-null ReadablePeriod argument
    @Test
    public void testStandardPeriodInWithReadablePeriod() {
        ReadablePeriod period = new Period(1, 2, 3, 4);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1);
        assertEquals(60, result);
    }

    // Test getValue method
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        int result = period.getValue();
        assertEquals(1, result);
    }

    // Test setValue method
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        period.setValue(2);

        int result = period.getValue();
        assertEquals(2, result);
    }

    // Test size method
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        int result = period.size();
        assertEquals(1, result);
    }

    // Test getFieldType(int) method with valid index
    @Test
    public void testGetFieldTypeWithValidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        DurationFieldType result = period.getFieldType(0);
        assertEquals(DurationFieldType.days(), result);
    }

    // Test getFieldType(int) method with invalid index
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        period.getFieldType(1);
    }

    // Test getValue(int) method with valid index
    @Test
    public void testGetValueWithValidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        int result = period.getValue(0);
        assertEquals(1, result);
    }

    // Test getValue(int) method with invalid index
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        period.getValue(1);
    }

    // Test get method with matching DurationFieldType
    @Test
    public void testGetWithMatchingFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        int result = period.get(DurationFieldType.days());
        assertEquals(1, result);
    }

    // Test get method with non-matching DurationFieldType
    @Test
    public void testGetWithNonMatchingFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        int result = period.get(DurationFieldType.hours());
        assertEquals(0, result);
    }

    // Test isSupported method with matching DurationFieldType
    @Test
    public void testIsSupportedWithMatchingFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        boolean result = period.isSupported(DurationFieldType.days());
        assertTrue(result);
    }

    // Test isSupported method with non-matching DurationFieldType
    @Test
    public void testIsSupportedWithNonMatchingFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        boolean result = period.isSupported(DurationFieldType.hours());
        assertFalse(result);
    }

    // Test toPeriod method
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        Period result = period.toPeriod();
        assertEquals(1, result.get(DurationFieldType.days()));
    }

    // Test toMutablePeriod method
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        MutablePeriod result = period.toMutablePeriod();
        assertEquals(1, result.get(DurationFieldType.days()));
    }

    // Test equals method with equal objects
    @Test
    public void testEqualsWithEqualObjects() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.hours();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        boolean result = period1.equals(period2);
        assertTrue(result);
    }

    // Test equals method with non-equal objects
    @Test
    public void testEqualsWithNonEqualObjects() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        boolean result = period1.equals(period2);
        assertFalse(result);
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        int result = period.hashCode();
        assertEquals(27 * (27 * 17 + 1) + DurationFieldType.days().hashCode(), result);
    }

    // Test compareTo method with equal objects
    @Test
    public void testCompareToWithEqualObjects() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        int result = period1.compareTo(period2);
        assertEquals(0, result);
    }

    // Test compareTo method with non-equal objects
    @Test(expected = ClassCastException.class)
    public void testCompareToWithNonEqualObjects() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        period1.compareTo(period2);
    }
}
