package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("foo", "foo"));
        assertTrue(Objects.equal("foo", new String("foo")));
        assertTrue(Objects.equal(new Integer(5), new Integer(5)));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode((Object)null));
        assertEquals(123456, Objects.hashCode(123456));
        assertEquals(987654321, Objects.hashCode("random"));
        assertEquals(246813579, Objects.hashCode(true, false, true));
    }

    @Test
    public void testToStringHelper() {
        assertEquals("class{foo=bar, baz=qux}", Objects.toStringHelper("class").add("foo", "bar").add("baz", "qux").toString());
        assertEquals("className{}", Objects.toStringHelper("className").toString());
        assertEquals("class{foo=bar, baz=qux, xyz=null}", Objects.toStringHelper("class").add("foo", "bar").add("baz", "qux").add("xyz", null).toString());
    }

    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("foo", Objects.firstNonNull(null, "foo"));
        assertEquals("bar", Objects.firstNonNull("bar", null));
        assertEquals("baz", Objects.firstNonNull("baz", "qux"));
    }
}