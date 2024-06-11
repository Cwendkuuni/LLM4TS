
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.io.IOException;

public class XMLEncoderTest {

    @Test
    public void testXMLEncoder_getEncoder() throws Exception {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testXMLEncoder_Constructor_NullEncoding() throws Exception {
        new XMLEncoder(null);
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testXMLEncoder_Constructor_InvalidEncoding() throws Exception {
        new XMLEncoder("invalid_encoding");
    }
    
    @Test
    public void testXMLEncoder_getEncoding() {
        XMLEncoder encoder = new XMLEncoder("US-ASCII");
        assertEquals("US-ASCII", encoder.getEncoding());
    }
    
    @Test
    public void testXMLEncoder_declaration() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }
    
    @Test
    public void testXMLEncoder_text_NoEscape() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "This is a test", false);
        assertEquals("This is a test", writer.toString());
    }
    
    @Test
    public void testXMLEncoder_text_Escape() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "<tag>", true);
        assertEquals("&lt;tag&gt;", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testXMLEncoder_text_InvalidCharacter() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, '\u0000');
    }
    
    @Test
    public void testXMLEncoder_whitespace() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.whitespace(writer, "   ");
        assertEquals("   ", writer.toString());
    }
    
    @Test(expected = InvalidXMLException.class)
    public void testXMLEncoder_whitespace_InvalidCharacter() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.whitespace(writer, "test\u0000");
    }
    
    @Test
    public void testXMLEncoder_attribute() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", "value", '\"', false);
        assertEquals(" name=\"value\"", writer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testXMLEncoder_attribute_InvalidQuotationMark() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", "value", '\u0000', false);
    }
    
    @Test(expected = InvalidXMLException.class)
    public void testXMLEncoder_attribute_InvalidCharacter() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", "test\u0000", '\"', false);
    }
}

