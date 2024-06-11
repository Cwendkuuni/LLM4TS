import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Typical use case: objects are equal
        assertTrue(Objects.equal("hello", "hello"));
        
        // Typical use case: objects are not equal
        assertFalse(Objects.equal("hello", "world"));
        
        // Edge case: both objects are null
        assertTrue(Objects.equal(null, null));
        
        // Error scenario: one object is null and the other is not
        assertFalse(Objects.equal(null, "hello"));
    }
    
    @Test
    public void testHashCode() {
        // Typical use case: array of objects
        Object[] objects1 = {1, 2, 3};
        Object[] objects2 = {1, 2, 3};
        assertEquals(Objects.hashCode(objects1), Objects.hashCode(objects2));
        
        // Typical use case: single object
        Object[] object = {1};
        assertEquals(Objects.hashCode(object), Objects.hashCode(1));
        
        // Edge case: empty array
        Object[] empty = {};
        assertEquals(Objects.hashCode(empty), 0);
    }
    
    @Test
    @Deprecated
    public void testToStringHelper() {
        // Typical use case: class name provided
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("ClassName");
        Object[] objects = {1, 2, 3};
        assertEquals("ClassName{1, 2, 3}", toStringHelper.addValue(objects).toString());
        
        // Typical use case: object provided
        Objects.ToStringHelper toStringHelper1 = Objects.toStringHelper(new Object());
        assertEquals("Object{}", toStringHelper1.toString());
        
        // Edge case: class provided
        Objects.ToStringHelper toStringHelper2 = Objects.toStringHelper(ObjectsTest.class);
        assertEquals("ObjectsTest{}", toStringHelper2.toString());
    }
    
    @Test
    @Deprecated
    public void testFirstNonNull() {
        // Typical use case: first object is not null, second object is null
        assertEquals("hello", Objects.firstNonNull("hello", null));
        
        // Typical use case: first object is null, second object is not null
        assertEquals("world", Objects.firstNonNull(null, "world"));
        
        // Typical use case: both objects are null
        assertNull(Objects.firstNonNull(null, null));
        
        // Error scenario: both objects are null, but second object is expected to be non-null
        assertNotNull(Objects.firstNonNull(null, null));
    }
}