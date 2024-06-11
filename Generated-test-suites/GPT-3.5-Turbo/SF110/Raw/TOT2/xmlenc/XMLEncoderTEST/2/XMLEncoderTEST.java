import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class XMLEncoderTest {
    
    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testXMLEncoderConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeclaration() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.declaration(writer);
            String result = writer.toString();
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", result);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testTextCharArrayWithInvalidXML() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        char[] invalidXml = {'&'};
        try {
            encoder.text(writer, invalidXml, 0, invalidXml.length, true);
            fail("InvalidXMLException not thrown");
        } catch (InvalidXMLException | NullPointerException | IndexOutOfBoundsException | IOException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testTextCharWithInvalidXML() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, '&');
            fail("InvalidXMLException not thrown");
        } catch (InvalidXMLException | IOException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testAttributeWithInvalidQuotationMark() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.attribute(writer, "name", "value", 'a', true);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException | NullPointerException | IOException e) {
            // Exception expected
        }
    }
  
}