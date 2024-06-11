import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertNotNull(encoder);
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception not expected");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder(null);
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructor_InvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder("InvalidEncoding");
    }

    @Test
    public void testDeclaration() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (IllegalArgumentException | UnsupportedEncodingException | NullPointerException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testText_OneParameter() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, "test text", true);
            assertEquals("test text", writer.toString());
        } catch (InvalidXMLException | NullPointerException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testText_ArrayParameter() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            char[] ch = "test text".toCharArray();
            encoder.text(writer, ch, 0, ch.length, true);
            assertEquals("test text", writer.toString());
        } catch (InvalidXMLException | NullPointerException | IndexOutOfBoundsException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testText_CharacterParameter() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, 't');
            assertEquals("t", writer.toString());
        } catch (InvalidXMLException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testText_CharacterParameter_WithEscapeAmpersands() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, '&', true);
            assertEquals("&amp;", writer.toString());
        } catch (InvalidXMLException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_CharacterParameter_InvalidCharacter() throws InvalidXMLException, IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, (char) 0xFF);
    }

    @Test
    public void testWhitespace_OneParameter() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.whitespace(writer, "    ");
            assertEquals("    ", writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testWhitespace_ArrayParameter() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            char[] ch = "    ".toCharArray();
            encoder.whitespace(writer, ch, 0, ch.length);
            assertEquals("    ", writer.toString());
        } catch (NullPointerException | InvalidXMLException | IndexOutOfBoundsException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testAttribute() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.attribute(writer, "name", "value", '\"', true);
            assertEquals(" name=\"value\"", writer.toString());
        } catch (NullPointerException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_InvalidQuotationMark() throws NullPointerException, IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", "value", '*', true);
    }
}