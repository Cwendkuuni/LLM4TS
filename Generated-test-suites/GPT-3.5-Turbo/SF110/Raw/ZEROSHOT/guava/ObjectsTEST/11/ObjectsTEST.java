import org.junit.Assert;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test when both objects are null
        Assert.assertTrue(Objects.equal(null, null));
        
        // Test when both objects are equal
        Object obj = new Object();
        Assert.assertTrue(Objects.equal(obj, obj));
        
        // Test when only one object is null
        Assert.assertFalse(Objects.equal(obj, null));
        Assert.assertFalse(Objects.equal(null, obj));
        
        // Test when objects are different
        Object obj1 = new Object();
        Object obj2 = new Object();
        Assert.assertFalse(Objects.equal(obj1, obj2));
    }
    
    @Test
    public void testHashCode() {
        // Test with null objects
        Assert.assertEquals(0, Objects.hashCode((Object[])null));
        
        // Test with one object
        Object obj = new Object();
        Assert.assertEquals(obj.hashCode(), Objects.hashCode(obj));
        
        // Test with multiple objects
        Object obj1 = new Object();
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1.hashCode(), Objects.hashCode(obj1, obj2));
    }
    
    @Test
    public void testToStringHelper() {
        // Test with null object
        String className = null;
        Objects.ToStringHelper helper = Objects.toStringHelper(className);
        Assert.assertEquals("null{}", helper.toString());
        
        // Test with non-null object
        className = "TestObject";
        helper = Objects.toStringHelper(className);
        Assert.assertEquals("TestObject{}", helper.toString());
        
        // Test with omitNullValues
        helper = Objects.toStringHelper(className).omitNullValues();
        Assert.assertEquals("TestObject{}", helper.toString());

        // Test with add
        helper = Objects.toStringHelper(className)
                .add("key1", "value1")
                .add("key2", "value2");
        Assert.assertEquals("TestObject{key1=value1, key2=value2}", helper.toString());
        
        // Test with addValue
        helper = Objects.toStringHelper(className)
                .addValue("value1")
                .addValue("value2");
        Assert.assertEquals("TestObject{value1, value2}", helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        // Test when first object is not null
        Object obj1 = new Object();
        Object obj2 = new Object();
        Assert.assertSame(obj1, Objects.firstNonNull(obj1, obj2));
        
        // Test when first object is null
        Assert.assertSame(obj2, Objects.firstNonNull(null, obj2));
    }
}