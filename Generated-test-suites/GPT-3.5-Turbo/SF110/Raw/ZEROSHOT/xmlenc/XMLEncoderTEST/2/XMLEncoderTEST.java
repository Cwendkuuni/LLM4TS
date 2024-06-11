import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import org.znerd.xmlenc.*;

public class XMLEncoderTest {

    private XMLEncoder xmlEncoder;

    @Before
    public void setUp() throws Exception {
        xmlEncoder = new XMLEncoder("UTF-8");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testUnsupportedEncodingException() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("UTF-32");
    }

    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", xmlEncoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        writer.close();
    }

    @Test
    public void testTextWithoutEscape() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, "This is a text", false);
        assertEquals("This is a text", writer.toString());
        writer.close();
    }

    @Test
    public void testTextWithEscape() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, "This is a <text>", true);
        assertEquals("This is a &lt;text&gt;", writer.toString());
        writer.close();
    }

    @Test
    public void testTextCharArray() throws IOException {
        StringWriter writer = new StringWriter();
        char[] ch = {'T', 'e', 's', 't'};
        xmlEncoder.text(writer, ch, 0, ch.length, true);
        assertEquals("Test", writer.toString());
        writer.close();
    }

    @Test
    public void testTextChar() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, 'T');
        assertEquals("T", writer.toString());
        writer.close();
    }

    @Test(expected = InvalidXMLException.class)
    public void testInvalidTextChar() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, 'ä');
        writer.close();
    }

    @Test
    public void testTextCharWithEscape() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, '<', true);
        assertEquals("&lt;", writer.toString());
        writer.close();
    }

    @Test
    public void testWhitespacesString() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.whitespace(writer, "   ");
        assertEquals("   ", writer.toString());
        writer.close();
    }

    @Test
    public void testWhitespaceCharArray() throws IOException {
        StringWriter writer = new StringWriter();
        char[] ch = {' ', ' ', ' '};
        xmlEncoder.whitespace(writer, ch, 0, ch.length);
        assertEquals("   ", writer.toString());
        writer.close();
    }

    @Test
    public void testAttribute() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.attribute(writer, "name", "value", '\"', true);
        assertEquals(" name=\"value\"", writer.toString());
        writer.close();
    }

    @Test
    public void testAttributeWithEscape() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.attribute(writer, "name", "value<", '\"', true);
        assertEquals(" name=\"value&lt;\"", writer.toString());
        writer.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidQuotationMarkAttribute() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.attribute(writer, "name", "value", '!', true);
        writer.close();
    }

}