package org.znerd.xmlenc;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class XMLEncoderTest {
    
    private StringWriter writer;
    private XMLEncoder encoder;
    
    @Before
    public void setUp() throws Exception {
        writer = new StringWriter();
        encoder = new XMLEncoder("UTF-8");
    }
    
    // Test declaration method
    @Test
    public void testDeclaration() throws IOException {
        encoder.declaration(writer);
        
        // Verify that the correct declaration is written
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }
    
    // Test text method with escapeAmpersands set to true
    @Test
    public void testTextWithEscapeAmpersands() throws IOException {
        String text = "This is a test &";
        encoder.text(writer, text, true);
        
        // Verify that the correct text is written with ampersand escaped
        assertEquals("This is a test &amp;", writer.toString());
    }
    
    // Test text method with escapeAmpersands set to false
    @Test
    public void testTextWithoutEscapeAmpersands() throws IOException {
        String text = "This is a test &";
        encoder.text(writer, text, false);
        
        // Verify that the correct text is written without escaping ampersand
        assertEquals("This is a test &", writer.toString());
    }
    
    // Test text method with invalid XML character
    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter() throws IOException {
        encoder.text(writer, '\u001F', true);
        
        // Expecting InvalidXMLException to be thrown
    }
    
    // Test whitespace method
    @Test
    public void testWhitespace() throws IOException {
        String whitespace = "  ";
        encoder.whitespace(writer, whitespace);
        
        // Verify that the correct whitespace is written
        assertEquals("  ", writer.toString());
    }
    
    // Test attribute method with quotation mark set to double quote and escapeAmpersands set to true
    @Test
    public void testAttributeWithDoubleQuoteAndEscapeAmpersands() throws IOException {
        String name = "attr";
        String value = "This is a test &";
        char quotationMark = '\"';
        encoder.attribute(writer, name, value, quotationMark, true);
        
        // Verify that the correct attribute is written with ampersand escaped
        assertEquals(" attr=\"This is a test &amp;\"", writer.toString());
    }
    
    // Test attribute method with quotation mark set to single quote and escapeAmpersands set to false
    @Test
    public void testAttributeWithSingleQuoteAndWithoutEscapeAmpersands() throws IOException {
        String name = "attr";
        String value = "This is a test &";
        char quotationMark = '\'';
        encoder.attribute(writer, name, value, quotationMark, false);
        
        // Verify that the correct attribute is written without escaping ampersand
        assertEquals(" attr='This is a test &'", writer.toString());
    }
    
    // Test attribute method with invalid quotation mark
    @Test(expected = IllegalArgumentException.class)
    public void testAttributeWithInvalidQuotationMark() throws IOException {
        String name = "attr";
        String value = "This is a test";
        char quotationMark = '$';
        encoder.attribute(writer, name, value, quotationMark, true);
        
        // Expecting IllegalArgumentException to be thrown
    }
}