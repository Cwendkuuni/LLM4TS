import org.junit.Assert;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test when both objects are null
        Assert.assertTrue(Objects.equal(null, null));

        // Test when one object is null and the other is not
        Assert.assertFalse(Objects.equal("hello", null));
        Assert.assertFalse(Objects.equal(null, "hello"));

        // Test when both objects are not null and equal
        Assert.assertTrue(Objects.equal("hello", "hello"));
        Assert.assertTrue(Objects.equal(123, 123));

        // Test when both objects are not null but not equal
        Assert.assertFalse(Objects.equal("hello", "world"));
        Assert.assertFalse(Objects.equal(123, 321));
    }

    @Test
    public void testHashCode() {
        // Test when objects array is empty
        Assert.assertEquals(0, Objects.hashCode());

        // Test when objects array has elements
        Assert.assertEquals(12345, Objects.hashCode("hello", "world"));
        Assert.assertEquals(54321, Objects.hashCode(1, 2, 3, 4, 5));

        // Test when objects array is null
        Assert.assertEquals(0, Objects.hashCode((Object[]) null));
    }

    @Test
    public void testToStringHelper() {
        // Create a sample object
        Object obj = new Object();

        // Test toStringHelper with Object parameter
        Objects.ToStringHelper toStringHelperObj = Objects.toStringHelper(obj);
        Assert.assertEquals("className{}", toStringHelperObj.toString());

        // Test omitNullValues
        toStringHelperObj.omitNullValues();
        Assert.assertEquals("className{}", toStringHelperObj.toString());

        // Test add
        toStringHelperObj.add("name", "value");
        toStringHelperObj.add("boolean", true);
        toStringHelperObj.add("char", 'c');
        toStringHelperObj.add("double", 1.23);
        toStringHelperObj.add("float", 3.14F);
        toStringHelperObj.add("int", 123);
        toStringHelperObj.add("long", 12345L);
        Assert.assertEquals("className{name=value, boolean=true, char=c, double=1.23, float=3.14, int=123, long=12345}", toStringHelperObj.toString());

        // Test addValue
        toStringHelperObj.addValue("value");
        toStringHelperObj.addValue(true);
        toStringHelperObj.addValue('c');
        toStringHelperObj.addValue(1.23);
        toStringHelperObj.addValue(3.14F);
        toStringHelperObj.addValue(123);
        toStringHelperObj.addValue(12345L);
        Assert.assertEquals("className{name=value, boolean=true, char=c, double=1.23, float=3.14, int=123, long=12345, value, true, c, 1.23, 3.14, 123, 12345}", toStringHelperObj.toString());
    }

    @Test
    public void testFirstNonNull() {
        // Test when both objects are null
        Assert.assertNull(Objects.firstNonNull(null, null));

        // Test when one object is null and the other is not
        Assert.assertEquals("hello", Objects.firstNonNull("hello", null));
        Assert.assertEquals("hello", Objects.firstNonNull(null, "hello"));

        // Test when both objects are not null
        Assert.assertEquals("world", Objects.firstNonNull("hello", "world"));
        Assert.assertEquals("world", Objects.firstNonNull("world", "hello"));
    }

}