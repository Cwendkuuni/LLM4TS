package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("", ""));
        assertTrue(Objects.equal("hello", "hello"));
        assertTrue(Objects.equal(1, 1));
        assertTrue(Objects.equal(true, true));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(1, Objects.hashCode(1));
        assertEquals(31, Objects.hashCode("hello"));
        assertEquals(-123, Objects.hashCode(-123.456));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper1 = Objects.toStringHelper(this);
        assertEquals("ObjectsTest{", helper1.toString());
        
        Objects.ToStringHelper helper2 = Objects.toStringHelper(ObjectsTest.class);
        assertEquals("ObjectsTest{", helper2.toString());
        
        Objects.ToStringHelper helper3 = Objects.toStringHelper("ObjectsTest");
        assertEquals("ObjectsTest{", helper3.toString());
        
        helper1.omitNullValues();
        assertEquals("ObjectsTest{", helper1.toString());
        
        helper1.add("key1", "value1");
        assertEquals("ObjectsTest{key1=value1", helper1.toString());
        
        helper1.add("key2", true);
        assertEquals("ObjectsTest{key1=value1, key2=true", helper1.toString());
        
        helper1.addValue(123);
        assertEquals("ObjectsTest{key1=value1, key2=true, 123", helper1.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("hello", Objects.firstNonNull(null, "hello"));
        assertEquals("world", Objects.firstNonNull("world", null));
        assertEquals(123, Objects.firstNonNull(123, null));
        assertEquals(false, Objects.firstNonNull(null, false));
    }
}