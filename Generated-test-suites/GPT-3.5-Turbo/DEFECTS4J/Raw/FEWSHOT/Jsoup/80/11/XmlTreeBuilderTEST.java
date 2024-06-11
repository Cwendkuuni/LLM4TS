package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(""), "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(""), "http://example.com", new ParseErrorList(), ParseSettings.preserveCase);
        assertNotNull(xmlTreeBuilder.stack);
        assertNotNull(xmlTreeBuilder.doc);
    }

    @Test
    public void testProcessStartTagToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTagToken = new Token.StartTag("tag", new Attributes());
        Element element = xmlTreeBuilder.insert(startTagToken);
        assertNotNull(element);
        assertEquals("tag", element.tagName());
    }

    @Test
    public void testProcessEndTagToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTagToken = new Token.EndTag("tag");
        xmlTreeBuilder.stack.add(new Element("tag"));
        xmlTreeBuilder.popStackToClose(endTagToken);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessCommentToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
    }

    @Test
    public void testProcessCharacterToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("text");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
    }

    @Test
    public void testProcessDoctypeToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctypeToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element("tag");
        xmlTreeBuilder.insertNode(element);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTagToken = new Token.StartTag("tag", new Attributes());
        Element element = xmlTreeBuilder.insert(startTagToken);
        assertNotNull(element);
        assertEquals("tag", element.tagName());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("text");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctypeToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTagToken = new Token.EndTag("tag");
        xmlTreeBuilder.stack.add(new Element("tag"));
        xmlTreeBuilder.popStackToClose(endTagToken);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<tag></tag>", "http://example.com", new ParseErrorList(), ParseSettings.preserveCase);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
    }
}