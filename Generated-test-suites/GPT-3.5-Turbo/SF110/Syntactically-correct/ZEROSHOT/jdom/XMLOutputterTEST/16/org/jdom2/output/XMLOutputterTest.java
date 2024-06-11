import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.*;
import java.io.*;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document(new Element("root"));
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        outputter.output(doc, writer);
        String output = writer.toString();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root />\n", output);
    }

    @Test
    public void testOutputDocType() throws IOException {
        DocType doctype = new DocType("root");
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        outputter.output(doctype, writer);
        String output = writer.toString();
        Assert.assertEquals("<!DOCTYPE root>\n", output);
    }

    @Test
    public void testOutputElement() throws IOException {
        Element element = new Element("root");
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        outputter.output(element, writer);
        String output = writer.toString();
        Assert.assertEquals("<root />\n", output);
    }

    // Add tests for other output() methods

    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String escaped = outputter.escapeAttributeEntities("\"<>&'");
        Assert.assertEquals("&quot;&lt;&gt;&amp;&apos;", escaped);
    }

    @Test
    public void testEscapeElementEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String escaped = outputter.escapeElementEntities("\"<>&'");
        Assert.assertEquals("\"<>&'", escaped);
    }
    
    // Add tests for other methods
    
    @Test
    public void testToString() {
        Format format = Format.getPrettyFormat();
        XMLOutputter outputter = new XMLOutputter(format);
        String str = outputter.toString();
        Assert.assertTrue(str.startsWith("XMLOutputter[omitDeclaration"));
        Assert.assertTrue(str.endsWith(", textMode = " + format.getMode() + "]"));
    }

}

