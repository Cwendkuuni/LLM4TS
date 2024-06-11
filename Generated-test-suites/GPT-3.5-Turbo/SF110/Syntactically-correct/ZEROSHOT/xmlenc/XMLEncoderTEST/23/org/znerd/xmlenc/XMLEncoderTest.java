import org.junit.jupiter.api.Test;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testXMLEncoderConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertNotNull(encoder);
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testXMLEncoderConstructorWithNullEncoding() {
        assertThrows(IllegalArgumentException.class, () -> {
            new XMLEncoder(null);
        });
    }

    @Test
    public void testXMLEncoderConstructorWithInvalidEncoding() {
        assertThrows(UnsupportedEncodingException.class, () -> {
            new XMLEncoder("InvalidEncoding");
        });
    }

    @Test
    public void testDeclaration() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.declaration(writer);
            String output = writer.toString();
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", output);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTextWithEscapeAmpersands() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, "Text with &amp;", true);
            String output = writer.toString();
            assertEquals("Text with &amp;amp;", output);
        } catch (InvalidXMLException | IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTextWithoutEscapeAmpersands() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, "Text with &amp;", false);
            String output = writer.toString();
            assertEquals("Text with &amp;", output);
        } catch (InvalidXMLException | IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTextWithCharArray() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, "Test".toCharArray(), 0, 4, true);
            String output = writer.toString();
            assertEquals("Test", output);
        } catch (InvalidXMLException | IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testTextWithInvalidCharacter() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, '\u007f');
            fail("Exception not thrown");
        } catch (InvalidXMLException | IOException e) {
            assertEquals("The character 0x7f is not valid.", e.getMessage());
        }
    }

    @Test
    public void testWhitespaceWithString() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.whitespace(writer, "   ");
            String output = writer.toString();
            assertEquals("   ", output);
        } catch (InvalidXMLException | IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testWhitespaceWithCharArray() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.whitespace(writer, "   ".toCharArray(), 0, 3);
            String output = writer.toString();
            assertEquals("   ", output);
        } catch (InvalidXMLException | IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testAttribute() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.attribute(writer, "name", "value", '\"', true);
            String output = writer.toString();
            assertEquals(" name=\"value\"", output);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
}