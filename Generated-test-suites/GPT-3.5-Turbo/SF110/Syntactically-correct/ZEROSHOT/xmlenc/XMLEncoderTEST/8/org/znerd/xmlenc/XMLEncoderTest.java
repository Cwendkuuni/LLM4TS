import org.junit.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderWithUnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("UTF-32");
    }

    @Test
    public void testDeclaration() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (NullPointerException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testDeclarationWithNullWriter() throws NullPointerException, IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(null);
    }

    @Test
    public void testTextWithStringAndEscapeAmpersands() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, "This is a test&");
            assertEquals("This is a test&amp;", writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTextWithCharArrayAndEscapeAmpersands() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, "This is a test&".toCharArray(), 0, 14, true);
            assertEquals("This is a test&amp;", writer.toString());
        } catch (NullPointerException | IndexOutOfBoundsException | InvalidXMLException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTextWithCharAndEscapeAmpersands() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, '&', true);
            assertEquals("&amp;", writer.toString());
        } catch (InvalidXMLException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTextWithCharAndNoEscapeAmpersands() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, '&', false);
            assertEquals("&", writer.toString());
        } catch (InvalidXMLException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testWhitespaceWithString() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.whitespace(writer, "  \n\t  ");
            assertEquals("  \n\t  ", writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testWhitespaceWithCharArray() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.whitespace(writer, "  \n\t  ".toCharArray(), 0, 7);
            assertEquals("  \n\t  ", writer.toString());
        } catch (NullPointerException | IndexOutOfBoundsException | InvalidXMLException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testWhitespaceWithNullWriter() throws NullPointerException, IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(null, "  \n\t  ");
    }

    @Test
    public void testAttributeWithString() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.attribute(writer, "attribute", "value&", '\"', true);
            assertEquals(" attribute=\"value&amp;\"", writer.toString());
        } catch (NullPointerException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeWithNullWriter() throws NullPointerException, IOException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(null, "attribute", "value", '\"', true);
    }

    @Test
    public void testAttributeWithInvalidQuotationMark() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.attribute(writer, "attribute", "value", '#', true);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (NullPointerException e) {
            fail("NullPointerException occurred: " + e.getMessage());
        } catch (IllegalArgumentException | IOException e) {
            assertEquals("Character 0x23 ('#') is not a valid quotation mark.", e.getMessage());
        }
    }
}