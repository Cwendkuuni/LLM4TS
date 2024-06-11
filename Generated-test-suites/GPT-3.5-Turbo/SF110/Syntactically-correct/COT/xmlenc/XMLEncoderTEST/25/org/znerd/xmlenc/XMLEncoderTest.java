import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class XMLEncoderTest {
    
    private XMLEncoder xmlEncoder;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @Before
    public void setUp() {
        xmlEncoder = new XMLEncoder("UTF-8");
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder xmlEncoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(xmlEncoder);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_UnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("UTF-32");
    }

    @Test
    public void testXMLEncoder() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            assertNotNull(xmlEncoder);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXMLEncoder_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testXMLEncoder_UnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder("UTF-32");
    }

    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", xmlEncoder.getEncoding());
    }

    @Test
    public void testDeclaration() {
        xmlEncoder.declaration(printWriter);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", stringWriter.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testDeclaration_NullWriter() throws NullPointerException, IOException {
        xmlEncoder.declaration(null);
    }

    @Test
    public void testText_String_True() {
        xmlEncoder.text(printWriter, "test", true);
        assertEquals("test", stringWriter.toString());
    }

    @Test
    public void testText_String_False() {
        xmlEncoder.text(printWriter, "test", false);
        assertEquals("test", stringWriter.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testText_String_NullWriter() throws NullPointerException, InvalidXMLException, IOException {
        xmlEncoder.text(null, "test", true);
    }

    @Test(expected = NullPointerException.class)
    public void testText_String_NullText() throws NullPointerException, InvalidXMLException, IOException {
        xmlEncoder.text(printWriter, null, true);
    }

    @Test
    public void testText_CharArray() {
        char[] ch = {'t', 'e', 's', 't'};
        xmlEncoder.text(printWriter, ch, 0, ch.length, true);
        assertEquals("test", stringWriter.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testText_CharArray_InvalidStart() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        char[] ch = {'t', 'e', 's', 't'};
        xmlEncoder.text(printWriter, ch, 10, ch.length, true);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testText_CharArray_InvalidLength() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        char[] ch = {'t', 'e', 's', 't'};
        xmlEncoder.text(printWriter, ch, 0, 10, true);
    }

    @Test(expected = NullPointerException.class)
    public void testText_CharArray_NullWriter() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        xmlEncoder.text(null, new char[4], 0, 4, true);
    }

    @Test(expected = NullPointerException.class)
    public void testText_CharArray_NullCharArray() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        xmlEncoder.text(printWriter, null, 0, 4, true);
    }

    @Test
    public void testText_Char() {
        xmlEncoder.text(printWriter, 't');
        assertEquals("t", stringWriter.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_Char_InvalidChar() throws InvalidXMLException, IOException {
        xmlEncoder.text(printWriter, 'ß');
    }

    @Test
    public void testText_Char_EscapeAmpersands_True() {
        xmlEncoder.text(printWriter, '&', true);
        assertEquals("&amp;", stringWriter.toString());
    }

    @Test
    public void testText_Char_EscapeAmpersands_False() {
        xmlEncoder.text(printWriter, '&', false);
        assertEquals("&", stringWriter.toString());
    }

    @Test
    public void testWhitespace_String() {
        xmlEncoder.whitespace(printWriter, " ");
        assertEquals(" ", stringWriter.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testWhitespace_String_NullWriter() throws NullPointerException, InvalidXMLException, IOException {
        xmlEncoder.whitespace(null, " ");
    }

    @Test(expected = NullPointerException.class)
    public void testWhitespace_String_NullWhitespace() throws NullPointerException, InvalidXMLException, IOException {
        xmlEncoder.whitespace(printWriter, null);
    }

    @Test
    public void testWhitespace_CharArray() {
        xmlEncoder.whitespace(printWriter, new char[] {' '}, 0, 1);
        assertEquals(" ", stringWriter.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWhitespace_CharArray_InvalidStart() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        xmlEncoder.whitespace(printWriter, new char[] {' '}, 10, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWhitespace_CharArray_InvalidLength() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        xmlEncoder.whitespace(printWriter, new char[] {' '}, 0, 10);
    }

    @Test(expected = NullPointerException.class)
    public void testWhitespace_CharArray_NullWriter() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        xmlEncoder.whitespace(null, new char[] {' '}, 0, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testWhitespace_CharArray_NullCharArray() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        xmlEncoder.whitespace(printWriter, null, 0, 1);
    }

    @Test
    public void testAttribute() {
        xmlEncoder.attribute(printWriter, "name", "value", '\"', true);
        assertEquals(" name=\"value\"", stringWriter.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAttribute_NullWriter() throws NullPointerException, IOException {
        xmlEncoder.attribute(null, "name", "value", '\"', true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_InvalidQuotationMark() throws IllegalArgumentException, NullPointerException, IOException {
        xmlEncoder.attribute(printWriter, "name", "value", 'ú', true);
    }

}