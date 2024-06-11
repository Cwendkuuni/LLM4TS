import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetInstance() {
        // Typical use case
        DurationFieldType type1 = DurationFieldType.seconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        Assert.assertNotNull(field1);
        Assert.assertEquals(type1, field1.getType());
        
        // Edge case: Field type is null
        DurationFieldType type2 = null;
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        Assert.assertNotNull(field2);
        
        // Error scenario: Field type is not supported
        DurationFieldType type3 = DurationFieldType.minutes();
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(type3);
        Assert.assertNotNull(field3);
        Assert.assertEquals(type3, field3.getType());
    }
    
    @Test
    public void testGetters() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Typical use case
        Assert.assertEquals(type, field.getType());
        
        // Typical use case
        Assert.assertEquals("years", field.getName());
        
        // Typical use case
        Assert.assertFalse(field.isSupported());
        
        // Typical use case
        Assert.assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Error scenario: getValue() is not supported
        field.getValue(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Error scenario: getValueAsLong() is not supported
        field.getValueAsLong(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Error scenario: getValue(instant) is not supported
        field.getValue(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Error scenario: getValueAsLong(instant) is not supported
        field.getValueAsLong(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Error scenario: getMillis(value) is not supported
        field.getMillis(100);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Error scenario: getMillis(value) is not supported
        field.getMillis(100L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Error scenario: getMillis(value) is not supported
        field.getMillis(100, 200);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantAsLong() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Error scenario: getMillis(value) is not supported
        field.getMillis(100L, 200L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Error scenario: add(instant, value) is not supported
        field.add(1000, 200);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Error scenario: add(instant, value) is not supported
        field.add(1000, 200L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Error scenario: getDifference(instant1, instant2) is not supported
        field.getDifference(1000, 2000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Error scenario: getDifferenceAsLong(instant1, instant2) is not supported
        field.getDifferenceAsLong(1000, 2000);
    }
    
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // Typical use case
        Assert.assertEquals(0, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.seconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        
        DurationFieldType type2 = DurationFieldType.weeks();
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        
        // Typical use case
        Assert.assertEquals(0, field1.compareTo(field2));
        
        // Edge case: duration field is supported
        Assert.assertEquals(1, field2.compareTo(DurationFieldType.days()));
    }
    
}