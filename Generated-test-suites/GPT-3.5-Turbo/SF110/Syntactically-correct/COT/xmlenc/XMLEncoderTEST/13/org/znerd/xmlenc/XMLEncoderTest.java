import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

public class XMLEncoderTest {

    

    @Test
    public void testGetEncoder() throws UnsupportedEncodingException {
        // Test case for getEncoder method
        // ...
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws UnsupportedEncodingException {
        // Test case for getEncoder method with null encoding
        // ...
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_UnsupportedEncoding() throws UnsupportedEncodingException {
        // Test case for getEncoder method with unsupported encoding
        // ...
        XMLEncoder.getEncoder("XYZ");
    }

    @Test
    public void testXMLEncoder() throws UnsupportedEncodingException {
        // Test case for XMLEncoder constructor
        // ...
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertNotNull(encoder);
    }

    // Add test cases for other methods...

    
}