import org.junit.*;
import java.io.*;
import static org.junit.Assert.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetEncoderWithInvalidEncoding() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("InvalidEncoding");
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals("UnsupportedEncodingException", e.getClass().getSimpleName());
        }
    }

    @Test
    public void testXMLEncoder() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testXMLEncoderWithNullEncoding() {
        try {
            XMLEncoder encoder = new XMLEncoder(null);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals("IllegalArgumentException", e.getClass().getSimpleName());
        }
    }

    @Test
    public void testDeclaration() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.declaration(writer);
            String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            assertEquals(expected, writer.toString());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testText() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, "HelloWorld", false);
            assertEquals("HelloWorld", writer.toString());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTextWithEscapeAmpersands() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, "&&", true);
            assertEquals("&amp;&amp;", writer.toString());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTextWithInvalidXML() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, "<>", false);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals("InvalidXMLException", e.getClass().getSimpleName());
        }
    }

    @Test
    public void testTextWithInvalidXMLAndEscapeAmpersands() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, "<>", true);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals("InvalidXMLException", e.getClass().getSimpleName());
        }
    }

    @Test
    public void testTextWithChar() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, 'A');
            assertEquals("A", writer.toString());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTextWithCharAndEscapeAmpersands() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, '&', true);
            assertEquals("&amp;", writer.toString());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTextWithCharAndInvalidXML() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, '<');
            assertEquals("&lt;", writer.toString());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTextWithCharAndInvalidXMLAndEscapeAmpersands() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, '<', true);
            assertEquals("&lt;", writer.toString());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testWhitespace() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.whitespace(writer, "  ");
            assertEquals("  ", writer.toString());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testAttribute() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.attribute(writer, "name", "value", '\"', false);
            assertEquals(" name=\"value\"", writer.toString());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testAttributeWithInvalidQuotationMark() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.attribute(writer, "name", "value", '\'', false);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals("IllegalArgumentException", e.getClass().getSimpleName());
        }
    }

    @Test
    public void testAttributeWithEscapeAmpersands() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.attribute(writer, "name", "&&", '\"', true);
            assertEquals(" name=\"&amp;&amp;\"", writer.toString());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
}