package org.znerd.xmlenc;

import org.junit.Assert;
import org.junit.Test;
import java.io.*;
import java.io.IOException;

public class XMLEncoderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_InvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("InvalidEncoding");
    }

    @Test
    public void testGetEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Assert.assertEquals("UTF-8", encoder.getEncoding());

        encoder = XMLEncoder.getEncoder("US-ASCII");
        Assert.assertEquals("US-ASCII", encoder.getEncoding());

        encoder = XMLEncoder.getEncoder("ISO-8859-1");
        Assert.assertEquals("ISO-8859-1", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Assert.assertEquals(expected, writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidCharacter() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "Test\u0001", true);
    }

    @Test
    public void testText_EscapeAmpersands() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "This & That", true);
        Assert.assertEquals("This &amp; That", writer.toString());
    }

    @Test
    public void testText_NoEscapeAmpersands() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "This & That", false);
        Assert.assertEquals("This & That", writer.toString());
    }

    @Test
    public void testText_CharArray() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, new char[]{'T', 'e', 's', 't'}, 0, 4, true);
        Assert.assertEquals("Test", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidCharArray() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, new char[]{'T', '\u0001', 's', 't'}, 0, 4, true);
    }

    @Test
    public void testText_Char() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, 'T');
        Assert.assertEquals("T", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidChar() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, '\u0001');
    }

    @Test
    public void testWhitespace_String() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.whitespace(writer, " ");
        Assert.assertEquals(" ", writer.toString());
    }

    @Test
    public void testWhitespace_CharArray() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.whitespace(writer, new char[]{' ', ' ', ' '}, 0, 3);
        Assert.assertEquals("   ", writer.toString());
    }

    @Test
    public void testAttribute() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '\"', true);
        String expected = " name=\"value\"";
        Assert.assertEquals(expected, writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_InvalidQuotationMark() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '|', true);
    }
}