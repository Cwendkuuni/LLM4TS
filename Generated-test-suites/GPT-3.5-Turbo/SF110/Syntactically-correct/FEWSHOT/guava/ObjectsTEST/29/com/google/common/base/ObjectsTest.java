package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal("foo", null));
        assertFalse(Objects.equal(null, "foo"));
        assertTrue(Objects.equal("foo", "foo"));
        assertFalse(Objects.equal("foo", "bar"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(0, Objects.hashCode(null));
        assertEquals(31, Objects.hashCode("foo"));
        assertEquals(31 + 32, Objects.hashCode("foo", "bar"));
    }
    
    // Test toStringHelper() methods
    @Test
    public void testToStringHelper_Object() {
        Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
        assertEquals("Object{}", helper.toString());
    }
    
    @Test
    public void testToStringHelper_Class() {
        Objects.ToStringHelper helper = Objects.toStringHelper(Object.class);
        assertEquals("Object{}", helper.toString());
    }
    
    @Test
    public void testToStringHelper_String() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
        assertEquals("ClassName{}", helper.toString());
    }
    
    // Test firstNonNull() method
    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("foo", Objects.firstNonNull("foo", null));
        assertEquals("bar", Objects.firstNonNull(null, "bar"));
        assertEquals("foo", Objects.firstNonNull("foo", "bar"));
    }
}