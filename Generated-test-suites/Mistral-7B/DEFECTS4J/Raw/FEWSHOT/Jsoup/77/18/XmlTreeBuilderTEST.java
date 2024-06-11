package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Attributes.fromMap;
import static org.jsoup.nodes.Xml.attr;
import static org.jsoup.nodes.Xml.tag;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.AttributeToken;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.DoctypeToken;
import org.jsoup.parser.Token.EndTagToken;
import org.jsoup.parser.Token.StartTagToken;
import org.jsoup.parser.Token.TextToken;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr=\"value\"><child attr2=\"value2\"/><subchild/></element></root>";
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader(xml), "http://example.com");
        assertEquals("root", doc.tagName());
        Element root = doc.getElementById("root");
        assertEquals(1, root.childNodeSize());
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals(1, element.childNodeSize());
        Element child = element.child(0);
        assertEquals("child", child.tagName());
        assertEquals("value2", child.attr("attr2"));
        assertEquals(0, child.childNodeSize());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new StartTagToken("root");
        Element element = parser.insert(token);
        assertEquals("root", element.tagName());
        assertEquals("http://example.com", element.baseUri());
        assertEquals(0, element.childNodeSize());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Token token = new EndTagToken("root");
        parser.popStackToClose(token);
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String comment = "-- This is a comment";
        Token token = new CommentToken(comment);
        Comment commentNode = parser.insert(token);
        assertEquals(comment, commentNode.data());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String charData = "This is some character data";
        Token token = new TextToken(charData);
        TextNode textNode = parser.insert(token);
        assertEquals(charData, textNode.data());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String doctypeName = "html";
        Token token = new DoctypeToken(doctypeName);
        DocumentType doctypeNode = parser.insert(token);
        assertEquals(doctypeName, doctypeNode.tagName());
        assertNull(doctypeNode.publicId());
        assertNull(doctypeNode.systemId());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String xmlFragment = "<root><element attr=\"value\"><child attr2=\"value2\"/></element></root>";
        List<Node> nodes = parser.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document doc = Jsoup.parse(xmlFragment, "http://example.com", Parser.xmlParser());
        assertEquals(nodes.size(), doc.childNodes().size());
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            Node docNode = doc.childNodes().get(i);
            assertEquals(node.getClass(), docNode.getClass());
            if (node instanceof Element) {
                Element element = (Element) node;
                Element docElement = (Element) docNode;
                assertEquals(element.tagName(), docElement.tagName());
                assertEquals(element.baseUri(), docElement.baseUri());
                assertEquals(element.attributes().size(), docElement.attributes().size());
                for (Attribute attr : element.attributes()) {
                    Attribute docAttr = docElement.attr(attr.key());
                    assertEquals(attr.value(), docAttr.value());
                }
            }
        }
    }

    @Test
    public void testProcessUnknownTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String xml = "<unknown>";
        Token token = new StartTagToken("unknown");
        Element element = parser.insert(token);
        assertTrue(element.tagName().equals("unknown") || element.tagName().equals(""));
    }

    @Test
    public void testProcessSelfClosingTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String xml = "<root><img src=\"image.png\" /></root>";
        Document doc = parser.parse(new StringReader(xml), "http://example.com");
        Element root = doc.getElementById("root");
        assertEquals(1, root.childNodeSize());
        Element img = root.child(0);
        assertTrue(img.tagName().equals("img") && img.isSelfClosing());
    }

    @Test
    public void testProcessCData() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String cdata = "<![CDATA[This is some CDATA]]>";
        Token token = new TextToken(cdata);
        CDataNode cdataNode = parser.insert(token);
        assertEquals("CDATA", cdataNode.tagName());
        assertEquals("This is some CDATA", cdataNode.data());
    }

    @Test
    public void testProcessXmlDeclaration() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document doc = parser.parse(new StringReader(xmlDeclaration), "http://example.com");
        DocumentType xmlDeclarationNode = doc.doctype();
        assertEquals("xml", xmlDeclarationNode.tagName());
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDeclarationNode.attributes().toString());
    }

    @Test
    public void testProcessEmptyElement() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        String xml = "<element/>";
        Document doc = parser.parse(new StringReader(xml), "http://example.com");
        Element element = doc.getElementById("element");
        assertEquals("element", element.tagName());
        assertEquals(0, element.childNodeSize());
    }
}