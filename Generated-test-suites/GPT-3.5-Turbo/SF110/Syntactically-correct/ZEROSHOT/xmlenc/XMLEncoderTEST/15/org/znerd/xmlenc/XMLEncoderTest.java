
package org.znerd.xmlenc;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;

public class XMLEncoderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_UnsupportedEncoding() throws UnsupportedEncodingException {
        XMLEncoder.getEncoder("UTF-32");
    }

    @Test
    public void testGetEncoder_ValidEncoding() throws UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }

    @Test
    public void testText_NoEscapeAmpersands() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "Hello World", false);
        assertEquals("Hello World", writer.toString());
    }

    @Test
    public void testText_EscapeAmpersands() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "Hello & World", true);
        assertEquals("Hello &amp; World", writer.toString());
    }

    @Test
    public void testWhitespace() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(writer, "   ");
        assertEquals("   ", writer.toString());
    }

    @Test
    public void testAttribute() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '\"', false);
        assertEquals(" name=\"value\"", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAttributeQuotationMark() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '*', false);
    }
}
