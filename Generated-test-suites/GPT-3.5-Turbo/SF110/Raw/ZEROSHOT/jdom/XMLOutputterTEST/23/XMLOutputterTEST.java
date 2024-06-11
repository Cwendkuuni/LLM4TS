package org.jdom2.output;

import org.jdom2.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class XMLOutputterTest {

    @Test
    public void testOutputString_Document() {
        Document doc = new Document();
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.outputString(doc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", result);
    }

    @Test
    public void testOutputString_DocType() {
        DocType doctype = new DocType("name");
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.outputString(doctype);
        assertEquals("<!DOCTYPE name>\n", result);
    }

    @Test
    public void testOutputString_Element() {
        Element element = new Element("name");
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.outputString(element);
        assertEquals("<name/>\n", result);
    }

    @Test
    public void testOutputString_List() {
        Element element1 = new Element("name1");
        Element element2 = new Element("name2");
        List<Content> list = new ArrayList<>();
        list.add(element1);
        list.add(element2);
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.outputString(list);
        assertEquals("<name1/>\n<name2/>\n", result);
    }

    @Test
    public void testOutputString_CDATA() {
        CDATA cdata = new CDATA("data");
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.outputString(cdata);
        assertEquals("<![CDATA[data]]>\n", result);
    }

    @Test
    public void testOutputString_Text() {
        Text text = new Text("text");
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.outputString(text);
        assertEquals("text\n", result);
    }

    @Test
    public void testOutputString_Comment() {
        Comment comment = new Comment("comment");
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.outputString(comment);
        assertEquals("<!--comment-->\n", result);
    }

    @Test
    public void testOutputString_ProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.outputString(pi);
        assertEquals("<?target data?>\n", result);
    }

    @Test
    public void testOutputString_EntityRef() {
        EntityRef entity = new EntityRef("name");
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.outputString(entity);
        assertEquals("&name;\n", result);
    }

    @Test
    public void testOutputString_ElementContent() {
        Element element = new Element("name");
        element.addContent("content");
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.outputElementContentString(element);
        assertEquals("content\n", result);
    }

    @Test
    public void testOutput_Document() throws IOException {
        Document doc = new Document();
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        outputter.output(doc, writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", writer.toString());
    }

    @Test
    public void testOutput_DocType() throws IOException {
        DocType doctype = new DocType("name");
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        outputter.output(doctype, writer);
        assertEquals("<!DOCTYPE name>\n", writer.toString());
    }

    @Test
    public void testOutput_Element() throws IOException {
        Element element = new Element("name");
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        outputter.output(element, writer);
        assertEquals("<name/>\n", writer.toString());
    }

    @Test
    public void testOutput_ElementContent() throws IOException {
        Element element = new Element("name");
        element.addContent("content");
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        outputter.outputElementContent(element, writer);
        assertEquals("content\n", writer.toString());
    }

    @Test
    public void testOutput_List() throws IOException {
        Element element1 = new Element("name1");
        Element element2 = new Element("name2");
        List<Content> list = new ArrayList<>();
        list.add(element1);
        list.add(element2);
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        outputter.output(list, writer);
        assertEquals("<name1/>\n<name2/>\n", writer.toString());
    }

    @Test
    public void testOutput_CDATA() throws IOException {
        CDATA cdata = new CDATA("data");
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        outputter.output(cdata, writer);
        assertEquals("<![CDATA[data]]>\n", writer.toString());
    }

    @Test
    public void testOutput_Text() throws IOException {
        Text text = new Text("text");
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        outputter.output(text, writer);
        assertEquals("text\n", writer.toString());
    }

    @Test
    public void testOutput_Comment() throws IOException {
        Comment comment = new Comment("comment");
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        outputter.output(comment, writer);
        assertEquals("<!--comment-->\n", writer.toString());
    }

    @Test
    public void testOutput_ProcessingInstruction() throws IOException {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        outputter.output(pi, writer);
        assertEquals("<?target data?>\n", writer.toString());
    }

    @Test
    public void testOutput_EntityRef() throws IOException {
        EntityRef entity = new EntityRef("name");
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        outputter.output(entity, writer);
        assertEquals("&name;\n", writer.toString());
    }

    @Test
    public void testEscapeAttributeEntities() {
        String str = "<data>";
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.escapeAttributeEntities(str);
        assertEquals("&lt;data&gt;", result);
    }

    @Test
    public void testEscapeElementEntities() {
        String str = "<data>";
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.escapeElementEntities(str);
        assertEquals("&lt;data&gt;", result);
    }

    @Test
    public void testClone() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputter clone = outputter.clone();
        assertNotSame(outputter, clone);
        assertEquals(outputter.getFormat(), clone.getFormat());
        assertEquals(outputter.getXMLOutputProcessor(), clone.getXMLOutputProcessor());
    }
}