package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        Validate.isTrue(2 + 2 == 4, "2 + 2 should equal 4");
        try {
            Validate.isTrue(2 + 2 == 5, "2 + 2 should equal 4");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("2 + 2 should equal 4", e.getMessage());
        }
    }
    
    @Test
    public void testNotNull() {
        String str = "Hello";
        assertNotNull(Validate.notNull(str));
        try {
            Validate.notNull(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("The validated object is null", e.getMessage());
        }
    }
    
    // Add more test cases for other methods
    
}