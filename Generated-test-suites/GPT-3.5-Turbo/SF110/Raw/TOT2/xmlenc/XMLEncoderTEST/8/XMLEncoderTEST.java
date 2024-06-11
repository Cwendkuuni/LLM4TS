import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.io.*;

public class XMLEncoderTest {
    private XMLEncoder xmlEncoder;

    @Before
    public void setUp() {
        xmlEncoder = new XMLEncoder("UTF-8");
    }

    @After
    public void tearDown() {
        xmlEncoder = null;
    }

    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", xmlEncoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter out = new StringWriter();
        xmlEncoder.declaration(out);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", out.toString());
    }

    @Test
    public void testTextCharArray() throws IOException {
        StringWriter out = new StringWriter();
        xmlEncoder.text(out, "Hello World", true);
        assertEquals("Hello World", out.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextChar() throws IOException {
        StringWriter out = new StringWriter();
        xmlEncoder.text(out, '\u0100');
    }

    @Test
    public void testWhitespace() throws IOException {
        StringWriter out = new StringWriter();
        xmlEncoder.whitespace(out, "   ");
        assertEquals("   ", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute() throws IOException {
        StringWriter out = new StringWriter();
        xmlEncoder.attribute(out, "name", "value", '>', true);
    }

    @Test
    public void testInvalidEncoding() {
        try {
            XMLEncoder invalidEncoder = new XMLEncoder("InvalidEncoding");
            fail("Should have thrown UnsupportedEncodingException");
        } catch (UnsupportedEncodingException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testInvalidText() throws IOException {
        StringWriter out = new StringWriter();
        try {
            xmlEncoder.text(out, "\u0000", true);
            fail("Should have thrown InvalidXMLException");
        } catch (InvalidXMLException e) {
            assertTrue(true);
        }
    }
}