import org.junit.*;
import java.io.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder.getEncoder("UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testXMLEncoder() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            Assert.assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXMLEncoderNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testXMLEncoderUnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder("invalid_encoding");
    }

    @Test
    public void testDeclaration() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder("UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        try {
            encoder.declaration(writer);
            Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (NullPointerException | IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testText() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder("UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        try {
            encoder.text(writer, "Test", true);
            Assert.assertEquals("Test", writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTextCharArray() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder("UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        try {
            encoder.text(writer, new char[]{'T', 'e', 's', 't'}, 0, 4, true);
            Assert.assertEquals("Test", writer.toString());
        } catch (NullPointerException | IndexOutOfBoundsException | InvalidXMLException | IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTextChar() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder("UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        try {
            encoder.text(writer, 'T');
            Assert.assertEquals("T", writer.toString());
        } catch (InvalidXMLException | IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTextCharEscapeAmpersands() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder("UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        try {
            encoder.text(writer, 'T', true);
            Assert.assertEquals("T", writer.toString());
        } catch (InvalidXMLException | IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testWhiteSpace() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.whitespace(writer, " ");
            Assert.assertEquals(" ", writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testWhiteSpaceCharArray() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            char[] ch = {' '};
            encoder.whitespace(writer, ch, 0, 1);
            Assert.assertEquals(" ", writer.toString());
        } catch (NullPointerException | IndexOutOfBoundsException | InvalidXMLException | IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAttribute() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder("UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        try {
            encoder.attribute(writer, "name", "value", '\"', true);
            Assert.assertEquals(" name=\"value\"", writer.toString());
        } catch (NullPointerException | IOException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeInvalidQuotationMark() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder("UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        encoder.attribute(writer, "name", "value", '*', true);
    }
}