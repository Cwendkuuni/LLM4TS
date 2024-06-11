import org.joda.time.DurationFieldType;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void getTypeTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertEquals(type, field.getType());
    }
    
    @Test
    public void getNameTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertEquals(type.getName(), field.getName());
    }
    
    @Test
    public void isSupportedTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertFalse(field.isSupported());
    }
    
    @Test
    public void isPreciseTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertTrue(field.isPrecise());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void getValueTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void getValueAsLongTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void getValueWithInstantTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1000, 123456789);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void getValueAsLongWithInstantTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1000, 123456789);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithIntTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithLongTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithIntAndInstantTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1000, 123456789);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithLongAndInstantTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1000L, 123456789);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void addWithInstantAndIntTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(123456789, 1000);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void addWithInstantAndLongTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(123456789, 1000L);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void getDifferenceTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(987654321, 123456789);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void getDifferenceAsLongTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(987654321, 123456789);
    }
    
    @Test
    public void getUnitMillisTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertEquals(0, field.getUnitMillis());
    }
    
    @Test
    public void compareToTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        Assert.assertEquals(0, field1.compareTo(field2));
    }
    
    @Test
    public void equalsTest_sameObject() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertTrue(field.equals(field));
    }
    
    @Test
    public void equalsTest_notAnInstance() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertFalse(field.equals(type));
    }
    
    @Test
    public void equalsTest_differentName() {
        DurationFieldType type1 = DurationFieldType.seconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        
        DurationFieldType type2 = DurationFieldType.millis();
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        
        Assert.assertFalse(field1.equals(field2));
    }
    
    @Test
    public void equalsTest_sameName() {
        DurationFieldType type1 = DurationFieldType.seconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        
        DurationFieldType type2 = DurationFieldType.seconds();
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        
        Assert.assertTrue(field1.equals(field2));
    }
    
    @Test
    public void hashCodeTest() {
        DurationFieldType type = DurationFieldType.millis();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertEquals(type.getName().hashCode(), field.hashCode());
    }
    
    @Test
    public void toStringTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertEquals("UnsupportedDurationField[seconds]", field.toString());
    }
    
    @Test
    public void readResolveTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        
        UnsupportedDurationField field2 = (UnsupportedDurationField) field1.readResolve();
        
        Assert.assertSame(field1, field2);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedTest() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        throw field.unsupported();
    }
}