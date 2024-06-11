import static org.junit.Assert.*;
import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import java.io.*;
import java.io.IOException;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderInvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("InvalidEncoding");
    }

    @Test
    public void testGetEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String encoding = encoder.getEncoding();
        assertNotNull(encoding);
        assertEquals("UTF-8", encoding);
    }

    @Test
    public void testDeclaration() throws IOException {
        Writer writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        String result = writer.toString();
        assertNotNull(result);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", result);
    }

    @Test
    public void testText() throws IOException {
        Writer writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "Test", true);
        String result = writer.toString();
        assertNotNull(result);
        assertEquals("Test", result);
    }

    @Test
    public void testWhitespace() throws IOException {
        Writer writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(writer, "  ");
        String result = writer.toString();
        assertNotNull(result);
        assertEquals("  ", result);
    }

    @Test
    public void testAttribute() throws IOException {
        Writer writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '\"', true);
        String result = writer.toString();
        assertNotNull(result);
        assertEquals(" name=\"value\"", result);
    }
}