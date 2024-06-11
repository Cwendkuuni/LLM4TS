import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Objects;

public class ObjectsTest {

    @Test
    public void testEqual_typical() {
        // Typical use case: a and b are equal objects
        Object a = new Object();
        Object b = a;
        assertTrue(Objects.equal(a, b));
    }
    
    @Test
    public void testEqual_nullObject() {
        // Edge case: a and b are null objects
        Object a = null;
        Object b = null;
        assertTrue(Objects.equal(a, b));
    }
    
    @Test
    public void testEqual_differentObjects() {
        // Error scenario: a and b are different objects
        Object a = new Object();
        Object b = new Object();
        assertFalse(Objects.equal(a, b));
    }
    
    @Test
    public void testHashCode_typical() {
        // Typical use case: multiple objects with different values
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        int hashCodeA = Objects.hashCode(a);
        int hashCodeB = Objects.hashCode(b);
        int hashCodeC = Objects.hashCode(c);
        assertNotEquals(hashCodeA, hashCodeB);
        assertNotEquals(hashCodeB, hashCodeC);
        assertNotEquals(hashCodeA, hashCodeC);
    }
    
    @Test
    public void testHashCode_nullObjects() {
        // Edge case: multiple null objects
        Object a = null;
        Object b = null;
        int hashCodeA = Objects.hashCode(a);
        int hashCodeB = Objects.hashCode(b);
        assertEquals(0, hashCodeA);
        assertEquals(0, hashCodeB);
    }
    
    @Test
    public void testToStringHelper_typical() {
        // Typical use case: create ToStringHelper and add values
        Object obj = new Object();
        ToStringHelper helper = Objects.toStringHelper(obj);
        helper.add("name", "Alice");
        helper.add("age", 25);
        String expectedString = obj.getClass().getSimpleName() + "{name=Alice, age=25}";
        assertEquals(expectedString, helper.toString());
    }
    
    @Test
    public void testToStringHelper_omitNullValues() {
        // Edge case: create ToStringHelper and add values with omitNullValues enabled
        Object obj = new Object();
        ToStringHelper helper = Objects.toStringHelper(obj).omitNullValues();
        helper.add("name", "Bob");
        helper.add("age", null);
        String expectedString = obj.getClass().getSimpleName() + "{name=Bob}";
        assertEquals(expectedString, helper.toString());
    }
    
    @Test
    public void testToStringHelper_addValue_typical() {
        // Typical use case: create ToStringHelper and add single value
        Object obj = new Object();
        ToStringHelper helper = Objects.toStringHelper(obj);
        helper.addValue("Alice");
        String expectedString = obj.getClass().getSimpleName() + "{Alice}";
        assertEquals(expectedString, helper.toString());
    }
    
    @Test
    public void testToStringHelper_addValue_nullValue() {
        // Edge case: create ToStringHelper and add null value
        Object obj = new Object();
        ToStringHelper helper = Objects.toStringHelper(obj);
        helper.addValue(null);
        String expectedString = obj.getClass().getSimpleName() + "{}";
        assertEquals(expectedString, helper.toString());
    }
    
    @Test
    public void testFirstNonNull_typical() {
        // Typical use case: first value is non-null, so it should be returned
        String first = "Hello";
        String second = "World";
        String result = Objects.firstNonNull(first, second);
        assertEquals(first, result);
    }
    
    @Test
    public void testFirstNonNull_nullFirstValue() {
        // Edge case: first value is null, so the second non-null value should be returned
        String first = null;
        String second = "World";
        String result = Objects.firstNonNull(first, second);
        assertEquals(second, result);
    }
    
    @Test
    public void testFirstNonNull_bothNull() {
        // Error scenario: both values are null, so null should be returned
        String first = null;
        String second = null;
        String result = Objects.firstNonNull(first, second);
        assertNull(result);
    }
}