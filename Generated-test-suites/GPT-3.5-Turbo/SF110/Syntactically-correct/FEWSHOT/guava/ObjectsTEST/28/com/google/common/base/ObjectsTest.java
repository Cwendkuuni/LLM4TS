package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        // Test equal objects
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertEquals(true, Objects.equal(obj1, obj2));
        
        // Test null objects
        obj1 = null;
        obj2 = null;
        assertEquals(true, Objects.equal(obj1, obj2));
        
        // Test unequal objects
        obj1 = new Object();
        obj2 = new Object();
        assertEquals(false, Objects.equal(obj1, obj2));
    }
    
    @Test
    public void testHashCode() {
        Object[] objects = new Object[] { "foo", 1, true, null };
        int expectedHashCode = Objects.hashCode(objects);
        int actualHashCode = Arrays.hashCode(objects);
        
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void testToStringHelper() {
        // Test adding name-value pair
        Objects.ToStringHelper helper = Objects.toStringHelper("Person").add("name", "John");
        String expectedString = "Person{name=John}";
        assertEquals(expectedString, helper.toString());
        
        // Test adding null value
        helper = Objects.toStringHelper("Person").add("name", null);
        expectedString = "Person{name=null}";
        assertEquals(expectedString, helper.toString());
        
        // Test omitting null values
        helper = Objects.toStringHelper("Person").omitNullValues().add("name", null).add("age", 30);
        expectedString = "Person{age=30}";
        assertEquals(expectedString, helper.toString());
        
        // Test adding multiple name-value pairs
        helper = Objects.toStringHelper("Person").add("name", "John").add("age", 30).add("gender", "Male");
        expectedString = "Person{name=John, age=30, gender=Male}";
        assertEquals(expectedString, helper.toString());
        
        // Test adding value only
        helper = Objects.toStringHelper("Person").addValue("John");
        expectedString = "Person{John}";
        assertEquals(expectedString, helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        // Test first non-null value is returned
        Integer first = null;
        Integer second = 10;
        Integer result = Objects.firstNonNull(first, second);
        assertEquals(second, result);
        
        // Test first non-null value is returned
        first = 20;
        second = null;
        result = Objects.firstNonNull(first, second);
        assertEquals(first, result);
        
        // Test both null values
        first = null;
        second = null;
        result = Objects.firstNonNull(first, second);
        assertNull(result);
    }
}