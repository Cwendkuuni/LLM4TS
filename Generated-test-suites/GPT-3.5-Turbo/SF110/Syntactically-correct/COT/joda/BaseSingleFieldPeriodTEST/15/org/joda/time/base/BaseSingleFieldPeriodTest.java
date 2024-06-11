
import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    
    @Test
    public void testBetweenWithReadableInstant() {
        ReadableInstant instant1 = new Instant(1000L);
        ReadableInstant instant2 = new Instant(2000L);
        DurationFieldType fieldType = DurationFieldType.seconds();
        
        int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
        
        assertEquals(1, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadableInstant() {
        ReadableInstant instant1 = null;
        ReadableInstant instant2 = new Instant(2000L);
        DurationFieldType fieldType = DurationFieldType.seconds();
        
        BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
    }
    
    @Test
    public void testBetweenWithReadablePartial() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1);
        ReadablePartial partial2 = new LocalDate(2021, 12, 31);
        ReadablePeriod period = new Period();
        
        int result = BaseSingleFieldPeriod.between(partial1, partial2, period);
        
        assertEquals(364, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadablePartial() {
        ReadablePartial partial1 = null;
        ReadablePartial partial2 = new LocalDate(2021, 12, 31);
        ReadablePeriod period = new Period();
        
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithDifferentSizeReadablePartial() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1);
        ReadablePartial partial2 = new LocalDateTime(2021, 12, 31, 23, 59, 59);
        ReadablePeriod period = new Period();
        
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNonContiguousReadablePartial() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1);
        ReadablePartial partial2 = new LocalDate(2021, 1, 3);
        ReadablePeriod period = new Period();
        
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        long n = 60;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        
        assertEquals(6, result);
    }
    
    @Test
    public void testStandardPeriodInWithNullPeriod() {
        ReadablePeriod period = null;
        long n = 60;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        
        assertEquals(0, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithNonPreciseFieldType() {
        ReadablePeriod period = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        long n = 0;
        
        BaseSingleFieldPeriod.standardPeriodIn(period, n);
    }
    
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = basePeriod.getValue();
        
        assertEquals(10, result);
    }
    
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        
        basePeriod.setValue(20);
        int result = basePeriod.getValue();
        
        assertEquals(20, result);
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = basePeriod.size();
        
        assertEquals(1, result);
    }
    
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        
        DurationFieldType result = basePeriod.getFieldType();
        
        assertNotNull(result);
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        
        PeriodType result = basePeriod.getPeriodType();
        
        assertNotNull(result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        
        basePeriod.getFieldType(1);
    }
    
    @Test
    public void testGetFieldTypeWithValidIndex() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        
        DurationFieldType result = basePeriod.getFieldType(0);
        
        assertNotNull(result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        
        basePeriod.getValue(1);
    }
    
    @Test
    public void testGetValueWithValidIndex() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = basePeriod.getValue(0);
        
        assertEquals(10, result);
    }
    
    @Test
    public void testGet() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        DurationFieldType fieldType = DurationFieldType.seconds();
        
        int result = basePeriod.get(fieldType);
        
        assertEquals(10, result);
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        DurationFieldType fieldType = DurationFieldType.seconds();
        
        boolean result = basePeriod.isSupported(fieldType);
        
        assertTrue(result);
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        
        Period result = basePeriod.toPeriod();
        
        assertNotNull(result);
        assertEquals(10, result.getValue(0));
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        
        MutablePeriod result = basePeriod.toMutablePeriod();
        
        assertNotNull(result);
        assertEquals(10, result.getValue(0));
    }
    
    @Test
    public void testEqualsWithSameInstance() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        
        boolean result = basePeriod.equals(basePeriod);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqualsWithEqualInstance() {
        BaseSingleFieldPeriod basePeriod1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod basePeriod2 = new BaseSingleFieldPeriodImpl(10);
        
        boolean result = basePeriod1.equals(basePeriod2);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqualsWithDifferentInstance() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        ReadablePeriod period = new Period(10);
        
        boolean result = basePeriod.equals(period);
        
        assertFalse(result);
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = basePeriod.hashCode();
        
        assertEquals(1755, result);
    }
    
    @Test
    public void testCompareToWithGreaterValue() {
        BaseSingleFieldPeriod basePeriod1 = new BaseSingleFieldPeriodImpl(20);
        BaseSingleFieldPeriod basePeriod2 = new BaseSingleFieldPeriodImpl(10);
        
        int result = basePeriod1.compareTo(basePeriod2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompareToWithSmallerValue() {
        BaseSingleFieldPeriod basePeriod1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod basePeriod2 = new BaseSingleFieldPeriodImpl(20);
        
        int result = basePeriod1.compareTo(basePeriod2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompareToWithEqualValue() {
        BaseSingleFieldPeriod basePeriod1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod basePeriod2 = new BaseSingleFieldPeriodImpl(10);
        
        int result = basePeriod1.compareTo(basePeriod2);
        
        assertEquals(0, result);
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClass() {
        BaseSingleFieldPeriod basePeriod = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriodImpl2 basePeriodImpl2 = new BaseSingleFieldPeriodImpl2(10);
        
        basePeriod.compareTo(basePeriodImpl2);
    }
    
    private class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {
        public BaseSingleFieldPeriodImpl(int iPeriod) {
            super(iPeriod);
        }
        
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.seconds();
        }
        
        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }
    
    private class BaseSingleFieldPeriodImpl2 extends BaseSingleFieldPeriod {
        public BaseSingleFieldPeriodImpl2(int iPeriod) {
            super(iPeriod);
        }
        
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.minutes();
        }
        
        @Override
        public PeriodType getPeriodType() {
            return PeriodType.time();
        }
    }
}
