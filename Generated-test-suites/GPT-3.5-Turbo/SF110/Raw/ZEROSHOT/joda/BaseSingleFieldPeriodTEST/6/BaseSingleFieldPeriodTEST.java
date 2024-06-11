import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import java.io.*;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_ReadableInstant_ReadableInstant_DurationFieldType() {
        ReadableInstant inst1 = new DateTime(2020, 1, 1, 0, 0);
        ReadableInstant inst2 = new DateTime(2021, 1, 1, 0, 0);
        int result = BaseSingleFieldPeriod.between(inst1, inst2, DurationFieldType.years());
        assertEquals(1, result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBetween_NullReadableInstant() {
        ReadableInstant inst1 = null;
        ReadableInstant inst2 = new DateTime(2021, 1, 1, 0, 0);
        BaseSingleFieldPeriod.between(inst1, inst2, DurationFieldType.years());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBetween_NullDurationFieldType() {
        ReadableInstant inst1 = new DateTime(2020, 1, 1, 0, 0);
        ReadableInstant inst2 = new DateTime(2021, 1, 1, 0, 0);
        BaseSingleFieldPeriod.between(inst1, inst2, null);
    }
    
    @Test
    public void testBetween_ReadablePartial_ReadablePartial_ReadablePeriod() {
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDate(2021, 1, 1);
        ReadablePeriod period = new Period(1, 0, 0, 0);
        int result = BaseSingleFieldPeriod.between(partial1, partial2, period);
        assertEquals(1, result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBetween_NullReadablePartial() {
        ReadablePartial partial1 = null;
        ReadablePartial partial2 = new LocalDate(2021, 1, 1);
        ReadablePeriod period = new Period(1, 0, 0, 0);
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBetween_DifferentSizes() {
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDate(2021, 1, 1, 0, 0);
        ReadablePeriod period = new Period(1, 0, 0, 0);
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBetween_DifferentFields() {
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDateTime(2021, 1, 1, 0, 0);
        ReadablePeriod period = new Period(1, 0, 0, 0);
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBetween_NonContiguousPartial() {
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDate(2021, 1, 1);
        ReadablePeriod period = new Period(1, 0, 1, 0);
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test
    public void testStandardPeriodIn_ReadablePeriod_long() {
        ReadablePeriod period = new Period(1, 0, 0, 0);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000L);
        assertEquals(1, result);
    }
    
    @Test
    public void testStandardPeriodIn_NullReadablePeriod() {
        ReadablePeriod period = null;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000L);
        assertEquals(0, result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testStandardPeriodIn_UnprecisePeriod() {
        ReadablePeriod period = new Period(1, 0, 1, 0);
        BaseSingleFieldPeriod.standardPeriodIn(period, 1000L);
    }
    
    @Test
    public void testBaseSingleFieldPeriod_Int() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        assertEquals(1, period.getValue());
    }
    
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        int result = period.getValue();
        assertEquals(1, result);
    }
    
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        period.setValue(2);
        int result = period.getValue();
        assertEquals(2, result);
    }
    
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        DurationFieldType result = period.getFieldType();
        assertNotNull(result);
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        PeriodType result = period.getPeriodType();
        assertNotNull(result);
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        int result = period.size();
        assertEquals(1, result);
    }
    
    @Test
    public void testGetFieldType_int() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        DurationFieldType result = period.getFieldType(0);
        assertNotNull(result);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testGetFieldType_InvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        period.getFieldType(1);
    }
    
    @Test
    public void testGetValue_int() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        int result = period.getValue(0);
        assertEquals(1, result);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testGetValue_InvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        period.getValue(1);
    }
    
    @Test
    public void testGet_DurationFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        int result = period.get(DurationFieldType.years());
        assertEquals(1, result);
    }
    
    @Test
    public void testGet_UnsupportedFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        int result = period.get(DurationFieldType.months());
        assertEquals(0, result);
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        boolean result = period.isSupported(DurationFieldType.years());
        assertTrue(result);
    }
    
    @Test
    public void testIsSupported_UnsupportedFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        boolean result = period.isSupported(DurationFieldType.months());
        assertFalse(result);
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        Period result = period.toPeriod();
        assertEquals(1, result.getYears());
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(1, result.getYears());
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodImpl(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodImpl(1);
        boolean result = period1.equals(period2);
        assertTrue(result);
    }
    
    @Test
    public void testEquals_DifferentType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodImpl(1);
        Object period2 = new Object();
        boolean result = period1.equals(period2);
        assertFalse(result);
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriodImpl(1);
        int result = period.hashCode();
        assertEquals(28, result);
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodImpl(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodImpl(2);
        int result = period1.compareTo(period2);
        assertEquals(-1, result);
    }
    
    @Test(expected=ClassCastException.class)
    public void testCompareTo_DifferentType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodImpl(1);
        Object period2 = new Object();
        period1.compareTo(period2);
    }
    
    private class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {
        protected BaseSingleFieldPeriodImpl(int iPeriod) {
            super(iPeriod);
        }
        
        public DurationFieldType getFieldType() {
            return DurationFieldType.years();
        }
        
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }
}