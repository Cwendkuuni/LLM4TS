import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.*;
import org.junit.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenReadableInstant() {
        ReadableInstant instant1 = new DateTime(2020, 1, 1, 0, 0);
        ReadableInstant instant2 = new DateTime(2021, 1, 1, 0, 0);
        DurationFieldType fieldType = DurationFieldType.years();
        
        int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
        
        Assert.assertEquals(1, result);
    }
    
    @Test
    public void testBetweenReadablePartial() {
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDate(2021, 1, 1);
        ReadablePeriod period = new Period(0, 1, 0, 0);
        
        int result = BaseSingleFieldPeriod.between(partial1, partial2, period);
        
        Assert.assertEquals(1, result);
    }
    
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(0, 1, 0, 0);
        long millis = 365 * 24 * 60 * 60 * 1000L;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millis);
        
        Assert.assertEquals(1, result);
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
        
        Assert.assertEquals(20, baseSingleFieldPeriod.getValue());
    }
    
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        DurationFieldType result = baseSingleFieldPeriod.getFieldType();
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        PeriodType result = baseSingleFieldPeriod.getPeriodType();
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.size();
        
        Assert.assertEquals(1, result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        baseSingleFieldPeriod.getFieldType(1);
    }
    
    @Test
    public void testGetFieldTypeWithValidIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        DurationFieldType result = baseSingleFieldPeriod.getFieldType(0);
        
        Assert.assertNull(result);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        baseSingleFieldPeriod.getValue(1);
    }
    
    @Test
    public void testGetValueWithValidIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.getValue(0);
        
        Assert.assertEquals(10, result);
    }
    
    @Test
    public void testGet() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.get(DurationFieldType.years());
        
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        boolean result = baseSingleFieldPeriod.isSupported(DurationFieldType.years());
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        Period result = baseSingleFieldPeriod.toPeriod();
        
        Assert.assertEquals(10, result.getYears());
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        MutablePeriod result = baseSingleFieldPeriod.toMutablePeriod();
        
        Assert.assertEquals(10, result.getYears());
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodImpl(10);
        
        boolean result = period1.equals(period2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testNotEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodImpl(20);
        
        boolean result = period1.equals(period2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
        
        int result = baseSingleFieldPeriod.hashCode();
        
        Assert.assertEquals(8298, result);
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodImpl(20);
        
        int result = period1.compareTo(period2);
        
        Assert.assertEquals(-1, result);
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClasses() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriodImpl(10);
        CustomSingleFieldPeriod period2 = new CustomSingleFieldPeriod(20);
        
        period1.compareTo(period2);
    }
}