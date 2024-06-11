package org.apache.commons.lang3;
import org.junit.*;
import static org.junit.Assert.*;

public class ValidateTest {
    
    @Test
    public void testIsTrueWithLong() {
        try {
            Validate.isTrue(5 > 2, "The value is not true", 10L);
        } catch (IllegalArgumentException e) {
            fail("Exception thrown when it shouldn't have");
        }
    }
    
    @Test
    public void testNotNull() {
        try {
            Validate.notNull("example");
        } catch (NullPointerException e) {
            fail("Exception thrown when it shouldn't have");
        }
        
        try {
            Validate.notNull(null);
            fail("NullPointerException not thrown when it should have");
        } catch (NullPointerException e) {
            // expected
        }
    }
    
    // More test cases for other methods can be added here
    
}