import org.junit.Before;
import org.junit.Test;
import org.jdom2.*;
import java.io.*;

public class XMLOutputterTest {

    private XMLOutputter outputter;
    private Document document;
    private Element rootElement;

    @Before
    public void setUp() {
        outputter = new XMLOutputter();
        document = new Document();
        rootElement = new Element("root");
    }

    @Test
    public void testSetFormat() {
        Format format = Format.getCompactFormat();
        outputter.setFormat(format);

        assert outputter.getFormat().equals(format);
    }

    @Test
    public void testXMLOutputProcessor() {
        XMLOutputProcessor processor = new XMLOutputter.DefaultXMLProcessor();
        outputter.setXMLOutputProcessor(processor);

        assert outputter.getXMLOutputProcessor().equals(processor);
    }

    @Test
    public void testOutput_Document_OutputStream() throws IOException {
        document.setRootElement(rootElement);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        outputter.output(document, out);

        String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>\n";
        assert out.toString().equals(expectedOutput);
    }

    @Test
    public void testOutput_DocType_OutputStream() throws IOException {
        DocType doctype = new DocType("root");
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        outputter.output(doctype, out);

        String expectedOutput = "<!DOCTYPE root>\n";
        assert out.toString().equals(expectedOutput);
    }

    @Test
    public void testOutput_Element_OutputStream() throws IOException {
        rootElement.setAttribute("attribute", "value");
        rootElement.addContent(new Element("child"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        outputter.output(rootElement, out);

        String expectedOutput = "<root attribute=\"value\">\n  <child/>\n</root>\n";
        assert out.toString().equals(expectedOutput);
    }

    @Test
    public void testOutputElementContent_Element_OutputStream() throws IOException {
        rootElement.addContent("text");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        outputter.outputElementContent(rootElement, out);

        String expectedOutput = "text";
        assert out.toString().equals(expectedOutput);
    }

    @Test
    public void testOutput_List_OutputStream() throws IOException {
        rootElement.addContent(new Element("child"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        outputter.output(rootElement.getContent(), out);

        String expectedOutput = "<child/>\n";
        assert out.toString().equals(expectedOutput);
    }

    @Test
    public void testOutput_CDATA_OutputStream() throws IOException {
        CDATA cdata = new CDATA("data");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        outputter.output(cdata, out);

        String expectedOutput = "<![CDATA[data]]>";
        assert out.toString().equals(expectedOutput);
    }

    @Test
    public void testOutput_Text_OutputStream() throws IOException {
        Text text = new Text("text");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        outputter.output(text, out);

        String expectedOutput = "text";
        assert out.toString().equals(expectedOutput);
    }

    @Test
    public void testOutput_Comment_OutputStream() throws IOException {
        Comment comment = new Comment("comment");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        outputter.output(comment, out);

        String expectedOutput = "<!--comment-->";
        assert out.toString().equals(expectedOutput);
    }

    @Test
    public void testOutput_ProcessingInstruction_OutputStream() throws IOException {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        outputter.output(pi, out);

        String expectedOutput = "<?target data?>";
        assert out.toString().equals(expectedOutput);
    }
    
    @Test
    public void testOutput_EntityRef_OutputStream() throws IOException {
        EntityRef entity = new EntityRef("ref");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        outputter.output(entity, out);

        String expectedOutput = "&ref;";
        assert out.toString().equals(expectedOutput);
    }

    @Test
    public void testOutputString_Document() {
        document.setRootElement(rootElement);

        String output = outputter.outputString(document);

        String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>\n";
        assert output.equals(expectedOutput);
    }

    @Test
    public void testOutputString_DocType() {
        DocType doctype = new DocType("root");

        String output = outputter.outputString(doctype);

        String expectedOutput = "<!DOCTYPE root>\n";
        assert output.equals(expectedOutput);
    }

    @Test
    public void testOutputString_Element() {
        rootElement.setAttribute("attribute", "value");
        rootElement.addContent(new Element("child"));

        String output = outputter.outputString(rootElement);

        String expectedOutput = "<root attribute=\"value\">\n  <child/>\n</root>\n";
        assert output.equals(expectedOutput);
    }

    @Test
    public void testOutputString_List() {
        rootElement.addContent(new Element("child"));

        String output = outputter.outputString(rootElement.getContent());

        String expectedOutput = "<child/>\n";
        assert output.equals(expectedOutput);
    }

    @Test
    public void testOutputString_CDATA() {
        CDATA cdata = new CDATA("data");

        String output = outputter.outputString(cdata);

        String expectedOutput = "<![CDATA[data]]>";
        assert output.equals(expectedOutput);
    }

    @Test
    public void testOutputString_Text() {
        Text text = new Text("text");

        String output = outputter.outputString(text);

        String expectedOutput = "text";
        assert output.equals(expectedOutput);
    }

    @Test
    public void testOutputString_Comment() {
        Comment comment = new Comment("comment");

        String output = outputter.outputString(comment);

        String expectedOutput = "<!--comment-->";
        assert output.equals(expectedOutput);
    }

    @Test
    public void testOutputString_ProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");

        String output = outputter.outputString(pi);

        String expectedOutput = "<?target data?>";
        assert output.equals(expectedOutput);
    }
    
    @Test
    public void escapeAttributeEntities() {
        String escaped = outputter.escapeAttributeEntities("<tag>");

        String expectedOutput = "&lt;tag&gt;";
        assert escaped.equals(expectedOutput);
    }

    @Test
    public void escapeElementEntities() {
        String escaped = outputter.escapeElementEntities("<tag>");

        String expectedOutput = "&lt;tag&gt;";
        assert escaped.equals(expectedOutput);
    }

    @Test
    public void testToString() {
        String output = outputter.toString();

        String expectedOutput = "XMLOutputter[omitDeclaration = false, encoding = UTF-8, omitEncoding = false, indent = '', expandEmptyElements = true, lineSeparator = '\\n', textMode = PRESERVE]";
        assert output.equals(expectedOutput);
    }
}