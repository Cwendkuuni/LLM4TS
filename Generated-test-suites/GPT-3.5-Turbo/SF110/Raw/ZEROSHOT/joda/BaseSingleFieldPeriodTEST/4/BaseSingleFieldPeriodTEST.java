import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_ReadableInstant() {
        ReadableInstant instant1 = new Instant(0);
        ReadableInstant instant2 = new Instant(1000);
        int result = BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.seconds());
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ReadableInstant_NullInput() {
        BaseSingleFieldPeriod.between(null, new Instant(1000), DurationFieldType.seconds());
    }

    @Test
    public void testBetween_ReadablePartial_ReadablePeriod() {
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDate(2020, 2, 1);
        ReadablePeriod period = new Period(1, 0, 0, 0);
        int result = BaseSingleFieldPeriod.between(partial1, partial2, period);
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ReadablePartial_NullInput() {
        ReadablePartial partial = new LocalDate(2020, 1, 1);
        BaseSingleFieldPeriod.between(partial, null, new Period(1, 0, 0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ReadablePartial_DifferentFields() {
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDate(2020, 2, 1);
        ReadablePeriod period = new Period(0, 1, 0, 0);
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_ReadablePartial_NotContiguous() {
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDate(2020, 2, 1);
        partial1 = partial1.with(DateTimeFieldType.dayOfWeek(), 1);
        partial2 = partial2.with(DateTimeFieldType.dayOfWeek(), 7);
        ReadablePeriod period = new Period(1, 0, 0, 0);
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(1, 0, 0, 0);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
        assertEquals(1000, result);
    }

    @Test
    public void testStandardPeriodIn_NullPeriod() {
        int result = BaseSingleFieldPeriod.standardPeriodIn(null, 1000);
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_UnsupportedFieldType() {
        ReadablePeriod period = new Period(1, 0, 0, 0);
        BaseSingleFieldPeriod.standardPeriodIn(period, 0);
    }

    @Test
    public void testBaseSingleFieldPeriod_GetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int result = period.getValue();
        assertEquals(10, result);
    }

    @Test
    public void testBaseSingleFieldPeriod_SetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        period.setValue(10);
        int result = period.getValue();
        assertEquals(10, result);
    }

    @Test
    public void testBaseSingleFieldPeriod_Size() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int result = period.size();
        assertEquals(1, result);
    }

    @Test
    public void testBaseSingleFieldPeriod_GetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        DurationFieldType result = period.getFieldType();
        assertEquals(DurationFieldType.seconds(), result);
    }

    @Test
    public void testBaseSingleFieldPeriod_GetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.yearMonthDay();
            }
        };
        PeriodType result = period.getPeriodType();
        assertEquals(PeriodType.yearMonthDay(), result);
    }

    @Test
    public void testBaseSingleFieldPeriod_GetFieldType_Int() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        DurationFieldType result = period.getFieldType(0);
        assertEquals(DurationFieldType.seconds(), result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBaseSingleFieldPeriod_GetFieldType_Int_InvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        period.getFieldType(1);
    }

    @Test
    public void testBaseSingleFieldPeriod_GetValue_Int() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int result = period.getValue(0);
        assertEquals(10, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBaseSingleFieldPeriod_GetValue_Int_InvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        period.getValue(1);
    }

    @Test
    public void testBaseSingleFieldPeriod_Get() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int result = period.get(DurationFieldType.seconds());
        assertEquals(10, result);
    }

    @Test
    public void testBaseSingleFieldPeriod_Get_UnsupportedFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int result = period.get(DurationFieldType.seconds());
        assertEquals(0, result);
    }

    @Test
    public void testBaseSingleFieldPeriod_IsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        boolean result = period.isSupported(DurationFieldType.seconds());
        assertTrue(result);
    }

    @Test
    public void testBaseSingleFieldPeriod_IsSupported_UnsupportedFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        boolean result = period.isSupported(DurationFieldType.seconds());
        assertFalse(result);
    }

    @Test
    public void testBaseSingleFieldPeriod_ToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        Period result = period.toPeriod();
        assertEquals(new Period(10, 0, 0, 0), result);
    }

    @Test
    public void testBaseSingleFieldPeriod_ToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(new MutablePeriod().withSeconds(10), result);
    }

    @Test
    public void testBaseSingleFieldPeriod_Equals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        boolean result = period1.equals(period2);
        assertTrue(result);
    }

    @Test
    public void testBaseSingleFieldPeriod_Equals_NotEqual() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        boolean result = period1.equals(period2);
        assertFalse(result);
    }

    @Test
    public void testBaseSingleFieldPeriod_HashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int result = period.hashCode();
        assertEquals(27 * (27 * 17 + 10) + DurationFieldType.seconds().hashCode(), result);
    }

    @Test
    public void testBaseSingleFieldPeriod_CompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.seconds();
            }
            
            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        int result = period1.compareTo(period2);
        assertEquals(-1, result);
    }
}