package org.znerd.xmlenc.test;

import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder.getEncoder("UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testXMLEncoder() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertNotNull(encoder);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetEncoding() {
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder("UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception thrown");
        }
        assertNotNull(encoder.getEncoding());
    }

    @Test
    public void testDeclaration() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (IllegalArgumentException | UnsupportedEncodingException | IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testText() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, "Test", true);
            assertEquals("Test", writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testWhitespace() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.whitespace(writer, "Test");
            assertEquals("Test", writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testAttribute() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.attribute(writer, "name", "value", '\"', true);
            assertEquals(" name=\"value\"", writer.toString());
        } catch (NullPointerException | IOException e) {
            fail("Exception thrown");
        }
    }
}