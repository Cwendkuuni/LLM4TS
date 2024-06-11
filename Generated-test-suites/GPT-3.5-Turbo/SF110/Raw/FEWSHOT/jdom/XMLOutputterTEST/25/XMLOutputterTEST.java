package org.jdom2.output;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

public class XMLOutputterTest {

    @Test
    public void testConstructor() {
        Format format = new Format();
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        XMLOutputter outputter = new XMLOutputter(format, processor);
        assertNotNull(outputter);
        assertEquals(format, outputter.getFormat());
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testDefaultConstructor() {
        XMLOutputter outputter = new XMLOutputter();
        assertNotNull(outputter);
        assertEquals(Format.getRawFormat(), outputter.getFormat());
        assertEquals(XMLOutputter.DEFAULTPROCESSOR, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testCopyConstructor() {
        Format format = new Format();
        XMLOutputter outputter = new XMLOutputter(format);
        assertNotNull(outputter);
        assertEquals(format, outputter.getFormat());
        assertEquals(XMLOutputter.DEFAULTPROCESSOR, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testSetFormat() {
        Format format = new Format();
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testGetFormat() {
        Format format = new Format();
        XMLOutputter outputter = new XMLOutputter(format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        XMLOutputter outputter = new XMLOutputter();
        outputter.setXMLOutputProcessor(processor);
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testGetXMLOutputProcessor() {
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        XMLOutputter outputter = new XMLOutputter(processor);
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testOutputDocument() throws IOException {
        Document document = new Document();
        StringWriter writer = new StringWriter();
        XMLOutputter outputter = new XMLOutputter();

        outputter.output(document, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testOutputDocType() throws IOException {
        DocType doctype = new DocType();
        StringWriter writer = new StringWriter();
        XMLOutputter outputter = new XMLOutputter();

        outputter.output(doctype, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testOutputElement() throws IOException {
        Element element = new Element("element");
        StringWriter writer = new StringWriter();
        XMLOutputter outputter = new XMLOutputter();

        outputter.output(element, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testOutputElementContent() throws IOException {
        Element element = new Element("element");
        StringWriter writer = new StringWriter();
        XMLOutputter outputter = new XMLOutputter();

        outputter.outputElementContent(element, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testOutputList() throws IOException {
        List<Content> list = new ArrayList<>();
        StringWriter writer = new StringWriter();
        XMLOutputter outputter = new XMLOutputter();

        outputter.output(list, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testOutputCDATA() throws IOException {
        CDATA cdata = new CDATA("CDATA");
        StringWriter writer = new StringWriter();
        XMLOutputter outputter = new XMLOutputter();

        outputter.output(cdata, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testOutputText() throws IOException {
        Text text = new Text("text");
        StringWriter writer = new StringWriter();
        XMLOutputter outputter = new XMLOutputter();

        outputter.output(text, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testOutputComment() throws IOException {
        Comment comment = new Comment("comment");
        StringWriter writer = new StringWriter();
        XMLOutputter outputter = new XMLOutputter();

        outputter.output(comment, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testOutputProcessingInstruction() throws IOException {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        StringWriter writer = new StringWriter();
        XMLOutputter outputter = new XMLOutputter();

        outputter.output(pi, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testOutputEntityRef() throws IOException {
        EntityRef entity = new EntityRef("name");
        StringWriter writer = new StringWriter();
        XMLOutputter outputter = new XMLOutputter();

        outputter.output(entity, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String str = "attribute";
        String expected = "attribute";
        String result = outputter.escapeAttributeEntities(str);
        assertEquals(expected, result);
    }

    @Test
    public void testEscapeElementEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String str = "element";
        String expected = "element";
        String result = outputter.escapeElementEntities(str);
        assertEquals(expected, result);
    }

    @Test
    public void testClone() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputter clone = outputter.clone();
        assertNotNull(clone);
        assertNotSame(outputter, clone);
        assertEquals(outputter.getFormat(), clone.getFormat());
        assertEquals(outputter.getXMLOutputProcessor(), clone.getXMLOutputProcessor());
    }

    @Test
    public void testToString() {
        XMLOutputter outputter = new XMLOutputter();
        String expected = "XMLOutputter[omitDeclaration = false, encoding = UTF-8, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = '\n', textMode = 0]";
        String result = outputter.toString();
        assertEquals(expected, result);
    }

}