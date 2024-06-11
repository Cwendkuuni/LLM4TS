package org.znerd.xmlenc;

import org.junit.Before;
import org.junit.Test;
import java.io.*;

import static org.junit.Assert.*;

public class XMLEncoderTest {
    
    private XMLEncoder encoder;
    private Writer writer;
    
    @Before
    public void setup() throws UnsupportedEncodingException {
        encoder = XMLEncoder.getEncoder("UTF-8");
        writer = new StringWriter();
    }
    
    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder.getEncoder(null);
            fail("IllegalArgumentException should be thrown when encoding is null");
        } catch (IllegalArgumentException e) {
            assertEquals("encoding == null", e.getMessage());
        }
        
        try {
            XMLEncoder.getEncoder("unsupported");
            fail("UnsupportedEncodingException should be thrown when encoding is unsupported");
        } catch (UnsupportedEncodingException e) {
            assertEquals("unsupported", e.getMessage());
        }
    }
    
    @Test
    public void testConstructor() throws UnsupportedEncodingException {
        try {
            new XMLEncoder(null);
            fail("IllegalArgumentException should be thrown when encoding is null");
        } catch (IllegalArgumentException e) {
            assertEquals("encoding == null", e.getMessage());
        }
        
        try {
            new XMLEncoder("unsupported");
            fail("UnsupportedEncodingException should be thrown when encoding is unsupported");
        } catch (UnsupportedEncodingException e) {
            assertEquals("unsupported", e.getMessage());
        }
        
        XMLEncoder asciiEncoder = new XMLEncoder("US-ASCII");
        assertEquals("US-ASCII", asciiEncoder.getEncoding());
        
        XMLEncoder utfEncoder = new XMLEncoder("UTF-8");
        assertEquals("UTF-8", utfEncoder.getEncoding());
    }
    
    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", encoder.getEncoding());
    }
    
    @Test
    public void testDeclaration() throws IOException {
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }
    
    @Test
    public void testText() throws IOException {
        encoder.text(writer, "test", true);
        assertEquals("test", writer.toString());
        
        encoder.text(writer, "test", false);
        assertEquals("test", writer.toString());
    }
    
    @Test
    public void testTextCharArray() throws IOException {
        char[] ch = {'t', 'e', 's', 't'};
        encoder.text(writer, ch, 0, 4, true);
        assertEquals("test", writer.toString());
        
        encoder.text(writer, ch, 0, 4, false);
        assertEquals("test", writer.toString());
    }
    
    @Test
    public void testTextChar() throws IOException {
        encoder.text(writer, 't');
        assertEquals("t", writer.toString());
    }
    
    @Test
    public void testTextCharWithEscapeAmpersands() throws IOException {
        encoder.text(writer, '&', true);
        assertEquals("&amp;", writer.toString());
    }
    
    @Test
    public void testTextCharWithoutEscapeAmpersands() throws IOException {
        encoder.text(writer, '&', false);
        assertEquals("&", writer.toString());
    }
    
    @Test
    public void testWhitespaceString() throws IOException {
        encoder.whitespace(writer, "   ");
        assertEquals("   ", writer.toString());
    }
    
    @Test
    public void testWhitespaceCharArray() throws IOException {
        char[] ch = {' ', ' ', ' '};
        encoder.whitespace(writer, ch, 0, 3);
        assertEquals("   ", writer.toString());
    }
    
    @Test
    public void testAttribute() throws IOException {
        encoder.attribute(writer, "name", "value", '\"', true);
        assertEquals(" name=\"value\"", writer.toString());
        
        encoder.attribute(writer, "name", "value", '\'', true);
        assertEquals(" name='value'", writer.toString());
        
        encoder.attribute(writer, "name", "value", '\"', false);
        assertEquals(" name=\"value\"", writer.toString());
        
        encoder.attribute(writer, "name", "value", '\'', false);
        assertEquals(" name='value'", writer.toString());
    }
}