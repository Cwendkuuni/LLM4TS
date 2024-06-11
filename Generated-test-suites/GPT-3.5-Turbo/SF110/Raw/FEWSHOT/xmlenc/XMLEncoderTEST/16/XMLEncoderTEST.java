import static org.junit.Assert.*;

import org.junit.Test;

public class XMLEncoderTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructor_InvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("invalidEncoding");
    }
    
    @Test
    public void testGetEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
        
        encoder = XMLEncoder.getEncoder("US-ASCII");
        assertEquals("US-ASCII", encoder.getEncoding());
        
        encoder = XMLEncoder.getEncoder("ISO-8859-1");
        assertEquals("ISO-8859-1", encoder.getEncoding());
    }
    
    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }
    
    @Test
    public void testText_String_NoEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "foo", false);
        assertEquals("foo", writer.toString());
    }
    
    @Test
    public void testText_String_EscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "foo & bar", true);
        assertEquals("foo &amp; bar", writer.toString());
    }
    
    @Test
    public void testText_CharArray_NoEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        char[] text = {'f', 'o', 'o'};
        encoder.text(writer, text, 0, text.length, false);
        assertEquals("foo", writer.toString());
    }
    
    @Test
    public void testText_CharArray_EscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        char[] text = {'f', 'o', 'o', '&', 'b', 'a', 'r'};
        encoder.text(writer, text, 0, text.length, true);
        assertEquals("foo &amp; bar", writer.toString());
    }
    
    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidChar() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, '\u0080');
    }
    
    @Test
    public void testWhitespace_String() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(writer, "   ");
        assertEquals("   ", writer.toString());
    }
    
    @Test
    public void testWhitespace_CharArray() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        char[] whitespace = {' ', ' ', ' '};
        encoder.whitespace(writer, whitespace, 0, whitespace.length);
        assertEquals("   ", writer.toString());
    }
    
    @Test
    public void testAttribute() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '\"', true);
        assertEquals(" name=\"value\"", writer.toString());
    }
}