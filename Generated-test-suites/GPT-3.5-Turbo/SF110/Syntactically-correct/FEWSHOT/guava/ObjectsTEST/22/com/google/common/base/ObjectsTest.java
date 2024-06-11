import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        // Test with two equal objects
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertEquals(true, Objects.equal(obj1, obj2));

        // Test with two different objects
        Object obj3 = new Object();
        Object obj4 = new Object();
        assertEquals(false, Objects.equal(obj3, obj4));

        // Test with null objects
        Object obj5 = null;
        Object obj6 = null;
        assertEquals(true, Objects.equal(obj5, obj6));

        // Test with one null object
        Object obj7 = new Object();
        Object obj8 = null;
        assertEquals(false, Objects.equal(obj7, obj8));
    }

    @Test
    public void testHashCode() {
        // Test with null objects
        Object[] objects1 = null;
        assertEquals(0, Objects.hashCode(objects1));

        // Test with multiple objects
        Object[] objects2 = {new Object(), new Object(), new Object()};
        assertEquals(Objects.hashCode(objects2), Objects.hashCode(objects2));

        // Test with one object
        Object[] objects3 = {new Object()};
        assertEquals(Objects.hashCode(objects3), Objects.hashCode(objects3));

        // Test with empty array
        Object[] objects4 = {};
        assertEquals(Objects.hashCode(objects4), 1);
    }

    @Test
    public void testToStringHelper() {
        // Test with an object
        Object obj1 = new Object();
        Objects.ToStringHelper helper1 = Objects.toStringHelper(obj1);
        assertEquals(obj1.getClass().getSimpleName(), helper1.toString());

        // Test with a class
        Class<?> cls = Object.class;
        Objects.ToStringHelper helper2 = Objects.toStringHelper(cls);
        assertEquals(cls.getSimpleName(), helper2.toString());

        // Test with a string
        String className = "String";
        Objects.ToStringHelper helper3 = Objects.toStringHelper(className);
        assertEquals(className, helper3.toString());
    }

    @Test
    public void testFirstNonNull() {
        // Test with two non-null objects
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertEquals(obj1, Objects.firstNonNull(obj1, obj2));

        // Test with null and non-null object
        Object obj3 = null;
        Object obj4 = new Object();
        assertEquals(obj4, Objects.firstNonNull(obj3, obj4));

        // Test with two null objects
        Object obj5 = null;
        Object obj6 = null;
        assertNull(Objects.firstNonNull(obj5, obj6));
    }
}