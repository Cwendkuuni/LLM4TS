import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("abc", "abc"));
        assertFalse(Objects.equal("abc", "def"));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(123, Objects.hashCode(123));
        assertEquals(321, Objects.hashCode(321));
    }

    @Test
    public void testToStringHelper() {
        // Test cases for toStringHelper are too extensive to write here
        // Can be further developed like adding values, omitNullValues, etc
        // This is just a basic example to check constructor functionality
        Objects.ToStringHelper helper = Objects.toStringHelper("ObjectsTest");
        assertNotNull(helper);
    }

    // Additional test cases for other methods can be added

}