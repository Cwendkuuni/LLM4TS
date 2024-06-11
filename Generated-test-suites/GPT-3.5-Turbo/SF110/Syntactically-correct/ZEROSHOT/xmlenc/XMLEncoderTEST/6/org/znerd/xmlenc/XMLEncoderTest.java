package org.znerd.xmlenc;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.junit.Test;
import static org.junit.Assert.*;

public class XMLEncoderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderWithUnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("UTF-32");
    }

    @Test
    public void testGetEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Writer writer = new StringWriter();
        encoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }

    @Test
    public void testTextWithoutEscapingAmpersands() throws IOException, InvalidXMLException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Writer writer = new StringWriter();
        encoder.text(writer, "this is some <text>", false);
        assertEquals("this is some <text>", writer.toString());
    }

    @Test
    public void testTextWithEscapingAmpersands() throws IOException, InvalidXMLException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Writer writer = new StringWriter();
        encoder.text(writer, "this is some &text&", true);
        assertEquals("this is some &amp;text&amp;", writer.toString());
    }

    @Test
    public void testWhitespace() throws IOException, InvalidXMLException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Writer writer = new StringWriter();
        encoder.whitespace(writer, "   ");
        assertEquals("   ", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeWithUnsupportedQuotationMark() throws IOException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Writer writer = new StringWriter();
        encoder.attribute(writer, "name", "value", '#', false);
    }

    @Test
    public void testAttributeWithDoubleQuotationMarkAndEscapingAmpersands() throws IOException, InvalidXMLException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Writer writer = new StringWriter();
        encoder.attribute(writer, "name", "value &", '\"', true);
        assertEquals(" name=\"value &amp;\"", writer.toString());
    }

    @Test
    public void testAttributeWithSingleQuotationMarkWithoutEscapingAmpersands() throws IOException, InvalidXMLException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Writer writer = new StringWriter();
        encoder.attribute(writer, "name", "value &", '\'', false);
        assertEquals(" name='value &'", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter() throws IOException, InvalidXMLException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Writer writer = new StringWriter();
        encoder.text(writer, '\u001F');
    }
    
    @Test(expected = InvalidXMLException.class)
    public void testAttributeWithInvalidCharacter() throws IOException, InvalidXMLException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Writer writer = new StringWriter();
        encoder.attribute(writer, "name", '\u007F', '\'', false);
    }
}