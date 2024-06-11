
package org.znerd.xmlenc;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class XMLEncoderTest {

    private XMLEncoder xmlEncoder;

    @Before
    public void setUp() {
        xmlEncoder = new XMLEncoder("UTF-8");
    }

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-16");
            assertNotNull(encoder);
            assertEquals("UTF-16", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructor_InvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder("invalid");
    }

    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", xmlEncoder.getEncoding());
    }

    @Test
    public void testDeclaration() {
        try {
            StringWriter writer = new StringWriter();
            xmlEncoder.declaration(writer);
            String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testText_WithEscapeAmpersands() {
        try {
            StringWriter writer = new StringWriter();
            String text = "This is a test & string";
            xmlEncoder.text(writer, text, true);
            String expected = "This is a test &amp; string";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testText_WithoutEscapeAmpersands() {
        try {
            StringWriter writer = new StringWriter();
            String text = "This is a test & string";
            xmlEncoder.text(writer, text, false);
            String expected = "This is a test & string";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testText_CharArray() {
        try {
            StringWriter writer = new StringWriter();
            char[] text = "This is a test & string".toCharArray();
            xmlEncoder.text(writer, text, 0, text.length, true);
            String expected = "This is a test &amp; string";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | IndexOutOfBoundsException | InvalidXMLException | IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testText_Char() {
        try {
            StringWriter writer = new StringWriter();
            char c = '&';
            xmlEncoder.text(writer, c);
            String expected = "&amp;";
            assertEquals(expected, writer.toString());
        } catch (InvalidXMLException | IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_Char_InvalidCharacter() throws InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        char c = '\u0080';
        xmlEncoder.text(writer, c);
    }

    @Test
    public void testWhitespace_String() {
        try {
            StringWriter writer = new StringWriter();
            String s = "   ";
            xmlEncoder.whitespace(writer, s);
            String expected = "   ";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testWhitespace_CharArray() {
        try {
            StringWriter writer = new StringWriter();
            char[] ch = "   ".toCharArray();
            xmlEncoder.whitespace(writer, ch, 0, ch.length);
            String expected = "   ";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | IndexOutOfBoundsException | InvalidXMLException | IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAttribute_WithEscapeAmpersands() {
        try {
            StringWriter writer = new StringWriter();
            String name = "attr";
            String value = "This is a test & value";
            char quotationMark = '\"';
            xmlEncoder.attribute(writer, name, value, quotationMark, true);
            String expected = " attr=\"This is a test &amp; value\"";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAttribute_WithoutEscapeAmpersands() {
        try {
            StringWriter writer = new StringWriter();
            String name = "attr";
            String value = "This is a test & value";
            char quotationMark = '\'';
            xmlEncoder.attribute(writer, name, value, quotationMark, false);
            String expected = " attr='This is a test & value'";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_InvalidQuotationMark() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        String name = "attr";
        String value = "This is a test & value";
        char quotationMark = '#';
        xmlEncoder.attribute(writer, name, value, quotationMark, true);
    }

}
