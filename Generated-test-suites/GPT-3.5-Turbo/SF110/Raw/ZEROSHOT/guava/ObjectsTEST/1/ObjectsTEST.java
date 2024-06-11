import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test when both objects are null
        assertTrue(Objects.equal(null, null));
        
        // Test when one object is null and the other is not
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));
        
        // Test when both objects are not null but are different objects
        assertFalse(Objects.equal(new Object(), new Object()));
        
        // Test when both objects are not null and are the same object
        Object obj = new Object();
        assertTrue(Objects.equal(obj, obj));
        
        // Test when both objects are equal
        String str1 = "Hello";
        String str2 = new String("Hello");
        assertTrue(Objects.equal(str1, str2));
        
        // Test when both objects are not equal
        assertFalse(Objects.equal("Hello", "World"));
    }
    
    @Test
    public void testHashCode() {
        // Test when no objects are provided
        assertEquals(Objects.hashCode(), 1);
        
        // Test when one object is provided
        assertEquals(Objects.hashCode(new Object()), Arrays.hashCode(new Object[]{new Object()}));
        
        // Test when multiple objects are provided
        assertEquals(Objects.hashCode(new Object(), new Object()), Arrays.hashCode(new Object[]{new Object(), new Object()}));
    }
    
    @Test
    public void testToStringHelper() {
        // Test toStringHelper with Object parameter
        Objects.ToStringHelper helper1 = Objects.toStringHelper(new Object());
        assertNotNull(helper1);
        
        // Test toStringHelper with Class parameter
        Objects.ToStringHelper helper2 = Objects.toStringHelper(Object.class);
        assertNotNull(helper2);
        
        // Test toStringHelper with String parameter
        Objects.ToStringHelper helper3 = Objects.toStringHelper("Object");
        assertNotNull(helper3);
    }
    
    @Test
    public void testFirstNonNull() {
        // Test when both objects are null
        assertNull(Objects.firstNonNull(null, null));
        
        // Test when one object is null and the other is not
        Object obj = new Object();
        assertEquals(obj, Objects.firstNonNull(null, obj));
        assertEquals(obj, Objects.firstNonNull(obj, null));
        
        // Test when both objects are not null
        assertEquals(obj, Objects.firstNonNull(obj, obj));
        
        // Test when both objects are not null but are different
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertEquals(obj1, Objects.firstNonNull(obj1, obj2));
    }
}