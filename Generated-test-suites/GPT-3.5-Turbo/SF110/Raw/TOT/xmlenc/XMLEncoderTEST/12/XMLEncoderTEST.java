import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class XMLEncoderTest {

    // Test cases for getEncoding() method

    @Test
    public void testGetEncoding() {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
    }

    // Test cases for declaration(Writer out) method

    @Test
    public void testDeclaration() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(out);

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expected, out.toString());
    }

    // Test cases for text(Writer out, String text, boolean escapeAmpersands) method

    @Test
    public void testTextWithoutAmpersands() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, "Hello World!", false);

        String expected = "Hello World!";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextWithAmpersands() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, "5 > 3 & 2 < 4", true);

        String expected = "5 &gt; 3 &amp; 2 &lt; 4";
        assertEquals(expected, out.toString());
    }

    // Test cases for text(Writer out, char[] ch, int start, int length, boolean escapeAmpersands) method

    @Test
    public void testTextCharArrayWithoutAmpersands() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, new char[]{'H', 'e', 'l', 'l', 'o'}, 0, 5, false);

        String expected = "Hello";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextCharArrayWithAmpersands() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, new char[]{'5', ' ', '>', ' ', '3', ' ', '&', ' ', '2', ' ', '<', ' ', '4'}, 0, 13, true);

        String expected = "5 &gt; 3 &amp; 2 &lt; 4";
        assertEquals(expected, out.toString());
    }

    // Test cases for text(Writer out, char c) method

    @Test
    public void testTextCharWithoutAmpersands() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, 'H');

        String expected = "H";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextCharWithAmpersands() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, '&');

        String expected = "&amp;";
        assertEquals(expected, out.toString());
    }

    // Test cases for text(Writer out, char c, boolean escapeAmpersands) method

    @Test
    public void testTextCharWithoutAmpersandsEscape() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, 'H', false);

        String expected = "H";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTextCharWithAmpersandsEscape() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(out, '&', true);

        String expected = "&amp;";
        assertEquals(expected, out.toString());
    }

    // Test cases for whitespace(Writer out, String s) method

    @Test
    public void testWhitespace() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(out, "   ");

        String expected = "   ";
        assertEquals(expected, out.toString());
    }

    // Test cases for whitespace(Writer out, char[] ch, int start, int length) method

    @Test
    public void testWhitespaceCharArray() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(out, new char[]{' ', ' ', ' '}, 0, 3);

        String expected = "   ";
        assertEquals(expected, out.toString());
    }

    // Test cases for attribute(Writer out, String name, String value, char quotationMark, boolean escapeAmpersands) method

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeWithInvalidQuotationMark() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(out, "name", "value", '$', true);
    }

    @Test
    public void testAttributeWithoutAmpersands() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(out, "name", "value", '\"', false);

        String expected = " name=\"value\"";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testAttributeWithAmpersands() throws IOException {
        Writer out = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(out, "name", "5 > 3 & 2 < 4", '\'', true);

        String expected = " name='5 &gt; 3 &amp; 2 &lt; 4'";
        assertEquals(expected, out.toString());
    }

}