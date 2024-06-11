
import org.junit.Assert;
import org.junit.Test;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        // Test when a and b are equal
        Object a = new Object();
        Object b = a;
        Assert.assertTrue(Objects.equal(a, b));
        
        // Test when a and b are not equal
        Object c= new Object();
        Object d = new Object();
        Assert.assertFalse(Objects.equal(c, d));
        
        // Test when a is null and b is not null
        Object e = null;
        Object f = new Object();
        Assert.assertFalse(Objects.equal(e, f));
        
        // Test when a is not null and b is null
        Object g = new Object();
        Object h = null;
        Assert.assertFalse(Objects.equal(g, h));
        
        // Test when both a and b are null
        Object i = null;
        Object j = null;
        Assert.assertTrue(Objects.equal(i, j));
    }
    
    @Test
    public void testHashCode() {
        // Test when there are no objects
        Assert.assertEquals(0, Objects.hashCode());
        
        // Test when there is one object
        Object k = new Object();
        Assert.assertEquals(k.hashCode(), Objects.hashCode(k));
        
        // Test when there are multiple objects
        Object l = new Object();
        Object m = new Object();
        Object n = new Object();
        Assert.assertEquals(Objects.hash(l, m, n), Objects.hashCode(l, m, n));
    }
    
    @Test
    public void testToStringHelper() {
        // Test when adding name-value pairs
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("TestClass");
        toStringHelper.add("name", "John");
        toStringHelper.add("age", 25);
        Assert.assertEquals("TestClass{name=John, age=25}", toStringHelper.toString());
        
        // Test when adding single values
        Objects.ToStringHelper toStringHelper2 = Objects.toStringHelper("TestClass");
        toStringHelper2.addValue("John");
        toStringHelper2.addValue(25);
        Assert.assertEquals("TestClass{John, 25}", toStringHelper2.toString());
        
        // Test when omitting null values
        Objects.ToStringHelper toStringHelper3 = Objects.toStringHelper("TestClass").omitNullValues();
        toStringHelper3.add("name", "John");
        toStringHelper3.add("age", null);
        Assert.assertEquals("TestClass{name=John}", toStringHelper3.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        // Test when both parameters are non-null
        String first = "Hello";
        String second = "World";
        Assert.assertEquals(first, Objects.firstNonNull(first, second));
        
        // Test when first parameter is null and second parameter is non-null
        String third = null;
        String fourth = "World";
        Assert.assertEquals(fourth, Objects.firstNonNull(third, fourth));
        
        // Test when first parameter is non-null and second parameter is null
        String fifth = "Hello";
        String sixth = null;
        Assert.assertEquals(fifth, Objects.firstNonNull(fifth, sixth));
        
        // Test when both parameters are null
        String seventh = null;
        String eighth = null;
        Assert.assertNull(Objects.firstNonNull(seventh, eighth));
    }
}
