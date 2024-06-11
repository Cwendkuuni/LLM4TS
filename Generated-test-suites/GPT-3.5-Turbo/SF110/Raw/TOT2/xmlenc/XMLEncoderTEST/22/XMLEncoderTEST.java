import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.znerd.xmlenc.XMLEncoder;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
        } catch (Exception e) {
            fail("Exception thrown while getting encoder");
        }
    }

    @Test
    public void testXMLEncoderConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            fail("Exception thrown while creating encoder");
        }
    }

    @Test
    public void testDeclaration() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown while writing declaration");
        }
    }

    @Test
    public void testText() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.text(writer, "sample text", true);
            assertEquals("sample text", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown while writing text");
        }
    }

    // Additional test methods for other public methods..

}