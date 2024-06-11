package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

public class XMLEncoderTest {
    private XMLEncoder encoder;
    
    @Before
    public void setUp() throws UnsupportedEncodingException {
        encoder = XMLEncoder.getEncoder("UTF-8");
    }

    @Test
    public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderUnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("UTF-31");
    }

    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws NullPointerException, IOException {
        Writer writer = new StringWriter();
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }
    
    @Test
    public void testTextEscapeAmpersands() throws NullPointerException, InvalidXMLException, IOException {
        Writer writer = new StringWriter();
        encoder.text(writer, "Hello & World", true);
        assertEquals("Hello &amp; World", writer.toString());
    }
    
    @Test
    public void testTextDoNotEscapeAmpersands() throws NullPointerException, InvalidXMLException, IOException {
        Writer writer = new StringWriter();
        encoder.text(writer, "Hello & World", false);
        assertEquals("Hello & World", writer.toString());
    }
    
    @Test
    public void testWhitespaceCharArray() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        Writer writer = new StringWriter();
        encoder.whitespace(writer, new char[] { ' ', '\n', '\t' }, 0, 3);
        assertEquals(" \n\t", writer.toString());
    }
    
    @Test
    public void testWhitespaceString() throws NullPointerException, InvalidXMLException, IOException {
        Writer writer = new StringWriter();
        encoder.whitespace(writer, "   ");
        assertEquals("   ", writer.toString());
    }
    
    @Test
    public void testAttributeEscapeAmpersands() throws NullPointerException, IOException {
        Writer writer = new StringWriter();
        encoder.attribute(writer, "name", "value & test", '\"', true);
        assertEquals(" name=\"value &amp; test\"", writer.toString());
    }
    
    @Test
    public void testAttributeDoNotEscapeAmpersands() throws NullPointerException, IOException {
        Writer writer = new StringWriter();
        encoder.attribute(writer, "name", "value & test", '\"', false);
        assertEquals(" name=\"value & test\"", writer.toString());
    }
    
}