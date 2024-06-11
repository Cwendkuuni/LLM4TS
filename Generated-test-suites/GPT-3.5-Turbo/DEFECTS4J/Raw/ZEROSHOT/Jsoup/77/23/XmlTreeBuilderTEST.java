package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("<xml></xml>", doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("<xml></xml>", doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Element doc = xmlTreeBuilder.currentElement();
        assertNotNull(doc);
        assertEquals("head", doc.nodeName());
        assertEquals(settings, doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("xml");
        startTag.attributes().put("attr", "value");
        Element el = xmlTreeBuilder.insert(startTag);
        assertNotNull(el);
        assertEquals("xml", el.tagName());
        assertEquals("value", el.attr("attr"));
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("xml");
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.stack.add(new Element("xml"));
        xmlTreeBuilder.stack.add(new Element("head"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("div", xmlTreeBuilder.stack.get(0).tagName());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
        Comment comment = (Comment) childNodes.get(0);
        assertEquals("comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("text");
        xmlTreeBuilder.insert(characterToken);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) childNodes.get(0);
        assertEquals("text", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        doctype.setPublicIdentifier("-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.setSystemIdentifier("http://www.w3.org/TR/html4/loose.dtd");
        doctype.setPubSysKey("PUBLIC");
        xmlTreeBuilder.insert(doctype);
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) childNodes.get(0);
        assertEquals("html", doctypeNode.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctypeNode.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctypeNode.getSystemIdentifier());
        assertEquals("PUBLIC", doctypeNode.getPubSysKey());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>test</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element div = (Element) childNodes.get(0);
        assertEquals("div", div.tagName());
        assertEquals("test", div.text());
    }

}