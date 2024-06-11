import org.junit.Assert;
import org.junit.Test;
import java.io.*;

public class XMLEncoderTest {
    @Test
    public void testConstructor() throws UnsupportedEncodingException {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        Assert.assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEncoding() throws UnsupportedEncodingException {
        new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructorWithInvalidEncoding() throws UnsupportedEncodingException {
        new XMLEncoder("invalid");
    }

    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.declaration(writer);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Assert.assertEquals(expected, writer.toString());
    }

    @Test
    public void testTextWithoutAmpersandEscape() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "Hello World", false);
        Assert.assertEquals("Hello World", writer.toString());
    }

    @Test
    public void testTextWithAmpersandEscape() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, "Hello & World", true);
        Assert.assertEquals("Hello &amp; World", writer.toString());
    }

    @Test
    public void testTextWithCharWithoutAmpersandEscape() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, 'A');
        Assert.assertEquals("A", writer.toString());
    }

    @Test
    public void testTextWithCharWithAmpersandEscape() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, '&', true);
        Assert.assertEquals("&amp;", writer.toString());
    }

    @Test
    public void testWhitespace() throws IOException, InvalidXMLException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.whitespace(writer, "   ");
        Assert.assertEquals("   ", writer.toString());
    }

    @Test
    public void testAttributeWithQuotationMark() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "attr", "value", '\"', true);
        String expected = " attr=\"value\"";
        Assert.assertEquals(expected, writer.toString());
    }

    @Test
    public void testAttributeWithApostrophe() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "attr", "value", '\'', true);
        String expected = " attr='value'";
        Assert.assertEquals(expected, writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeWithInvalidQuotationMark() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.attribute(writer, "attr", "value", '$', true);
    }
}