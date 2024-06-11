import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Objects.*;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        // Typical use case where both objects are equal
        assertTrue(Objects.equal("hello", "hello"));
        
        // Typical use case where both objects are not equal
        assertFalse(Objects.equal("hello", "world"));
        
        // Edge case where one object is null and the other is not null
        assertFalse(Objects.equal(null, "hello"));
        
        // Edge case where both objects are null
        assertTrue(Objects.equal(null, null));
    }
    
    @Test
    public void testHashCode() {
        // Typical use case with multiple objects
        assertEquals(Objects.hashCode("hello", "world"), ("hello"+"world").hashCode());
        
        // Edge case with no objects
        assertEquals(Objects.hashCode(), 0);
        
        // Edge case with null object
        assertEquals(Objects.hashCode(null), 0);
        
        // Edge case with one object
        assertEquals(Objects.hashCode("hello"), "hello".hashCode());
    }
    
    @Test
    public void testFirstNonNull() {
        // Typical use case where both objects are non-null
        assertEquals(Objects.firstNonNull("hello", "world"), "hello");
        
        // Typical use case where one object is non-null and other is null
        assertEquals(Objects.firstNonNull(null, "world"), "world");
        
        // Typical use case where both objects are null
        assertNull(Objects.firstNonNull(null, null));
    }
    
    @Test
    public void testToStringHelper() {
        // Typical use case with adding multiple values
        String expectedOutput = "TestClass{name=value, age=30, isAlive=true}";
        String actualOutput = Objects.toStringHelper(TestClass.class)
                                .add("name", "value")
                                .add("age", 30)
                                .add("isAlive", true)
                                .toString();
        assertEquals(expectedOutput, actualOutput);
        
        // Edge case with adding null value
        expectedOutput = "TestClass{name=null}";
        actualOutput = Objects.toStringHelper(TestClass.class)
                            .add("name", null)
                            .toString();
        assertEquals(expectedOutput, actualOutput);
    }
    
    // Custom class used for testing toStringHelper method
    class TestClass {
        private String name;
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }
}