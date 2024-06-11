import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() throws Exception {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertNotNull(encoder);
    }

    @Test
    public void testXMLEncoder() throws Exception {
        String encoding = "UTF-8";
        XMLEncoder encoder = new XMLEncoder(encoding);
        assertNotNull(encoder);
    }

    @Test
    public void testGetEncoding() {
        String encoding = "UTF-8";
        XMLEncoder encoder = new XMLEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }

    // Additional test cases for other methods should be added here...

}