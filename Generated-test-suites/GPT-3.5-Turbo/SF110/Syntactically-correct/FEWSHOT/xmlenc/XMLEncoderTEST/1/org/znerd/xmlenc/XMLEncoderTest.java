
package org.znerd.xmlenc;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.io.ByteArrayOutputStream;
import org.hamcrest.CoreMatchers;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() throws UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_InvalidEncoding() throws UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("INVALID_ENCODING");
    }

    @Test
    public void testXMLEncoder_WithValidEncoding() throws UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertNotNull(encoder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXMLEncoder_WithNullEncoding() throws UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testXMLEncoder_WithInvalidEncoding() throws UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder("INVALID_ENCODING");
    }

    @Test
    public void testGetEncoding() throws UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        encoder.declaration(writer);
        writer.flush();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testText_EscapeAmpersands() throws IOException, InvalidXMLException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        String text = "This is a & test";
        encoder.text(writer, text, true);
        writer.flush();
        String expected = "This is a &amp; test";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testText_NoEscapeAmpersands() throws IOException, InvalidXMLException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        String text = "This is a & test";
        encoder.text(writer, text, false);
        writer.flush();
        String expected = "This is a & test";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testWhitespace_String() throws IOException, InvalidXMLException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        String whitespace = "    \n\t   ";
        encoder.whitespace(writer, whitespace);
        writer.flush();
        String expected = "    \n\t   ";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testWhitespace_CharArray() throws IOException, InvalidXMLException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        char[] whitespace = {' ', ' ', ' ', ' ', '\n', '\t', ' ', ' ', ' ', ' '};
        encoder.whitespace(writer, whitespace, 0, whitespace.length);
        writer.flush();
        String expected = "    \n\t   ";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testAttribute_EscapeAmpersands() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        String name = "attr";
        String value = "This is a & test";
        char quotationMark = '\"';
        encoder.attribute(writer, name, value, quotationMark, true);
        writer.flush();
        String expected = " attr=\"This is a &amp; test\"";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testAttribute_NoEscapeAmpersands() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        String name = "attr";
        String value = "This is a & test";
        char quotationMark = '\"';
        encoder.attribute(writer, name, value, quotationMark, false);
        writer.flush();
        String expected = " attr=\"This is a & test\"";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }
}

