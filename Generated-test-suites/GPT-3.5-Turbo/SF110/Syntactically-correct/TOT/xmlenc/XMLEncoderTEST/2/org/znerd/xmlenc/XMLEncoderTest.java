import org.junit.BeforeClass;
import org.junit.Test;
import java.io.StringWriter;
import java.io.Writer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static org.junit.Assert.*;

public class XMLEncoderTest{
    private static XMLEncoder xmlEncoder;

    @BeforeClass
    public static void setUp() throws UnsupportedEncodingException {
        xmlEncoder = XMLEncoder.getEncoder("UTF-8");
    }

    @Test
    public void testGetEncoder_ValidEncoding_ReturnsEncoder() throws UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_InvalidEncoding_ThrowsException() throws UnsupportedEncodingException {
        XMLEncoder.getEncoder("invalidEncoding");
    }

    @Test
    public void testDeclaration_ValidWriter_WritesDeclaration() throws IOException {
        Writer writer = new StringWriter();
        xmlEncoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testDeclaration_NullWriter_ThrowsException() throws IOException {
        xmlEncoder.declaration(null);
    }

    @Test
    public void testText_ValidWriterAndText_EscapesSpecialCharacters() throws IOException {
        Writer writer = new StringWriter();
        String text = "<text>";
        xmlEncoder.text(writer, text, false);
        assertEquals("&lt;text&gt;", writer.toString());
    }

    @Test
    public void testText_ValidWriterAndCharArray_EscapesSpecialCharacters() throws IOException {
        Writer writer = new StringWriter();
        char[] ch = "<text>".toCharArray();
        xmlEncoder.text(writer, ch, 0, ch.length, false);
        assertEquals("&lt;text&gt;", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_ValidWriterAndCharArray_ThrowsExceptionForInvalidCharacter() throws IOException {
        Writer writer = new StringWriter();
        char[] ch = {0xffff};
        xmlEncoder.text(writer, ch, 0, ch.length, false);
    }

    @Test
    public void testText_ValidWriterAndChar_EscapesSpecialCharacter() throws IOException {
        Writer writer = new StringWriter();
        char c = '<';
        xmlEncoder.text(writer, c);
        assertEquals("&lt;", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_ValidWriterAndChar_ThrowsExceptionForInvalidCharacter() throws IOException {
        Writer writer = new StringWriter();
        char c = 0xffff;
        xmlEncoder.text(writer, c);
    }

    @Test
    public void testText_ValidWriterCharAndEscapeAmpersands_EscapesAmpersands() throws IOException {
        Writer writer = new StringWriter();
        char c = '&';
        xmlEncoder.text(writer, c, true);
        assertEquals("&amp;", writer.toString());
    }

    @Test
    public void testWhitespace_ValidWriterAndString_WritesWhitespace() throws IOException {
        Writer writer = new StringWriter();
        String whitespace = "  ";
        xmlEncoder.whitespace(writer, whitespace);
        assertEquals("  ", writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testWhitespace_NullWriter_ThrowsException() throws IOException {
        xmlEncoder.whitespace(null, "  ");
    }

    @Test(expected = InvalidXMLException.class)
    public void testWhitespace_InvalidWhitespace_ThrowsException() throws IOException {
        Writer writer = new StringWriter();
        String whitespace = " invalid white space";
        xmlEncoder.whitespace(writer, whitespace);
    }

    @Test
    public void testAttribute_ValidWriterNameValueAndQuotationMark_WritesAttribute() throws IOException {
        Writer writer = new StringWriter();
        String name = "attr";
        String value = "value";
        char quotationMark = '\"';
        xmlEncoder.attribute(writer, name, value, quotationMark, false);
        assertEquals(" attr=\"value\"", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_InvalidQuotationMark_ThrowsException() throws IOException {
        Writer writer = new StringWriter();
        String name = "attr";
        String value = "value";
        char quotationMark = '*';
        xmlEncoder.attribute(writer, name, value, quotationMark, false);
    }

    @Test(expected = NullPointerException.class)
    public void testAttribute_NullWriter_ThrowsException() throws IOException {
        String name = "attr";
        String value = "value";
        char quotationMark = '\"';
        xmlEncoder.attribute(null, name, value, quotationMark, false);
    }

    @Test
    public void testAttribute_ValidWriterNameValueEscapeAmpersandsAndQuotationMark_WritesAttribute() throws IOException {
        Writer writer = new StringWriter();
        String name = "attr";
        String value = "value&";
        char quotationMark = '\"';
        xmlEncoder.attribute(writer, name, value, quotationMark, true);
        assertEquals(" attr=\"value&amp;\"", writer.toString());
    }
}