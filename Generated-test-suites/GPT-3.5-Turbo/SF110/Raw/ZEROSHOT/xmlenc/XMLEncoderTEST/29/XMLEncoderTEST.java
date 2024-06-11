
import org.junit.Before;
import org.junit.Test;
import org.znerd.xmlenc.InvalidXMLException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class XMLEncoderTest {

    private XMLEncoder xmlEncoder;

    @Before
    public void setUp() throws UnsupportedEncodingException {
        xmlEncoder = XMLEncoder.getEncoder("UTF-8");
    }

    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", xmlEncoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder invalidXmlEncoder = new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructorWithUnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder invalidXmlEncoder = new XMLEncoder("invalidEncoding");
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter out = new StringWriter();
        xmlEncoder.declaration(out);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", out.toString());
    }

    @Test
    public void testTextWithoutEscapeAmpersands() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        xmlEncoder.text(out, "Hello World", false);
        assertEquals("Hello World", out.toString());
    }

    @Test
    public void testTextWithEscapeAmpersands() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        xmlEncoder.text(out, "Hello & World", true);
        assertEquals("Hello &amp; World", out.toString());
    }

    @Test
    public void testCharTextWithoutEscapeAmpersands() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        xmlEncoder.text(out, 'A');
        assertEquals("A", out.toString());
    }

    @Test
    public void testCharTextWithEscapeAmpersands() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        xmlEncoder.text(out, '&', true);
        assertEquals("&amp;", out.toString());
    }

    @Test
    public void testWhitespace() throws IOException, InvalidXMLException {
        StringWriter out = new StringWriter();
        xmlEncoder.whitespace(out, " ");
        assertEquals(" ", out.toString());
    }

    @Test
    public void testAttributeWithDoubleQuotationMark() throws IOException {
        StringWriter out = new StringWriter();
        xmlEncoder.attribute(out, "name", "value", '\"', true);
        assertEquals(" name=\"value\"", out.toString());
    }

    @Test
    public void testAttributeWithSingleQuotationMark() throws IOException {
        StringWriter out = new StringWriter();
        xmlEncoder.attribute(out, "name", "value", '\'', true);
        assertEquals(" name='value'", out.toString());
    }

}
