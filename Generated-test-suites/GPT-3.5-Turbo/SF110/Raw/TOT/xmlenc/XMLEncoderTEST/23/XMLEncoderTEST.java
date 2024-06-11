
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

public class XMLEncoderTest {

    private XMLEncoder xmlEncoder;

    @Before
    public void setup() throws UnsupportedEncodingException {
        xmlEncoder = new XMLEncoder("UTF-8");
    }

    // Test for getEncoder() method
    @Test
    public void testGetEncoder() {
        try {
            assertNotNull(xmlEncoder.getEncoder("UTF-8"));
        } catch (Exception e) {
            fail("Exception encountered: " + e.getMessage());
        }
    }

    // Test for constructor with encoding parameter
    @Test
    public void testConstructor() {
        try {
            assertNotNull(xmlEncoder);
        } catch (Exception e) {
            fail("Exception encountered: " + e.getMessage());
        }
    }

    // Test for getEncoding() method
    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", xmlEncoder.getEncoding());
    }

    // Test for declaration() method
    @Test
    public void testDeclaration() {
        try {
            StringWriter writer = new StringWriter();
            xmlEncoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (Exception e) {
            fail("Exception encountered: " + e.getMessage());
        }
    }

    // Test for text() method with String parameter
    @Test
    public void testTextWithString() {
        try {
            StringWriter writer = new StringWriter();
            xmlEncoder.text(writer, "This is a test", false);
            assertEquals("This is a test", writer.toString());
        } catch (Exception e) {
            fail("Exception encountered: " + e.getMessage());
        }
    }

    // Test for text() method with char array parameter
    @Test
    public void testTextWithCharArray() {
        try {
            StringWriter writer = new StringWriter();
            xmlEncoder.text(writer, "Hello".toCharArray(), 0, 5, false);
            assertEquals("Hello", writer.toString());
        } catch (Exception e) {
            fail("Exception encountered: " + e.getMessage());
        }
    }

    // Test for text() method with char parameter
    @Test
    public void testTextWithChar() {
        try {
            StringWriter writer = new StringWriter();
            xmlEncoder.text(writer, 'A');
            assertEquals("A", writer.toString());
        } catch (Exception e) {
            fail("Exception encountered: " + e.getMessage());
        }
    }

    // Test for text() method with char parameter and escapeAmpersands set to true
    @Test
    public void testTextWithCharAndEscapeAmpersands() {
        try {
            StringWriter writer = new StringWriter();
            xmlEncoder.text(writer, '&', true);
            assertEquals("&amp;", writer.toString());
        } catch (Exception e) {
            fail("Exception encountered: " + e.getMessage());
        }
    }

    // Test for whitespace() method with String parameter
    @Test
    public void testWhitespaceWithString() {
        try {
            StringWriter writer = new StringWriter();
            xmlEncoder.whitespace(writer, "     ");
            assertEquals("     ", writer.toString());
        } catch (Exception e) {
            fail("Exception encountered: " + e.getMessage());
        }
    }

    // Test for whitespace() method with char array parameter
    @Test
    public void testWhitespaceWithCharArray() {
        try {
            StringWriter writer = new StringWriter();
            xmlEncoder.whitespace(writer, "   \t   \n   \r   ".toCharArray(), 0, 16);
            assertEquals("   \t   \n   \r   ", writer.toString());
        } catch (Exception e) {
            fail("Exception encountered: " + e.getMessage());
        }
    }

    // Test for attribute() method
    @Test
    public void testAttribute() {
        try {
            StringWriter writer = new StringWriter();
            xmlEncoder.attribute(writer, "name", "value", '\"', true);
            assertEquals(" name=\"value\"", writer.toString());
        } catch (Exception e) {
            fail("Exception encountered: " + e.getMessage());
        }
    }

}

