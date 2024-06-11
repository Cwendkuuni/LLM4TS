import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class XMLEncoderTest {

    private XMLEncoder encoder;

    @Before
    public void setUp() throws Exception {
        encoder = new XMLEncoder("UTF-8");
    }

    @Test
    public void testGetEncoder_ValidEncoding_ReturnsXMLEncoderObject() {
        XMLEncoder result = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding_ThrowsIllegalArgumentException() {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_InvalidEncoding_ThrowsUnsupportedEncodingException() {
        XMLEncoder.getEncoder("invalid_encoding");
    }

    @Test
    public void testXMLEncoder_ValidEncoding_InitializesFieldsCorrectly() {
        assertEquals("UTF-8", encoder.getEncoding());
        assertFalse(encoder._sevenBitEncoding);
        assertArrayEquals("UTF-8".toCharArray(), encoder._encodingCharArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXMLEncoder_NullEncoding_ThrowsIllegalArgumentException() {
        new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testXMLEncoder_InvalidEncoding_ThrowsUnsupportedEncodingException() {
        new XMLEncoder("invalid_encoding");
    }

    @Test
    public void testDeclaration_WriterNotNull_WritesDeclaration() throws Exception {
        StringWriter writer = new StringWriter();
        encoder.declaration(writer);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expected, writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testDeclaration_WriterNull_ThrowsNullPointerException() throws Exception {
        encoder.declaration(null);
    }

    @Test
    public void testText_StringWriterNotNull_NoAmpersandEscaping() throws Exception {
        StringWriter writer = new StringWriter();
        encoder.text(writer, "abc", false);
        assertEquals("abc", writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testText_WriterNull_ThrowsNullPointerException() throws Exception {
        encoder.text(null, "abc", true);
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidChar_ThrowsInvalidXMLException() throws Exception {
        StringWriter writer = new StringWriter();
        encoder.text(writer, "\u00C8", true); // Invalid character: È
    }

    @Test
    public void testText_CharArrayWriter_ValidChars_NoAmpersandEscaping() throws Exception {
        CharArrayWriter writer = new CharArrayWriter();
        char[] chars = {'a', 'b', 'c'};
        encoder.text(writer, chars, 0, chars.length, false);
        assertEquals("abc", writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testText_CharsNull_ThrowsNullPointerException() throws Exception {
        encoder.text(new StringWriter(), null, 0, 5, true);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testText_InvalidIndex_ThrowsIndexOutOfBoundsException() throws Exception {
        encoder.text(new StringWriter(), new char[] {'a', 'b', 'c'}, 0, 6, true);
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidCharInCharArray_ThrowsInvalidXMLException() throws Exception {
        char[] chars = {'a', '\u00C8', 'c'}; // Invalid character: È
        encoder.text(new StringWriter(), chars, 0, chars.length, true);
    }

    @Test
    public void testText_CharWriter_ValidChar_NoAmpersandEscaping() throws Exception {
        StringWriter writer = new StringWriter();
        encoder.text(writer, 'a');
        assertEquals("a", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidChar_ThrowsInvalidXMLException() throws Exception {
        StringWriter writer = new StringWriter();
        encoder.text(writer, '\u00C8'); // Invalid character: È
    }

    @Test
    public void testText_CharWriter_ValidChar_EscapeAmpersands() throws Exception {
        StringWriter writer = new StringWriter();
        encoder.text(writer, '&', true);
        assertEquals("&amp;", writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testWhitespace_WriterNull_ThrowsNullPointerException() throws Exception {
        encoder.whitespace(null, "   ");
    }

    @Test(expected = InvalidXMLException.class)
    public void testWhitespace_InvalidWhitespace_ThrowsInvalidXMLException() throws Exception {
        StringWriter writer = new StringWriter();
        encoder.whitespace(writer, " a "); // Invalid whitespace character
    }

    @Test
    public void testWhitespace_ValidWhitespace_WritesWhitespace() throws Exception {
        StringWriter writer = new StringWriter();
        encoder.whitespace(writer, "  ");
        assertEquals("  ", writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAttribute_WriterNull_ThrowsNullPointerException() throws Exception {
        encoder.attribute(null, "name", "value", '\"', true);
    }

    @Test(expected = NullPointerException.class)
    public void testAttribute_NameNull_ThrowsNullPointerException() throws Exception {
        encoder.attribute(new StringWriter(), null, "value", '\"', true);
    }

    @Test(expected = NullPointerException.class)
    public void testAttribute_ValueNull_ThrowsNullPointerException() throws Exception {
        encoder.attribute(new StringWriter(), "name", null, '\"', true);
    }

    @Test
    public void testAttribute_ValidArguments_WritesAttribute() throws Exception {
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", "value", '\"', true);
        assertEquals(" name=\"value\"", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_InvalidQuotationMark_ThrowsIllegalArgumentException() throws Exception {
        encoder.attribute(new StringWriter(), "name", "value", '!', true); // Invalid quotation mark
    }

}