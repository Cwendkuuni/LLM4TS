package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

public class XMLEncoderTest {

    private static final String ENCODING = "UTF-8";
    private XMLEncoder encoder;

    @Before
    public void setUp() throws Exception {
        encoder = XMLEncoder.getEncoder(ENCODING);
    }

    @Test
    public void testGetEncoding() {
        assertEquals(ENCODING, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() throws Exception {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderWithInvalidEncoding() throws Exception {
        XMLEncoder.getEncoder("invalid encoding");
    }

    @Test
    public void testDeclaration() throws Exception {
        Writer writer = new StringWriter();
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"" + ENCODING + "\"?>", writer.toString());
    }

    @Test
    public void testTextWithEscapeAmpersands() throws Exception {
        Writer writer = new StringWriter();
        encoder.text(writer, "hello & world", true);
        assertEquals("hello &amp; world", writer.toString());
    }

    @Test
    public void testTextWithoutEscapeAmpersands() throws Exception {
        Writer writer = new StringWriter();
        encoder.text(writer, "hello & world", false);
        assertEquals("hello & world", writer.toString());
    }

    @Test
    public void testTextWithInvalidCharacter() throws Exception {
        Writer writer = new StringWriter();
        try {
            encoder.text(writer, "\u001B", true);
        } catch (InvalidXMLException e) {
            assertEquals("The character 0x1b is not valid.", e.getMessage());
        }
    }

    @Test
    public void testTextWithSpecialCharacters() throws Exception {
        Writer writer = new StringWriter();
        encoder.text(writer, "<>&\"'", true);
        assertEquals("&lt;&gt;&amp;&quot;&apos;", writer.toString());
    }

    @Test
    public void testTextWithEscapeAmpersand() throws Exception {
        Writer writer = new StringWriter();
        encoder.text(writer, '&', true);
        assertEquals("&amp;", writer.toString());
    }

    @Test
    public void testWhitespace() throws Exception {
        Writer writer = new StringWriter();
        encoder.whitespace(writer, "   ");
        assertEquals("   ", writer.toString());
    }

    @Test
    public void testAttribute() throws Exception {
        Writer writer = new StringWriter();
        encoder.attribute(writer, "name", "value", '\'', true);
        assertEquals(" name='value'", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeWithInvalidQuotationMark() throws Exception {
        Writer writer = new StringWriter();
        encoder.attribute(writer, "name", "value", '@', true);
    }
}