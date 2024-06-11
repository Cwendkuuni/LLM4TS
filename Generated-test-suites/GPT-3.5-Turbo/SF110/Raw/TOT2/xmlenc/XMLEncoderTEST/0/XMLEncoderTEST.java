import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class XMLEncoderTest {
    
    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            fail("Exception thrown when creating encoder with valid encoding");
        }
    }
    
    @Test
    public void testGetEncoderWithInvalidEncoding() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("ISO-12345");
            fail("Exception not thrown when creating encoder with invalid encoding");
        } catch (UnsupportedEncodingException e) {
            // This is the expected behavior
        }
    }
    
    @Test
    public void testDeclaration() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (IOException e) {
            fail("Exception thrown during declaration method");
        }
    }
    
    @Test
    public void testText() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, "Hello World", true);
            assertEquals("Hello World", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown during text method");
        }
    }
    
    // Add more test cases for each method as needed
    
}