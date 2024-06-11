package org.znerd.xmlenc;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class XMLEncoderTest {
    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception not expected");
        }

        try {
            XMLEncoder encoder = new XMLEncoder("US-ASCII");
            assertEquals("US-ASCII", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception not expected");
        }

        try {
            XMLEncoder encoder = new XMLEncoder("ISO-8859-1");
            assertEquals("ISO-8859-1", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception not expected");
        }

        try {
            XMLEncoder encoder = new XMLEncoder("InvalidEncoding");
            fail("Exception expected");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            assertEquals("InvalidEncoding", e.getMessage());
        }
    }

    @Test
    public void testDeclaration() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (NullPointerException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testText() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            encoder.text(writer, "This is a test", false);
            assertEquals("This is a test", writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testTextWithCharArray() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        char[] ch = "This is a test".toCharArray();

        try {
            encoder.text(writer, ch, 0, ch.length, false);
            assertEquals("This is a test", writer.toString());
        } catch (NullPointerException | IndexOutOfBoundsException | InvalidXMLException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testTextWithChar() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            encoder.text(writer, 'A');
            assertEquals("A", writer.toString());
        } catch (InvalidXMLException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testTextWithCharAndEscapeAmpersands() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            encoder.text(writer, '&', true);
            assertEquals("&amp;", writer.toString());
        } catch (InvalidXMLException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testWhitespaceWithString() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            encoder.whitespace(writer, "  ");
            assertEquals("  ", writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testWhitespaceWithCharArray() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        char[] ch = "  ".toCharArray();

        try {
            encoder.whitespace(writer, ch, 0, ch.length);
            assertEquals("  ", writer.toString());
        } catch (NullPointerException | IndexOutOfBoundsException | InvalidXMLException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testAttribute() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            encoder.attribute(writer, "name", "value", '"', false);
            assertEquals(" name=\"value\"", writer.toString());
        } catch (NullPointerException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testInvalidQuotationMark() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            encoder.attribute(writer, "name", "value", '!', false);
            fail("Exception expected");
        } catch (NullPointerException | IllegalArgumentException | IOException e) {
            assertEquals("Character 0x21 ('!') is not a valid quotation mark.", e.getMessage());
        }
    }

    @Test
    public void testEscapeAmpersandsInAttribute() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            encoder.attribute(writer, "name", "value&", '"', true);
            assertEquals(" name=\"value&amp;\"", writer.toString());
        } catch (NullPointerException | IOException e) {
            fail("Exception not expected");
        }
    }
}