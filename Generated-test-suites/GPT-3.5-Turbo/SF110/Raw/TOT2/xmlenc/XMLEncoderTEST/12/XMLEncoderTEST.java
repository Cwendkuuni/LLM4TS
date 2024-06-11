package org.znerd.xmlenc;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoding() {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testText_String() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, "Hello World", true);
            assertEquals("Hello World", writer.toString());
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    // Add more test cases for other methods

}