package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal("foo", null));
        assertFalse(Objects.equal(null, "bar"));
        assertTrue(Objects.equal("foo", "foo"));
        assertFalse(Objects.equal("foo", "bar"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(0, Objects.hashCode(null));
        assertEquals(123, Objects.hashCode(123));
        assertEquals(1234567890, Objects.hashCode(123, 456, 789, 0));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        assertEquals("TestClass{}", helper.toString());

        helper.addValue(true);
        assertEquals("TestClass{true}", helper.toString());

        helper.add("name", "John");
        assertEquals("TestClass{true, name=John}", helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("foo", Objects.firstNonNull("foo", null));
        assertEquals("bar", Objects.firstNonNull(null, "bar"));
        assertEquals("foo", Objects.firstNonNull("foo", "bar"));
    }
}