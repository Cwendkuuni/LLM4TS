
package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void between_NullReadableInstant_ThrowsException() {
        BaseSingleFieldPeriod.between(null, new DateTime(), DurationFieldType.days());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void between_NullDurationFieldType_ThrowsException() {
        BaseSingleFieldPeriod.between(new DateTime(), new DateTime(), null);
    }
    
    @Test
    public void between_SameInstant_ReturnsZero() {
        assertEquals(0, BaseSingleFieldPeriod.between(new DateTime(), new DateTime(), DurationFieldType.days()));
    }
    
    @Test
    public void between_DifferentInstants_ReturnsDifference() {
        DateTime start = new DateTime(2022, 1, 1, 0, 0);
        DateTime end = new DateTime(2022, 1, 5, 0, 0);
        assertEquals(4, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void between_NullReadablePartial_ThrowsException() {
        BaseSingleFieldPeriod.between(null, new DateTime(), new Period());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void between_InvalidPartialSize_ThrowsException() {
        Period period = new Period().withFields(1, 2, 3);
        BaseSingleFieldPeriod.between(period, period, period);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void between_InvalidPartialFields_ThrowsException() {
        Period period1 = new Period().withFields(1, 2);
        Period period2 = new Period().withFields(3, 4);
        BaseSingleFieldPeriod.between(period1, period2, period1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void between_NonContiguousPartial_ThrowsException() {
        Period period1 = new Period().withYears(2022);
        Period period2 = new Period().withMonths(1);
        BaseSingleFieldPeriod.between(period1, period2, period1);
    }
    
    @Test
    public void between_ValidPartial_ReturnsDifference() {
        Period period1 = new Period().withDays(7);
        Period period2 = new Period().withHours(168);
        assertEquals(7, BaseSingleFieldPeriod.between(period1, period2, period1));
    }
    
    @Test
    public void standardPeriodIn_NullPeriod_ReturnsZero() {
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void standardPeriodIn_InvalidFieldType_ThrowsException() {
        Period period = new Period().withMonths(1);
        BaseSingleFieldPeriod.standardPeriodIn(period, 1);
    }
    
    @Test
    public void standardPeriodIn_ValidPeriod_ReturnsStandardPeriod() {
        Period period = new Period().withSeconds(5);
        assertEquals(5, BaseSingleFieldPeriod.standardPeriodIn(period, 1000));
    }
    
    @Test
    public void getValue_ReturnsValue() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        assertEquals(10, singleFieldPeriod.getValue());
    }
    
    @Test
    public void setValue_ModifiesValue() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        singleFieldPeriod.setValue(20);
        assertEquals(20, singleFieldPeriod.getValue());
    }
    
    @Test
    public void getFieldType_ReturnsFieldType() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        assertEquals(DurationFieldType.minutes(), singleFieldPeriod.getFieldType());
    }
    
    @Test
    public void getPeriodType_ReturnsPeriodType() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        assertEquals(PeriodType.minutes(), singleFieldPeriod.getPeriodType());
    }
    
    @Test
    public void size_ReturnsOne() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        assertEquals(1, singleFieldPeriod.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void getFieldType_InvalidIndex_ThrowsException() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        singleFieldPeriod.getFieldType(1);
    }
    
    @Test
    public void getFieldType_ValidIndex_ReturnsFieldType() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        assertEquals(DurationFieldType.minutes(), singleFieldPeriod.getFieldType(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void getValue_InvalidIndex_ThrowsException() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        singleFieldPeriod.getValue(1);
    }
    
    @Test
    public void getValue_ValidIndex_ReturnsValue() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        assertEquals(10, singleFieldPeriod.getValue(0));
    }
    
    @Test
    public void get_ValidFieldType_ReturnsValue() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        assertEquals(10, singleFieldPeriod.get(DurationFieldType.minutes()));
    }
    
    @Test
    public void get_InvalidFieldType_ReturnsZero() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        assertEquals(0, singleFieldPeriod.get(DurationFieldType.hours()));
    }
    
    @Test
    public void isSupported_ValidFieldType_ReturnsTrue() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        assertTrue(singleFieldPeriod.isSupported(DurationFieldType.minutes()));
    }
    
    @Test
    public void isSupported_InvalidFieldType_ReturnsFalse() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        assertFalse(singleFieldPeriod.isSupported(DurationFieldType.hours()));
    }
    
    @Test
    public void toPeriod_ReturnsPeriod() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        assertEquals(Period.minutes(10), singleFieldPeriod.toPeriod());
    }
    
    @Test
    public void toMutablePeriod_ReturnsMutablePeriod() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        assertEquals(new Period().withMinutes(10), singleFieldPeriod.toMutablePeriod());
    }
    
    @Test
    public void equals_SameInstance_ReturnsTrue() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        assertEquals(true, singleFieldPeriod.equals(singleFieldPeriod));
    }
    
    @Test
    public void equals_InstanceWithSameTypeAndValue_ReturnsTrue() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriodImpl(10);
        assertEquals(true, singleFieldPeriod1.equals(singleFieldPeriod2));
    }
    
    @Test
    public void equals_InstanceWithDifferentType_ReturnsFalse() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        assertFalse(singleFieldPeriod.equals(new Period().withMinutes(10)));
    }
    
    @Test
    public void equals_InstanceWithDifferentValue_ReturnsFalse() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriodImpl(20);
        assertFalse(singleFieldPeriod1.equals(singleFieldPeriod2));
    }
    
    @Test
    public void hashCode_EqualsObjects_HaveSameHashCode() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriodImpl(10);
        assertEquals(singleFieldPeriod1.hashCode(), singleFieldPeriod2.hashCode());
    }
    
    @Test
    public void hashCode_DifferentObjects_HaveDifferentHashCode() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriodImpl(20);
        assertNotEquals(singleFieldPeriod1.hashCode(), singleFieldPeriod2.hashCode());
    }
    
    @Test(expected = ClassCastException.class)
    public void compareTo_DifferentClassTypes_ThrowsException() {
        BaseSingleFieldPeriod singleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        singleFieldPeriod.compareTo(new BaseSingleFieldPeriodImpl2(10));
    }
    
    @Test
    public void compareTo_SmallerValue_ReturnsNegative() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriodImpl(5);
        BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriodImpl(10);
        assertTrue(singleFieldPeriod1.compareTo(singleFieldPeriod2) < 0);
    }
    
    @Test
    public void compareTo_LargerValue_ReturnsPositive() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriodImpl(5);
        assertTrue(singleFieldPeriod1.compareTo(singleFieldPeriod2) > 0);
    }
    
    @Test
    public void compareTo_SameValue_ReturnsZero() {
        BaseSingleFieldPeriod singleFieldPeriod1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod singleFieldPeriod2 = new BaseSingleFieldPeriodImpl(10);
        assertEquals(0, singleFieldPeriod1.compareTo(singleFieldPeriod2));
    }
    
    
    private static class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {
        
        protected BaseSingleFieldPeriodImpl(int iPeriod) {
            super(iPeriod);
        }
        
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.minutes();
        }
        
        @Override
        public PeriodType getPeriodType() {
            return PeriodType.minutes();
        }
    }
    
    private static class BaseSingleFieldPeriodImpl2 extends BaseSingleFieldPeriod {
        
        protected BaseSingleFieldPeriodImpl2(int iPeriod) {
            super(iPeriod);
        }
        
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.seconds();
        }
        
        @Override
        public PeriodType getPeriodType() {
            return PeriodType.seconds();
        }
    }
}
