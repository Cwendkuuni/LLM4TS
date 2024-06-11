
package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.Arrays;

public class ObjectsTest {
    @Test
    public void testEqual() {
        // Test equal objects
        assertEquals(true, Objects.equal("foo", "foo"));
        assertEquals(true, Objects.equal(null, null));

        // Test unequal objects
        assertEquals(false, Objects.equal("foo", "bar"));
        assertEquals(false, Objects.equal("foo", null));
        assertEquals(false, Objects.equal(null, "bar"));
    }

    @Test
    public void testHashCode() {
        // Test no objects
        assertEquals(0, Objects.hashCode());

        // Test single object
        assertEquals("foo".hashCode(), Objects.hashCode("foo"));

        // Test multiple objects
        assertEquals(Arrays.hashCode(new Object[]{"foo", "bar", "baz"}), Objects.hashCode("foo", "bar", "baz"));
    }

    @Test
    public void testToStringHelper() {
        // Create ToStringHelper object
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("TestClass");

        // Test add methods
        toStringHelper.add("name", "John");
        assertEquals("TestClass{name=John}", toStringHelper.toString());

        toStringHelper.add("age", 25);
        assertEquals("TestClass{name=John, age=25}", toStringHelper.toString());

        toStringHelper.add("isStudent", true);
        assertEquals("TestClass{name=John, age=25, isStudent=true}", toStringHelper.toString());

        // Test addValue methods
        toStringHelper.addValue(5.6);
        assertEquals("TestClass{name=John, age=25, isStudent=true, 5.6}", toStringHelper.toString());

        toStringHelper.addValue('A');
        assertEquals("TestClass{name=John, age=25, isStudent=true, 5.6, A}", toStringHelper.toString());

        toStringHelper.addValue(null);
        assertEquals("TestClass{name=John, age=25, isStudent=true, 5.6, A, null}", toStringHelper.toString());

        toStringHelper.addValue(false);
        assertEquals("TestClass{name=John, age=25, isStudent=true, 5.6, A, null, false}", toStringHelper.toString());
    }

    @Test
    public void testFirstNonNull() {
        // Test both values non-null
        assertEquals("foo", Objects.firstNonNull("foo", "bar"));

        // Test first value null
        assertEquals("bar", Objects.firstNonNull(null, "bar"));

        // Test second value null
        assertEquals("foo", Objects.firstNonNull("foo", null));

        // Test both values null
        assertNull(Objects.firstNonNull(null, null));
    }
}
