import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        // Test typical use case
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
        } catch (Exception e) {
            fail("Exception thrown for valid input");
        }
        
        // Test for exception
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder(null);
            fail("IllegalArgumentException not thrown for null encoding");
        } catch (IllegalArgumentException e) {
            // pass
        } catch (Exception e) {
            fail("Wrong exception thrown for null encoding");
        }
    }

    @Test
    public void testGetEncoding() {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        
        // Test typical use case
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() {
        // Test for typical use case
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown for valid input");
        }
    }

    // More test cases for other methods...

}