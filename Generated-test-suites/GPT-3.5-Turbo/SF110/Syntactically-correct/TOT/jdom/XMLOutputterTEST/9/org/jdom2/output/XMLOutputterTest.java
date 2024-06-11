import static org.junit.Assert.*;
import org.jdom2.*;
import org.junit.*;

public class XMLOutputterTest {
    private XMLOutputter outputter;

    @Before
    public void setUp() {
        outputter = new XMLOutputter();
    }

    @Test
    public void testSetFormat() {
        Format format = new Format();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        outputter.setXMLOutputProcessor(processor);
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testOutput_Document() {
        Document doc = new Document();
        doc.setRootElement(new Element("root"));
        assertDoesNotThrow(() -> outputter.output(doc, System.out));
    }

    @Test
    public void testOutput_DocType() {
        DocType doctype = new DocType("root");
        assertDoesNotThrow(() -> outputter.output(doctype, System.out));
    }

    @Test
    public void testOutput_Element() {
        Element element = new Element("root");
        assertDoesNotThrow(() -> outputter.output(element, System.out));
    }

    @Test
    public void testOutput_ElementContent() {
        Element element = new Element("root");
        element.setText("content");
        assertDoesNotThrow(() -> outputter.outputElementContent(element, System.out));
    }

    @Test
    public void testOutput_List() {
        Element element = new Element("root");
        element.addContent(new Element("child1"));
        element.addContent(new Element("child2"));
        assertDoesNotThrow(() -> outputter.output(element.getContent(), System.out));
    }

    @Test
    public void testOutput_CDATA() {
        CDATA cdata = new CDATA("data");
        assertDoesNotThrow(() -> outputter.output(cdata, System.out));
    }

    @Test
    public void testOutput_Text() {
        Text text = new Text("text");
        assertDoesNotThrow(() -> outputter.output(text, System.out));
    }

    @Test
    public void testOutput_Comment() {
        Comment comment = new Comment("comment");
        assertDoesNotThrow(() -> outputter.output(comment, System.out));
    }

    @Test
    public void testOutput_ProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        assertDoesNotThrow(() -> outputter.output(pi, System.out));
    }

    @Test
    public void testOutput_EntityRef() {
        EntityRef entity = new EntityRef("entity");
        assertDoesNotThrow(() -> outputter.output(entity, System.out));
    }

    @Test
    public void testOutputString_Document() {
        Document doc = new Document();
        doc.setRootElement(new Element("root"));
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator()
                + "<root/>" + System.lineSeparator();
        assertEquals(expected, outputter.outputString(doc));
    }

    @Test
    public void testOutputString_DocType() {
        DocType doctype = new DocType("root");
        String expected = "<!DOCTYPE root>" + System.lineSeparator();
        assertEquals(expected, outputter.outputString(doctype));
    }

    @Test
    public void testOutputString_Element() {
        Element element = new Element("root");
        String expected = "<root/>" + System.lineSeparator();
        assertEquals(expected, outputter.outputString(element));
    }

    @Test
    public void testOutputString_List() {
        Element element = new Element("root");
        element.addContent(new Element("child1"));
        element.addContent(new Element("child2"));
        String expected = "<root>" + System.lineSeparator()
                + "  <child1/>" + System.lineSeparator()
                + "  <child2/>" + System.lineSeparator()
                + "</root>" + System.lineSeparator();
        assertEquals(expected, outputter.outputString(element.getContent()));
    }

    @Test
    public void testOutputString_CDATA() {
        CDATA cdata = new CDATA("data");
        String expected = "<![CDATA[data]]>" + System.lineSeparator();
        assertEquals(expected, outputter.outputString(cdata));
    }

    @Test
    public void testOutputString_Text() {
        Text text = new Text("text");
        String expected = "text";
        assertEquals(expected, outputter.outputString(text));
    }

    @Test
    public void testOutputString_Comment() {
        Comment comment = new Comment("comment");
        String expected = "<!--comment-->" + System.lineSeparator();
        assertEquals(expected, outputter.outputString(comment));
    }

    @Test
    public void testOutputString_ProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        String expected = "<?target data?>" + System.lineSeparator();
        assertEquals(expected, outputter.outputString(pi));
    }

    @Test
    public void testOutputString_EntityRef() {
        EntityRef entity = new EntityRef("entity");
        String expected = "&entity;";
        assertEquals(expected, outputter.outputString(entity));
    }

    @Test
    public void testEscapeAttributeEntities() {
        String str = "<&\"'>";
        String expected = "&lt;&amp;&quot;&apos;&gt;";
        assertEquals(expected, outputter.escapeAttributeEntities(str));
    }

    @Test
    public void testEscapeElementEntities() {
        String str = "<&\"'>";
        String expected = "&lt;&amp;&quot;&apos;&gt;";
        assertEquals(expected, outputter.escapeElementEntities(str));
    }

    @Test
    public void testClone() {
        XMLOutputter clone = outputter.clone();
        assertNotSame(outputter, clone);
        assertEquals(outputter.getFormat(), clone.getFormat());
    }

    @Test
    public void testToString() {
        String expected = "XMLOutputter[omitDeclaration = false, encoding = UTF-8, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = '\r\n', textMode = PRESERVE]";
        assertEquals(expected, outputter.toString());
    }
}