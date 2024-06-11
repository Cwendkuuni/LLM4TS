import static org.junit.Assert.*;
import org.junit.Test;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder_typicalCase() {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
        assertTrue(encoder._sevenBitEncoding);
        assertEquals("UTF-8", encoder._encoding);
        assertArrayEquals("UTF-8".toCharArray(), encoder._encodingCharArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_nullEncoding() {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_invalidEncoding() {
        XMLEncoder.getEncoder("invalid");
    }

    @Test
    public void testConstructor_typicalCase() throws UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder("UTF-16");
        assertEquals("UTF-16", encoder.getEncoding());
        assertFalse(encoder._sevenBitEncoding);
        assertEquals("UTF-16", encoder._encoding);
        assertArrayEquals("UTF-16".toCharArray(), encoder._encodingCharArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullEncoding() throws UnsupportedEncodingException {
        new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructor_invalidEncoding() throws UnsupportedEncodingException {
        new XMLEncoder("invalid");
    }

    @Test
    public void testDeclaration_typicalCase() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testDeclaration_nullWriter() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(null);
    }

    @Test
    public void testWhitespace_typicalCase() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(writer, "  ");
        assertEquals("  ", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testWhitespace_invalidCharacters() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(writer, "test\n");
    }

    @Test(expected = NullPointerException.class)
    public void testWhitespace_nullWriter() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(null, "  ");
    }

    @Test
    public void testText_typicalCase() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "Hello, World!", true);
        assertEquals("Hello, World!", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_invalidCharacters() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "Hello, \u0007", true);
    }

    @Test(expected = NullPointerException.class)
    public void testText_nullWriter() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(null, "Hello, World!", true);
    }

    @Test
    public void testAttribute_typicalCase() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '"', true);
        assertEquals(" name=\"value\"", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_invalidQuotationMark() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '$', true);
    }

    @Test(expected = NullPointerException.class)
    public void testAttribute_nullWriter() throws IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(null, "name", "value", '"', true);
    }

}