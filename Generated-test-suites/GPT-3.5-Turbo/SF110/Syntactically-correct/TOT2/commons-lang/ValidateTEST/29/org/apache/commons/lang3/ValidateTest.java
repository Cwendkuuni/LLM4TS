package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrue_long() {
        Validate.isTrue(true, "Test message %d", 10L);
        try {
            Validate.isTrue(false, "Test message %d", 10L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testNotNull() {
        Integer obj = 5;
        assertNotNull(Validate.notNull(obj));
        
        try {
            Validate.notNull(null, "Custom message");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
    @Test
    public void testNotEmpty() {
        String[] array = new String[]{"element1", "element2"};
        assertArrayEquals(array, Validate.notEmpty(array));
        
        try {
            Validate.notEmpty(new String[0], "Custom message");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    // Add test cases for each method in the Validate class
}