import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_instant() {
        ReadableInstant instant1 = new DateTime(2022, 1, 1, 0, 0, 0, 0);
        ReadableInstant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0);
        DurationFieldType fieldType = DurationFieldType.days();
        int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_instant_withNullInstant() {
        ReadableInstant instant1 = null;
        ReadableInstant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0);
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_instant_withNullFieldType() {
        ReadableInstant instant1 = new DateTime(2022, 1, 1, 0, 0, 0, 0);
        ReadableInstant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0);
        DurationFieldType fieldType = null;
        BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
    }

    @Test
    public void testBetween_partial() {
        ReadablePartial partial1 = new LocalDate(2022, 1, 1);
        ReadablePartial partial2 = new LocalDate(2022, 1, 2);
        ReadablePeriod period = Period.days(1);
        int result = BaseSingleFieldPeriod.between(partial1, partial2, period);
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_partial_withNullPartial() {
        ReadablePartial partial1 = null;
        ReadablePartial partial2 = new LocalDate(2022, 1, 2);
        ReadablePeriod period = Period.days(1);
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_partial_withDifferentSize() {
        ReadablePartial partial1 = new LocalDate(2022, 1, 1);
        ReadablePartial partial2 = new LocalDate(2022, 1, 2, 1);
        ReadablePeriod period = Period.days(1);
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_partial_withDifferentFields() {
        ReadablePartial partial1 = new LocalDate(2022, 1, 1);
        ReadablePartial partial2 = new LocalDate(2022, 1, 2);
        ReadablePeriod period = Period.hours(1);
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_partial_withNonContiguousPartial() {
        ReadablePartial partial1 = new LocalDate(2022, 1, 1);
        ReadablePartial partial2 = new LocalDate(2022, 1, 3);
        ReadablePeriod period = Period.days(1);
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.hours(24);
        long n = 1000;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        assertEquals(86400000, result);
    }

    @Test
    public void testStandardPeriodIn_withNullPeriod() {
        ReadablePeriod period = null;
        long n = 1000;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_withNonPreciseDurationField() {
        ReadablePeriod period = Period.minutes(30);
        long n = 1000;
        BaseSingleFieldPeriod.standardPeriodIn(period, n);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        int result = basePeriod.getValue();
        assertEquals(4, result);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        basePeriod.setValue(5);
        int result = basePeriod.getValue();
        assertEquals(5, result);
    }

    @Test
    public void testGetFieldType() {
        // Subclasses should provide implementation of this method
        // Test case for BaseSingleFieldPeriod itself would not make sense
        // Implementation-specific tests should be added in subclass tests
    }

    @Test
    public void testGetPeriodType() {
        // Subclasses should provide implementation of this method
        // Test case for BaseSingleFieldPeriod itself would not make sense
        // Implementation-specific tests should be added in subclass tests
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        int result = basePeriod.size();
        assertEquals(1, result);
    }

    @Test
    public void testGetFieldType_withValidIndex() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        DurationFieldType result = basePeriod.getFieldType(0);
        assertEquals(basePeriod.getFieldType(), result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_withInvalidIndex() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        basePeriod.getFieldType(1);
    }

    @Test
    public void testGetValue_withValidIndex() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        int result = basePeriod.getValue(0);
        assertEquals(basePeriod.getValue(), result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_withInvalidIndex() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        basePeriod.getValue(1);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        int result = basePeriod.get(basePeriod.getFieldType());
        assertEquals(basePeriod.getValue(), result);
    }

    @Test
    public void testGet_withDifferentFieldType() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        int result = basePeriod.get(DurationFieldType.days());
        assertEquals(0, result);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        boolean result = basePeriod.isSupported(basePeriod.getFieldType());
        assertTrue(result);
    }

    @Test
    public void testIsSupported_withDifferentFieldType() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        boolean result = basePeriod.isSupported(DurationFieldType.days());
        assertFalse(result);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        Period result = basePeriod.toPeriod();
        assertEquals(Period.ZERO.withFields(basePeriod), result);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        MutablePeriod result = basePeriod.toMutablePeriod();
        MutablePeriod expected = new MutablePeriod();
        expected.add(basePeriod);
        assertEquals(expected, result);
    }

    @Test
    public void testEquals_withSameObject() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        boolean result = basePeriod.equals(basePeriod);
        assertTrue(result);
    }

    @Test
    public void testEquals_withDifferentClass() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        boolean result = basePeriod.equals(new Object());
        assertFalse(result);
    }

    @Test
    public void testEquals_withDifferentPeriodType() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        boolean result = basePeriod.equals(new BaseSingleFieldPeriod(4) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.years();
            }
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.years();
            }
        });
        assertFalse(result);
    }

    @Test
    public void testEquals_withDifferentValue() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        boolean result = basePeriod.equals(new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        });
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        int result = basePeriod.hashCode();
        int expected = 27 * (27 * 17 + 4) + basePeriod.getFieldType().hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void testCompareTo_withSamePeriodTypeAndGreaterValue() {
        BaseSingleFieldPeriod basePeriod1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        BaseSingleFieldPeriod basePeriod2 = new BaseSingleFieldPeriod(4) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        int result = basePeriod1.compareTo(basePeriod2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareTo_withSamePeriodTypeAndSmallerValue() {
        BaseSingleFieldPeriod basePeriod1 = new BaseSingleFieldPeriod(4) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        BaseSingleFieldPeriod basePeriod2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        int result = basePeriod1.compareTo(basePeriod2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareTo_withSamePeriodTypeAndSameValue() {
        BaseSingleFieldPeriod basePeriod1 = new BaseSingleFieldPeriod(4) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        BaseSingleFieldPeriod basePeriod2 = new BaseSingleFieldPeriod(4) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        int result = basePeriod1.compareTo(basePeriod2);
        assertEquals(0, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareTo_withDifferentClass() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4);
        basePeriod.compareTo(new Object());
    }

    @Test
    public void testToString() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriod(4) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        String result = basePeriod.toString();
        String expected = "4 days";
        assertEquals(expected, result);
    }
}