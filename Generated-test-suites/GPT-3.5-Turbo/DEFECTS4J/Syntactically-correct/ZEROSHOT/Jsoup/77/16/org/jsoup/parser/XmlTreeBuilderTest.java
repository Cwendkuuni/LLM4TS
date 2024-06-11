package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for XmlTreeBuilder.
 */
public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private String baseUri;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        baseUri = "http://example.com";
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParse() {
        String input = "<xml><tag>value</tag></xml>";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("xml", doc.nodeName());
        assertEquals(1, doc.childNodes().size());
        Node node = doc.childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof Element);
        Element element = (Element) node;
        assertEquals("tag", element.nodeName());
        assertEquals("value", element.text());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml><tag>value</tag></xml>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, errors, settings);
        assertNotNull(xmlTreeBuilder.doc);
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("xml", xmlTreeBuilder.doc.nodeName());
        assertEquals(0, errors.size());
    }

    @Test
    public void testProcess() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        xmlTreeBuilder.process(startTag);
        assertFalse(xmlTreeBuilder.stack.isEmpty());
        Node node = xmlTreeBuilder.currentElement();
        assertNotNull(node);
        assertTrue(node instanceof Element);
        Element element = (Element) node;
        assertEquals("tag", element.nodeName());

        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tag");
        xmlTreeBuilder.process(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("tag"), baseUri);
        xmlTreeBuilder.insertNode(element);
        Node node = xmlTreeBuilder.currentElement();
        assertNotNull(node);
        assertTrue(node instanceof Element);
        assertEquals("tag", node.nodeName());
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        startTag.attributes().put("attr", "value");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("tag", element.nodeName());
        assertEquals(baseUri, element.baseUri());
        assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment();
        comment.data("comment");
        xmlTreeBuilder.insert(comment);
        Node node = xmlTreeBuilder.currentElement();
        assertNotNull(node);
        assertTrue(node instanceof Comment);
        assertEquals("comment", node.outerHtml());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character token = new Token.Character();
        token.data("data");
        xmlTreeBuilder.insert(token);
        Node node = xmlTreeBuilder.currentElement();
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        assertEquals("data", node.outerHtml());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("doctype");
        xmlTreeBuilder.insert(doctype);
        Node node = xmlTreeBuilder.currentElement();
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);
        assertEquals("<!DOCTYPE doctype>", node.outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tag");
        Element element = new Element(Tag.valueOf("tag"), baseUri);
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());

        xmlTreeBuilder.stack.add(element);
        Element element2 = new Element(Tag.valueOf("tag"), baseUri);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.popStackToClose(endTag);
        assertFalse(xmlTreeBuilder.stack.isEmpty());
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertSame(element, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>value</tag>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertNotNull(node);
        assertTrue(node instanceof Element);
        Element element = (Element) node;
        assertEquals("tag", element.nodeName());
        assertEquals("value", element.text());
    }
}