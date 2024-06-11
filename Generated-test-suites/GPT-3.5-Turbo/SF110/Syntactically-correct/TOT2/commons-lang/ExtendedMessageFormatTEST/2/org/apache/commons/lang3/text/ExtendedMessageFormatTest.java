package org.apache.commons.lang3.text;
import org.junit.*;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    
    private ExtendedMessageFormat extendedMessageFormat;
    
    @Before
    public void setUp() {
        extendedMessageFormat = new ExtendedMessageFormat("Test {0}");
    }
    
    @Test
    public void testApplyPattern() {
        // Typical use case
        extendedMessageFormat.applyPattern("New {0}");
        assertEquals("New {0}", extendedMessageFormat.toPattern());
        
        // Edge case: Empty pattern
        extendedMessageFormat.applyPattern("");
        assertEquals("", extendedMessageFormat.toPattern());
        
        // Error scenario: Invalid pattern
        try {
            extendedMessageFormat.applyPattern("Invalid {1");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testEquals() {
        // Typical use case
        ExtendedMessageFormat other = new ExtendedMessageFormat("Test {0}");
        assertTrue(extendedMessageFormat.equals(other));
        
        // Edge case: Same object
        assertTrue(extendedMessageFormat.equals(extendedMessageFormat));
        
        // Error scenario: Different class
        assertFalse(extendedMessageFormat.equals("Test"));
    }
    
    // Add more test cases for other methods
    
}