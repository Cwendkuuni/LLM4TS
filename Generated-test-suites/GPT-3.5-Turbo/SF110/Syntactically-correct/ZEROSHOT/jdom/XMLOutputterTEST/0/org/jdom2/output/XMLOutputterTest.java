import org.junit.*;

public class XMLOutputterTest {

    @Test
    public void testSetFormat() {
        XMLOutputter xmlo = new XMLOutputter();
        Format format = new Format();
        xmlo.setFormat(format);
        Assert.assertEquals(format, xmlo.getFormat());
    }
    
    @Test
    public void testOutput() throws IOException {
        XMLOutputter xmlo = new XMLOutputter();
        Document doc = new Document();
        OutputStream out = new ByteArrayOutputStream();
        xmlo.output(doc, out);
        Assert.assertEquals("", out.toString());
    }

    @Test
    public void testOutputString() {
        XMLOutputter xmlo = new XMLOutputter();
        Document doc = new Document();
        String output = xmlo.outputString(doc);
        Assert.assertEquals("", output);
    }

    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter xmlo = new XMLOutputter();
        String str = "attribute";
        String escaped = xmlo.escapeAttributeEntities(str);
        Assert.assertEquals(str, escaped);
    }

    @Test
    public void testEscapeElementEntities() {
        XMLOutputter xmlo = new XMLOutputter();
        String str = "element";
        String escaped = xmlo.escapeElementEntities(str);
        Assert.assertEquals(str, escaped);
    }

    @Test
    public void testClone() {
        XMLOutputter xmlo1 = new XMLOutputter();
        XMLOutputter xmlo2 = xmlo1.clone();
        Assert.assertNotSame(xmlo1, xmlo2);
    }

    @Test
    public void testToString() {
        XMLOutputter xmlo = new XMLOutputter();
        String expected = "XMLOutputter[omitDeclaration = false, encoding = UTF-8, omitEncoding = false, indent = '  ', expandEmptyElements = false, lineSeparator = '\\r\\n', textMode = PRESERVE]";
        String result = xmlo.toString();
        Assert.assertEquals(expected, result);
    }
}