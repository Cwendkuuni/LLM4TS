
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test equal objects
        assertTrue(Objects.equal("hello", "hello"));

        // Test unequal objects
        assertFalse(Objects.equal("hello", "world"));

        // Test null objects
        assertTrue(Objects.equal(null, null));

        assertFalse(Objects.equal("hello", null));
        assertFalse(Objects.equal(null, "hello"));
    }

    @Test
    public void testHashCode() {
        // Test empty array
        assertEquals(0, Objects.hashCode());

        // Test single object
        assertEquals(123, Objects.hashCode(123));

        // Test multiple objects
        assertEquals(499577, Objects.hashCode("hello", true, 3.14));

        // Test null objects
        assertEquals(0, Objects.hashCode(null));
        assertEquals(246, Objects.hashCode(null, 123));
        assertEquals(246, Objects.hashCode(123, null));
    }

    @Test
    public void testToStringHelper() {
        // Test add method with name and value
        assertEquals("Objects.ToStringHelper{test=123}", Objects.toStringHelper("test").add("name", 123).toString());

        // Test add method with boolean value
        assertEquals("Objects.ToStringHelper{test=true}", Objects.toStringHelper("test").add("name", true).toString());

        // Test add method with char value
        assertEquals("Objects.ToStringHelper{test=a}", Objects.toStringHelper("test").add("name", 'a').toString());

        // Test add method with double value
        assertEquals("Objects.ToStringHelper{test=3.14}", Objects.toStringHelper("test").add("name", 3.14).toString());

        // Test add method with float value
        assertEquals("Objects.ToStringHelper{test=3.14}", Objects.toStringHelper("test").add("name", 3.14f).toString());

        // Test add method with int value
        assertEquals("Objects.ToStringHelper{test=123}", Objects.toStringHelper("test").add("name", 123).toString());

        // Test add method with long value
        assertEquals("Objects.ToStringHelper{test=123}", Objects.toStringHelper("test").add("name", 123L).toString());

        // Test addValue method with object value
        assertEquals("Objects.ToStringHelper{test=123}", Objects.toStringHelper("test").addValue(123).toString());

        // Test addValue method with boolean value
        assertEquals("Objects.ToStringHelper{test=true}", Objects.toStringHelper("test").addValue(true).toString());

        // Test addValue method with char value
        assertEquals("Objects.ToStringHelper{test=a}", Objects.toStringHelper("test").addValue('a').toString());

        // Test addValue method with double value
        assertEquals("Objects.ToStringHelper{test=3.14}", Objects.toStringHelper("test").addValue(3.14).toString());

        // Test addValue method with float value
        assertEquals("Objects.ToStringHelper{test=3.14}", Objects.toStringHelper("test").addValue(3.14f).toString());

        // Test addValue method with int value
        assertEquals("Objects.ToStringHelper{test=123}", Objects.toStringHelper("test").addValue(123).toString());

        // Test addValue method with long value
        assertEquals("Objects.ToStringHelper{test=123}", Objects.toStringHelper("test").addValue(123L).toString());

        // Test omitNullValues method
        assertEquals("Objects.ToStringHelper{test=123}", Objects.toStringHelper("test").omitNullValues().add("name", null).addValue(123).toString());
    }

    @Test
    public void testFirstNonNull() {
        // Test non-null first argument
        assertEquals("hello", Objects.firstNonNull("hello", "world"));
        assertEquals(123, Objects.firstNonNull(123, 456));

        // Test null first argument
        assertEquals("world", Objects.firstNonNull(null, "world"));
        assertEquals(456, Objects.firstNonNull(null, 456));
    }
}
