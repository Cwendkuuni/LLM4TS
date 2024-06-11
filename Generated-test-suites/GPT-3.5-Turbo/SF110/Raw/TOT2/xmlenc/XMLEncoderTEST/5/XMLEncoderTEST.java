import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;

public class XMLEncoderTest {
    
    @Test
    public void testGetEncoder() {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
    }
    
    @Test
    public void testXMLEncoderConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertNotNull(encoder);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception not expected");
        }
    }

    // Additional test cases for other public methods
    
}