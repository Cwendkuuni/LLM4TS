import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringWriter;
import java.io.IOException;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertNotNull(encoder);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test
    public void testXMLEncoder() {
        String encoding = "UTF-8";
        XMLEncoder encoder = new XMLEncoder(encoding);
        assertNotNull(encoder);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }

    @Test
    public void testText() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "Hello World", true);
        assertEquals("Hello World", writer.toString());
    }

    // Add more test cases for other methods as needed

}