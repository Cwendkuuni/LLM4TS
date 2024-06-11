import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
        } catch (Exception e) {
            fail("Exception thrown while creating encoder");
        }
    }

    @Test
    public void testXMLEncoderConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            fail("Exception thrown in constructor");
        }
    }

    @Test
    public void testDeclaration() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown while writing declaration");
        }
    }

    // Additional test cases for other methods can be added here
    
}