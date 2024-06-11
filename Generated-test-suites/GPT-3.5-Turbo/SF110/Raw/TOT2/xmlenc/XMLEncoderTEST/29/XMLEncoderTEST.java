import static org.junit.Assert.*;
import org.junit.Test;

public class XMLEncoderTest {
    
    @Test
    public void testGetEncoder() {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
    }
    
    @Test
    public void testXMLEncoderConstructor() {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testXMLEncoderConstructorWithNullEncoding() {
        new XMLEncoder(null);
    }
}