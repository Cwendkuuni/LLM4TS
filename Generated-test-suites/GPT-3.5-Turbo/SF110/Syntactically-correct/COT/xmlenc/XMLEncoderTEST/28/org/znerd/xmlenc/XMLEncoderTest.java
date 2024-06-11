
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        String encoding = "UTF-8";
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
            assertEquals(encoding, encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        String encoding = "UTF-8";
        try {
            XMLEncoder encoder = new XMLEncoder(encoding);
            assertEquals(encoding, encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeclaration() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            encoder.declaration(writer);
            String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testText() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            encoder.text(writer, "Hello world", true);
            String expected = "Hello world";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTextWithCharArray() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            char[] ch = {'H', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
            encoder.text(writer, ch, 0, ch.length, true);
            String expected = "Hello world";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | IndexOutOfBoundsException | InvalidXMLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTextWithChar() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            encoder.text(writer, 'A');
            String expected = "A";
            assertEquals(expected, writer.toString());
        } catch (InvalidXMLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTextWithCharAndEscapeAmpersand() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            char ch = '&';
            encoder.text(writer, ch, true);
            String expected = "&amp;";
            assertEquals(expected, writer.toString());
        } catch (InvalidXMLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWhitespaceWithString() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            encoder.whitespace(writer, "   ");
            String expected = "   ";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWhitespaceWithCharArray() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            char[] ch = {' ', ' ', ' '};
            encoder.whitespace(writer, ch, 0, ch.length);
            String expected = "   ";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | IndexOutOfBoundsException | InvalidXMLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAttributeWithEscapeAmpersands() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            encoder.attribute(writer, "name", "value", '\"', true);
            String expected = " name=\"value\"";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAttributeWithoutEscapeAmpersands() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            encoder.attribute(writer, "name", "value", '\"', false);
            String expected = " name=\"value\"";
            assertEquals(expected, writer.toString());
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }
}

