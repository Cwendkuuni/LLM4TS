import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.io.UnsupportedEncodingException;

public class XMLEncoderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_InvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("invalid_encoding");
    }

    @Test
    public void testGetEncoder_UTF8Encoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
    }

    @Test
    public void testGetEncoder_UTF16Encoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-16");
        assertNotNull(encoder);
    }

    @Test
    public void testGetEncoder_USASCIIEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("US-ASCII");
        assertNotNull(encoder);
    }

    @Test
    public void testGetEncoder_ASCIIEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("ASCII");
        assertNotNull(encoder);
    }

    @Test
    public void testGetEncoder_ISO8859Encoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("ISO-8859-1");
        assertNotNull(encoder);
    }

    @Test
    public void testGetEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        Writer writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }

    @Test
    public void testText() throws IOException, InvalidXMLException {
        Writer writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "test", true);
        assertEquals("test", writer.toString());
    }

    @Test
    public void testAttribute() throws IOException {
        Writer writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '\'', true);
        assertEquals(" name='value'", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_InvalidQuotationMark() throws IOException {
        Writer writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '!', true);
    }

}