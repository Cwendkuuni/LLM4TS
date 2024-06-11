package org.znerd.xmlenc;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            fail("Exception thrown while testing getEncoder(): " + e.getMessage());
        }
    }

    @Test
    public void testGetEncoder_InvalidEncoding() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("InvalidEncoding");
            fail("UnsupportedEncodingException should have been thrown");
        } catch (UnsupportedEncodingException e) {
            // Expected exception, test passes
        } catch (Exception e) {
            fail("Unexpected exception thrown while testing getEncoder_InvalidEncoding(): " + e.getMessage());
        }
    }

    @Test
    public void testDeclaration() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            encoder.declaration(writer);
            String declaration = writer.toString();
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", declaration);
        } catch (Exception e) {
            fail("Exception thrown while testing declaration(): " + e.getMessage());
        }
    }

    @Test
    public void testText() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            String text = "Hello World";
            encoder.text(writer, text, false);
            String encodedText = writer.toString();
            assertEquals("Hello World", encodedText);
        } catch (Exception e) {
            fail("Exception thrown while testing text(): " + e.getMessage());
        }
    }

    // Add more test cases for the other methods...

}