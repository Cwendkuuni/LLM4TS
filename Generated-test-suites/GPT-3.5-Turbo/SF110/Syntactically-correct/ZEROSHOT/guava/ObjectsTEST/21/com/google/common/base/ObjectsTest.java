import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    // Test equal method
    @Test
    public void testEqual() {
        // Test case 1: a and b are null
        Object a1 = null;
        Object b1 = null;
        assertTrue(Objects.equal(a1, b1));
        
        // Test case 2: a is null, b is not null
        Object a2 = null;
        Object b2 = new Object();
        assertFalse(Objects.equal(a2, b2));
        
        // Test case 3: a and b are the same object
        Object obj = new Object();
        Object a3 = obj;
        Object b3 = obj;
        assertTrue(Objects.equal(a3, b3));
        
        // Test case 4: a and b are different objects but equal
        Integer a4 = new Integer(10);
        Integer b4 = new Integer(10);
        assertTrue(Objects.equal(a4, b4));
        
        // Test case 5: a and b are different objects and not equal
        Integer a5 = new Integer(10);
        Integer b5 = new Integer(20);
        assertFalse(Objects.equal(a5, b5));
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        // Test case 1: objects is null
        Object[] objects1 = null;
        int hashCode1 = Objects.hashCode(objects1);
        assertEquals(0, hashCode1);
        
        // Test case 2: objects is an empty array
        Object[] objects2 = new Object[0];
        int hashCode2 = Objects.hashCode(objects2);
        assertEquals(1, hashCode2);
        
        // Test case 3: objects contains one element
        Object[] objects3 = new Object[1];
        objects3[0] = new Integer(10);
        int hashCode3 = Objects.hashCode(objects3);
        assertEquals(Objects.hash(objects3[0]), hashCode3);
        
        // Test case 4: objects contains multiple elements
        Object[] objects4 = new Object[3];
        objects4[0] = new Integer(10);
        objects4[1] = new String("test");
        objects4[2] = new Double(3.14);
        int hashCode4 = Objects.hashCode(objects4);
        assertEquals(Objects.hash(objects4[0], objects4[1], objects4[2]), hashCode4);
    }

    // Test toStringHelper methods
    @Test
    public void testToStringHelper() {
        // Test case 1: toStringHelper with an object
        Object obj1 = new Object();
        Objects.ToStringHelper helper1 = Objects.toStringHelper(obj1);
        assertNotNull(helper1);
        
        // Test case 2: toStringHelper with a class
        Class<?> clazz2 = this.getClass();
        Objects.ToStringHelper helper2 = Objects.toStringHelper(clazz2);
        assertNotNull(helper2);
        
        // Test case 3: toStringHelper with a className
        String className3 = "ClassName";
        Objects.ToStringHelper helper3 = Objects.toStringHelper(className3);
        assertNotNull(helper3);
        
        // Test case 4: add method
        Object obj4 = new Object();
        Objects.ToStringHelper helper4 = Objects.toStringHelper(obj4);
        helper4.add("name", "value");
        assertEquals("ClassName{name=value}", helper4.toString());
        
        // Test case 5: addValue method
        Object obj5 = new Object();
        Objects.ToStringHelper helper5 = Objects.toStringHelper(obj5);
        helper5.addValue("value");
        assertEquals("ClassName{value}", helper5.toString());
    }

    // Test firstNonNull method
    @Test
    public void testFirstNonNull() {
        // Test case 1: first and second are null
        Object first1 = null;
        Object second1 = null;
        Object result1 = Objects.firstNonNull(first1, second1);
        assertNull(result1);
        
        // Test case 2: first is null, second is not null
        Object first2 = null;
        Object second2 = new Object();
        Object result2 = Objects.firstNonNull(first2, second2);
        assertSame(second2, result2);
        
        // Test case 3: first is not null, second is null
        Object first3 = new Object();
        Object second3 = null;
        Object result3 = Objects.firstNonNull(first3, second3);
        assertSame(first3, result3);
        
        // Test case 4: first and second are not null
        Object first4 = new Object();
        Object second4 = new Object();
        Object result4 = Objects.firstNonNull(first4, second4);
        assertSame(first4, result4);
    }
}