package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("foo", "foo"));
        assertFalse(Objects.equal("foo", "bar"));
        assertFalse(Objects.equal(null, "foo"));
        assertFalse(Objects.equal("foo", null));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(1, Objects.hashCode(null));
        assertEquals(31, Objects.hashCode("foo"));
        assertEquals(961, Objects.hashCode("foo", "bar"));
        assertEquals(29791, Objects.hashCode("foo", "bar", "baz"));
        assertEquals(-862545276, Objects.hashCode("foo", null, "baz"));
    }
    
    @Test
    @Deprecated
    public void testToStringHelper_Object() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
        assertNotNull(helper);
    }
    
    @Test
    @Deprecated
    public void testToStringHelper_Class() {
        Objects.ToStringHelper helper = Objects.toStringHelper(Integer.class);
        assertNotNull(helper);
    }
    
    @Test
    @Deprecated
    public void testToStringHelper_String() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Class");
        assertNotNull(helper);
    }
    
    @Test
    @Deprecated
    public void testFirstNonNull() {
        assertEquals("foo", Objects.firstNonNull("foo", null));
        assertEquals("bar", Objects.firstNonNull(null, "bar"));
        assertNull(Objects.firstNonNull(null, null));
    }
}