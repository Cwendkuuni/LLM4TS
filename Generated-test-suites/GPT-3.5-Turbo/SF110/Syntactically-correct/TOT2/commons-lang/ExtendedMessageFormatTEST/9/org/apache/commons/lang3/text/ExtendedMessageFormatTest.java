package org.apache.commons.lang3.text;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertNotNull(emf);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        emf.applyPattern("Hello {0}");
        assertEquals(emf.toPattern(), "Hello {0}");
    }

    @Test
    public void testInvalidFormatElement() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        try {
            emf.applyPattern("Hello {");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello {0}");
        assertEquals(emf1, emf2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertNotNull(emf.hashCode());
    }
    
    // Add more test cases for other methods as needed
    
}