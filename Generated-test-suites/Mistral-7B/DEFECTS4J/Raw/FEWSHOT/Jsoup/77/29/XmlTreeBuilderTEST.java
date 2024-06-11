package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<xml/>";
        Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");
        assertEquals(1, xmlDocument.childNodes().size());
        assertTrue(xmlDocument.childNodes().get(0) instanceof Element);
        assertEquals("xml", xmlDocument.childNodes().get(0).nodeName());
    }

    @Test
    public void testParseSimpleXmlWithElements() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><element1>Hello</element1><element2>World</element2></root>";
        Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        assertEquals(1, xmlDocument.childNodes().size());
        Element rootElement = (Element) xmlDocument.childNodes().get(0);
        assertEquals("root", rootElement.nodeName());

        List<Node> childNodes = rootElement.childNodes();
        assertEquals(2, childNodes.size());

        Element element1 = (Element) childNodes.get(0);
        assertEquals("element1", element1.nodeName());
        assertEquals("Hello", element1.text().getBytes().toString());

        Element element2 = (Element) childNodes.get(1);
        assertEquals("element2", element2.nodeName());
        assertEquals("World", element2.text().getBytes().toString());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><element selfClosing='true'/></root>";
        Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        assertEquals(1, xmlDocument.childNodes().size());
        Element rootElement = (Element) xmlDocument.childNodes().get(0);
        assertEquals("root", rootElement.nodeName());

        List<Node> childNodes = rootElement.childNodes();
        assertEquals(1, childNodes.size());

        Element element = (Element) childNodes.get(0);
        assertEquals("element", element.nodeName());
        assertTrue(element.isSelfClosing());
        assertNull(element.text());
    }

    @Test
    public void testParseXmlWithNestedElements() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><element1><element2>Hello</element2></element1></root>";
        Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        assertEquals(1, xmlDocument.childNodes().size());
        Element rootElement = (Element) xmlDocument.childNodes().get(0);
        assertEquals("root", rootElement.nodeName());

        List<Node> childNodes = rootElement.childNodes();
        assertEquals(1, childNodes.size());

        Element element1 = (Element) childNodes.get(0);
        assertEquals("element1", element1.nodeName());

        List<Node> grandChildNodes = element1.childNodes();
        assertEquals(1, grandChildNodes.size());

        Element element2 = (Element) grandChildNodes.get(0);
        assertEquals("element2", element2.nodeName());
        assertEquals("Hello", element2.text().getBytes().toString());
    }

    @Test
    public void testParseXmlWithComments() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><!-- This is a comment --><element>Hello</element></root>";
        Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        assertEquals(1, xmlDocument.childNodes().size());
        Element rootElement = (Element) xmlDocument.childNodes().get(0);
        assertEquals("root", rootElement.nodeName());

        List<Node> childNodes = rootElement.childNodes();
        assertEquals(1, childNodes.size());

        Element element = (Element) childNodes.get(0);
        assertEquals("element", element.nodeName());
        assertEquals("Hello", element.text().getBytes().toString());

        List<Node> commentNodes = rootElement.childNodes();
        Node commentNode = commentNodes.get(0);
        assertTrue(commentNode instanceof Comment);
        assertEquals("This is a comment", commentNode.toString());
    }

    @Test
    public void testParseXmlWithCdataSection() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><![CDATA[This is a CData section]]><element>Hello</element></root>";
        Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        assertEquals(1, xmlDocument.childNodes().size());
        Element rootElement = (Element) xmlDocument.childNodes().get(0);
        assertEquals("root", rootElement.nodeName());

        List<Node> childNodes = rootElement.childNodes();
        assertEquals(2, childNodes.size());

        Node cdataNode = childNodes.get(0);
        assertTrue(cdataNode instanceof CDataNode);
        assertEquals("This is a CData section", cdataNode.toString());

        Element element = (Element) childNodes.get(1);
        assertEquals("element", element.nodeName());
        assertEquals("Hello", element.text().getBytes().toString());
    }

    @Test
    public void testParseXmlWithDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<!DOCTYPE root SYSTEM 'xml.dtd'><root></root>";
        Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        assertEquals(1, xmlDocument.childNodes().size());
        Element rootElement = (Element) xmlDocument.childNodes().get(0);
        assertEquals("root", rootElement.nodeName());

        List<Node> childNodes = rootElement.childNodes();
        assertEquals(0, childNodes.size());

        DocumentType doctypeNode = (DocumentType) xmlDocument.childNodes().get(0);
        assertEquals("root", doctypeNode.nodeName());
        assertEquals("xml.dtd", doctypeNode.getSystemIdentifier());
    }

    @Test
    public void testParseXmlWithEmptyElements() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><element/></root>";
        Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        assertEquals(1, xmlDocument.childNodes().size());
        Element rootElement = (Element) xmlDocument.childNodes().get(0);
        assertEquals("root", rootElement.nodeName());

        List<Node> childNodes = rootElement.childNodes();
        assertEquals(1, childNodes.size());

        Element element = (Element) childNodes.get(0);
        assertEquals("element", element.nodeName());
        assertTrue(element.isEmpty());
        assertNull(element.text());
    }

    @Test
    public void testParseXmlWithEmptyRootElement() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root/>";
        Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        assertEquals(1, xmlDocument.childNodes().size());
        Element rootElement = (Element) xmlDocument.childNodes().get(0);
        assertEquals("root", rootElement.nodeName());

        List<Node> childNodes = rootElement.childNodes();
        assertEquals(0, childNodes.size());
    }

    @Test
    public void testParseXmlWithInvalidXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<html><body><foo><bar></body>";
        Document xmlDocument = null;

        try {
            xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");
        } catch (Exception e) {
            // Expected
        }

        assertNull(xmlDocument);
    }
}