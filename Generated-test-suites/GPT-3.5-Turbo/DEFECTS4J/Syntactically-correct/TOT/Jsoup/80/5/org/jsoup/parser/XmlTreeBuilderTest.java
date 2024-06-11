package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings defaultSettings = builder.defaultSettings();
        assertNotNull(defaultSettings);
        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        Document document = builder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        Document document = builder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        builder.initialiseParse(input, baseUri, errors, settings);
        // Test some initialized values
        assertFalse(builder.stack.isEmpty());
        assertNotNull(builder.doc);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("xml");
        Element element = builder.insert(startTag);
        assertNotNull(element);
        assertEquals("xml", element.tagName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("xml"), ""));
        Token.EndTag endTag = new Token.EndTag("xml");
        builder.popStackToClose(endTag);
        assertTrue(builder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("Comment");
        builder.insert(commentToken);
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNode(0) instanceof Comment);
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("Character");
        builder.insert(characterToken);
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNode(0) instanceof TextNode);
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        builder.insert(doctype);
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("child"), "");
        builder.insertNode(element);
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNode(0) instanceof Element);
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><child></child></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = builder.parseFragment(input, baseUri, errors, settings);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("xml", ((Element) nodes.get(0)).tagName());
    }
}