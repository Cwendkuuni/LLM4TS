package org.znerd.xmlenc;

import org.junit.Test;
import java.io.*;

public class XMLEncoderTest {
    
    @Test
    public void testGetEncoder() {
        // Test typical use case
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
        
        // Test exception: unsupported encoding
        try {
            XMLEncoder.getEncoder("ISO-2022-JP");
            fail("Expected UnsupportedEncodingException");
        } catch (UnsupportedEncodingException e) {
            // Expected exception
        }
        
        // Test exception: null encoding
        try {
            XMLEncoder.getEncoder(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals("encoding == null", e.getMessage());
        }
    }
    
    @Test
    public void testDeclaration() {
        // Test typical use case
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        
        // Test exception: null Writer
        try {
            encoder.declaration(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
    
    // Additional test cases for other methods should be included here
    
}