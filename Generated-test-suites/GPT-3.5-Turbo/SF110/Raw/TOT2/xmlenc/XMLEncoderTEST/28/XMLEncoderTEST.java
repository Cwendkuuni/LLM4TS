import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        // Typical use case
        assertNotNull(XMLEncoder.getEncoder("UTF-8"));

        // Edge case: encoding not supported
        assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("ISO-8859-2"));
    }

    @Test
    public void testXMLEncoderConstructor() {
        // Typical use case
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());

        // Edge case: encoding is null
        assertThrows(IllegalArgumentException.class, () -> new XMLEncoder(null));

        // Edge case: encoding not supported
        assertThrows(UnsupportedEncodingException.class, () -> new XMLEncoder("ISO-8859-1"));
    }

    @Test
    public void testDeclaration() throws Exception {
        StringWriter writer = new StringWriter();
        
        // Typical use case
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }

    // More test cases for other public methods like text, whitespace, attribute, etc.

}