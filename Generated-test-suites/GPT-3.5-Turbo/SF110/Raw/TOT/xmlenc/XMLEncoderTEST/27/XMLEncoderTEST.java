package org.znerd.xmlenc;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder_ValidEncoding_ReturnsXMLEncoderObject() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding_ThrowsIllegalArgumentException() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder(null);
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_InvalidEncoding_ThrowsUnsupportedEncodingException() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("invalid-encoding");
    }
    
    @Test
    public void testXMLEncoder_ValidEncoding_EncodingSet() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testXMLEncoder_NullEncoding_ThrowsIllegalArgumentException() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder(null);
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testXMLEncoder_InvalidEncoding_ThrowsUnsupportedEncodingException() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder("invalid-encoding");
    }
    
    @Test
    public void testDeclaration_ValidWriter_DeclarationWritten() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expected, writer.toString());
    }
    
    @Test(expected = NullPointerException.class)
    public void testDeclaration_NullWriter_ThrowsNullPointerException() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(null);
    }
    
    @Test
    public void testText_ValidWriterAndText_TextWritten() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "Hello World", false);
        assertEquals("Hello World", writer.toString());
    }
    
    @Test(expected = NullPointerException.class)
    public void testText_NullWriter_ThrowsNullPointerException() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(null, "Hello World", false);
    }
    
    @Test
    public void testText_ValidWriterCharArrayValidIndices_TextWritten() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "Hello World".toCharArray(), 0, 11, false);
        assertEquals("Hello World", writer.toString());
    }
    
    @Test(expected = NullPointerException.class)
    public void testText_NullCharArray_ThrowsNullPointerException() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(new StringWriter(), null, 0, 0, false);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testText_InvalidIndices_ThrowsIndexOutOfBoundsException() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(new StringWriter(), "Hello World".toCharArray(), 0, 12, false);
    }
    
    @Test
    public void testText_ValidWriterChar_TextWritten() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, 'H');
        assertEquals("H", writer.toString());
    }
    
    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidCharacter_ThrowsInvalidXMLException() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(new StringWriter(), '\u0080');
    }
    
    @Test
    public void testWhitespace_ValidWriterAndString_WhitespaceWritten() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(writer, "   ");
        assertEquals("   ", writer.toString());
    }
    
    @Test(expected = NullPointerException.class)
    public void testWhitespace_NullWriter_ThrowsNullPointerException() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(null, "   ");
    }
    
    @Test
    public void testWhitespace_ValidWriterCharArrayValidIndices_WhitespaceWritten() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(writer, "   ".toCharArray(), 0, 3);
        assertEquals("   ", writer.toString());
    }
    
    @Test(expected = NullPointerException.class)
    public void testWhitespace_NullCharArray_ThrowsNullPointerException() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(new StringWriter(), null, 0, 0);
    }
    
    @Test
    public void testAttribute_ValidWriterAndValues_AttributeWritten() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '\"', false);
        String expected = " name=\"value\"";
        assertEquals(expected, writer.toString());
    }
    
    @Test(expected = NullPointerException.class)
    public void testAttribute_NullWriter_ThrowsNullPointerException() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(null, "name", "value", '\"', false);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_InvalidQuotationMark_ThrowsIllegalArgumentException() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(new StringWriter(), "name", "value", '@', false);
    }
    
    @Test
    public void testAttribute_ValidWriterCharValues_AttributeWritten() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '\"', false);
        String expected = " name=\"value\"";
        assertEquals(expected, writer.toString());
    }  
}