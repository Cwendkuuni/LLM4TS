package org.znerd.xmlenc;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
        // Arrange
        String encoding = "UTF-8";

        // Act
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);

        // Assert
        assertNotNull(encoder);
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        // Arrange
        String encoding = null;

        // Act
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);

        // Assert
        // Exception expected
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_InvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        // Arrange
        String encoding = "INVALID";

        // Act
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);

        // Assert
        // Exception expected
    }

    @Test
    public void testXMLEncoder_Constructor() throws IllegalArgumentException, UnsupportedEncodingException {
        // Arrange
        String encoding = "UTF-8";

        // Act
        XMLEncoder encoder = new XMLEncoder(encoding);

        // Assert
        assertEquals(encoding, encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXMLEncoder_Constructor_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        // Arrange
        String encoding = null;

        // Act
        XMLEncoder encoder = new XMLEncoder(encoding);

        // Assert
        // Exception expected
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testXMLEncoder_Constructor_InvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        // Arrange
        String encoding = "INVALID";

        // Act
        XMLEncoder encoder = new XMLEncoder(encoding);

        // Assert
        // Exception expected
    }

    @Test
    public void testDeclaration() throws IOException {
        // Arrange
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

        // Act
        encoder.declaration(writer);
        String result = writer.toString();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testText() throws IOException {
        // Arrange
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String text = "Test Text";

        // Act
        encoder.text(writer, text, false);
        String result = writer.toString();

        // Assert
        assertEquals(text, result);
    }

    @Test
    public void testTextWithEscapeAmpersands() throws IOException {
        // Arrange
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String text = "Test & Text";
        String expected = "Test &amp; Text";

        // Act
        encoder.text(writer, text, true);
        String result = writer.toString();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testTextWithCharArray() throws IOException {
        // Arrange
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        char[] ch = "Test Text".toCharArray();

        // Act
        encoder.text(writer, ch, 0, ch.length, false);
        String result = writer.toString();

        // Assert
        assertEquals("Test Text", result);
    }

    @Test
    public void testTextWithCharArrayAndEscapeAmpersands() throws IOException {
        // Arrange
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        char[] ch = "Test & Text".toCharArray();
        String expected = "Test &amp; Text";

        // Act
        encoder.text(writer, ch, 0, ch.length, true);
        String result = writer.toString();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testTextWithChar() throws IOException {
        // Arrange
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        char c = 'A';

        // Act
        encoder.text(writer, c);
        String result = writer.toString();

        // Assert
        assertEquals("A", result);
    }

    @Test
    public void testTextWithCharAndEscapeAmpersands() throws IOException {
        // Arrange
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        char c = '&';
        String expected = "&amp;";

        // Act
        encoder.text(writer, c, true);
        String result = writer.toString();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testWhitespaceWithString() throws IOException {
        // Arrange
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String whitespace = "  \t ";

        // Act
        encoder.whitespace(writer, whitespace);
        String result = writer.toString();

        // Assert
        assertEquals(whitespace, result);
    }

    @Test
    public void testWhitespaceWithCharArray() throws IOException {
        // Arrange
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        char[] ch = "  \t ".toCharArray();

        // Act
        encoder.whitespace(writer, ch, 0, ch.length);
        String result = writer.toString();

        // Assert
        assertEquals("  \t ", result);
    }

    @Test
    public void testAttribute() throws IOException {
        // Arrange
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String name = "attr";
        String value = "Test Value";
        char quotationMark = '\"';
        String expected = " attr=\"Test Value\"";

        // Act
        encoder.attribute(writer, name, value, quotationMark, false);
        String result = writer.toString();

        // Assert
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_InvalidQuotationMark() throws IOException {
        // Arrange
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        String name = "attr";
        String value = "Test Value";
        char quotationMark = '?';

        // Act
        encoder.attribute(writer, name, value, quotationMark, false);

        // Assert
        // Exception expected
    }
}