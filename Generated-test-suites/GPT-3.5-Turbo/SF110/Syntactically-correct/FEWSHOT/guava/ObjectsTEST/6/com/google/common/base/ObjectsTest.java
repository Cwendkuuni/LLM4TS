
package com.google.common.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        assertEquals(true, Objects.equal(null, null));
        assertEquals(true, Objects.equal("foo", "foo"));
        assertEquals(true, Objects.equal("foo", new String("foo")));
        assertEquals(false, Objects.equal("foo", "bar"));
        assertEquals(false, Objects.equal(null, "foo"));
        assertEquals(false, Objects.equal("foo", null));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(0, Objects.hashCode(null));
        assertEquals(123, Objects.hashCode(123));
        assertEquals(321, Objects.hashCode(321));
        assertEquals(123456789, Objects.hashCode(123, 456, 789));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Class");
        assertEquals("Class{}", helper.toString());
        
        helper.addValue("value");
        assertEquals("Class{value}", helper.toString());
        
        helper.add("name", "John");
        assertEquals("Class{value, name=John}", helper.toString());
        
        helper.add("age", 25);
        assertEquals("Class{value, name=John, age=25}", helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertEquals("foo", Objects.firstNonNull("foo", "bar"));
        assertEquals("foo", Objects.firstNonNull(null, "foo"));
        assertEquals("bar", Objects.firstNonNull("bar", null));
        assertEquals(null, Objects.firstNonNull(null, null));
    }
}
