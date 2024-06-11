import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class XMLEncoderTest {
    
    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
        } catch (Exception e) {
            fail("Failed to create encoder");
        }
    }
    
    @Test
    public void testXMLEncoderConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertNotNull(encoder);
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            fail("Failed to initialize encoder");
        }
    }
    
    @Test
    public void testDeclaration() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (Exception e) {
            fail("Failed to write declaration");
        }
    }
    
    @Test
    public void testTextWithString() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, "Hello World", false);
            assertEquals("Hello World", writer.toString());
        } catch (Exception e) {
            fail("Failed to write text with string");
        }
    }
    
    @Test
    public void testTextWithCharArray() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, new char[]{'H','e','l','l','o'}, 0, 5, true);
            assertEquals("Hello", writer.toString());
        } catch (Exception e) {
            fail("Failed to write text with char array");
        }
    }
    
    @Test
    public void testTextWithChar() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.text(writer, 'a');
            assertEquals("a", writer.toString());
        } catch (Exception e) {
            fail("Failed to write text with char");
        }
    }
    
    @Test
    public void testWhitespaceWithString() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.whitespace(writer, "    ");
            assertEquals("    ", writer.toString());
        } catch (Exception e) {
            fail("Failed to write whitespace with string");
        }
    }
    
    @Test
    public void testWhitespaceWithCharArray() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.whitespace(writer, new char[]{' ',' ',' ',' '}, 0, 4);
            assertEquals("    ", writer.toString());
        } catch (Exception e) {
            fail("Failed to write whitespace with char array");
        }
    }
    
    @Test
    public void testAttribute() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        try {
            encoder.attribute(writer, "name", "value", '\"', true);
            assertEquals(" name=\"value\"", writer.toString());
        } catch (Exception e) {
            fail("Failed to write attribute");
        }
    }
}