package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual_WhenObjectsEqual_ReturnsTrue() {
        assertTrue(Objects.equal("Hello", "Hello"));
    }
    
    @Test
    public void testEqual_WhenObjectsNotEqual_ReturnsFalse() {
        assertFalse(Objects.equal("Hello", "World"));
    }
    
    @Test
    public void testEqual_WhenOneObjectIsNull_ReturnsFalse() {
        assertFalse(Objects.equal(null, "Hello"));
    }
    
    @Test
    public void testEqual_WhenBothObjectsAreNull_ReturnsTrue() {
        assertTrue(Objects.equal(null, null));
    }
    
    @Test
    public void testHashCode_WhenObjectsAreNull_ReturnsZero() {
        int hashCode = Objects.hashCode(null, null);
        assertEquals(0, hashCode);
    }
    
    @Test
    public void testHashCode_WhenObjectsAreNotNull_CalculatesHashCode() {
        int hashCode = Objects.hashCode("Hello", "World");
        assertEquals(86589699, hashCode);
    }
    
    @Test
    public void testToStringHelper_AddsNameAndValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        helper.add("name", "John");
        helper.add("age", 25);
        
        String result = helper.toString();
        assertEquals("Test{name=John, age=25}", result);
    }
    
    @Test
    public void testToStringHelper_OmitsNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        helper.omitNullValues();
        helper.add("name", "John");
        helper.add("age", null);
        
        String result = helper.toString();
        assertEquals("Test{name=John}", result);
    }
    
    @Test
    public void testFirstNonNull_WhenFirstObjectNotNull_ReturnsFirstObject() {
        String result = Objects.firstNonNull("Hello", "World");
        assertEquals("Hello", result);
    }
    
    @Test
    public void testFirstNonNull_WhenFirstObjectIsNull_ReturnsSecondObject() {
        String result = Objects.firstNonNull(null, "World");
        assertEquals("World", result);
    }
}