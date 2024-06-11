package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("", ""));
        assertTrue(Objects.equal("foo", "foo"));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(1, Objects.hashCode(null));
        assertEquals(31, Objects.hashCode("foo"));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("className");
        assertNotNull(helper);
    }

    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertNull(Objects.firstNonNull(null, ""));
        assertEquals("foo", Objects.firstNonNull("foo", null));
    }
}