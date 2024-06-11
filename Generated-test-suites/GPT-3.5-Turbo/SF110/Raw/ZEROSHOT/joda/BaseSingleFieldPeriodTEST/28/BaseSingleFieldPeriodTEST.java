import org.joda.time.*;
import org.junit.*;

public class BaseSingleFieldPeriodTest {
    
    @Test
    public void testBetweenWithReadableInstant() {
        ReadableInstant instant1 = new DateTime(2021, 1, 1, 0, 0, 0);
        ReadableInstant instant2 = new DateTime(2021, 1, 2, 0, 0, 0);
        DurationFieldType fieldType = DurationFieldType.days();
        
        int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
        
        Assert.assertEquals(1, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithReadableInstantNull() {
        ReadableInstant instant1 = null;
        ReadableInstant instant2 = new DateTime(2021, 1, 2, 0, 0, 0);
        DurationFieldType fieldType = DurationFieldType.days();
        
        BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
    }
    
    @Test
    public void testBetweenWithReadablePartial() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1);
        ReadablePartial partial2 = new LocalDate(2021, 1, 3);
        ReadablePeriod period = Period.days(1);
        
        int result = BaseSingleFieldPeriod.between(partial1, partial2, period);
        
        Assert.assertEquals(2, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithReadablePartialNull() {
        ReadablePartial partial1 = null;
        ReadablePartial partial2 = new LocalDate(2021, 1, 3);
        ReadablePeriod period = Period.days(1);
        
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithDifferentSizeReadablePartial() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1);
        ReadablePartial partial2 = new LocalDateTime(2021, 1, 3, 12, 0, 0);
        ReadablePeriod period = Period.days(1);
        
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithDifferentFieldsReadablePartial() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1);
        ReadablePartial partial2 = new LocalDate(2021, 1, 3);
        ReadablePeriod period = Period.hours(1);
        
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNonContiguousReadablePartial() {
        ReadablePartial partial1 = new LocalDate(2021, 1, 1);
        ReadablePartial partial2 = new LocalDate(2021, 1, 3).withFieldAdded(DurationFieldType.months(), 1);
        ReadablePeriod period = Period.days(1);
        
        BaseSingleFieldPeriod.between(partial1, partial2, period);
    }
    
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.years(1).withMonths(6);
        long n = 365 * 24 * 60 * 60 * 1000L;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        
        Assert.assertEquals(1, result);
    }
    
    @Test
    public void testStandardPeriodInWithNullPeriod() {
        ReadablePeriod period = null;
        long n = 365 * 24 * 60 * 60 * 1000L;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        
        Assert.assertEquals(0, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithNonPreciseField() {
        ReadablePeriod period = Period.days(1);
        long n = 1000L;
        
        BaseSingleFieldPeriod.standardPeriodIn(period, n);
    }
    
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        int result = period.getValue();
        
        Assert.assertEquals(10, result);
    }
    
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        period.setValue(20);
        
        int result = period.getValue();
        
        Assert.assertEquals(20, result);
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        int result = period.size();
        
        Assert.assertEquals(1, result);
    }
    
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        DurationFieldType result = period.getFieldType();
        
        Assert.assertEquals(DurationFieldType.standard(), result);
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        PeriodType result = period.getPeriodType();
        
        Assert.assertEquals(PeriodType.standard(), result);
    }
    
    @Test
    public void testGetFieldTypeWithIndex() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        DurationFieldType result = period.getFieldType(0);
        
        Assert.assertEquals(DurationFieldType.standard(), result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        period.getFieldType(1);
    }
    
    @Test
    public void testGetValueWithIndex() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        int result = period.getValue(0);
        
        Assert.assertEquals(10, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        period.getValue(1);
    }
    
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        int result = period.get(DurationFieldType.standard());
        
        Assert.assertEquals(10, result);
    }
    
    @Test
    public void testGetWithDifferentFieldType() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        int result = period.get(DurationFieldType.minutes());
        
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        boolean result = period.isSupported(DurationFieldType.standard());
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsSupportedWithDifferentFieldType() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        boolean result = period.isSupported(DurationFieldType.minutes());
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        Period result = period.toPeriod();
        
        Assert.assertEquals(10, result.get(DurationFieldType.standard()));
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        MutablePeriod result = period.toMutablePeriod();
        
        Assert.assertEquals(10, result.get(DurationFieldType.standard()));
    }
    
    @Test
    public void testEqualsWithSameObject() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        boolean result = period.equals(period);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualsWithDifferentObject() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new CustomSingleFieldPeriod(10);
        
        boolean result = period1.equals(period2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualsWithDifferentPeriodType() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new CustomSingleFieldPeriod(10) {
            public PeriodType getPeriodType() {
                return PeriodType.standard().withMillisRemoved();
            }
        };
        
        boolean result = period1.equals(period2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentValues() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new CustomSingleFieldPeriod(20);
        
        boolean result = period1.equals(period2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new CustomSingleFieldPeriod(10);
        
        int result = period.hashCode();
        
        Assert.assertEquals(27 * (27 * 17 + 10) + DurationFieldType.standard().hashCode(), result);
    }
    
    @Test
    public void testCompareToWithDifferentClasses() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new AnotherCustomSingleFieldPeriod(10);
        
        try {
            period1.compareTo(period2);
        } catch (ClassCastException e) {
            Assert.assertTrue(true);
        }
    }
    
    @Test
    public void testCompareToWithGreaterValue() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(20);
        BaseSingleFieldPeriod period2 = new CustomSingleFieldPeriod(10);
        
        int result = period1.compareTo(period2);
        
        Assert.assertEquals(1, result);
    }
    
    @Test
    public void testCompareToWithSmallerValue() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new CustomSingleFieldPeriod(20);
        
        int result = period1.compareTo(period2);
        
        Assert.assertEquals(-1, result);
    }
    
    @Test
    public void testCompareToWithEqualValue() {
        BaseSingleFieldPeriod period1 = new CustomSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new CustomSingleFieldPeriod(10);
        
        int result = period1.compareTo(period2);
        
        Assert.assertEquals(0, result);
    }
    
    private static class CustomSingleFieldPeriod extends BaseSingleFieldPeriod {
        public CustomSingleFieldPeriod(int iPeriod) {
            super(iPeriod);
        }
        
        public DurationFieldType getFieldType() {
            return DurationFieldType.standard();
        }
        
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }
    
    private static class AnotherCustomSingleFieldPeriod extends BaseSingleFieldPeriod {
        public AnotherCustomSingleFieldPeriod(int iPeriod) {
            super(iPeriod);
        }
        
        public DurationFieldType getFieldType() {
            return DurationFieldType.standard();
        }
        
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }
}