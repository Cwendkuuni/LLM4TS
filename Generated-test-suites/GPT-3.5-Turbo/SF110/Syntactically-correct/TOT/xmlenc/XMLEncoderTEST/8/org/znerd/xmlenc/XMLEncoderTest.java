import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.io.ByteArrayOutputStream;
import java.io.Writer;

public class XMLEncoderTest {

    // Test case for getEncoder method
    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder xmlEncoder = XMLEncoder.getEncoder("UTF-8");
            assertEquals("UTF-8", xmlEncoder.getEncoding());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // Test case for XMLEncoder constructor
    @Test
    public void testXMLEncoderConstructor() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            assertEquals("UTF-8", xmlEncoder.getEncoding());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // Test case for declaration method
    @Test
    public void testDeclaration() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new StringWriter();
            xmlEncoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // Test case for text method with String input
    @Test
    public void testTextWithString() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new StringWriter();
            xmlEncoder.text(writer, "Hello World", true);
            assertEquals("Hello World", writer.toString());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // Test case for text method with char array input
    @Test
    public void testTextWithCharArray() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new StringWriter();
            xmlEncoder.text(writer, "Hello World".toCharArray(), 0, 11, true);
            assertEquals("Hello World", writer.toString());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // Test case for whitespace method with String input
    @Test
    public void testWhitespaceWithString() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new StringWriter();
            xmlEncoder.whitespace(writer, "   ");
            assertEquals("   ", writer.toString());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // Test case for whitespace method with char array input
    @Test
    public void testWhitespaceWithCharArray() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new StringWriter();
            xmlEncoder.whitespace(writer, "\n\t\t");
            assertEquals("\n\t\t", writer.toString());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // Test case for attribute method
    @Test
    public void testAttribute() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new StringWriter();
            xmlEncoder.attribute(writer, "name", "value", '\'', true);
            assertEquals(" name='value'", writer.toString());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // Test case for text method with invalid character
    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter() {
        XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new StringWriter();
        xmlEncoder.text(writer, (char) 128);
    }

    // Test case for attribute method with invalid quotation mark
    @Test(expected = IllegalArgumentException.class)
    public void testAttributeWithInvalidQuotationMark() {
        XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new StringWriter();
        xmlEncoder.attribute(writer, "name", "value", 'X', true);
    }

    // Test case for XMLEncoder constructor with null encoding
    @Test(expected = IllegalArgumentException.class)
    public void testXMLEncoderConstructorWithNullEncoding() {
        XMLEncoder xmlEncoder = new XMLEncoder(null);
    }

    // Test case for XMLEncoder constructor with unsupported encoding
    @Test(expected = UnsupportedEncodingException.class)
    public void testXMLEncoderConstructorWithUnsupportedEncoding() {
        XMLEncoder xmlEncoder = new XMLEncoder("UTF-32");
    }

}