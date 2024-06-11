import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test equality of two different objects
        assertFalse(Objects.equal(new Object(), new Object()));
        
        // Test equality of two null objects
        assertTrue(Objects.equal(null, null));
        
        // Test equality of null and non-null objects
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));
        
        // Test equality of two equal objects
        assertTrue(Objects.equal("Hello", "Hello"));
    }
    
    @Test
    public void testHashCode() {
        // Test hashCode of a null object
        assertEquals(0, Objects.hashCode(null));
        
        // Test hashCode of an empty array
        assertEquals(1, Objects.hashCode());
        
        // Test hashCode of an array with objects
        assertEquals(101, Objects.hashCode("Hello", "World"));
    }
    
    @Test
    public void testToStringHelper() {
        // Test toStringHelper with null value
        assertEquals("TestClass{value=null}", Objects.toStringHelper("TestClass").add("value", null).toString());
        
        // Test toStringHelper with boolean value
        assertEquals("TestClass{value=true}", Objects.toStringHelper("TestClass").add("value", true).toString());
        
        // Test toStringHelper with char value
        assertEquals("TestClass{value=a}", Objects.toStringHelper("TestClass").add("value", 'a').toString());
        
        // Test toStringHelper with double value
        assertEquals("TestClass{value=3.14}", Objects.toStringHelper("TestClass").add("value", 3.14).toString());
        
        // Test toStringHelper with float value
        assertEquals("TestClass{value=2.718}", Objects.toStringHelper("TestClass").add("value", 2.718f).toString());
        
        // Test toStringHelper with int value
        assertEquals("TestClass{value=42}", Objects.toStringHelper("TestClass").add("value", 42).toString());
        
        // Test toStringHelper with long value
        assertEquals("TestClass{value=1234567890}", Objects.toStringHelper("TestClass").add("value", 1234567890L).toString());
        
        // Test toStringHelper with null values omitted
        assertEquals("TestClass{}", Objects.toStringHelper("TestClass").omitNullValues().toString());
    }
    
    @Test
    public void testFirstNonNull() {
        // Test firstNonNull with two non-null values
        assertEquals(1, Objects.firstNonNull(1, 2));
        
        // Test firstNonNull with first value null
        assertEquals(2, Objects.firstNonNull(null, 2));
        
        // Test firstNonNull with second value null
        assertEquals("test", Objects.firstNonNull("test", null));
        
        // Test firstNonNull with both values null
        assertNull(Objects.firstNonNull(null, null));
    }
}