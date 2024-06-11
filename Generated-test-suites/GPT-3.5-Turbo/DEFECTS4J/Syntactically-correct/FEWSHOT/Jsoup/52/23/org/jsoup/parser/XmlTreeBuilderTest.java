package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals("http://example.com", xmlTreeBuilder.baseUri);
        assertEquals(0, errors.size());
        assertNotNull(xmlTreeBuilder.doc);
        assertNotNull(xmlTreeBuilder.stack);
    }

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("test");
        xmlTreeBuilder.process(startTag);

        Element currentElement = xmlTreeBuilder.currentElement();
        assertEquals("test", currentElement.nodeName());
        assertNotNull(currentElement.attributes());
    }

    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("test");
        xmlTreeBuilder.process(endTag);

        Element currentElement = xmlTreeBuilder.currentElement();
        assertNull(currentElement);
    }

    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("test");
        xmlTreeBuilder.process(comment);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof Comment);
        assertEquals("test", ((Comment) node).getData());
    }

    @Test
    public void testProcess_Character() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("test");
        xmlTreeBuilder.process(character);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof TextNode);
        assertEquals("test", ((TextNode) node).getWholeText());
    }

    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.process(doctype);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof DocumentType);
        assertEquals("html", ((DocumentType) node).getName());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node node = new TextNode("test", "http://example.com");
        xmlTreeBuilder.insertNode(node);

        Node insertedNode = xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(node, insertedNode);
    }

    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("test");
        Element insertedElement = xmlTreeBuilder.insert(startTag);

        Element currentElement = xmlTreeBuilder.currentElement();
        assertEquals("test", currentElement.nodeName());
        assertEquals(insertedElement, currentElement);
    }

    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("test");
        xmlTreeBuilder.insert(comment);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof Comment);
        assertEquals("test", ((Comment) node).getData());
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("test");
        xmlTreeBuilder.insert(character);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof TextNode);
        assertEquals("test", ((TextNode) node).getWholeText());
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctype);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertTrue(node instanceof DocumentType);
        assertEquals("html", ((DocumentType) node).getName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("test");
        xmlTreeBuilder.popStackToClose(endTag);

        Element currentElement = xmlTreeBuilder.currentElement();
        assertNull(currentElement);
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodeList = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(1, nodeList.size());
        assertTrue(nodeList.get(0) instanceof Element);
        assertEquals("root", ((Element) nodeList.get(0)).nodeName());
    }
}