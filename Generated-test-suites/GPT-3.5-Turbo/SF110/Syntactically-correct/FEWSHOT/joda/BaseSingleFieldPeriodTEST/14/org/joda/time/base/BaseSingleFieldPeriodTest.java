package org.joda.time.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    @Test
    public void testBetween() {
        ReadableInstant instant1 = new Instant(0);
        ReadableInstant instant2 = new Instant(1000);
        DurationFieldType fieldType = DurationFieldType.days();
        assertEquals(0, BaseSingleFieldPeriod.between(instant1, instant2, fieldType));
        
        ReadablePartial partial1 = new LocalDate(2019, 1, 1);
        ReadablePartial partial2 = new LocalDate(2019, 1, 2);
        ReadablePeriod period = new Period(0, 1, 2, 0);
        assertEquals(1, BaseSingleFieldPeriod.between(partial1, partial2, period));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullInstant1() {
        ReadableInstant instant2 = new Instant(1000);
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod.between(null, instant2, fieldType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullInstant2() {
        ReadableInstant instant1 = new Instant(0);
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod.between(instant1, null, fieldType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullPartial1() {
        ReadablePartial partial2 = new LocalDate(2019, 1, 2);
        ReadablePeriod period = new Period(0, 1, 2, 0);
        BaseSingleFieldPeriod.between(null, partial2, period);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NullPartial2() {
        ReadablePartial partial1 = new LocalDate(2019, 1, 1);
        ReadablePeriod period = new Period(0, 1, 2, 0);
        BaseSingleFieldPeriod.between(partial1, null, period);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_DifferentPartialFieldTypes() {
        ReadablePartial partial1 = new LocalDate(2019, 1, 1);
        ReadablePartial partial2 = new LocalTime(0, 0);
        ReadablePeriod period = new Period(0, 1, 2, 0);
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetween_NotContiguousPartial() {
        ReadablePartial partial1 = new LocalDate(2019, 1, 1);
        ReadablePartial partial2 = new LocalDate(2019, 1, 3);
        ReadablePeriod period = new Period(0, 1, 2, 0);
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_NullPeriod() {
        long n = 1000;
        BaseSingleFieldPeriod.standardPeriodIn(null, n);
    }
    
    @Test
    public void testStandardPeriodIn_ZeroPeriod() {
        long n = 1000;
        ReadablePeriod period = new Period(0, 0, 0, 0);
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(period, n));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_NotPreciseField() {
        long n = 1000;
        ReadablePeriod period = new Period(1, 0, 0, 0);
        BaseSingleFieldPeriod.standardPeriodIn(period, n);
    }
    
    @Test
    public void testStandardPeriodIn() {
        long n = 1000;
        ReadablePeriod period = new Period(1, 2, 3, 4);
        assertEquals(1234, BaseSingleFieldPeriod.standardPeriodIn(period, n));
    }
    
    @Test
    public void testBaseSingleFieldPeriod() {
        int iPeriod = 123;
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(iPeriod);
        assertEquals(iPeriod, baseSingleFieldPeriod.getValue());
    }
    
    @Test
    public void testSetValue() {
        int iPeriod = 123;
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(iPeriod);
        
        int iPeriodNew = 456;
        baseSingleFieldPeriod.setValue(iPeriodNew);
        
        assertEquals(iPeriodNew, baseSingleFieldPeriod.getValue());
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(123);
        assertEquals(1, baseSingleFieldPeriod.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_InvalidIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(123);
        baseSingleFieldPeriod.getFieldType(1);
    }
    
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(123);
        DurationFieldType fieldType = baseSingleFieldPeriod.getFieldType(0);
        
        assertNotNull(fieldType);
        assertEquals(DurationFieldType.class, fieldType.getClass());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_InvalidIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(123);
        baseSingleFieldPeriod.getValue(1);
    }
    
    @Test
    public void testGetValue() {
        int iPeriod = 123;
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(iPeriod);
        
        int value = baseSingleFieldPeriod.getValue(0);
        assertEquals(iPeriod, value);
    }
    
    @Test
    public void testGet() {
        int iPeriod = 123;
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(iPeriod);
        DurationFieldType fieldType = baseSingleFieldPeriod.getFieldType();
        
        int value = baseSingleFieldPeriod.get(fieldType);
        assertEquals(iPeriod, value);
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(123);
        DurationFieldType fieldType = baseSingleFieldPeriod.getFieldType();
        
        assertTrue(baseSingleFieldPeriod.isSupported(fieldType));
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(123);
        Period period = baseSingleFieldPeriod.toPeriod();
        
        assertNotNull(period);
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(123, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(123);
        MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
        
        assertNotNull(mutablePeriod);
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(123, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }
    
    @Test
    public void testEquals_SameInstance() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(123);
        assertTrue(baseSingleFieldPeriod.equals(baseSingleFieldPeriod));
    }
    
    @Test
    public void testEquals_NullObject() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(123);
        assertFalse(baseSingleFieldPeriod.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(123);
        assertFalse(baseSingleFieldPeriod.equals(new Object()));
    }
    
    @Test
    public void testEquals_DifferentPeriodType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new MockBaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new MockBaseSingleFieldPeriod2(123);
        assertFalse(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
    }
    
    @Test
    public void testEquals_DifferentValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new MockBaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new MockBaseSingleFieldPeriod(456);
        assertFalse(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
    }
    
    @Test
    public void testEquals_SameValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new MockBaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new MockBaseSingleFieldPeriod(123);
        assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
    }
    
    @Test
    public void testHashCode() {
        int iPeriod = 123;
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(iPeriod);
        
        int expectedHashCode = 27 * (27 * 17 + iPeriod) + baseSingleFieldPeriod.getFieldType().hashCode();
        assertEquals(expectedHashCode, baseSingleFieldPeriod.hashCode());
    }
    
    @Test
    public void testCompareTo_SameClass() {
        int iPeriod1 = 123;
        MockBaseSingleFieldPeriod baseSingleFieldPeriod1 = new MockBaseSingleFieldPeriod(iPeriod1);
        
        int iPeriod2 = 456;
        MockBaseSingleFieldPeriod baseSingleFieldPeriod2 = new MockBaseSingleFieldPeriod(iPeriod2);
        
        assertTrue(baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2) < 0);
        assertTrue(baseSingleFieldPeriod2.compareTo(baseSingleFieldPeriod1) > 0);
        assertTrue(baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod1) == 0);
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareTo_DifferentClass() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new MockBaseSingleFieldPeriod(123);
        baseSingleFieldPeriod.compareTo(new Object());
    }
}

class MockBaseSingleFieldPeriod extends BaseSingleFieldPeriod {
    public MockBaseSingleFieldPeriod(int iPeriod) {
        super(iPeriod);
    }
    
    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.days();
    }
    
    @Override
    public PeriodType getPeriodType() {
        return null;
    }
    
    @Override
    public int compareTo(BaseSingleFieldPeriod baseSingleFieldPeriod) {
        return Integer.compare(getValue(), baseSingleFieldPeriod.getValue());
    }
}

class MockBaseSingleFieldPeriod2 extends BaseSingleFieldPeriod {
    public MockBaseSingleFieldPeriod2(int iPeriod) {
        super(iPeriod);
    }
    
    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.hours();
    }
    
    @Override
    public PeriodType getPeriodType() {
        return null;
    }
    
    @Override
    public int compareTo(BaseSingleFieldPeriod baseSingleFieldPeriod) {
        return Integer.compare(getValue(), baseSingleFieldPeriod.getValue());
    }
}