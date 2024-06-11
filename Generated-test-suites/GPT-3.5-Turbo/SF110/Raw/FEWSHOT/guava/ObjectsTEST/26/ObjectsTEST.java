package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        assertTrue(Objects.equals(null, null));
        assertEquals(false, Objects.equals(null, new Object()));
        assertEquals(false, Objects.equals(new Object(), null));
        assertEquals(true, Objects.equals("hello", "hello"));
        assertEquals(false, Objects.equals("hello", "world"));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(1, Objects.hashCode(null));
        assertEquals(31, Objects.hashCode(1));
        assertEquals(961, Objects.hashCode(1, 2, 3));
        assertEquals(961, Objects.hashCode(3, 2, 1));
        assertEquals(19531, Objects.hashCode("hello", "world"));
    }

    @Test
    public void testToStringHelper() {
        assertNotNull(Objects.toStringHelper(new Object()));
        assertNotNull(Objects.toStringHelper(Object.class));
        assertNotNull(Objects.toStringHelper("ClassName"));

        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(new Object());
        assertEquals("ClassName{}", toStringHelper.toString());

        toStringHelper.add("name", "Alice");
        toStringHelper.addValue(true);
        toStringHelper.addValue(10);
        toStringHelper.addValue(3.14);
        toStringHelper.addValue('c');
        assertEquals("ClassName{name=Alice, true, 10, 3.14, c}", toStringHelper.toString());
    }

    @Test
    public void testFirstNonNull() {
        assertEquals("hello", Objects.firstNonNull(null, "hello"));
        assertEquals("world", Objects.firstNonNull("world", "hello"));
        assertEquals(10, Objects.firstNonNull(null, 10));
        assertEquals(20, Objects.firstNonNull(20, 10));
    }
}