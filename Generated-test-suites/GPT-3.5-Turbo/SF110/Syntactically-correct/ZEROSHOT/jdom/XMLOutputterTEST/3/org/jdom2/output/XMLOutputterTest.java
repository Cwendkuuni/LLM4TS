
package org.jdom2.output;

import org.jdom2.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.List;

public class XMLOutputterTest {

    private XMLOutputter xmlOutputter;

    @Before
    public void setup() {
        xmlOutputter = new XMLOutputter();
    }

    @Test
    public void testSetFormat() {
        Format format = Format.getPrettyFormat();
        xmlOutputter.setFormat(format);
        Assert.assertEquals(format, xmlOutputter.getFormat());
    }

    @Test
    public void testGetXMLOutputProcessor() {
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        xmlOutputter.setXMLOutputProcessor(processor);
        Assert.assertEquals(processor, xmlOutputter.getXMLOutputProcessor());
    }

    @Test
    public void testOutputStringWithDocument() {
        Document doc = new Document();
        String result = xmlOutputter.outputString(doc);
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", result);
    }

    @Test
    public void testOutputStringWithDocType() {
        DocType doctype = new DocType("root");
        String result = xmlOutputter.outputString(doctype);
        Assert.assertEquals("<!DOCTYPE root>\n", result);
    }

    @Test
    public void testOutputStringWithElement() {
        Element element = new Element("root");
        String result = xmlOutputter.outputString(element);
        Assert.assertEquals("<root/>\n", result);
    }

    @Test
    public void testOutputStringWithList() {
        List<Content> list = new Element("root").getContent();
        String result = xmlOutputter.outputString(list);
        Assert.assertEquals("<root/>\n", result);
    }

    @Test
    public void testOutputStringWithCDATA() {
        CDATA cdata = new CDATA("data");
        String result = xmlOutputter.outputString(cdata);
        Assert.assertEquals("<![CDATA[data]]>\n", result);
    }

    @Test
    public void testOutputStringWithText() {
        Text text = new Text("hello");
        String result = xmlOutputter.outputString(text);
        Assert.assertEquals("hello", result);
    }

    @Test
    public void testOutputStringWithComment() {
        Comment comment = new Comment("comment");
        String result = xmlOutputter.outputString(comment);
        Assert.assertEquals("<!--comment-->\n", result);
    }

    @Test
    public void testOutputStringWithProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        String result = xmlOutputter.outputString(pi);
        Assert.assertEquals("<?target data?>\n", result);
    }

    @Test
    public void testOutputStringWithEntityRef() {
        EntityRef entity = new EntityRef("entity");
        String result = xmlOutputter.outputString(entity);
        Assert.assertEquals("&entity;", result);
    }

    @Test
    public void testOutputStringWithElementContent() {
        Element element = new Element("root");
        element.addContent(new Text("hello"));
        String result = xmlOutputter.outputElementContentString(element);
        Assert.assertEquals("hello", result);
    }

    @Test
    public void testEscapeAttributeEntities() {
        String str = "<attribute>";
        String result = xmlOutputter.escapeAttributeEntities(str);
        Assert.assertEquals("&lt;attribute&gt;", result);
    }

    @Test
    public void testEscapeElementEntities() {
        String str = "<element>";
        String result = xmlOutputter.escapeElementEntities(str);
        Assert.assertEquals("&lt;element&gt;", result);
    }

}
