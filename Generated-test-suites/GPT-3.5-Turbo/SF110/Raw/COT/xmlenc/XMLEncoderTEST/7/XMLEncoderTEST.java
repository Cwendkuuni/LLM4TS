import org.junit.Before;
import org.junit.Test;
import java.io.*;

public class XMLEncoderTest {
    
    private XMLEncoder xmlEncoder;
    
    @Before
    public void setUp() throws Exception {
        xmlEncoder = new XMLEncoder("UTF-8");
    }
    
    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws Exception {
        XMLEncoder.getEncoder(null);
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_InvalidEncoding() throws Exception {
        XMLEncoder.getEncoder("INVALID_ENCODING");
    }
    
    @Test
    public void testConstructor() {
        try {
            assertEquals("UTF-8", xmlEncoder.getEncoding());
            assertFalse(xmlEncoder._sevenBitEncoding);
            assertEquals(5, xmlEncoder._encodingCharArray.length);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
    }
    
    // Additional test cases for other public methods...
    
}