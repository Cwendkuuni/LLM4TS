package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class XMLEncoderTest {
    
    @Test
    public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderInvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("invalid_encoding");
    }
    
    @Test
    public void testConstructor() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
        assertEquals(false, encoder._sevenBitEncoding);
        assertEquals("UTF-8".toCharArray(), encoder._encodingCharArray);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder(null);
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructorInvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder("invalid_encoding");
    }
    
    @Test
    public void testDeclaration() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }
    
    @Test
    public void testTextWithEscapeAmpersands() throws IOException, InvalidXMLException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "Hello &amp; world", true);
        assertEquals("Hello &amp;amp; world", writer.toString());
    }
    
    @Test
    public void testTextWithoutEscapeAmpersands() throws IOException, InvalidXMLException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "Hello &amp; world", false);
        assertEquals("Hello &amp; world", writer.toString());
    }
    
    @Test
    public void testTextCharWithEscapeAmpersands() throws IOException, InvalidXMLException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, '&', true);
        assertEquals("&amp;", writer.toString());
    }
    
    @Test
    public void testTextCharWithoutEscapeAmpersands() throws IOException, InvalidXMLException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, '&', false);
        assertEquals("&", writer.toString());
    }
    
    @Test
    public void testWhitespace() throws IOException, InvalidXMLException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.whitespace(writer, "   ");
        assertEquals("   ", writer.toString());
    }
    
    @Test
    public void testAttributeWithEscapeAmpersands() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", "value &", '\"', true);
        assertEquals(" name=\"value &amp;\"", writer.toString());
    }
    
    @Test
    public void testAttributeWithoutEscapeAmpersands() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", "value &", '\"', false);
        assertEquals(" name=\"value &\"", writer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAttributeInvalidQuotationMark() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", "value", ' ', false);
    }

}