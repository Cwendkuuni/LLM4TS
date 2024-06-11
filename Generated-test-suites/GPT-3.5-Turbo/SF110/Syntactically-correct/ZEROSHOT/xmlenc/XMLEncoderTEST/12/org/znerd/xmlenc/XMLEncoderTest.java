import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() throws Exception {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws Exception {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_UnsupportedEncoding() throws Exception {
        XMLEncoder.getEncoder("InvalidEncoding");
    }

    @Test
    public void testXMLEncoder() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXMLEncoder_NullEncoding() throws Exception {
        new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testXMLEncoder_UnsupportedEncoding() throws Exception {
        new XMLEncoder("InvalidEncoding");
    }

    @Test
    public void testGetEncoding() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.declaration(writer);
        writer.close();
        String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expectedResult, writer.toString());
    }

    @Test
    public void testText_String_EscapeAmpersands() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "This is a test & example.", true);
        writer.close();
        String expectedResult = "This is a test &amp; example.";
        assertEquals(expectedResult, writer.toString());
    }

    @Test
    public void testText_CharArray_EscapeAmpersands() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "This is a test & example.".toCharArray(), 0, 23, true);
        writer.close();
        String expectedResult = "This is a test &amp; example.";
        assertEquals(expectedResult, writer.toString());
    }

    @Test
    public void testText_CharArray_NoEscapeAmpersands() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, "This is a test & example.".toCharArray(), 0, 23, false);
        writer.close();
        String expectedResult = "This is a test & example.";
        assertEquals(expectedResult, writer.toString());
    }

    @Test
    public void testText_Char_EscapeAmpersands() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, '&', true);
        writer.close();
        String expectedResult = "&amp;";
        assertEquals(expectedResult, writer.toString());
    }

    @Test
    public void testText_Char_NoEscapeAmpersands() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.text(writer, '&', false);
        writer.close();
        String expectedResult = "&";
        assertEquals(expectedResult, writer.toString());
    }

    @Test
    public void testWhitespace_String() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.whitespace(writer, "    ");
        writer.close();
        String expectedResult = "    ";
        assertEquals(expectedResult, writer.toString());
    }

    @Test
    public void testWhitespace_CharArray() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.whitespace(writer, new char[] {' ', ' ', ' ', ' '}, 0, 4);
        writer.close();
        String expectedResult = "    ";
        assertEquals(expectedResult, writer.toString());
    }

    @Test
    public void testAttribute_EscapeAmpersands() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "attributeName", "This is a test & example.", '\"', true);
        writer.close();
        String expectedResult = " attributeName=\"This is a test &amp; example.\"";
        assertEquals(expectedResult, writer.toString());
    }

    @Test
    public void testAttribute_NoEscapeAmpersands() throws Exception {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "attributeName", "This is a test & example.", '\"', false);
        writer.close();
        String expectedResult = " attributeName=\"This is a test & example.\"";
        assertEquals(expectedResult, writer.toString());
    }

}