package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XMLEncoderTest {
    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }
    
    @Test
    public void testGetEncoderWithValidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
        
        encoder = XMLEncoder.getEncoder("US-ASCII");
        assertEquals("US-ASCII", encoder.getEncoding());
        
        encoder = XMLEncoder.getEncoder("ISO-8859-1");
        assertEquals("ISO-8859-1", encoder.getEncoding());
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderWithInvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("invalid");
    }
    
    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }
    
    @Test
    public void testTextWithEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "foo", true);
        assertEquals("foo", writer.toString());
        
        writer = new StringWriter();
        encoder.text(writer, "<bar>", true);
        assertEquals("&lt;bar&gt;", writer.toString());
        
        writer = new StringWriter();
        encoder.text(writer, "foo & bar", true);
        assertEquals("foo &amp; bar", writer.toString());
    }
    
    @Test
    public void testTextWithoutEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "foo", false);
        assertEquals("foo", writer.toString());
        
        writer = new StringWriter();
        encoder.text(writer, "<bar>", false);
        assertEquals("&lt;bar&gt;", writer.toString());
        
        writer = new StringWriter();
        encoder.text(writer, "foo & bar", false);
        assertEquals("foo & bar", writer.toString());
    }
    
    @Test
    public void testTextWithCharArray() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, new char[] {'f', 'o', 'o'}, 0, 3, true);
        assertEquals("foo", writer.toString());
        
        writer = new StringWriter();
        encoder.text(writer, new char[] {'<', 'b', 'a', 'r', '>'}, 0, 5, true);
        assertEquals("&lt;bar&gt;", writer.toString());
        
        writer = new StringWriter();
        encoder.text(writer, new char[] {'f', 'o', 'o', ' ', '&', ' ', 'b', 'a', 'r'}, 0, 9, true);
        assertEquals("foo &amp; bar", writer.toString());
    }
    
    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, '\u00FF');
    }
    
    @Test
    public void testWhitespace() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(writer, "  ");
        assertEquals("  ", writer.toString());
        
        writer = new StringWriter();
        encoder.whitespace(writer, new char[] {' ', ' ', ' ', ' '}, 0, 4);
        assertEquals("    ", writer.toString());
    }
    
    @Test
    public void testAttributeWithEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '\"', true);
        assertEquals(" name=\"value\"", writer.toString());
        
        writer = new StringWriter();
        encoder.attribute(writer, "name", "<value>", '\"', true);
        assertEquals(" name=\"&lt;value&gt;\"", writer.toString());
        
        writer = new StringWriter();
        encoder.attribute(writer, "name", "value & bar", '\"', true);
        assertEquals(" name=\"value &amp; bar\"", writer.toString());
    }
    
    @Test
    public void testAttributeWithoutEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '\"', false);
        assertEquals(" name=\"value\"", writer.toString());
        
        writer = new StringWriter();
        encoder.attribute(writer, "name", "<value>", '\"', false);
        assertEquals(" name=\"&lt;value&gt;\"", writer.toString());
        
        writer = new StringWriter();
        encoder.attribute(writer, "name", "value & bar", '\"', false);
        assertEquals(" name=\"value & bar\"", writer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAttributeWithInvalidQuotationMark() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '!', true);
    }
}