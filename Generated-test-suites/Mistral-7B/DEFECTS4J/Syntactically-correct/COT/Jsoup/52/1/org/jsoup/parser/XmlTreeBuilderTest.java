package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

/**
 * Test cases for XmlTreeBuilder class
 */
public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<root><child><grandChild /></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, null, null);
        assertEquals("root", xmlTreeBuilder.doc.nodeName());
    }

    @Test
    public void testProcessStartTag() {
        String input = "<root><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.runParser();
        Element root = (Element) xmlTreeBuilder.doc.childNodes().get(0);
        Element child = (Element) root.childNodes().get(0);
        assertEquals("child", child.nodeName());
        assertEquals(1, child.childNodes().size());
    }

    @Test
    public void testProcessEndTag() {
        String input = "<root><child><grandChild /></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.runParser();
        Element root = (Element) xmlTreeBuilder.doc.childNodes().get(0);
        Element child = (Element) root.childNodes().get(0);
        Element grandChild = (Element) child.childNodes().get(0);
        assertEquals(0, child.childNodes().size());
        assertEquals(1, xmlTreeBuilder.stack.size());
        xmlTreeBuilder.popStackToClose(new Token.EndTag("grandChild"));
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        String input = "<!-- This is a comment -->";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.runParser();
        List<Node> commentNodes = xmlTreeBuilder.doc.childNodes();
        assertEquals(1, commentNodes.size());
        Comment comment = (Comment) commentNodes.get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testProcessCharacter() {
        String input = "<root>&#x26;<child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.runParser();
        List<Node> textNodes = xmlTreeBuilder.doc.childNodes();
        assertEquals(2, textNodes.size());
        TextNode rootTextNode = (TextNode) textNodes.get(0);
        assertEquals("&amp;", rootTextNode.text());
        TextNode childTextNode = (TextNode) textNodes.get(1);
        assertEquals("", childTextNode.text());
    }

    @Test(expected = RuntimeException.class)
    public void testProcessUnexpectedTokenType() {
        String input = "<root>"; // missing end tag
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.runParser();
    }

    @Test
    public void testInsertNode() {
        String input = "<root><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.runParser();
        Element root = (Element) xmlTreeBuilder.doc.childNodes().get(0);
        Element child = (Element) root.childNodes().get(0);
        Node newNode = new Element("newTag", null, null);
        xmlTreeBuilder.insertNode(newNode);
        List<Node> newNodeList = xmlTreeBuilder.doc.childNodes();
        assertEquals(3, newNodeList.size());
        assertEquals("newTag", newNodeList.get(2).nodeName());
    }

    @Test
    public void testInsertComment() {
        String input = "<root><!-- This is a comment -->";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.runParser();
        List<Node> commentNodes = xmlTreeBuilder.doc.childNodes();
        assertEquals(1, commentNodes.size());
        Comment comment = (Comment) commentNodes.get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testInsertCharacter() {
        String input = "<root>&#x26;<child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.runParser();
        List<Node> textNodes = xmlTreeBuilder.doc.childNodes();
        assertEquals(2, textNodes.size());
        xmlTreeBuilder.insert(new Token.Character('X'));
        List<Node> newTextNodes = xmlTreeBuilder.doc.childNodes();
        assertEquals(3, newTextNodes.size());
        TextNode newTextNode = (TextNode) newTextNodes.get(2);
        assertEquals("X", newTextNode.text());
    }

    @Test
    public void testInsertDocumentType() {
        String input = "<!DOCTYPE root SYSTEM \"root.dtd\">";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.runParser();
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals("root", doctypeNode.nodeName());
        assertEquals("root.dtd", doctypeNode.getSystemIdentifier());
    }

    @Test
    public void testParseFragment() {
        String input = "<root><child><grandChild></grandChild></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(input, null, null);
        assertEquals(3, parsedNodes.size());
        Element root = (Element) parsedNodes.get(0);
        Element child = (Element) root.childNodes().get(0);
        Element grandChild = (Element) child.childNodes().get(0);
        assertEquals("root", root.nodeName());
        assertEquals("child", child.nodeName());
        assertEquals("grandChild", grandChild.nodeName());
    }

    @Test(expected = RuntimeException.class)
    public void testParseFragmentWithError() {
        String input = "<root><child></child><!-- This is an error -->"; // XML declaration is not well-formed
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(input, null, null);
    }
}