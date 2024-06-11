
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test when both objects are null
        assertTrue(Objects.equal(null, null));
        
        // Test when one object is null and the other is not
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));
        
        // Test when both objects are not null and are equal
        assertTrue(Objects.equal(new Object(), new Object()));
        
        // Test when both objects are not null but not equal
        assertFalse(Objects.equal(new Object(), new Object()));
    }

    @Test
    public void testHashCode() {
        // Test when objects array is empty
        assertEquals(0, Objects.hashCode());

        // Test when objects array contains one object
        Object obj = new Object();
        assertEquals(Objects.hashCode(obj), obj.hashCode());
        
        // Test when objects array contains multiple objects
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertEquals(Objects.hashCode(obj1, obj2), Arrays.hashCode(new Object[] {obj1, obj2}));
    }

    @Test
    public void testToStringHelper() {
        // Test toStringHelper() methods
        assertNotNull(Objects.toStringHelper(new Object()));
        
        assertNotNull(Objects.toStringHelper(Object.class));
        
        assertNotNull(Objects.toStringHelper("className"));
    }

    @Test
    public void testFirstNonNull() {
        // Test when both objects are null
        assertNull(Objects.firstNonNull(null, null));
        
        // Test when first object is null and second object is not null
        Object obj2 = new Object();
        assertSame(obj2, Objects.firstNonNull(null, obj2));
        
        // Test when first object is not null and second object is null
        Object obj1 = new Object();
        assertSame(obj1, Objects.firstNonNull(obj1, null));
        
        // Test when both objects are not null
        Object obj3 = new Object();
        assertSame(obj1, Objects.firstNonNull(obj1, obj3));
    }

}
