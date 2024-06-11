
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test when both objects are null
        assertTrue(Objects.equal(null, null));
        
        // Test when one object is null
        assertFalse(Objects.equal("hello", null));
        assertFalse(Objects.equal(null, "hello"));
        
        // Test when two objects are equal
        assertTrue(Objects.equal("hello", "hello"));
        
        // Test when two objects are not equal
        assertFalse(Objects.equal("hello", "world"));
    }

    @Test
    public void testHashCode() {
        // Test with no objects
        assertEquals(0, Objects.hashCode());
        
        // Test with one object
        assertEquals(1, Objects.hashCode(null));
        
        // Test with multiple objects
        assertEquals(731063, Objects.hashCode("hello", 42, true));
    }

    @Test
    public void testToStringHelper() {
        // Test with object
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        helper.add("name", "John Doe");
        helper.add("age", 25);
        assertEquals("Test{name=John Doe, age=25}", helper.toString());
        
        // Test with class
        helper = Objects.toStringHelper(ObjectsTest.class);
        helper.add("name", "Test Class");
        helper.add("version", 1.0);
        assertEquals("ObjectsTest{name=Test Class, version=1.0}", helper.toString());
        
        // Test with string
        helper = Objects.toStringHelper("String");
        helper.add("value", "HelloWorld");
        helper.addValue(true);
        assertEquals("String{value=HelloWorld, true}", helper.toString());
    }

    @Test
    public void testFirstNonNull() {
        // Test with both null objects
        assertNull(Objects.firstNonNull(null, null));
        
        // Test with first object null
        assertEquals("hello", Objects.firstNonNull(null, "hello"));
        
        // Test with second object null
        assertEquals("world", Objects.firstNonNull("world", null));
        
        // Test with both non-null objects
        assertEquals("hello", Objects.firstNonNull("hello", "world"));
    }

}
