import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    // Test case for equal method
    @Test
    public void testEqual() {
        // Typical use case: two equal objects
        assertTrue(Objects.equal("hello", "hello"));
        
        // Typical use case: two different objects
        assertFalse(Objects.equal("hello", "world"));
        
        // Edge case: both objects are null
        assertTrue(Objects.equal(null, null));
        
        // Edge case: one object is null
        assertFalse(Objects.equal("hello", null));
        assertFalse(Objects.equal(null, "hello"));
    }

    // Test case for hashCode method
    @Test
    public void testHashCode() {
        // Typical use case: multiple objects
        assertEquals(Objects.hashCode("hello", "world"), Objects.hashCode("hello", "world"));
        
        // Edge case: single object
        assertEquals(Objects.hashCode("hello"), Objects.hashCode("hello"));
        
        // Edge case: empty object
        assertEquals(Objects.hashCode(), 0);
    }

    // Test case for toStringHelper methods
    @Test
    public void testToStringHelper() {
        // Typical use case: add values to ToStringHelper
        String expectedToString = "ObjectsTest{a=1, b=2}";
        String actualToString = Objects.toStringHelper(this.getClass())
            .add("a", 1)
            .add("b", 2)
            .toString();
        assertEquals(expectedToString, actualToString);
        
        // Edge case: omit null values
        expectedToString = "ObjectsTest{a=1}";
        actualToString = Objects.toStringHelper(this.getClass())
            .add("a", 1)
            .add("b", null)
            .omitNullValues()
            .toString();
        assertEquals(expectedToString, actualToString);
        
        // Edge case: add single value
        expectedToString = "ObjectsTest{a=1}";
        actualToString = Objects.toStringHelper(this.getClass())
            .addValue(1)
            .toString();
        assertEquals(expectedToString, actualToString);
        
        // Edge case: empty class name
        expectedToString = "{a=1}";
        actualToString = Objects.toStringHelper("")
            .add("a", 1)
            .toString();
        assertEquals(expectedToString, actualToString);
    }

    // Test case for firstNonNull method
    @Test
    public void testFirstNonNull() {
        // Typical use case: first object is not null
        assertEquals("hello", Objects.firstNonNull("hello", "world"));
        
        // Typical use case: first object is null
        assertEquals("world", Objects.firstNonNull(null, "world"));
        
        // Edge case: both objects are null
        assertNull(Objects.firstNonNull(null, null));
    }

}