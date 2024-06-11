import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.Assert;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadableInstant() {
        ReadableInstant readableInstant1 = null;
        ReadableInstant readableInstant2 = DateTimeUtils.now();
        DurationFieldType durationFieldType = DurationFieldType.days();
        
        between(readableInstant1, readableInstant2, durationFieldType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullReadablePartial() {
        ReadablePartial readablePartial1 = null;
        ReadablePartial readablePartial2 = DateTimeUtils.now().toReadablePartial();
        ReadablePeriod readablePeriod = new Period();
        
        between(readablePartial1, readablePartial2, readablePeriod);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithDifferentReadablePartialSizes() {
        ReadablePartial readablePartial1 = DateTimeUtils.now().toReadablePartial();
        ReadablePartial readablePartial2 = DateTimeUtils.now().minusDays(1).toReadablePartial();
        ReadablePeriod readablePeriod = new Period();
        
        between(readablePartial1, readablePartial2, readablePeriod);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNonContiguousReadablePartial() {
        ReadablePartial readablePartial1 = DateTimeUtils.now().toReadablePartial();
        ReadablePartial readablePartial2 = DateTimeUtils.now().minusMonths(1).toReadablePartial();
        ReadablePeriod readablePeriod = new Period();
        
        between(readablePartial1, readablePartial2, readablePeriod);
    }
    
    @Test
    public void testStandardPeriodInWithNullReadablePeriod() {
        ReadablePeriod readablePeriod = null;
        long n = 1000;
        
        int result = standardPeriodIn(readablePeriod, n);
        
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.getValue();
        
        Assert.assertEquals(10, result);
    }
    
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        baseSingleFieldPeriod.setValue(20);
        int result = baseSingleFieldPeriod.getValue();
        
        Assert.assertEquals(20, result);
    }
    
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        DurationFieldType result = baseSingleFieldPeriod.getFieldType();
        
        Assert.assertEquals(DurationFieldType.days(), result);
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        PeriodType result = baseSingleFieldPeriod.getPeriodType();
        
        Assert.assertEquals(PeriodType.days(), result);
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.size();
        
        Assert.assertEquals(1, result);
    }
    
    @Test
    public void testGetFieldTypeWithValidIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        DurationFieldType result = baseSingleFieldPeriod.getFieldType(0);
        
        Assert.assertEquals(DurationFieldType.days(), result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        baseSingleFieldPeriod.getFieldType(1);
    }
    
    @Test
    public void testGetValueWithValidIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.getValue(0);
        
        Assert.assertEquals(10, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        baseSingleFieldPeriod.getValue(1);
    }
    
    @Test
    public void testGetWithFieldTypeEqualToCurrentFieldType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.get(DurationFieldType.days());
        
        Assert.assertEquals(10, result);
    }
    
    @Test
    public void testGetWithFieldTypeNotEqualToCurrentFieldType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.get(DurationFieldType.hours());
        
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testIsSupportedWithSupportedFieldType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        boolean result = baseSingleFieldPeriod.isSupported(DurationFieldType.days());
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsSupportedWithUnsupportedFieldType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        boolean result = baseSingleFieldPeriod.isSupported(DurationFieldType.hours());
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        Period result = baseSingleFieldPeriod.toPeriod();
        
        Assert.assertEquals(new Period(10), result);
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        MutablePeriod result = baseSingleFieldPeriod.toMutablePeriod();
        
        Assert.assertEquals(new MutablePeriod().withFieldAdded(DurationFieldType.days(), 10), result);
    }
    
    @Test
    public void testEqualsWithEqualObjects() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriodImpl(10);
        
        boolean result = baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualsWithNotEqualObjects() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriodImpl(20);
        
        boolean result = baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.hashCode();
        
        Assert.assertEquals(27 * (27 * 17 + 10) + DurationFieldType.days().hashCode(), result);
    }
    
    @Test
    public void testCompareToWithGreaterValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriodImpl(20);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        
        Assert.assertEquals(1, result);
    }
    
    @Test
    public void testCompareToWithSmallerValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriodImpl(20);
        
        int result = baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
        
        Assert.assertEquals(-1, result);
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClass() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriodSubclass(10);
        
        baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2);
    }
}

class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {
    BaseSingleFieldPeriodImpl(int iPeriod) {
        super(iPeriod);
    }
    
    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.days();
    }
    
    @Override
    public PeriodType getPeriodType() {
        return PeriodType.days();
    }
    
    @Override
    public int compareTo(BaseSingleFieldPeriod baseSingleFieldPeriod) {
        return super.compareTo(baseSingleFieldPeriod);
    }
}

class BaseSingleFieldPeriodSubclass extends BaseSingleFieldPeriod {
    BaseSingleFieldPeriodSubclass(int iPeriod) {
        super(iPeriod);
    }
    
    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.hours();
    }
    
    @Override
    public PeriodType getPeriodType() {
        return PeriodType.hours();
    }
    
    @Override
    public int compareTo(BaseSingleFieldPeriod baseSingleFieldPeriod) {
        return super.compareTo(baseSingleFieldPeriod);
    }
}