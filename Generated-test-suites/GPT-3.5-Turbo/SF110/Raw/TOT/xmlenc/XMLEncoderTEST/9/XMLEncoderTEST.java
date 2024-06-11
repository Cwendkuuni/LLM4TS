package org.znerd.xmlenc;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class XMLEncoderTest {

    private Writer writer;

    @Before
    public void setUp() {
        writer = new StringWriter();
    }

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_UnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("abc");
    }

    @Test
    public void testDeclaration() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = NullPointerException.class)
    public void testDeclaration_NullWriter() throws NullPointerException, IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(null);
    }

    @Test
    public void testText_String() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.text(writer, "This is a test", true);
            assertEquals("This is a test", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testText_CharArray() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            char[] text = "This is a test".toCharArray();
            encoder.text(writer, text, 0, text.length, true);
            assertEquals("This is a test", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testText_Char() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.text(writer, 'A');
            assertEquals("A", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testText_Char_EscapeAmpersands() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.text(writer, '&', true);
            assertEquals("&amp;", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_Char_InvalidCharacter() throws InvalidXMLException, IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, '\u0080');
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_CharArray_InvalidCharacter() throws InvalidXMLException, IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        char[] text = { '\u0080' };
        encoder.text(writer, text, 0, text.length, true);
    }

    @Test
    public void testWhitespace_String() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.whitespace(writer, "   ");
            assertEquals("   ", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testWhitespace_CharArray() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            char[] whitespace = { ' ', '\t', '\n', '\r' };
            encoder.whitespace(writer, whitespace, 0, whitespace.length);
            assertEquals(" \t\n\r", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = InvalidXMLException.class)
    public void testWhitespace_CharArray_InvalidCharacter() throws InvalidXMLException, IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        char[] whitespace = { '\u0080' };
        encoder.whitespace(writer, whitespace, 0, whitespace.length);
    }

    @Test
    public void testAttribute() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.attribute(writer, "name", "test", '\"', true);
            assertEquals(" name=\"test\"", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_InvalidQuotationMark() throws IllegalArgumentException, IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "name", "test", '\'', true);
    }

    @Test(expected = NullPointerException.class)
    public void testAttribute_NullWriter() throws NullPointerException, IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(null, "name", "test", '\'', true);
    }

    @Test
    public void testSevenBitEncoding() {
        try {
            XMLEncoder encoder = new XMLEncoder("US-ASCII");
            assertTrue(encoder._sevenBitEncoding);
            encoder = new XMLEncoder("ASCII");
            assertTrue(encoder._sevenBitEncoding);
            encoder = new XMLEncoder("ISO-8859-1");
            assertTrue(encoder._sevenBitEncoding);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testNonSevenBitEncoding() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertFalse(encoder._sevenBitEncoding);
            encoder = new XMLEncoder("UTF-16");
            assertFalse(encoder._sevenBitEncoding);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}