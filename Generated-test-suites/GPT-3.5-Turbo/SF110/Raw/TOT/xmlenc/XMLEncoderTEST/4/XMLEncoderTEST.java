import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.io.StringWriter;

public class XMLEncoderTest {
    
    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testGetEncoder_NullEncoding() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder(null);
            fail("Exception not occurred");
        } catch (IllegalArgumentException e) {
            assertEquals("encoding == null", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testGetEncoder_UnsupportedEncoding() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-32");
            fail("Exception not occurred");
        } catch (IllegalArgumentException e) {
            assertFalse("UnsupportedEncodingException not occurred", e instanceof UnsupportedEncodingException);
        } catch (UnsupportedEncodingException e) {
            assertEquals("UTF-32", e.getMessage());
        }
    }
    
    @Test
    public void testGetEncoding() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testDeclaration() {
        XMLEncoder encoder;
        StringWriter out;
        try {
            encoder = XMLEncoder.getEncoder("UTF-8");
            out = new StringWriter();
            encoder.declaration(out);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", out.toString());
        } catch (IllegalArgumentException | UnsupportedEncodingException | IOException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testText() {
        XMLEncoder encoder;
        StringWriter out;
        try {
            encoder = XMLEncoder.getEncoder("UTF-8");
            out = new StringWriter();
            encoder.text(out, "Test", false);
            assertEquals("Test", out.toString());
        } catch (IllegalArgumentException | UnsupportedEncodingException | IOException | InvalidXMLException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testText_EscapeAmpersands() {
        XMLEncoder encoder;
        StringWriter out;
        try {
            encoder = XMLEncoder.getEncoder("UTF-8");
            out = new StringWriter();
            encoder.text(out, "Text &", true);
            assertEquals("Test &amp;", out.toString());
        } catch (IllegalArgumentException | UnsupportedEncodingException | IOException | InvalidXMLException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testText_CharArray() {
        XMLEncoder encoder;
        StringWriter out;
        try {
            encoder = XMLEncoder.getEncoder("UTF-8");
            out = new StringWriter();
            char[] ch = {'T', 'e', 's', 't'};
            encoder.text(out, ch, 0, ch.length, false);
            assertEquals("Test", out.toString());
        } catch (IllegalArgumentException | UnsupportedEncodingException | IOException | InvalidXMLException | IndexOutOfBoundsException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testText_CharArray_EscapeAmpersands() {
        XMLEncoder encoder;
        StringWriter out;
        try {
            encoder = XMLEncoder.getEncoder("UTF-8");
            out = new StringWriter();
            char[] ch = {'T', 'e', 's', 't', '&'};
            encoder.text(out, ch, 0, ch.length, true);
            assertEquals("Test &amp;", out.toString());
        } catch (IllegalArgumentException | UnsupportedEncodingException | IOException | InvalidXMLException | IndexOutOfBoundsException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testText_Character() {
        XMLEncoder encoder;
        StringWriter out;
        try {
            encoder = XMLEncoder.getEncoder("UTF-8");
            out = new StringWriter();
            encoder.text(out, 'T');
            assertEquals("T", out.toString());
        } catch (IllegalArgumentException | UnsupportedEncodingException | IOException | InvalidXMLException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testText_Character_EscapeAmpersands() {
        XMLEncoder encoder;
        StringWriter out;
        try {
            encoder = XMLEncoder.getEncoder("UTF-8");
            out = new StringWriter();
            encoder.text(out, '&', true);
            assertEquals("&amp;", out.toString());
        } catch (IllegalArgumentException | UnsupportedEncodingException | IOException | InvalidXMLException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testWhitespace_String() {
        XMLEncoder encoder;
        StringWriter out;
        try {
            encoder = XMLEncoder.getEncoder("UTF-8");
            out = new StringWriter();
            encoder.whitespace(out, "     ");
            assertEquals("     ", out.toString());
        } catch (IllegalArgumentException | UnsupportedEncodingException | IOException | InvalidXMLException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testWhitespace_CharArray() {
        XMLEncoder encoder;
        StringWriter out;
        try {
            encoder = XMLEncoder.getEncoder("UTF-8");
            out = new StringWriter();
            char[] ch = {' ', ' ', ' ', ' '};
            encoder.whitespace(out, ch, 0, ch.length);
            assertEquals("    ", out.toString());
        } catch (IllegalArgumentException | UnsupportedEncodingException | IOException | InvalidXMLException | IndexOutOfBoundsException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testAttribute() {
        XMLEncoder encoder;
        StringWriter out;
        try {
            encoder = XMLEncoder.getEncoder("UTF-8");
            out = new StringWriter();
            encoder.attribute(out, "name", "value", '\"', false);
            assertEquals(" name=\"value\"", out.toString());
        } catch (IllegalArgumentException | UnsupportedEncodingException | IOException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testAttribute_InvalidQuotation() {
        XMLEncoder encoder;
        StringWriter out;
        try {
            encoder = XMLEncoder.getEncoder("UTF-8");
            out = new StringWriter();
            encoder.attribute(out, "name", "value", ' ', false);
            fail("Exception not occurred");
        } catch (IllegalArgumentException e) {
            assertEquals("Character 0x20 (' ') is not a valid quotation mark.", e.getMessage());
        } catch (IOException e) {
            fail("Exception occurred");
        }
    }
    
}