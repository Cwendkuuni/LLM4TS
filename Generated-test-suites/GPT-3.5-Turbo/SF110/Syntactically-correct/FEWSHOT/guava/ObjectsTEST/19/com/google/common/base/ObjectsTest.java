package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Nullable;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("", ""));
        assertTrue(Objects.equal("foo", "foo"));
        assertTrue(Objects.equal(new Object(), new Object()));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode((Object[]) null));
        assertEquals(1, Objects.hashCode((Object) null));
        assertEquals(112994, Objects.hashCode("foo", "bar", "baz"));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        assertEquals("Test{}", helper.toString());

        helper.add("name", "John");
        helper.addValue("Doe");
        assertEquals("Test{name=John, Doe}", helper.toString());

        helper.add("age", 25);
        helper.addValue(true);
        assertEquals("Test{name=John, Doe, age=25, true}", helper.toString());
    }

    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("", Objects.firstNonNull("", null));
        assertEquals("foo", Objects.firstNonNull(null, "foo"));
        assertEquals("bar", Objects.firstNonNull("bar", "foo"));
    }

}