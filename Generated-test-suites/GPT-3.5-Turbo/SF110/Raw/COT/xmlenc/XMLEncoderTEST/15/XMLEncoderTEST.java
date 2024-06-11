import org.junit.Test;
import java.io.*;

public class XMLEncoderTest {
    private XMLEncoder xmlEncoder;
    private StringWriter stringWriter;

    @Before
    public void setUp() throws Exception {
        xmlEncoder = new XMLEncoder("UTF-8");
        stringWriter = new StringWriter();
    }

    @Test
    public void testGetEncoder() throws Exception {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() throws Exception {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderWithInvalidEncoding() throws Exception {
        XMLEncoder.getEncoder("invalid");
    }

    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", xmlEncoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws Exception {
        xmlEncoder.declaration(stringWriter);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", stringWriter.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testDeclarationWithNullWriter() throws Exception {
        xmlEncoder.declaration(null);
    }

    @Test
    public void testText() throws Exception {
        xmlEncoder.text(stringWriter, "Hello World", true);
        assertEquals("Hello World", stringWriter.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testTextWithNullWriter() throws Exception {
        xmlEncoder.text(null, "Hello World", true);
    }

    @Test(expected = NullPointerException.class)
    public void testTextWithNullText() throws Exception {
        xmlEncoder.text(stringWriter, null, true);
    }

    @Test
    public void testTextWithEscapeAmpersands() throws Exception {
        xmlEncoder.text(stringWriter, "AT&T", true);
        assertEquals("AT&amp;T", stringWriter.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter() throws Exception {
        xmlEncoder.text(stringWriter, '©');
    }

    @Test
    public void testTextWithValidCharacter() throws Exception {
        xmlEncoder.text(stringWriter, 'A');
        assertEquals("A", stringWriter.toString());
    }

    @Test
    public void testWhitespace() throws Exception {
        xmlEncoder.whitespace(stringWriter, "   ");
        assertEquals("   ", stringWriter.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testWhitespaceWithNullWriter() throws Exception {
        xmlEncoder.whitespace(null, "   ");
    }

    @Test(expected = NullPointerException.class)
    public void testWhitespaceWithNullText() throws Exception {
        xmlEncoder.whitespace(stringWriter, null);
    }

    @Test(expected = InvalidXMLException.class)
    public void testWhitespaceWithInvalidCharacter() throws Exception {
        xmlEncoder.whitespace(stringWriter, "©");
    }

    @Test
    public void testAttribute() throws Exception {
        xmlEncoder.attribute(stringWriter, "name", "value", '"', true);
        assertEquals(" name=\"value\"", stringWriter.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeWithNullWriter() throws Exception {
        xmlEncoder.attribute(null, "name", "value", '"', true);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeWithNullName() throws Exception {
        xmlEncoder.attribute(stringWriter, null, "value", '"', true);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeWithNullValue() throws Exception {
        xmlEncoder.attribute(stringWriter, "name", null, '"', true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeWithInvalidQuotationMark() throws Exception {
        xmlEncoder.attribute(stringWriter, "name", "value", '$', true);
    }
}