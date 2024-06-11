
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        // test case 1: both objects are null
        Object a1 = null;
        Object b1 = null;
        assertTrue(Objects.equal(a1, b1));
        
        // test case 2: one object is null and the other is not
        Object a2 = null;
        Object b2 = new Object();
        assertFalse(Objects.equal(a2, b2));
        
        // test case 3: both objects are not null and they are equal
        Object a3 = new Object();
        Object b3 = a3;
        assertTrue(Objects.equal(a3, b3));
        
        // test case 4: both objects are not null and they are not equal
        Object a4 = new Object();
        Object b4 = new Object();
        assertFalse(Objects.equal(a4, b4));
    }
    
    @Test
    public void testHashCode() {
        // test case 1: array of objects is null
        Object[] objects1 = null;
        int expectedHash1 = 0;
        assertEquals(expectedHash1, Objects.hashCode(objects1));
        
        // test case 2: array of objects is empty
        Object[] objects2 = new Object[0];
        int expectedHash2 = 1;
        assertEquals(expectedHash2, Objects.hashCode(objects2));
        
        // test case 3: array of objects has a single element
        Object[] objects3 = { new Object() };
        int expectedHash3 = objects3[0].hashCode();
        assertEquals(expectedHash3, Objects.hashCode(objects3));
        
        // test case 4: array of objects has multiple elements
        Object[] objects4 = { new Object(), new Object(), new Object() };
        int expectedHash4 = Arrays.hashCode(objects4);
        assertEquals(expectedHash4, Objects.hashCode(objects4));
    }
    
    @Test
    public void testToStringHelper() {
        // test case 1: creating a helper with an object
        Object obj1 = new Object();
        Objects.ToStringHelper helper1 = Objects.toStringHelper(obj1);
        assertEquals(obj1.getClass().getSimpleName(), helper1.toString());
        
        // test case 2: creating a helper with a class
        Class<?> clazz2 = Object.class;
        Objects.ToStringHelper helper2 = Objects.toStringHelper(clazz2);
        assertEquals(clazz2.getSimpleName(), helper2.toString());
        
        // test case 3: creating a helper with a string
        String className3 = "TestClass";
        Objects.ToStringHelper helper3 = Objects.toStringHelper(className3);
        assertEquals(className3, helper3.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        // test case 1: first object is not null
        Integer first1 = 1;
        Integer second1 = 2;
        assertEquals(first1, Objects.firstNonNull(first1, second1));
        
        // test case 2: first object is null
        Integer first2 = null;
        Integer second2 = 2;
        assertEquals(second2, Objects.firstNonNull(first2, second2));
        
        // test case 3: both objects are null
        Integer first3 = null;
        Integer second3 = null;
        assertNull(Objects.firstNonNull(first3, second3));
    }
}
