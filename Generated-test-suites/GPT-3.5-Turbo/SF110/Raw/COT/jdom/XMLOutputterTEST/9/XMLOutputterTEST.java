import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XMLOutputterTest {

    private static final XMLOutputter DEFAULT_OUTPUTTER = new XMLOutputter();

    @Test
    public void testConstructor() {
        XMLOutputter outputter = new XMLOutputter();
        assertNotNull(outputter.getFormat());
        assertNotNull(outputter.getXMLOutputProcessor());
    }

    @Test
    public void testConstructorWithFormat() {
        Format format = Format.getDefaultFormat();
        XMLOutputter outputter = new XMLOutputter(format);
        assertEquals(format, outputter.getFormat());
        assertNotNull(outputter.getXMLOutputProcessor());
    }

    @Test
    public void testConstructorWithProcessor() {
        XMLOutputProcessor processor = DEFAULT_OUTPUTTER.getXMLOutputProcessor();
        XMLOutputter outputter = new XMLOutputter(processor);
        assertNotNull(outputter.getFormat());
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testConstructorWithFormatAndProcessor() {
        Format format = Format.getRawFormat();
        XMLOutputProcessor processor = DEFAULT_OUTPUTTER.getXMLOutputProcessor();
        XMLOutputter outputter = new XMLOutputter(format, processor);
        assertEquals(format, outputter.getFormat());
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testSetFormat() {
        XMLOutputter outputter = new XMLOutputter();
        Format newFormat = Format.getRawFormat();
        outputter.setFormat(newFormat);
        assertEquals(newFormat, outputter.getFormat());
    }

    @Test
    public void testGetFormat() {
        XMLOutputter outputter = new XMLOutputter();
        assertNotNull(outputter.getFormat());
    }

    @Test
    public void testGetXMLOutputProcessor() {
        XMLOutputter outputter = new XMLOutputter();
        assertNotNull(outputter.getXMLOutputProcessor());
    }

    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputProcessor processor = DEFAULT_OUTPUTTER.getXMLOutputProcessor();
        outputter.setXMLOutputProcessor(processor);
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(doc, outputStream);

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>\n";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testOutputDocType() throws IOException {
        DocType docType = new DocType("root");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(docType, outputStream);

        String expected = "<!DOCTYPE root>\n";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testOutputElement() throws IOException {
        Element element = new Element("root");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(element, outputStream);

        String expected = "<root/>\n";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testOutputElementContent() throws IOException {
        Element element = new Element("root");
        element.addContent(new Text("Content"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputter outputter = new XMLOutputter();
        outputter.outputElementContent(element, outputStream);

        String expected = "Content";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testOutputList() throws IOException {
        List<Content> list = new ArrayList<>();
        list.add(new Element("root"));
        list.add(new Text("Text"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(list, outputStream);

        String expected = "<root/>Text";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testOutputCDATA() throws IOException {
        CDATA cdata = new CDATA("CDATA");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(cdata, outputStream);

        String expected = "<![CDATA[CDATA]]>";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testOutputText() throws IOException {
        Text text = new Text("Text");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(text, outputStream);

        String expected = "Text";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testOutputComment() throws IOException {
        Comment comment = new Comment("Comment");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(comment, outputStream);

        String expected = "<!--Comment-->";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(pi, outputStream);

        String expected = "<?target data?>";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        EntityRef entity = new EntityRef("entity");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(entity, outputStream);

        String expected = "&entity;";
        assertEquals(expected, outputStream.toString("UTF-8"));
    }

    @Test
    public void testOutputString() {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);

        XMLOutputter outputter = new XMLOutputter();

        String docString = outputter.outputString(doc);
        String expectedDocString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>\n";
        assertEquals(expectedDocString, docString);

        String rootString = outputter.outputString(root);
        String expectedRootString = "<root/>\n";
        assertEquals(expectedRootString, rootString);
    }

    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String str = "\"'<>&";
        String escaped = outputter.escapeAttributeEntities(str);

        String expected = "&quot;&apos;&lt;&gt;&amp;";
        assertEquals(expected, escaped);
    }

    @Test
    public void testEscapeElementEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String str = "<>&";
        String escaped = outputter.escapeElementEntities(str);

        String expected = "&lt;&gt;&amp;";
        assertEquals(expected, escaped);
    }
}