import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class XMLEncoderTest {
    
    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            fail("Exception thrown unexpectedly");
        }
    }
    
    @Test
    public void testConstructorValidEncoding() {
        try {
            XMLEncoder encoder = new XMLEncoder("ISO-8859-1");
            assertEquals("ISO-8859-1", encoder.getEncoding());
        } catch (Exception e) {
            fail("Exception thrown unexpectedly");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder("invalid_encoding");
    }
    
    @Test
    public void testDeclaration() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown unexpectedly");
        }
    }
    
    @Test
    public void testText() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, "Test text", true);
            assertEquals("Test text", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown unexpectedly");
        }
    }
    
    // More test cases for other methods
    
}