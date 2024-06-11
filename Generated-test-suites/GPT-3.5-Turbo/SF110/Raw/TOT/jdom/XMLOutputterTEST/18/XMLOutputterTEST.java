
import org.jdom2.*;
import org.jdom2.output.support.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class XMLOutputterTest {

    private XMLOutputter xmlOutputter;

    @Before
    public void setUp() {
        xmlOutputter = new XMLOutputter();
    }

    @Test
    public void testSetAndGetFormat() {
        Format format = Format.getRawFormat().clone();
        xmlOutputter.setFormat(format);
        assertEquals(format, xmlOutputter.getFormat());
    }

    @Test
    public void testSetAndGetXMLOutputProcessor() {
        XMLOutputProcessor processor = new XMLOutputter.DefaultXMLProcessor();
        xmlOutputter.setXMLOutputProcessor(processor);
        assertEquals(processor, xmlOutputter.getXMLOutputProcessor());
    }

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document();
        StringWriter writer = new StringWriter();
        xmlOutputter.output(doc, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testOutputDocType() throws IOException {
        DocType doctype = new DocType("rootElement", "publicID", "systemID");
        StringWriter writer = new StringWriter();
        xmlOutputter.output(doctype, writer);
        assertEquals("<!DOCTYPE rootElement PUBLIC \"publicID\" \"systemID\">", writer.toString());
    }

    @Test
    public void testOutputElement() throws IOException {
        Element element = new Element("root");
        StringWriter writer = new StringWriter();
        xmlOutputter.output(element, writer);
        assertEquals("<root/>", writer.toString());
    }

    @Test
    public void testOutputElementContent() throws IOException {
        Element element = new Element("root");
        StringWriter writer = new StringWriter();
        xmlOutputter.outputElementContent(element, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testOutputList() throws IOException {
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        java.util.List<Content> list = new java.util.ArrayList<>();
        list.add(element1);
        list.add(element2);
        StringWriter writer = new StringWriter();
        xmlOutputter.output(list, writer);
        assertEquals("<element1/><element2/>", writer.toString());
    }

    @Test
    public void testOutputCDATA() throws IOException {
        CDATA cdata = new CDATA("cdata content");
        StringWriter writer = new StringWriter();
        xmlOutputter.output(cdata, writer);
        assertEquals("<![CDATA[cdata content]]>", writer.toString());
    }

    @Test
    public void testOutputText() throws IOException {
        Text text = new Text("text content");
        StringWriter writer = new StringWriter();
        xmlOutputter.output(text, writer);
        assertEquals("text content", writer.toString());
    }

    @Test
    public void testOutputComment() throws IOException {
        Comment comment = new Comment("comment content");
        StringWriter writer = new StringWriter();
        xmlOutputter.output(comment, writer);
        assertEquals("<!--comment content-->", writer.toString());
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        StringWriter writer = new StringWriter();
        xmlOutputter.output(pi, writer);
        assertEquals("<?target data?>", writer.toString());
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        EntityRef entity = new EntityRef("entity");
        StringWriter writer = new StringWriter();
        xmlOutputter.output(entity, writer);
        assertEquals("&entity;", writer.toString());
    }

    @Test
    public void testOutputStringDocument() {
        Document doc = new Document();
        String output = xmlOutputter.outputString(doc);
        assertEquals("", output);
    }

    @Test
    public void testOutputStringDocType() {
        DocType doctype = new DocType("rootElement", "publicID", "systemID");
        String output = xmlOutputter.outputString(doctype);
        assertEquals("<!DOCTYPE rootElement PUBLIC \"publicID\" \"systemID\">", output);
    }

    @Test
    public void testOutputStringElement() {
        Element element = new Element("root");
        String output = xmlOutputter.outputString(element);
        assertEquals("<root/>", output);
    }

    @Test
    public void testOutputStringList() {
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        java.util.List<Content> list = new java.util.ArrayList<>();
        list.add(element1);
        list.add(element2);
        String output = xmlOutputter.outputString(list);
        assertEquals("<element1/><element2/>", output);
    }

    @Test
    public void testOutputStringCDATA() {
        CDATA cdata = new CDATA("cdata content");
        String output = xmlOutputter.outputString(cdata);
        assertEquals("<![CDATA[cdata content]]>", output);
    }

    @Test
    public void testOutputStringText() {
        Text text = new Text("text content");
        String output = xmlOutputter.outputString(text);
        assertEquals("text content", output);
    }

    @Test
    public void testOutputStringComment() {
        Comment comment = new Comment("comment content");
        String output = xmlOutputter.outputString(comment);
        assertEquals("<!--comment content-->", output);
    }

    @Test
    public void testOutputStringProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        String output = xmlOutputter.outputString(pi);
        assertEquals("<?target data?>", output);
    }

    @Test
    public void testOutputStringEntityRef() {
        EntityRef entity = new EntityRef("entity");
        String output = xmlOutputter.outputString(entity);
        assertEquals("&entity;", output);
    }

    @Test
    public void testOutputElementContentString() {
        Element element = new Element("root");
        String output = xmlOutputter.outputElementContentString(element);
        assertEquals("", output);
    }

    @Test
    public void testEscapeAttributeEntities() {
        String str = "<test attribute=\"value\">";
        String escaped = xmlOutputter.escapeAttributeEntities(str);
        assertEquals("&lt;test attribute=&quot;value&quot;&gt;", escaped);
    }

    @Test
    public void testEscapeElementEntities() {
        String str = "<test>content</test>";
        String escaped = xmlOutputter.escapeElementEntities(str);
        assertEquals("&lt;test&gt;content&lt;/test&gt;", escaped);
    }

    @Test
    public void testClone() {
        XMLOutputter clone = xmlOutputter.clone();
        assertNotSame(xmlOutputter, clone);
        assertEquals(xmlOutputter.getFormat(), clone.getFormat());
        assertEquals(xmlOutputter.getXMLOutputProcessor(), clone.getXMLOutputProcessor());
    }

    @Test
    public void testToString() {
        Format format = Format.getRawFormat().clone();
        xmlOutputter.setFormat(format);
        String expected = "XMLOutputter[omitDeclaration = " + format.omitDeclaration + ", " +
                "encoding = " + format.encoding + ", " +
                "omitEncoding = " + format.omitEncoding + ", " +
                "indent = '" + format.indent + "'" + ", " +
                "expandEmptyElements = " + format.expandEmptyElements + ", " +
                "lineSeparator = '[\\n]', " +
                "textMode = " + format.mode + "]";
        assertEquals(expected, xmlOutputter.toString());
    }
}

