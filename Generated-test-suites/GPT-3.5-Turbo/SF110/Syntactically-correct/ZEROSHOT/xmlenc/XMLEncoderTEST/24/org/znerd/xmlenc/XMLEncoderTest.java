import org.junit.*;
import java.io.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class XMLEncoderTest {

    private XMLEncoder xmlEncoder;
    
    @Before
    public void setUp() throws UnsupportedEncodingException {
        xmlEncoder = XMLEncoder.getEncoder("UTF-8");
    }

    @Test
    public void testGetEncoder() throws UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Assert.assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() throws UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderWithInvalidEncoding() throws UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("invalidEncoding");
    }

    @Test
    public void testGetEncoding() {
        Assert.assertEquals("UTF-8", xmlEncoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        xmlEncoder.declaration(writer);
        writer.flush();

        String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Assert.assertEquals(expectedDeclaration, outputStream.toString());
    }

    @Test
    public void testText() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        xmlEncoder.text(writer, "Text to encode", true);
        writer.flush();

        Assert.assertEquals("Text to encode", outputStream.toString());
    }

    @Test
    public void testWhitespace() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        xmlEncoder.whitespace(writer, "  whitespace ");

        writer.flush();

        Assert.assertEquals("  whitespace ", outputStream.toString());
    }

    @Test
    public void testAttribute() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        xmlEncoder.attribute(writer, "name", "value", '\"', true);

        writer.flush();

        Assert.assertEquals(" name=\"value\"", outputStream.toString());
    }
    
    @Test
    public void testTextWithEscapeAmpersands() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        xmlEncoder.text(writer, "&", true);
        writer.flush();
        
        Assert.assertEquals("&amp;", outputStream.toString());
    }
    
    @Test
    public void testTextWithoutEscapeAmpersands() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        xmlEncoder.text(writer, "&", false);
        writer.flush();
        
        Assert.assertEquals("&", outputStream.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        xmlEncoder.text(writer, '\u03A9');
        writer.flush();
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacterAndEscapeAmpersands() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        xmlEncoder.text(writer, '\u03A9', true);
        writer.flush();
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacterAndWithoutEscapeAmpersands() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        xmlEncoder.text(writer, '\u03A9', false);
        writer.flush();
    }
}