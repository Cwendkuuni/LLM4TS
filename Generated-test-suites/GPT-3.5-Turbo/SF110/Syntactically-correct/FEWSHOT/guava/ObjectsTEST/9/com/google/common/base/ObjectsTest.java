package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        assertEquals(true, Objects.equal(null, null));
        assertEquals(false, Objects.equal(null, "foo"));
        assertEquals(false, Objects.equal("foo", null));
        assertEquals(true, Objects.equal("foo", "foo"));
        assertEquals(false, Objects.equal("foo", "bar"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(1, Objects.hashCode(1));
        assertEquals(31, Objects.hashCode(31));
        assertEquals(31 * 31, Objects.hashCode(31, 31));
        assertEquals(31 * 31 * 31, Objects.hashCode(31, 31, 31));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        assertEquals("Test{}", helper.toString());
        
        helper.add("name", "John");
        assertEquals("Test{name=John}", helper.toString());
        
        helper.addValue(true);
        assertEquals("Test{name=John, true}", helper.toString());
        
        helper.add("age", 30);
        assertEquals("Test{name=John, true, age=30}", helper.toString());
        
        helper.omitNullValues();
        assertEquals("Test{name=John, true, age=30}", helper.toString());
        
        helper.add("address", null);
        assertEquals("Test{name=John, true, age=30}", helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertEquals("foo", Objects.firstNonNull(null, "foo"));
        assertEquals("bar", Objects.firstNonNull("bar", null));
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("test", Objects.firstNonNull("test", "test"));
    }
}