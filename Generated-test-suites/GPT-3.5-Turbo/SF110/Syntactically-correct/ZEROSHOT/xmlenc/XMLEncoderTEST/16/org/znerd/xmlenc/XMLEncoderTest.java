import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import static org.junit.Assert.*;

public class XMLEncoderTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding_ThrowsIllegalArgumentException() throws UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_InvalidEncoding_ThrowsUnsupportedEncodingException() throws UnsupportedEncodingException {
        XMLEncoder.getEncoder("invalid");
    }
    
    @Test
    public void testGetEncoder_ValidEncoding_ReturnsXMLEncoder() throws UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
    }
    
    @Test
    public void testDeclaration_WritesDeclarationToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        
        encoder.declaration(writer);
        
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }
    
    @Test
    public void testText_WritesTextToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        
        encoder.text(writer, "<text>", true);
        
        assertEquals("&lt;text&gt;", writer.toString());
    }
    
    @Test
    public void testText_WithCharArray_WritesTextToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        
        encoder.text(writer, "<text>".toCharArray(), 0, 6, true);
        
        assertEquals("&lt;text&gt;", writer.toString());
    }
    
    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidCharacter_ThrowsInvalidXMLException() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        
        encoder.text(writer, '\u0000', true);
    }
    
    @Test
    public void testWhitespace_WritesWhitespaceToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        
        encoder.whitespace(writer, "  ");
        
        assertEquals("  ", writer.toString());
    }
    
    @Test
    public void testAttribute_WritesAttributeToWriter() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        
        encoder.attribute(writer, "attribute", "value", '\"', true);
        
        assertEquals(" attribute=\"value\"", writer.toString());
    }
}