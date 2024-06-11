package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.junit.Test;

public class ValidateTest {
    
    @Test
    public void testIsTrueLong() {
        try {
            Validate.isTrue(true, "The value %s is not true", 10L);
            Validate.isTrue(false, "The value %s is not true", 5L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The value 5 is not true", e.getMessage());
        }
    }
    
    @Test
    public void testNotNull() {
        try {
            Object obj = null;
            Validate.notNull(obj, "The validated object is null");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("The validated object is null", e.getMessage());
        }
    }
    
    @Test
    public void testNotEmptyArray() {
        try {
            String[] array = new String[0];
            Validate.notEmpty(array, "The validated array is empty");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated array is empty", e.getMessage());
        }
    }
    
    @Test
    public void testNotEmptyCollection() {
        try {
            List<Integer> collection = new ArrayList<>();
            Validate.notEmpty(collection, "The validated collection is empty");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated collection is empty", e.getMessage());
        }
    }
    
    @Test
    public void testNotEmptyMap() {
        try {
            Map<String, Integer> map = new HashMap<>();
            Validate.notEmpty(map, "The validated map is empty");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The validated map is empty", e.getMessage());
        }
    }
    
    // Additional test cases for other methods can be added here...

}