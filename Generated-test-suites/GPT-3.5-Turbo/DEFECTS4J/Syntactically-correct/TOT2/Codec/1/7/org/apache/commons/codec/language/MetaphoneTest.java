package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("AN", metaphone.metaphone("ANN"));
        
        // Edge cases
        assertEquals("", metaphone.metaphone(""));
        assertEquals("A", metaphone.metaphone("A"));
        
        // Error scenarios
        assertEquals("", metaphone.metaphone(null));
    }

    @Test
    public void testEncodeObject() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        try {
            Object result = metaphone.encode("HELLO");
            assertEquals("HL", result);
        } catch (EncoderException e) {
            // Handle exception
        }
        
        // Edge cases
        try {
            Object result = metaphone.encode("");
            assertEquals("", result);
        } catch (EncoderException e) {
            // Handle exception
        }
        
        // Error scenarios
        try {
            Object result = metaphone.encode(123); // Should throw EncoderException
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals(true, metaphone.isMetaphoneEqual("HELLO", "HALO"));
        
        // Edge cases
        assertEquals(true, metaphone.isMetaphoneEqual("", ""));
        assertEquals(false, metaphone.isMetaphoneEqual("HELLO", ""));
        
        // Error scenarios
        assertEquals(false, metaphone.isMetaphoneEqual(null, "HELLO"));
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals(4, metaphone.getMaxCodeLen());
        
        // Set max code length and test
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
        
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
        
        // Test negative value
        metaphone.setMaxCodeLen(-1);
        assertEquals(4, metaphone.getMaxCodeLen()); // Should not set negative values
    }
}