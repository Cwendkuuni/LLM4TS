package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EOF;

public class XmlTreeBuilderTest {

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token.StartTag("root", null, null);
        Element root = xmlTreeBuilder.insert(startTagToken);
        assertEquals("root", root.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token.StartTag("root", null, null);
        Element root = xmlTreeBuilder.insert(startTagToken);
        Token endTagToken = new Token.EndTag("root");
        xmlTreeBuilder.popStackToClose(endTagToken);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        assertTrue(xmlTreeBuilder.doc.body().childNodes().get(0) instanceof Comment);
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token characterToken = new Token.Character('a');
        xmlTreeBuilder.insert(characterToken);
        assertTrue(xmlTreeBuilder.doc.body().childNodes().get(0) instanceof TextNode);
        assertEquals("a", xmlTreeBuilder.doc.body().childNodes().get(0).text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token doctypeToken = new Token.Doctype("html", "public", "http://www.w3.org/TR/html4/strict.dtd");
        xmlTreeBuilder.insert(doctypeToken);
        assertTrue(xmlTreeBuilder.doc.childNodes().get(0) instanceof DocumentType);
    }

    @Test(expected = AssertionError.class)
    public void testInvalidTokenType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token invalidToken = new Token(-1, "");
        xmlTreeBuilder.process(invalidToken);
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element>Hello World</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, null);
        Document doc = nodes.get(0).asDocument();
        Element rootElement = doc.getRootElement();
        assertEquals("root", rootElement.nodeName());
        Element element = rootElement.getElementsByTagName("element").get(0);
        assertEquals("element", element.nodeName());
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testParseFragmentWithContext() {
        String xmlFragment = "<root><element>Hello World</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element contextElement = new Element("context");
        Document doc = xmlTreeBuilder.parseFragment(xmlFragment, contextElement, null, null).get(0).asDocument();
        Element rootElement = doc.getRootElement();
        assertEquals("root", rootElement.nodeName());
        Element element = rootElement.getElementsByTagName("element").get(0);
        assertEquals("element", element.nodeName());
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testParseEmptyFragment() {
        String xmlFragment = "";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, null);
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testParseFragmentWithInvalidXml() {
        String xmlFragment = "<root><element><";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, null);
        assertTrue(nodes.isEmpty());
    }
}