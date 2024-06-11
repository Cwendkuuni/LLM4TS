package org.znerd.xmlenc;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.junit.Test;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder_validEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertNotNull(encoder);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_nullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_invalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("INVALID-ENCODING");
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expectedDeclaration, writer.toString());
    }

    @Test
    public void testText_noSpecialCharacters() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String text = "This is a test";
        encoder.text(writer, text, false);
        assertEquals(text, writer.toString());
    }

    @Test
    public void testText_withSpecialCharacters_escapeAmpersandsTrue() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String text = "This <tag> & that";
        String expectedText = "This &lt;tag&gt; &amp; that";
        encoder.text(writer, text, true);
        assertEquals(expectedText, writer.toString());
    }

    @Test
    public void testText_withSpecialCharacters_escapeAmpersandsFalse() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String text = "This <tag> & that";
        String expectedText = "This <tag> &amp; that";
        encoder.text(writer, text, false);
        assertEquals(expectedText, writer.toString());
    }

    @Test
    public void testText_singleCharacter_escapeAmpersandsTrue() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        char c = '<';
        String expectedText = "&lt;";
        encoder.text(writer, c, true);
        assertEquals(expectedText, writer.toString());
    }

    @Test
    public void testText_singleCharacter_escapeAmpersandsFalse() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        char c = '<';
        String expectedText = "<";
        encoder.text(writer, c, false);
        assertEquals(expectedText, writer.toString());
    }

    @Test
    public void testWhitespace() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String whitespace = "   ";
        encoder.whitespace(writer, whitespace);
        assertEquals(whitespace, writer.toString());
    }

    @Test
    public void testAttribute_validAttribute() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "attribute";
        String value = "This is a value";
        char quotationMark = '"';
        encoder.attribute(writer, name, value, quotationMark, false);
        String expectedAttribute = " attribute=\"This is a value\"";
        assertEquals(expectedAttribute, writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_invalidQuotationMark() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "attribute";
        String value = "This is a value";
        char quotationMark = '!';
        encoder.attribute(writer, name, value, quotationMark, false);
    }

    @Test
    public void testAttribute_withSpecialCharacters_escapeAmpersandsTrue() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "attribute";
        String value = "This <tag> & that";
        char quotationMark = '"';
        String expectedAttribute = " attribute=\"This &lt;tag&gt; &amp; that\"";
        encoder.attribute(writer, name, value, quotationMark, true);
        assertEquals(expectedAttribute, writer.toString());
    }

    @Test
    public void testAttribute_withSpecialCharacters_escapeAmpersandsFalse() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        String name = "attribute";
        String value = "This <tag> & that";
        char quotationMark = '"';
        String expectedAttribute = " attribute=\"This <tag> &amp; that\"";
        encoder.attribute(writer, name, value, quotationMark, false);
        assertEquals(expectedAttribute, writer.toString());
    }

}