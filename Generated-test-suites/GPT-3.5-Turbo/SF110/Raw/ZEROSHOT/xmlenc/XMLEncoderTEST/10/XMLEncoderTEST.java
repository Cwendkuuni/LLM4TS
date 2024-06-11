package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.junit.Before;
import org.junit.Test;

public class XMLEncoderTest {
    private XMLEncoder encoder;
    
    @Before
    public void setUp() throws UnsupportedEncodingException {
        encoder = XMLEncoder.getEncoder("UTF-8");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructorWithUnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("UTF-32");
    }
    
    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", encoder.getEncoding());
    }
    
    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        encoder.declaration(writer);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testTextWithEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        String text = "This is a test & test.";
        encoder.text(writer, text, true);
        assertEquals("This is a test &amp; test.", writer.toString());
    }
    
    @Test
    public void testTextWithoutEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        String text = "This is a test & test.";
        encoder.text(writer, text, false);
        assertEquals("This is a test & test.", writer.toString());
    }
    
    @Test
    public void testTextWithCharArray() throws IOException {
        StringWriter writer = new StringWriter();
        char[] ch = { 'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't' };
        encoder.text(writer, ch, 0, ch.length, true);
        assertEquals("This is a test", writer.toString());
    }
    
    @Test
    public void testTextWithChar() throws IOException {
        StringWriter writer = new StringWriter();
        char c = 'T';
        encoder.text(writer, c);
        assertEquals("T", writer.toString());
    }
    
    @Test
    public void testTextWithCharAndEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        char c = '&';
        encoder.text(writer, c, true);
        assertEquals("&amp;", writer.toString());
    }
    
    @Test
    public void testTextWithCharAndWithoutEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        char c = '&';
        encoder.text(writer, c, false);
        assertEquals("&", writer.toString());
    }
    
    @Test
    public void testWhitespaceWithString() throws IOException {
        StringWriter writer = new StringWriter();
        String text = "     ";
        encoder.whitespace(writer, text);
        assertEquals("     ", writer.toString());
    }
    
    @Test
    public void testWhitespaceWithCharArray() throws IOException {
        StringWriter writer = new StringWriter();
        char[] ch = { ' ', ' ', ' ', ' ' };
        encoder.whitespace(writer, ch, 0, ch.length);
        assertEquals("    ", writer.toString());
    }
    
    @Test
    public void testAttributeWithEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        String name = "id";
        String value = "1234";
        char quotationMark = '\"';
        encoder.attribute(writer, name, value, quotationMark, true);
        assertEquals(" id=\"1234\"", writer.toString());
    }
    
    @Test
    public void testAttributeWithoutEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        String name = "id";
        String value = "1234";
        char quotationMark = '\"';
        encoder.attribute(writer, name, value, quotationMark, false);
        assertEquals(" id=\"1234\"", writer.toString());
    }
}