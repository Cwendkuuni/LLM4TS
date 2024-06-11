package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        assertEquals(true, Objects.equal(null, null));
        assertEquals(false, Objects.equal(null, ""));
        assertEquals(false, Objects.equal("", null));
        assertEquals(true, Objects.equal("", ""));
        assertEquals(true, Objects.equal("foo", "foo"));
        assertEquals(false, Objects.equal("foo", "bar"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(123, Objects.hashCode(123));
        assertEquals(123456, Objects.hashCode(123, 456));
        assertEquals(123456789, Objects.hashCode(123, 456, 789));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper(this);
        assertEquals("ObjectsTest{", helper.toString());
        
        helper.add("name", "John");
        assertEquals("ObjectsTest{name=John}", helper.toString());
        
        helper.addValue(25);
        assertEquals("ObjectsTest{name=John, 25}", helper.toString());
        
        helper.add("active", true);
        assertEquals("ObjectsTest{name=John, 25, active=true}", helper.toString());
        
        helper.omitNullValues();
        assertEquals("ObjectsTest{name=John, 25, active=true}", helper.toString());
        
        helper.add("address", null);
        assertEquals("ObjectsTest{name=John, 25, active=true}", helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertEquals("foo", Objects.firstNonNull("foo", "bar"));
        assertEquals("bar", Objects.firstNonNull(null, "bar"));
        assertEquals("baz", Objects.firstNonNull(null, null, "baz"));
        assertNull(Objects.firstNonNull());
    }
}