import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoding() {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }

    // Add more test cases for other methods...

}