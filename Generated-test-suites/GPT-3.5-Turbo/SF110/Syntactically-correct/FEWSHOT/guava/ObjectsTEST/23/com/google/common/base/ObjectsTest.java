
package com.google.common.base;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test when a and b are the same object
        assertTrue(Objects.equal("foo", "foo"));

        // Test when a is null and b is null
        assertTrue(Objects.equal(null, null));

        // Test when a is null and b is not null
        assertFalse(Objects.equal(null, "foo"));

        // Test when a is not null and b is null
        assertFalse(Objects.equal("foo", null));

        // Test when a and b are different objects
        assertFalse(Objects.equal("foo", "bar"));
    }

    @Test
    public void testHashCode() {
        // Test when objects is null
        assertEquals(0, Objects.hashCode(null));

        // Test when objects is empty
        assertEquals(1, Objects.hashCode());

        // Test when objects contain a single object
        assertEquals(Objects.hashCode("foo"), Objects.hashCode("foo"));

        // Test when objects contain multiple objects
        assertEquals(Objects.hashCode("foo", "bar"), Objects.hashCode("foo", "bar"));
        assertNotEquals(Objects.hashCode("foo", "bar"), Objects.hashCode("bar", "foo"));
    }

    @Test
    public void testToStringHelper() {
        // Test when self is not null
        assertNotNull(Objects.toStringHelper("foo"));

        // Test when self is null
        assertNotNull(Objects.toStringHelper((Object) null));

        // Test add and addValue methods
        assertEquals("ClassName{foo=bar, baz=qux}", Objects.toStringHelper("ClassName")
                .add("foo", "bar")
                .add("baz", "qux")
                .toString());

        // Test addValue methods
        assertEquals("ClassName{foo, baz}", Objects.toStringHelper("ClassName")
                .addValue("foo")
                .addValue("baz")
                .toString());

        // Test omitNullValues method
        assertEquals("ClassName{foo=bar}", Objects.toStringHelper("ClassName")
                .omitNullValues()
                .add("foo", "bar")
                .add("baz", null)
                .toString());
    }

    @Test
    public void testFirstNonNull() {
        // Test when first is not null
        assertEquals("foo", Objects.firstNonNull("foo", "bar"));

        // Test when first is null
        assertEquals("bar", Objects.firstNonNull(null, "bar"));

        // Test when second is null
        assertEquals("foo", Objects.firstNonNull("foo", null));

        // Test when both first and second are null
        assertNull(Objects.firstNonNull(null, null));
    }
}
