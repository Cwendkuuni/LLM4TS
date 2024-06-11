import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Typical use case: both objects are equal
        assertTrue(Objects.equal("hello", "hello"));
        
        // Typical use case: both objects are null
        assertTrue(Objects.equal(null, null));
        
        // Typical use case: only one object is null
        assertFalse(Objects.equal("hello", null));
        
        // Edge case: one object is not null, the other is null
        assertFalse(Objects.equal("hello", null));
        
        // Error scenario: non-equal objects
        assertFalse(Objects.equal("hello", "world"));
    }

    @Test
    public void testHashCode() {
        // Typical use case: multiple non-null objects
        assertEquals(Objects.hashCode(1, "hello", true), -1024557414);

        // Typical use case: single non-null object
        assertEquals(Objects.hashCode("hello"), 99162322);

        // Typical use case: empty arguments
        assertEquals(Objects.hashCode(), 0);

        // Typical use case: null arguments
        assertEquals(Objects.hashCode(null, null), 0);
        
        // Edge case: single null object
        assertEquals(Objects.hashCode(null), 0);
        
        // Error scenario: non-null and null objects
        assertNotEquals(Objects.hashCode("hello", null), 0);
    }

    @Test
    public void testToStringHelper() {
        // Typical use case: adding multiple values
        assertEquals(Objects.toStringHelper("TestClass")
                .add("name", "John")
                .add("age", 25)
                .toString(), "TestClass{name=John, age=25}");
                
        // Typical use case: adding null value
        assertEquals(Objects.toStringHelper("TestClass")
                .add("name", "John")
                .add("age", null)
                .toString(), "TestClass{name=John, age=null}");
                
        // Typical use case: omitting null values
        assertEquals(Objects.toStringHelper("TestClass")
                .add("name", "John")
                .add("age", null)
                .omitNullValues()
                .toString(), "TestClass{name=John}");
                
        // Edge case: adding value with special characters
        assertEquals(Objects.toStringHelper("TestClass")
                .add("name", "John")
                .add("age", "!@#$%^&*")
                .toString(), "TestClass{name=John, age=!@#$%^&*}");
            
        // Error scenario: using addValue instead of add
        assertNotEquals(Objects.toStringHelper("TestClass")
                .addValue("John")
                .addValue(25)
                .toString(), "TestClass{name=John, age=25}");
    }
}