package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Writer;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

public class XMLEncoderTest {
    private XMLEncoder xmlEncoder;
    private Writer writer;

    @Before
    public void setUp() throws IllegalArgumentException, UnsupportedEncodingException {
        xmlEncoder = new XMLEncoder("UTF-8");
        writer = mock(Writer.class);
    }

    @Test
    public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test
    public void testConstructor() throws IllegalArgumentException, UnsupportedEncodingException {
        assertEquals("UTF-8", xmlEncoder.getEncoding());
        assertEquals("UTF-8", new String(xmlEncoder._encodingCharArray));
        assertEquals(false, xmlEncoder._sevenBitEncoding);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructorWithInvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder("invalidEncoding");
    }

    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", xmlEncoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws IOException {
        xmlEncoder.declaration(writer);

        verify(writer).write(XMLEncoder.DECLARATION_START, 0, XMLEncoder.DECLARATION_START_LENGTH);
        verify(writer).write(xmlEncoder._encodingCharArray);
        verify(writer).write(XMLEncoder.DECLARATION_END, 0, XMLEncoder.DECLARATION_END_LENGTH);
    }

    @Test
    public void testTextWithString() throws IOException {
        xmlEncoder.text(writer, "text", true);

        verify(writer).write("text".toCharArray(), 0, 4);
    }

    @Test
    public void testTextWithCharArray() throws IOException {
        char[] ch = "text".toCharArray();
        xmlEncoder.text(writer, ch, 0, ch.length, true);

        verify(writer).write(ch, 0, ch.length);
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter() throws IOException {
        xmlEncoder.text(writer, "invalid".toCharArray(), 0, 1, true);
    }

    @Test
    public void testTextWithCharacter() throws IOException {
        xmlEncoder.text(writer, 't');

        verify(writer).write('t');
    }

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacterAsCharacter() throws IOException {
        xmlEncoder.text(writer, '\u00FF');
    }

    @Test
    public void testTextWithEscapeAmpersands() throws IOException {
        xmlEncoder.text(writer, 't', true);

        verify(writer).write('t');
    }

    @Test
    public void testTextWithoutEscapeAmpersands() throws IOException {
        xmlEncoder.text(writer, 't', false);

        verify(writer).write('t');
    }

    @Test
    public void testWhitespaceWithString() throws IOException {
        xmlEncoder.whitespace(writer, "   ");

        verify(writer).write("   ".toCharArray());
    }

    @Test
    public void testWhitespaceWithCharArray() throws IOException {
        char[] ch = "   ".toCharArray();
        xmlEncoder.whitespace(writer, ch, 0, ch.length);

        verify(writer).write(ch, 0, ch.length);
    }

    @Test(expected = InvalidXMLException.class)
    public void testWhitespaceWithInvalidCharacter() throws IOException {
        xmlEncoder.whitespace(writer, " \u00FF ");
    }

    @Test
    public void testAttribute() throws IOException {
        xmlEncoder.attribute(writer, "name", "value", '\"', true);

        verify(writer).write(32);
        verify(writer).write("name".toCharArray());
        verify(writer).write(XMLEncoder.EQUALS_QUOTE, 0, 2);
        verify(writer).write("value".toCharArray(), 0, 5);
        verify(writer).write('\"');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeWithInvalidQuotationMark() throws IOException {
        xmlEncoder.attribute(writer, "name", "value", '\'', true);
    }
}