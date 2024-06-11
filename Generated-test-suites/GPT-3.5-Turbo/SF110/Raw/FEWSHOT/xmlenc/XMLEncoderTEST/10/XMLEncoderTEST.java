package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

public class XMLEncoderTest {

    private static final String ENCODING = "UTF-8";
    private XMLEncoder encoder;

    @Before
    public void setup() throws IllegalArgumentException, UnsupportedEncodingException {
        encoder = XMLEncoder.getEncoder(ENCODING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructorWithInvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("invalidEncoding");
    }

    @Test
    public void testGetEncoding() {
        assertEquals(ENCODING, encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        final Writer out = new StringWriter();
        encoder.declaration(out);
        assertEquals("<?xml version=\"1.0\" encoding=\"" + ENCODING + "\"?>", out.toString());
    }

    @Test
    public void testTextWithEscapeAmpersands() throws InvalidXMLException, IOException {
        final Writer out = new StringWriter();
        final String text = "This is a & test";
        encoder.text(out, text, true);
        assertEquals("This is a &amp; test", out.toString());
    }

    @Test
    public void testTextWithoutEscapeAmpersands() throws InvalidXMLException, IOException {
        final Writer out = new StringWriter();
        final String text = "This is a & test";
        encoder.text(out, text, false);
        assertEquals("This is a & test", out.toString());
    }

    @Test
    public void testWhitespace() throws InvalidXMLException, IOException {
        final Writer out = new StringWriter();
        final String whitespace = " ";
        encoder.whitespace(out, whitespace);
        assertEquals(whitespace, out.toString());
    }

    @Test
    public void testAttributeWithEscapeAmpersands() throws IOException {
        final Writer out = new StringWriter();
        final String name = "attribute";
        final String value = "This is a & attribute";
        final char quotationMark = '\"';
        encoder.attribute(out, name, value, quotationMark, true);
        assertEquals(" attribute=\"This is a &amp; attribute\"", out.toString());
    }

    @Test
    public void testAttributeWithoutEscapeAmpersands() throws IOException {
        final Writer out = new StringWriter();
        final String name = "attribute";
        final String value = "This is a & attribute";
        final char quotationMark = '\"';
        encoder.attribute(out, name, value, quotationMark, false);
        assertEquals(" attribute=\"This is a & attribute\"", out.toString());
    }
    
    // Add more test methods to test all methods in the XMLEncoder class
    
}