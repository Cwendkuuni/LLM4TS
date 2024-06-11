package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.List;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<xml></xml>", "");
        Element root = doc.body();
        assertEquals("xml", root.tagName());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        builder.initialiseParse("<xml></xml>", "", errors, ParseSettings.preserveCase);
        assertEquals(0, errors.size());
    }

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("xml");
        assertTrue(builder.process(startTag));
        Element root = builder.currentElement();
        assertEquals("xml", root.tagName());
    }

    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.process(new Token.StartTag("xml"));
        Token.EndTag endTag = new Token.EndTag("xml");
        assertTrue(builder.process(endTag));
        assertNull(builder.currentElement());
    }

    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("test comment");
        assertTrue(builder.process(comment));
        Node node = builder.currentElement().childNode(0);
        assertTrue(node instanceof Comment);
        Comment commentNode = (Comment) node;
        assertEquals("test comment", commentNode.getData());
    }

    @Test
    public void testProcess_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("test character");
        assertTrue(builder.process(character));
        Node node = builder.currentElement().childNode(0);
        assertTrue(node instanceof TextNode);
        TextNode textNode = (TextNode) node;
        assertEquals("test character", textNode.getWholeText());
    }

    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("testDoctype");
        assertTrue(builder.process(doctype));
        Node node = builder.currentElement().childNode(0);
        assertTrue(node instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) node;
        assertEquals("testDoctype", doctypeNode.getName());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element("testElement");
        builder.insertNode(element);
        Node node = builder.currentElement().childNode(0);
        assertEquals(element, node);
    }

    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("xml");
        Element element = builder.insert(startTag);
        assertEquals("xml", element.tagName());
    }

    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("test comment");
        builder.insert(comment);
        Node node = builder.currentElement().childNode(0);
        assertTrue(node instanceof Comment);
        Comment commentNode = (Comment) node;
        assertEquals("test comment", commentNode.getData());
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("test character");
        builder.insert(character);
        Node node = builder.currentElement().childNode(0);
        assertTrue(node instanceof TextNode);
        TextNode textNode = (TextNode) node;
        assertEquals("test character", textNode.getWholeText());
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("testDoctype");
        builder.insert(doctype);
        Node node = builder.currentElement().childNode(0);
        assertTrue(node instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) node;
        assertEquals("testDoctype", doctypeNode.getName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.insert(new Token.StartTag("xml"));
        builder.insert(new Token.StartTag("body"));
        builder.insert(new Token.StartTag("p"));
        Token.EndTag endTag = new Token.EndTag("body");
        builder.popStackToClose(endTag);
        assertEquals(1, builder.stack.size());
        assertEquals("xml", builder.currentElement().tagName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = builder.parseFragment("<p>test</p>", "", errors, ParseSettings.preserveCase);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("p", element.tagName());
    }
}