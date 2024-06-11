package org.apache.commons.lang3;
import org.junit.Test;

public class ValidateTest {
    
    @Test
    public void testIsTrue() {
        // typical use case
        Validate.isTrue(1 == 1);
        
        // edge case
        try {
            Validate.isTrue(1 == 0, "Expected %d to be equal to %d", 1, 0);
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    @Test
    public void testNotNull() {
        // typical use case
        String str = "Hello";
        Validate.notNull(str);
        
        // edge case
        try {
            Validate.notNull(null, "String cannot be null");
        } catch (NullPointerException e) {
            // success
        }
    }
    
    // Add test cases for the remaining methods
    
}