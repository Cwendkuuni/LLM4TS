package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXmlTree() {
        String xmlString = "<root><element attr=\"value\">content</element></root>";
        Document doc = XmlTreeBuilder.parse(xmlString, "base-uri");
        Element root = doc.getRootElement();
        List<Node> childNodes = root.childNodes();

        assertEquals(1, childNodes.size());
        assertEquals("element", childNodes.get(0).nodeName());
        Element element = (Element) childNodes.get(0);
        assertEquals("value", element.attributes().get("attr").getValue());
        assertEquals("content", element.text());
    }

    @Test
    public void testParseXmlTreeWithComment() {
        String xmlString = "<root><!-- comment --><element attr=\"value\">content</element></root>";
        Document doc = XmlTreeBuilder.parse(xmlString, "base-uri");
        List<Node> childNodes = doc.getRootElement().childNodes();

        assertEquals(2, childNodes.size());
        assertSame(childNodes.get(0), doc.getElementsByTag("comment").get(0));
        Element element = (Element) childNodes.get(1);
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attributes().get("attr").getValue());
        assertEquals("content", element.text());
    }

    @Test
    public void testParseXmlTreeWithDocType() {
        String xmlString = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]><root><element attr=\"value\">content</element></root>";
        Document doc = XmlTreeBuilder.parse(xmlString, "base-uri");
        List<Node> childNodes = doc.getRootElement().childNodes();

        assertEquals(3, childNodes.size());
        DocumentType doctype = (DocumentType) childNodes.get(0);
        assertEquals("root", doctype.nodeName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        Element element = (Element) childNodes.get(1);
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attributes().get("attr").getValue());
        assertEquals("content", element.text());
    }

    @Test
    public void testParseXmlTreeWithSelfClosingTag() {
        String xmlString = "<root><element /></root>";
        Document doc = XmlTreeBuilder.parse(xmlString, "base-uri");
        List<Node> childNodes = doc.getRootElement().childNodes();

        assertEquals(1, childNodes.size());
        Element element = (Element) childNodes.get(0);
        assertEquals("element", element.nodeName());
        assertTrue(element.hasText());
        assertTrue(element.text().isEmpty());
    }

    @Test
    public void testParseXmlTreeWithUnknownTag() {
        String xmlString = "<root><unknownTag /></root>";
        Document doc = XmlTreeBuilder.parse(xmlString, "base-uri");
        List<Node> childNodes = doc.getRootElement().childNodes();

        assertEquals(1, childNodes.size());
        Element element = (Element) childNodes.get(0);
        assertEquals("unknownTag", element.nodeName());
        assertTrue(element.hasText());
        assertTrue(element.text().isEmpty());
    }

    @Test
    public void testParseXmlTreeWithEmptyElement() {
        String xmlString = "<root><element></element></root>";
        Document doc = XmlTreeBuilder.parse(xmlString, "base-uri");
        List<Node> childNodes = doc.getRootElement().childNodes();

        assertEquals(1, childNodes.size());
        Element element = (Element) childNodes.get(0);
        assertEquals("element", element.nodeName());
        assertTrue(element.hasText());
        assertTrue(element.text().isEmpty());
    }

    @Test
    public void testParseXmlTreeWithBogusComment() {
        String xmlString = "<root><!DOCTYPE root [ <!ENTITY e \"foo\"> ]><!-- &e;--><element attr=\"value\">content</element></root>";
        Document doc = XmlTreeBuilder.parse(xmlString, "base-uri");
        List<Node> childNodes = doc.getRootElement().childNodes();

        assertEquals(2, childNodes.size());
        Element element = (Element) childNodes.get(1);
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attributes().get("attr").getValue());
        assertEquals("content", element.text());

        Node commentNode = doc.getElementsByTag("comment").get(0);
        assertTrue(commentNode instanceof XmlDeclaration);
        XmlDeclaration xmlDeclaration = (XmlDeclaration) commentNode;
        assertEquals("xml", xmlDeclaration.nodeName());
        assertEquals("base-uri", xmlDeclaration.baseUri());
        assertTrue(xmlDeclaration.getEntities().size() > 0);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root", "base-uri", new ParseSettings());
        xmlTreeBuilder.stack.add(root);
        Element element1 = new Element("element1", "base-uri", new ParseSettings());
        xmlTreeBuilder.insert(new Token.StartTag(null, "element1", new ParseSettings(), new Attribute[0]));
        xmlTreeBuilder.stack.add(element1);
        Element element2 = new Element("element2", "base-uri", new ParseSettings());
        xmlTreeBuilder.insert(new Token.StartTag(null, "element2", new ParseSettings(), new Attribute[0]));
        xmlTreeBuilder.stack.add(element2);

        xmlTreeBuilder.popStackToClose(new Token.EndTag(null, "element1", new ParseSettings()));

        assertEquals(2, xmlTreeBuilder.stack.size());
        assertEquals(element2, xmlTreeBuilder.stack.get(1));
    }

    @Test
    public void testPopStackToCloseWithNotFound() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root", "base-uri", new ParseSettings());
        xmlTreeBuilder.stack.add(root);
        Element element1 = new Element("element1", "base-uri", new ParseSettings());
        xmlTreeBuilder.insert(new Token.StartTag(null, "element1", new ParseSettings(), new Attribute[0]));
        xmlTreeBuilder.stack.add(element1);

        xmlTreeBuilder.popStackToClose(new Token.EndTag(null, "element2", new ParseSettings()));

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(root, xmlTreeBuilder.stack.get(0));
    }
}