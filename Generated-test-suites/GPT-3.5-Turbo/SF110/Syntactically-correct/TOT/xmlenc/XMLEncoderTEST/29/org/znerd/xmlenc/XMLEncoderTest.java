import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XMLEncoderTest {
    
    private static final String VALID_ENCODING = "UTF-8";
    private static final String INVALID_ENCODING = "INVALID_ENCODING";
    
    private XMLEncoder encoder;
    
    @Before
    public void setUp() {
        encoder = new XMLEncoder(VALID_ENCODING);
    }
    
    @Test
    public void testGetEncoder_ValidEncoding_ReturnsXMLEncoderObject() {
        XMLEncoder result = XMLEncoder.getEncoder(VALID_ENCODING);
        assertNotNull(result);
        assertEquals(VALID_ENCODING, result.getEncoding());
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_InvalidEncoding_ThrowsUnsupportedEncodingException() {
        XMLEncoder.getEncoder(INVALID_ENCODING);
    }
    
    @Test
    public void testGetEncoding_ValidEncoding_ReturnsEncodingString() {
        String result = encoder.getEncoding();
        assertEquals(VALID_ENCODING, result);
    }
    
    @Test
    public void testDeclaration_WriterObject_WritesCorrectDeclaration() {
        // Test with a StringWriter object
        StringWriter writer = new StringWriter();
        encoder.declaration(writer);
        
        String result = writer.toString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expected, result);
    }
    
    @Test
    public void testText_WriterObjectAndStringEscapedText_WritesCorrectEscapedText() {
        // Test with a StringWriter object
        StringWriter writer = new StringWriter();
        String text = "This is &lt;escaped&gt; text.";
        boolean escapeAmpersands = true;
        
        encoder.text(writer, text, escapeAmpersands);
        
        String result = writer.toString();
        String expected = "This is &amp;lt;escaped&amp;gt; text.";
        assertEquals(expected, result);
    }
    
    @Test
    public void testText_WriterObjectAndCharArrayEscapedText_WritesCorrectEscapedText() {
        // Test with a StringWriter object
        StringWriter writer = new StringWriter();
        char[] text = "This is &lt;escaped&gt; text.".toCharArray();
        int start = 0;
        int length = text.length;
        boolean escapeAmpersands = true;
        
        encoder.text(writer, text, start, length, escapeAmpersands);
        
        String result = writer.toString();
        String expected = "This is &amp;lt;escaped&amp;gt; text.";
        assertEquals(expected, result);
    }
    
    @Test
    public void testText_WriterObjectAndUnescapedChar_WritesCorrectChar() {
        // Test with a StringWriter object
        StringWriter writer = new StringWriter();
        char c = 'a';
        
        encoder.text(writer, c);
        
        String result = writer.toString();
        String expected = "a";
        assertEquals(expected, result);
    }
    
    @Test
    public void testText_WriterObjectAndEscapedChar_WritesCorrectEscapedChar() {
        // Test with a StringWriter object
        StringWriter writer = new StringWriter();
        char c = '<';
        
        encoder.text(writer, c);
        
        String result = writer.toString();
        String expected = "&lt;";
        assertEquals(expected, result);
    }
    
    @Test(expected = InvalidXMLException.class)
    public void testText_WriterObjectAndInvalidChar_ThrowsInvalidXMLException() {
        // Test with a StringWriter object
        StringWriter writer = new StringWriter();
        char c = (char) 255; // Invalid character
        
        encoder.text(writer, c);
    }
    
    @Test
    public void testText_WriterObjectAndUnescapedCharAndEscapeAmpersands_WritesCorrectChar() {
        // Test with a StringWriter object
        StringWriter writer = new StringWriter();
        char c = 'a';
        boolean escapeAmpersands = true;
        
        encoder.text(writer, c, escapeAmpersands);
        
        String result = writer.toString();
        String expected = "a";
        assertEquals(expected, result);
    }
    
    @Test
    public void testText_WriterObjectAndEscapedCharAndEscapeAmpersands_WritesCorrectEscapedChar() {
        // Test with a StringWriter object
        StringWriter writer = new StringWriter();
        char c = '<';
        boolean escapeAmpersands = true;
        
        encoder.text(writer, c, escapeAmpersands);
        
        String result = writer.toString();
        String expected = "&lt;";
        assertEquals(expected, result);
    }
    
    @Test
    public void testText_WriterObjectAndEscapedAmpersandCharAndEscapeAmpersands_WritesCorrectEscapedChar() {
        // Test with a StringWriter object
        StringWriter writer = new StringWriter();
        char c = '&';
        boolean escapeAmpersands = true;
        
        encoder.text(writer, c, escapeAmpersands);
        
        String result = writer.toString();
        String expected = "&amp;";
        assertEquals(expected, result);
    }
    
    @Test(expected = InvalidXMLException.class)
    public void testText_WriterObjectAndInvalidCharAndEscapeAmpersands_ThrowsInvalidXMLException() {
        // Test with a StringWriter object
        StringWriter writer = new StringWriter();
        char c = (char) 255; // Invalid character
        boolean escapeAmpersands = true;
        
        encoder.text(writer, c, escapeAmpersands);
    }
    
    @Test
    public void testWhitespace_WriterObjectAndString_WritesCorrectWhitespace() {
        // Test with a StringWriter object
        StringWriter writer = new StringWriter();
        String whitespace = "     ";
        
        encoder.whitespace(writer, whitespace);
        
        String result = writer.toString();
        String expected = "     ";
        assertEquals(expected, result);
    }
    
    @Test
    public void testWhitespace_WriterObjectAndCharArray_WritesCorrectWhitespace() {
        // Test with a StringWriter object
        StringWriter writer = new StringWriter();
        char[] whitespace = "     ".toCharArray();
        int start = 0;
        int length = whitespace.length;
        
        encoder.whitespace(writer, whitespace, start, length);
        
        String result = writer.toString();
        String expected = "     ";
        assertEquals(expected, result);
    }

    @Test
    public void testAttribute_WriterObjectAndNameAndValueAndQuotationMarkAndEscapeAmpersands_WritesCorrectAttribute() {
        // Test with a StringWriter object
        StringWriter writer = new StringWriter();
        String name = "attribute";
        String value = "This is \"quoted\" value.";
        char quotationMark = '\"';
        boolean escapeAmpersands = true;
        
        encoder.attribute(writer, name, value, quotationMark, escapeAmpersands);
        
        String result = writer.toString();
        String expected = " attribute=\"This is &quot;quoted&quot; value.\"";
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_WriterObjectAndNameAndValueAndInvalidQuotationMarkAndEscapeAmpersands_ThrowsIllegalArgumentException() {
        // Test with a StringWriter object
        StringWriter writer = new StringWriter();
        String name = "attribute";
        String value = "This is 'quoted' value.";
        char quotationMark = '#'; // Invalid quotation mark
        boolean escapeAmpersands = true;
        
        encoder.attribute(writer, name, value, quotationMark, escapeAmpersands);
    }
    
}