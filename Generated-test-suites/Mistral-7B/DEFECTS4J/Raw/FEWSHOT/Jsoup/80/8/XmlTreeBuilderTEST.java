package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;

import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(new StringReader(""), "baseUri");
        assertNotNull(document);
        assertNotNull(document.childNodes());
        assertEquals(0, document.childNodes().size());
    }

    @Test
    public void testParseXmlWithRootElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(new StringReader("<root></root>"), "baseUri");
        Element rootElement = document.getElementsByTagName("root").get(0);
        assertNotNull(rootElement);
        assertEquals("root", rootElement.nodeName());
        assertEquals(0, rootElement.childNodes().size());
    }

    @Test
    public void testParseXmlWithRootElementAndChildElements() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(new StringReader("<root><child></child></root>"), "baseUri");
        Element rootElement = document.getElementsByTagName("root").get(0);
        assertNotNull(rootElement);
        assertEquals("root", rootElement.nodeName());
        List<Node> childNodes = rootElement.childNodes();
        assertEquals(1, childNodes.size());
        Element childElement = (Element) childNodes.get(0);
        assertNotNull(childElement);
        assertEquals("child", childElement.nodeName());
        assertEquals(0, childElement.childNodes().size());
    }

    @Test
    public void testParseXmlWithRootDocumentType() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(new StringReader("<!DOCTYPE root [<!ATTLIST root attr CDATA #REQUIRED>]><root></root>"), "baseUri");
        DocumentType documentType = document.doctype();
        assertNotNull(documentType);
        assertEquals("root", documentType.nodeName());
        assertEquals("root", documentType.getName());
        assertEquals("baseUri", documentType.getSystemIdentifier());
        assertEquals(null, documentType.getPublicIdentifier());
        assertEquals(null, documentType.getPubSysKey());
    }

    @Test
    public void testParseXmlWithComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(new StringReader("<root><!-- This is a comment --><child></child></root>"), "baseUri");
        Comment comment = document.getElementsByTagName("comment").get(0);
        assertNotNull(comment);
        assertEquals("This is a comment", comment.getData());
        assertEquals("comment", comment.nodeName());
        assertEquals(0, comment.childNodes().size());
    }

    @Test
    public void testParseXmlWithCData() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(new StringReader("<root><![CDATA[This is CData]]></root>"), "baseUri");
        CDataNode cdataNode = (CDataNode) document.getElementsByTagName("CData").get(0);
        assertNotNull(cdataNode);
        assertEquals("This is CData", cdataNode.getData());
        assertEquals("CData", cdataNode.nodeName());
        assertEquals(0, cdataNode.childNodes().size());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(new StringReader("<root><selfClosing/></root>"), "baseUri");
        Element selfClosingElement = document.getElementsByTagName("selfClosing").get(0);
        assertNotNull(selfClosingElement);
        assertEquals("selfClosing", selfClosingElement.nodeName());
        assertEquals(0, selfClosingElement.childNodes().size());
        assertSame(selfClosingElement, selfClosingElement.getParentNode());
    }

    @Test
    public void testParseXmlWithNestedSelfClosingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(new StringReader("<root><selfClosing><selfClosing></selfClosing></selfClosing></root>"), "baseUri");
        Element selfClosingElement = document.getElementsByTagName("selfClosing").get(0);
        assertNotNull(selfClosingElement);
        assertEquals("selfClosing", selfClosingElement.nodeName());
        assertEquals(0, selfClosingElement.childNodes().size());
        Element parentSelfClosingElement = selfClosingElement.getParentElement();
        assertNotNull(parentSelfClosingElement);
        assertEquals("selfClosing", parentSelfClosingElement.nodeName());
        assertEquals(1, parentSelfClosingElement.childNodes().size());
        assertSame(parentSelfClosingElement, parentSelfClosingElement.getParentNode());
    }

    @Test
    public void testParseXmlWithEmptyElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(new StringReader("<root><empty/></root>"), "baseUri");
        Element emptyElement = document.getElementsByTagName("empty").get(0);
        assertNotNull(emptyElement);
        assertEquals("empty", emptyElement.nodeName());
        assertEquals(0, emptyElement.childNodes().size());
    }

    @Test
    public void testParseXmlWithInvalidTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(new StringReader("<root><invalid/></root>"), "baseUri");
        Element invalidElement = document.getElementsByTagName("invalid").get(0);
        assertNull(invalidElement);
    }

    @Test
    public void testParseXmlWithMixedContent() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(new StringReader("<root><child>Text</child><child><![CDATA[CData]]></child></root>"), "baseUri");
        Element rootElement = document.getElementsByTagName("root").get(0);
        assertNotNull(rootElement);
        assertEquals("root", rootElement.nodeName());
        List<Node> childNodes = rootElement.childNodes();
        assertEquals(2, childNodes.size());
        Element firstChildElement = (Element) childNodes.get(0);
        assertNotNull(firstChildElement);
        assertEquals("child", firstChildElement.nodeName());
        assertEquals("Text", firstChildElement.childNodes().get(0).getData());
        assertEquals(0, firstChildElement.childNodes().size());
        Element secondChildElement = (Element) childNodes.get(1);
        assertNotNull(secondChildElement);
        assertEquals("child", secondChildElement.nodeName());
        CDataNode cdataNode = (CDataNode) secondChildElement.childNodes().get(0);
        assertNotNull(cdataNode);
        assertEquals("CData", cdataNode.nodeName());
        assertEquals("CData", cdataNode.getData());
        assertEquals(0, cdataNode.childNodes().size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root><child></child></root>", "baseUri", ParseSettings.preserveCase, ParseErrorList.noTracking());
        assertEquals(2, nodes.size());
        Node rootNode = nodes.get(0);
        assertNotNull(rootNode);
        assertEquals("root", rootNode.nodeName());
        List<Node> rootChildren = rootNode.childNodes();
        assertEquals(1, rootChildren.size());
        Node childNode = rootChildren.get(0);
        assertNotNull(childNode);
        assertEquals("child", childNode.nodeName());
        assertEquals(0, childNode.childNodes().size());
    }
}