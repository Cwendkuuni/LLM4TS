import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder.getEncoder("UTF-8");
            assertTrue(true);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetEncoder_NullEncoding() {
        assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
    }

    @Test
    public void testGetEncoder_UnsupportedEncoding() {
        assertThrows(UnsupportedEncodingException.class, () -> XMLEncoder.getEncoder("UTF-32"));
    }

    @Test
    public void testXMLEncoder() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testXMLEncoder_NullEncoding() {
        assertThrows(IllegalArgumentException.class, () -> new XMLEncoder(null));
    }

    @Test
    public void testXMLEncoder_UnsupportedEncoding() {
        assertThrows(UnsupportedEncodingException.class, () -> new XMLEncoder("UTF-32"));
    }

    @Test
    public void testXMLEncoder_InvalidCharacter() {
        assertThrows(InvalidXMLException.class, () -> new XMLEncoder("\u0080"));
    }

    @Test
    public void testDeclaration() throws IOException {
        Writer writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testText() throws IOException, InvalidXMLException {
        Writer writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "Test Text", true);
        String expected = "Test Text";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testText_InvalidCharacter() {
        Writer writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertThrows(InvalidXMLException.class, () -> encoder.text(writer, "\u0080", true));
    }

    @Test
    public void testWhitespace() throws IOException, InvalidXMLException {
        Writer writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(writer, "   ");
        String expected = "   ";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testAttribute() throws IOException {
        Writer writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '\"', true);
        String expected = " name=\"value\"";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testAttribute_InvalidQuotationMark() {
        Writer writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertThrows(IllegalArgumentException.class, () -> encoder.attribute(writer, "name", "value", '*', true));
    }

    @Test
    public void testAttribute_InvalidCharacter() {
        Writer writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertThrows(InvalidXMLException.class, () -> encoder.attribute(writer, "name", "\u0080", '\"', true));
    }
}