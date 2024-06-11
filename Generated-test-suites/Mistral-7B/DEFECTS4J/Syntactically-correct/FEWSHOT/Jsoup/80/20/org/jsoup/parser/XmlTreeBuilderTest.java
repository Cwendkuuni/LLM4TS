package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokenizer;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader(xmlString), "baseUri");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals("baseUri", root.baseUri());
        Element element = root.getChild(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseWithSelfClosingTag() {
        String xmlString = "<image src=\"image.png\" />";
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader(xmlString), "baseUri");
        Element image = doc.getElementsByTag("image").first();
        assertEquals("image", image.tagName());
        assertEquals("baseUri", image.baseUri());
        assertNull(image.text());
    }

    @Test
    public void testParseWithComment() {
        String xmlString = "<!-- This is a comment --><root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader(xmlString), "baseUri");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals("baseUri", root.baseUri());
        List<Node> comments = root.childNodes();
        assertEquals(1, comments.size());
        Comment comment = (Comment) comments.get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseWithCData() {
        String xmlString = "<root><![CDATA[This is some CDATA content]]><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader(xmlString), "baseUri");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals("baseUri", root.baseUri());
        List<Node> cdataNodes = root.childNodes();
        assertEquals(1, cdataNodes.size());
        Node cdataNode = cdataNodes.get(0);
        assertTrue(cdataNode instanceof CDataNode);
        assertEquals("This is some CDATA content", cdataNode.text());
    }

    @Test
    public void testParseWithDoctype() {
        String xmlString = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader(xmlString), "baseUri");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals("baseUri", root.baseUri());
        DocumentType doctype = root.childNodes().get(0);
        assertEquals("root", doctype.nodeName());
        assertEquals("baseUri", doctype.baseUri());
        Element element = root.getChild(1);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseFragment() {
        String xmlString = "<root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder parser = new XmlTreeBuilder();
        List<Node> nodes = parser.parseFragment(xmlString, "baseUri", ParseSettings.preserveCase, ParseErrorList.noTracking());
        Node root = nodes.get(0);
        assertEquals(1, nodes.size());
        assertEquals("root", root.tagName());
        assertEquals("baseUri", root.baseUri());
        Element element = (Element) root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Tokenizer tokenizer = new Tokenizer("<?xml version=\"1.0\"?>\n<root><element attr=\"value\">Content</element></root>");
        Token token = tokenizer.nextToken();
        assertEquals(Token.Type.StartDocument, token.type());
        token = tokenizer.nextToken();
        assertEquals(Token.Type.Comment, token.type());
        token = tokenizer.nextToken();
        assertEquals(Token.Type.StartTag, token.type());
        Element element = parser.insert(token.asStartTag());
        assertEquals("element", element.tagName());
        assertEquals("baseUri", element.baseUri());
        Map<String, Attribute> attributes = element.attributes();
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get("attr");
        assertEquals("value", attribute.getValue());
        assertEquals("value", attribute.getValueByAttributeName("attr"));
    }

    @Test(expected = RuntimeException.class)
    public void testProcessInvalidTokenType() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Tokenizer tokenizer = new Tokenizer("<?xml version=\"1.0\"?>\n<root><element attr=\"value\">Content</element></root>");
        Token token = tokenizer.nextToken();
        assertEquals(Token.Type.StartDocument, token.type());
        token = tokenizer.nextToken();
        assertEquals(Token.Type.Comment, token.type());
        token = tokenizer.nextToken();
        assertEquals(Token.Type.StartTag, token.type());
        parser.process(new Token(1, Token.Type.EOF));
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Element root = new Element("root", "baseUri", null);
        Element element1 = new Element("element1", "baseUri", null);
        Element element2 = new Element("element2", "baseUri", null);
        parser.stack.add(root);
        parser.stack.add(element1);
        parser.stack.add(element2);
        parser.popStackToClose(new Token.EndTag("element2"));
        assertEquals(2, parser.stack.size());
        assertEquals(element1, parser.stack.get(parser.stack.size() - 1));
    }

    @Test
    public void testPopStackToCloseUnmatchedTag() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Element root = new Element("root", "baseUri", null);
        Element element1 = new Element("element1", "baseUri", null);
        parser.stack.add(root);
        parser.stack.add(element1);
        parser.popStackToClose(new Token.EndTag("element3"));
        assertEquals(2, parser.stack.size());
        assertEquals(element1, parser.stack.get(parser.stack.size() - 1));
    }
}