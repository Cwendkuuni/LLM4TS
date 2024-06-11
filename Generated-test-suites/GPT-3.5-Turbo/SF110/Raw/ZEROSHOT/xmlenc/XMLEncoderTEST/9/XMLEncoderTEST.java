package org.znerd.xmlenc;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.io.UnsupportedEncodingException;

public class XMLEncoderTest {
    private XMLEncoder xmlEncoder;

    @Before
    public void setUp() throws UnsupportedEncodingException {
        xmlEncoder = XMLEncoder.getEncoder("UTF-8");
    }

    @After
    public void tearDown() {
        xmlEncoder = null;
    }

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            Assert.assertNotNull(encoder);
            Assert.assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_InvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("invalid");
    }

    @Test
    public void testConstructor_SevenBitEncoding() {
        try {
            XMLEncoder encoder = new XMLEncoder("US-ASCII");
            Assert.assertEquals("US-ASCII", encoder.getEncoding());
            Assert.assertTrue(encoder._sevenBitEncoding);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor_NotSevenBitEncoding() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            Assert.assertEquals("UTF-8", encoder.getEncoding());
            Assert.assertFalse(encoder._sevenBitEncoding);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder(null);
    }

    @Test
    public void testGetEncoding() {
        Assert.assertEquals("UTF-8", xmlEncoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.declaration(writer);
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }

    @Test
    public void testText() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, "sample text", true);
        Assert.assertEquals("sample text", writer.toString());
    }

    @Test
    public void testText_CharArray() throws IOException {
        StringWriter writer = new StringWriter();
        char[] ch = "sample text".toCharArray();
        xmlEncoder.text(writer, ch, 0, ch.length, true);
        Assert.assertEquals("sample text", writer.toString());
    }

    @Test
    public void testText_char() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, 'a');
        Assert.assertEquals("a", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidChar() throws Exception {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, '\t');
    }

    @Test
    public void testText_CharWithEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, '&', true);
        Assert.assertEquals("&amp;", writer.toString());
    }

    @Test
    public void testText_CharWithoutEscapeAmpersands() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, '&', false);
        Assert.assertEquals("&", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidCharWithEscapeAmpersands() throws Exception {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, '\t', true);
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidCharWithoutEscapeAmpersands() throws Exception {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, '\t', false);
    }

    @Test
    public void testWhitespace_String() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.whitespace(writer, "  sample text  ");
        Assert.assertEquals("  sample text  ", writer.toString());
    }

    @Test
    public void testWhitespace_CharArray() throws IOException {
        StringWriter writer = new StringWriter();
        char[] ch = "  sample text  ".toCharArray();
        xmlEncoder.whitespace(writer, ch, 0, ch.length);
        Assert.assertEquals("  sample text  ", writer.toString());
    }

    @Test
    public void testAttribute() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.attribute(writer, "name", "value", '"', true);
        Assert.assertEquals(" name=\"value\"", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_InvalidQuotationMark() throws IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.attribute(writer, "name", "value", '!', true);
    }
}