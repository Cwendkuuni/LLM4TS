
package org.znerd.xmlenc;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class XMLEncoderTest {

    private XMLEncoder encoder;

    @Before
    public void setUp() throws UnsupportedEncodingException {
        encoder = XMLEncoder.getEncoder("UTF-8");
    }

    @Test
    public void testGetEncoder() {
         XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
         assertNotNull(encoder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNull() {
         XMLEncoder encoder = XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderInvalid() throws UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("InvalidEncoding");
    }

    @Test
    public void testGetEncoding() {
        String encoding = encoder.getEncoding();
        assertEquals("UTF-8", encoding);
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter out = new StringWriter();
        encoder.declaration(out);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", out.toString());
    }

    @Test
    public void testText() throws IOException {
        StringWriter out = new StringWriter();
        encoder.text(out, "This is <tag> & text", false);
        assertEquals("This is <tag> & text", out.toString());
    }

    @Test
    public void testTextSingleChar() throws IOException {
        StringWriter out = new StringWriter();
        encoder.text(out, '<');
        assertEquals("&lt;", out.toString());
    }

    @Test
    public void testTextEscapeAmpersands() throws IOException {
        StringWriter out = new StringWriter();
        encoder.text(out, "This is & text", true);
        assertEquals("This is &amp; text", out.toString());
    }

    @Test
    public void testWhitespace() throws IOException {
        StringWriter out = new StringWriter();
        encoder.whitespace(out, "    ");
        assertEquals("    ", out.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testWhitespaceInvalid() throws IOException {
        StringWriter out = new StringWriter();
        encoder.whitespace(out, "\n");
    }

    @Test
    public void testAttribute() throws IOException {
        StringWriter out = new StringWriter();
        encoder.attribute(out, "name", "value", '"', false);
        assertEquals(" name=\"value\"", out.toString());
    }

    @Test
    public void testAttributeSingleChar() throws IOException {
        StringWriter out = new StringWriter();
        encoder.attribute(out, "name", "<value>", '\'', true);
        assertEquals(" name='&lt;value&gt;'", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeInvalidQuote() throws IOException {
        StringWriter out = new StringWriter();
        encoder.attribute(out, "name", "value", '!', true);
    }
}
