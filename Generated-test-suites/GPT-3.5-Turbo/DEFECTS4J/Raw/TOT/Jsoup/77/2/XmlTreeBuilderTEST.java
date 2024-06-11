package org.jsoup.parser;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(parseSettings);
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testParseReaderBaseUri() throws IOException {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        StringReader reader = new StringReader("<xml><body><h1>Title</h1></body></xml>");
        Document document = xmlTreeBuilder.parse(reader, "https://example.com");
        assertNotNull(document);
        assertEquals("https://example.com", document.baseUri());
        assertEquals("<xml><body><h1>Title</h1></body></xml>", document.outerHtml());
    }

    @Test
    public void testParseStringBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><body><h1>Title</h1></body></xml>";
        Document document = xmlTreeBuilder.parse(input, "https://example.com");
        assertNotNull(document);
        assertEquals("https://example.com", document.baseUri());
        assertEquals("<xml><body><h1>Title</h1></body></xml>", document.outerHtml());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        StringReader reader = new StringReader("<xml><body><h1>Title</h1></body></xml>");
        xmlTreeBuilder.initialiseParse(reader, "https://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTag = new Token(Token.TokenType.StartTag, "tag");
        xmlTreeBuilder.process(startTag);
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("tag", xmlTreeBuilder.currentElement().nodeName());

        Token endTag = new Token(Token.TokenType.EndTag, "tag");
        xmlTreeBuilder.process(endTag);
        assertNull(xmlTreeBuilder.currentElement());

        Token comment = new Token(Token.TokenType.Comment, "comment");
        xmlTreeBuilder.process(comment);
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("comment", xmlTreeBuilder.currentElement().nodeName());

        Token character = new Token(Token.TokenType.Character, "text");
        xmlTreeBuilder.process(character);
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("text", xmlTreeBuilder.currentElement().nodeName());

        Token doctype = new Token(Token.TokenType.Doctype, "doctype");
        xmlTreeBuilder.process(doctype);
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("doctype", xmlTreeBuilder.currentElement().nodeName());

        Token eof = new Token(Token.TokenType.EOF, "");
        assertTrue(xmlTreeBuilder.process(eof));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.doc = new Document("https://example.com");
        Element parent = new Element("parent");
        xmlTreeBuilder.stack.add(parent);

        Node node = new TextNode("text");
        xmlTreeBuilder.insertNode(node);
        assertEquals(1, parent.childNodes().size());
        assertEquals(node, parent.childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.doc = new Document("https://example.com");
        xmlTreeBuilder.baseUri = "https://example.com";
        xmlTreeBuilder.settings = ParseSettings.preserveCase;

        Token.StartTag startTag = new Token.StartTag("tag", "attr=value", true);
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("tag", element.nodeName());
        assertEquals("https://example.com", element.baseUri());
        assertEquals(1, element.attributes().size());

        Token.StartTag unknownStartTag = new Token.StartTag("unknown", "attr=value", true);
        Element unknownElement = xmlTreeBuilder.insert(unknownStartTag);
        assertNotNull(unknownElement);
        assertEquals("unknown", unknownElement.nodeName());
        assertTrue(unknownElement.tag().isKnownTag());
        assertTrue(unknownElement.tag().isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.doc = new Document("https://example.com");
        xmlTreeBuilder.baseUri = "https://example.com";

        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);

        Token.Comment bogusCommentToken = new Token.Comment("<!-- xml version=\"1.0\" -->");
        xmlTreeBuilder.insert(bogusCommentToken);
        assertEquals(2, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNode(1) instanceof Comment);
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNode(1);
        assertTrue(comment.bogus());
        assertEquals("xml version=\"1.0\"", comment.getData());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.doc = new Document("https://example.com");

        Token.Character characterToken = new Token.Character("text", false);
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);

        Token.Character cdataToken = new Token.Character("cdata", true);
        xmlTreeBuilder.insert(cdataToken);
        assertEquals(2, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNode(1) instanceof CDataNode);
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.doc = new Document("https://example.com");

        Token.Doctype doctypeToken = new Token.Doctype("doctype", "", "");
        xmlTreeBuilder.insert(doctypeToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element parent = new Element("parent");
        Element child = new Element("child");
        Element grandChild = new Element("grandChild");
        xmlTreeBuilder.stack.add(parent);
        xmlTreeBuilder.stack.add(child);
        xmlTreeBuilder.stack.add(grandChild);

        Token.EndTag endTag = new Token.EndTag("child");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(parent, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.doc = new Document("https://example.com");

        List<Node> nodes = xmlTreeBuilder.parseFragment("<tag>text</tag>", "https://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
}