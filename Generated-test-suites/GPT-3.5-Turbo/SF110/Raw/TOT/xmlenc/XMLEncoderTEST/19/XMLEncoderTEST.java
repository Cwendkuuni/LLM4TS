import org.junit.*;
import java.io.*;

public class XMLEncoderTest {

    private XMLEncoder encoder;

    @Before
    public void setUp() throws UnsupportedEncodingException {
        encoder = new XMLEncoder("UTF-8");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_InvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("UTF-32");
    }

    @Test
    public void testGetEncoding() {
        Assert.assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test(expected = NullPointerException.class)
    public void testDeclaration_NullWriter() throws NullPointerException, IOException {
        encoder.declaration(null);
    }

    @Test
    public void testDeclaration_ValidWriter() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        encoder.declaration(writer);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Assert.assertEquals(expected, writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testText_NullWriter() throws NullPointerException, InvalidXMLException, IOException {
        encoder.text(null, "Text", true);
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidCharacter() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        encoder.text(writer, "Text\u0001", true);
    }

    @Test
    public void testText_ValidWriter() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        encoder.text(writer, "Text", true);
        Assert.assertEquals("Text", writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testText_NullCharacterArray() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        encoder.text(null, 0, 5, true);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testText_InvalidCharacterArrayIndex() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        char[] ch = {'T', 'e', 'x', 't'};
        encoder.text(null, -1, 5, true);
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidCharacterArray() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        char[] ch = {'T', 'e', 'x', 't', '\u0001'};
        encoder.text(writer, ch, 0, 5, true);
    }

    @Test
    public void testText_ValidCharacterArray() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        char[] ch = {'T', 'e', 'x', 't'};
        encoder.text(writer, ch, 0, 4, true);
        Assert.assertEquals("Text", writer.toString());
    }

    @Test(expected = InvalidXMLException.class)
    public void testText_InvalidCharacter() throws InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        encoder.text(writer, '\u0001');
    }

    @Test
    public void testText_ValidCharacter() throws InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        encoder.text(writer, 'T');
        Assert.assertEquals("T", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttribute_InvalidQuotationMark() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", "value", ' ', true);
    }

    @Test(expected = NullPointerException.class)
    public void testAttribute_NullName() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, null, "value", '\"', true);
    }

    @Test(expected = NullPointerException.class)
    public void testAttribute_NullValue() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", null, '\"', true);
    }

    @Test
    public void testAttribute_ValidAttributes() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        encoder.attribute(writer, "name", "value", '\"', true);
        String expected = " name=\"value\"";
        Assert.assertEquals(expected, writer.toString());
    }

    @Test
    public void testWhitespace_ValidWriter() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        encoder.whitespace(writer, "  ");
        Assert.assertEquals("  ", writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testWhitespace_NullCharacterArray() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        encoder.whitespace(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWhitespace_InvalidCharacterArrayIndex() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        char[] ch = {' ', ' '};
        encoder.whitespace(null, -1, 2);
    }

    @Test(expected = InvalidXMLException.class)
    public void testWhitespace_InvalidCharacterArray() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        char[] ch = {'\u0001', ' '};
        encoder.whitespace(writer, ch, 0, 2);
    }

    @Test
    public void testWhitespace_ValidCharacterArray() throws NullPointerException, IndexOutOfBoundsException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        char[] ch = {' ', ' '};
        encoder.whitespace(writer, ch, 0, 2);
        Assert.assertEquals("  ", writer.toString());
    }

}