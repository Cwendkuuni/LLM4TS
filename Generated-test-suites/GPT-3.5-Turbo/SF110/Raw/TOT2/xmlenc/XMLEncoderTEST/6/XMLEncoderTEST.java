import org.junit.Test;
import java.io.StringWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception thrown when it should not have been.");
        }
    }

    @Test
    public void testXMLEncoderInitialization() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception thrown when it should not have been.");
        }
    }

    // Additional test cases for other methods can be added here

}