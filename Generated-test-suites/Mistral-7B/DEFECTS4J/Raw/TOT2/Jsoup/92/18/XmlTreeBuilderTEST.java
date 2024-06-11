package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {
    @Test
    public void testParseXml() {
        String xmlString = "<root><child1>Hello</child1><child2><grandChild>World</grandChild></child2></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        Element root = doc.getRootElement();
        assertEquals("root", root.tagName());
        assertEquals(2, root.childElementCount());
    }

    @Test
    public void testParseXmlWithEmptyDocument() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(""), null);
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseXmlWithInvalidXmlCharacters() {
        String xmlString = "<root>&<invalid>&</root>"; // Replace "<invalid>" with an invalid XML character
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = null;
        try {
            doc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        } catch (IllegalArgumentException e) {
            assertEquals("Error parsing input as XML: &<invalid>& is an invalid XML character", e.getMessage());
        }
        assertEquals(null, doc);
    }

    @Test
    public void testParseXmlWithMalformedDocument() {
        String xmlString = "<root><child>"; // Missing end tag
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = null;
        try {
            doc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        } catch (IllegalStateException e) {
            assertEquals("Error parsing input as XML: Expected </child>, but found no closing tag.", e.getMessage());
        }
        assertEquals(null, doc);
    }

    @Test
    public void testParseXmlWithUnknownTag() {
        String xmlString = "<root><unknownTag/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        Element root = doc.getRootElement();
        Element unknownTag = root.child(0);
        assertEquals("unknownTag", unknownTag.tagName());
    }

    @Test
    public void testParseXmlWithExternalDTD() {
        String xmlString = "<!DOCTYPE root [ <!ATTLIST myattr CDATA #REQUIRED > ]><root><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        assertTrue(doc.childNodes().size() > 1);
        DocumentType docType = (DocumentType) doc.childNodes().get(0);
        assertEquals("root", docType.nodeName());
        assertEquals("root", docType.getName());
    }

    @Test
    public void testParseXmlWithExternalEntities() {
        String xmlString = "<root>&entity;<child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        Element root = doc.getRootElement();
        Element child = root.child(0);
        assertEquals("child", child.tagName());
        Text node = (Text) root.child(1);
        assertEquals("entity", node.getData());
    }

    @Test
    public void testParseXmlWithProcessingInstruction() {
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        ProcessingInstruction pi = doc.processingInstruction("xml");
        assertEquals("xml", pi.target());
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", pi.data());
    }

    @Test
    public void testParseXmlWithComment() {
        String xmlString = "<!-- This is a comment --><root><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        Comment comment = (Comment) doc.childNodes().get(0);
        assertEquals("This is a comment", comment.data());
    }

    @Test
    public void testParseXmlWithCDATASection() {
        String xmlString = "<root><![CDATA[ <html><body><p>This is a CDATA section</p></body></html> ]]></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        Text cdata = (Text) doc.childNodes().get(1);
        assertEquals("<html><body><p>This is a CDATA section</p></body></html>", cdata.getData());
    }
}