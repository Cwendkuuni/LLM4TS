import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import org.znerd.xmlenc.XMLEncoder;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testConstructorWithValidEncoding() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testConstructorWithInvalidEncoding() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-32");
            fail("Exception expected");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            // Pass
        }
    }

    // Add more test cases for other methods and edge cases

}