package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    // Test cases for equal method
    @Test
    public void testEqual() {
        // Typical use case: equal objects
        assertTrue(Objects.equal("Hello", "Hello"));
        
        // Typical use case: not equal objects
        assertFalse(Objects.equal("Hello", "World"));
        
        // Edge case: null objects
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "Hello"));
        assertFalse(Objects.equal("Hello", null));
    }

    // Test cases for hashCode method
    @Test
    public void testHashCode() {
        // Typical use case: multiple objects
        assertEquals(Objects.hashCode("Hello", "World"), "HelloWorld".hashCode());
        
        // Typical use case: single object
        assertEquals(Objects.hashCode("Hello"), "Hello".hashCode());
        
        // Edge case: null object
        assertEquals(Objects.hashCode((Object) null), 0);
    }

    // Test cases for toStringHelper method 
    @Test
    public void testToStringHelper() {
        // Typical use case: custom class name
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.add("name", "John");
        helper.add("age", 25);
        assertEquals(helper.toString(), "TestClass{name=John, age=25}");
        
        // Edge case: empty class name
        helper = Objects.toStringHelper("");
        helper.add("name", "John");
        assertEquals(helper.toString(), "{name=John}");
        
        // Edge case: null class name
        helper = Objects.toStringHelper((String) null);
        helper.add("name", "John");
        assertEquals(helper.toString(), "{name=John}");
    }

    // Test cases for firstNonNull method
    @Test
    public void testFirstNonNull() {
        // Typical use case: non-null first object
        assertEquals(Objects.firstNonNull("Hello", "World", "Java"), "Hello");
        
        // Typical use case: non-null second object
        assertEquals(Objects.firstNonNull(null, "World", "Java"), "World");
        
        // Typical use case: null first and second objects
        assertEquals(Objects.firstNonNull(null, null, "Java"), "Java");
        
        // Edge case: empty objects array
        assertEquals(Objects.firstNonNull(), null);
    }

}