import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class XMLEncoderTest {
    // Test the getEncoder() method
    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder.getEncoder("UTF-8");
            XMLEncoder.getEncoder("UTF-16");
            XMLEncoder.getEncoder("US-ASCII");
            XMLEncoder.getEncoder("ISO-8859-1");
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    // Test the constructor
    @Test
    public void testConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    // Test the declaration() method
    @Test
    public void testDeclaration() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    // Test the text() method with text String
    @Test
    public void testTextWithString() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.text(writer, "Hello World", false);
            assertEquals("Hello World", writer.toString());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    // Test the text() method with text char array
    @Test
    public void testTextWithCharArray() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.text(writer, new char[]{'H', 'i'}, 0, 2, false);
            assertEquals("Hi", writer.toString());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    // Test the text() method with single char
    @Test
    public void testTextWithSingleChar() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.text(writer, 'A');
            assertEquals("A", writer.toString());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    // Test the whitespace() method with String
    @Test
    public void testWhitespaceWithString() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.whitespace(writer, "   ");
            assertEquals("   ", writer.toString());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    // Test the whitespace() method with char array
    @Test
    public void testWhitespaceWithCharArray() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.whitespace(writer, new char[]{' ', ' ', ' '}, 0, 3);
            assertEquals("   ", writer.toString());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    // Test the attribute() method
    @Test
    public void testAttribute() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.attribute(writer, "name", "value", '\"', false);
            assertEquals(" name=\"value\"", writer.toString());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }
}

