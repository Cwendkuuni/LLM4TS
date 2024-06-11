
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    // equal method
    @Test
    public void testEqual_SameObject_ReturnsTrue() {
        Object obj = new Object();
        assertTrue(Objects.equal(obj, obj));
    }
    
    @Test
    public void testEqual_NullObject_ReturnsFalse() {
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));
        assertTrue(Objects.equal(null, null));
    }
    
    @Test
    public void testEqual_EqualNonNullableObjects_ReturnsTrue() {
        assertTrue(Objects.equal("test", "test"));
    }
    
    @Test
    public void testEqual_NotEqualNonNullableObjects_ReturnsFalse() {
        assertFalse(Objects.equal("test1", "test2"));
    }
    
    // hashCode method
    @Test
    public void testHashCode_EmptyArray_ReturnsZero() {
        assertEquals(0, Objects.hashCode());
    }
    
    @Test
    public void testHashCode_NullObject_ReturnsZero() {
        assertEquals(0, Objects.hashCode(null));
    }
    
    @Test
    public void testHashCode_SingleObject_ReturnsCorrectHashCode() {
        assertEquals(123, Objects.hashCode(new Object()));
    }
    
    @Test
    public void testHashCode_MultipleObjects_ReturnsCombinedHashCode() {
        assertEquals(123456, Objects.hashCode(new Object(), new Object()));
    }
    
    // toStringHelper method
    @Test
    public void testToStringHelper_Object_ReturnsToStringWithClassName() {
        Object obj = new Object();
        String expected = "java.lang.Object{}";
        assertEquals(expected, Objects.toStringHelper(obj).toString());
    }
    
    @Test
    public void testToStringHelper_Class_ReturnsToStringWithClassName() {
        String className = "TestClass";
        String expected = "TestClass{}";
        assertEquals(expected, Objects.toStringHelper(className).toString());
    }
    
    // firstNonNull method
    @Test
    public void testFirstNonNull_BothNull_ReturnsNull() {
        assertNull(Objects.firstNonNull(null, null));
    }
    
    @Test
    public void testFirstNonNull_FirstObjectNotNull_ReturnsFirstObject() {
        Object obj = new Object();
        assertEquals(obj, Objects.firstNonNull(obj, null));
    }
    
    @Test
    public void testFirstNonNull_SecondObjectNotNull_ReturnsSecondObject() {
        Object obj = new Object();
        assertEquals(obj, Objects.firstNonNull(null, obj));
    }
    
}

