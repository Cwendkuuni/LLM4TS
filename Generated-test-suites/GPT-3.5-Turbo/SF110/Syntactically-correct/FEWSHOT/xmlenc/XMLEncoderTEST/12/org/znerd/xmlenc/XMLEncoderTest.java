
package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class XMLEncoderTest {
    private XMLEncoder encoder;

    @Before
    public void setup() {
        encoder = new XMLEncoder("UTF-8");
    }

    @Test
    public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder xmlEncoder = XMLEncoder.getEncoder("UTF-8");
        assertEquals("UTF-8", xmlEncoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderWithUnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("INVALID_ENCODING");
    }

    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }

    @Test
    public void testTextWithEscapeAmpersands() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        encoder.text(writer, "This is a test & example", true);
        assertEquals("This is a test &amp; example", writer.toString());
    }

    @Test
    public void testTextWithoutEscapeAmpersands() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        encoder.text(writer, "This is a test & example", false);
        assertEquals("This is a test & example", writer.toString());
    }

    @Test
    public void testTextWithCharArrayAndEscapeAmpersands() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        encoder.text(writer, "This is a test & example".toCharArray(), 0, 22, true);
        assertEquals("This is a test &amp; example", writer.toString());
    }

    @Test
    public void testTextWithCharArrayWithoutEscapeAmpersands() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        encoder.text(writer, "This is a test & example".toCharArray(), 0, 22, false);
        assertEquals("This is a test & example", writer.toString());
    }

    @Test
    public void testTextWithCharAndEscapeAmpersands() throws InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        encoder.text(writer, '&', true);
        assertEquals("&amp;", writer.toString());
    }

    @Test
    public void testTextWithCharWithoutEscapeAmpersands() throws InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        encoder.text(writer, '&', false);
        assertEquals("&", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidChar() throws InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        encoder.text(writer, (char) 129);
    }

    @Test
    public void testWhitespaceWithString() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        encoder.whitespace(writer, "   ");
        assertEquals("   ", writer.toString());
    }

    @Test
    public void testWhitespaceWithCharArray() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        encoder.whitespace(writer, "   ".toCharArray(), 0, 3);
        assertEquals("   ", writer.toString());
    }

    @Test
    public void testAttributeWithEscapeAmpersands() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "test", "This is a test & example", '\"', true);
        assertEquals(" test=\"This is a test &amp; example\"", writer.toString());
    }

    @Test
    public void testAttributeWithoutEscapeAmpersands() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "test", "This is a test & example", '\"', false);
        assertEquals(" test=\"This is a test & example\"", writer.toString());
    }

    @Test
    public void testAttributeWithInvalidQuotationMark() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "test", "Example", '}', true);
        assertEquals(" test=\"Example\"", writer.toString());
    }
}
