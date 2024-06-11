import org.junit.Before;
import org.junit.Test;
import java.io.StringWriter;
import java.io.Writer;
import static org.junit.Assert.*;

public class XMLEncoderTest {

    private XMLEncoder encoder;

    @Before
    public void setUp() throws Exception {
        encoder = new XMLEncoder("UTF-8");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_InvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("InvalidEncoding");
    }

    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws Exception {
        Writer writer = new StringWriter();
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }

    @Test
    public void testText_String_EscapeAmpersands() throws Exception {
        Writer writer = new StringWriter();
        encoder.text(writer, "hello &amp; world", true);
        assertEquals("hello &amp;amp; world", writer.toString());
    }

    @Test
    public void testText_String_NotEscapeAmpersands() throws Exception {
        Writer writer = new StringWriter();
        encoder.text(writer, "hello &amp; world", false);
        assertEquals("hello &amp; world", writer.toString());
    }

    @Test
    public void testText_CharArray() throws Exception {
        Writer writer = new StringWriter();
        encoder.text(writer, "hello".toCharArray(), 0, 5, true);
        assertEquals("hello", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_CharArray_InvalidCharacter() throws Exception {
        Writer writer = new StringWriter();
        encoder.text(writer, new char[] { '\u0080' }, 0, 1, true);
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_CharArray_InvalidCharacter_SevenBitEncoding() throws Exception {
        encoder = new XMLEncoder("US-ASCII");
        Writer writer = new StringWriter();
        encoder.text(writer, new char[] { '\u0080' }, 0, 1, true);
    }

    @Test
    public void testText_Char() throws Exception {
        Writer writer = new StringWriter();
        encoder.text(writer, 'a');
        assertEquals("a", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_Char_InvalidCharacter() throws Exception {
        Writer writer = new StringWriter();
        encoder.text(writer, '\u0080');
    }

    @Test
    public void testText_Char_EscapeAmpersands() throws Exception {
        Writer writer = new StringWriter();
        encoder.text(writer, '&', true);
        assertEquals("&amp;", writer.toString());
    }

    @Test
    public void testText_Char_NotEscapeAmpersands() throws Exception {
        Writer writer = new StringWriter();
        encoder.text(writer, '&', false);
        assertEquals("&", writer.toString());
    }

    @Test
    public void testWhitespace_String() throws Exception {
        Writer writer = new StringWriter();
        encoder.whitespace(writer, "  \t\r\n");
        assertEquals("  \t\r\n", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testWhitespace_CharArray_InvalidCharacter() throws Exception {
        Writer writer = new StringWriter();
        encoder.whitespace(writer, new char[] { '\u0001' }, 0, 1);
    }

    @Test
    public void testAttribute() throws Exception {
        Writer writer = new StringWriter();
        encoder.attribute(writer, "name", "value", '"', true);
        assertEquals(" name=\"value\"", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_InvalidQuotationMark() throws Exception {
        Writer writer = new StringWriter();
        encoder.attribute(writer, "name", "value", '#', true);
    }

    @Test(expected = InvalidXMLException.class)
    public void testAttribute_InvalidCharacter() throws Exception {
        Writer writer = new StringWriter();
        encoder.attribute(writer, "name", "value\"", '"', true);
    }
}