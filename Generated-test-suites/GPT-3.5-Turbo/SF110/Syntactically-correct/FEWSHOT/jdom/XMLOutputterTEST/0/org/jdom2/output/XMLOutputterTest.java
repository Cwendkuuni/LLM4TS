
package org.jdom2.output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jdom2.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class XMLOutputterTest {
    
    XMLOutputter outputter;

    @Before
    public void setUp() {
        outputter = new XMLOutputter();
    }

    @Test
    public void testSetFormat() {
        Format format = Format.getCompactFormat();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testGetXMLOutputProcessor() {
        XMLOutputProcessor processor = new XMLOutputProcessor();
        outputter.setXMLOutputProcessor(processor);
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testOutputDocument() throws IOException {
        Document document = new Document(new Element("root"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputter.output(document, out);
        assertTrue(out.toString().contains("<root></root>"));
    }

    @Test
    public void testOutputDocType() throws IOException {
        DocType doctype = new DocType("root");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputter.output(doctype, out);
        assertTrue(out.toString().contains("<!DOCTYPE root>"));
    }

    @Test
    public void testOutputElement() throws IOException {
        Element element = new Element("root");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputter.output(element, out);
        assertTrue(out.toString().contains("<root></root>"));
    }

    @Test
    public void testOutputElementContent() throws IOException {
        Element element = new Element("root");
        element.addContent(new Element("child"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputter.outputElementContent(element, out);
        assertTrue(out.toString().contains("<child></child>"));
    }

    @Test
    public void testOutputList() throws IOException {
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputter.output(List.of(child1, child2), out);
        assertTrue(out.toString().contains("<child1></child1><child2></child2>"));
    }

    @Test
    public void testOutputCDATA() throws IOException {
        CDATA cdata = new CDATA("data");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputter.output(cdata, out);
        assertTrue(out.toString().contains("<![CDATA[data]]>"));
    }

    @Test
    public void testOutputText() throws IOException {
        Text text = new Text("text");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputter.output(text, out);
        assertTrue(out.toString().contains("text"));
    }

    @Test
    public void testOutputComment() throws IOException {
        Comment comment = new Comment("comment");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputter.output(comment, out);
        assertTrue(out.toString().contains("<!--comment-->"));
    }
    
    @Test
    public void testOutputProcessingInstruction() throws IOException {
        ProcessingInstruction pi = new ProcessingInstruction("target", "");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputter.output(pi, out);
        assertTrue(out.toString().contains("<?target ?>"));
    }

    @Test
    public void testEscapeAttributeEntities() {
        String str = "<&>";
        String escaped = outputter.escapeAttributeEntities(str);
        assertEquals("&lt;&amp;&gt;", escaped);
    }

    @Test
    public void testEscapeElementEntities() {
        String str = "<&>";
        String escaped = outputter.escapeElementEntities(str);
        assertEquals("&lt;&amp;&gt;", escaped);
    }

    @Test
    public void testClone() {
        XMLOutputter clone = outputter.clone();
        assertEquals(outputter.getFormat(), clone.getFormat());
        assertEquals(outputter.getXMLOutputProcessor(), clone.getXMLOutputProcessor());
    }

    @Test
    public void testToString() {
        String str = outputter.toString();
        assertTrue(str.contains("omitDeclaration = false"));
        assertTrue(str.contains("encoding = UTF-8"));
        assertTrue(str.contains("omitEncoding = false"));
        assertTrue(str.contains("indent = ''"));
        assertTrue(str.contains("expandEmptyElements = true"));
        assertTrue(str.contains("lineSeparator = '\\n'"));
        assertTrue(str.contains("textMode = PRESERVE"));
    }
}
