package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
    }

    @Test
    public void testParseWithReader() {
        String input = "<root>Hello, World!</root>";
        Reader reader = new StringReader(input);
        Document doc = xmlTreeBuilder.parse(reader, "");
        assertNotNull(doc);
        assertEquals("root", doc.nodeName());
        assertEquals("Hello, World!", doc.text());
    }

    @Test
    public void testParseWithString() {
        String input = "<root>Hello, World!</root>";
        Document doc = xmlTreeBuilder.parse(input, "");
        assertNotNull(doc);
        assertEquals("root", doc.nodeName());
        assertEquals("Hello, World!", doc.text());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root>Hello, World!</root>";
        Reader reader = new StringReader(input);
        ParseErrorList parseErrorList = ParseErrorList.noTracking();
        ParseSettings parseSettings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(reader, "", parseErrorList, parseSettings);

        Element root = xmlTreeBuilder.currentElement();
        assertNotNull(root);
        assertEquals("root", root.nodeName());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div", null);
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("", element.text());
    }

    @Test
    public void testProcessEndTag() {
        String input = "<div></div>";
        Document doc = xmlTreeBuilder.parse(input, "");
        assertNotNull(doc);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);

        Element root = xmlTreeBuilder.currentElement();
        assertNull(root);
    }

    @Test
    public void testProcessComment() {
        String input = "<!-- Comment -->";
        Document doc = xmlTreeBuilder.parse(input, "");
        assertNotNull(doc);

        Token.Comment commentToken = new Token.Comment(" Comment ");
        xmlTreeBuilder.insert(commentToken);

        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Comment);
        Comment comment = (Comment) nodes.get(0);
        assertEquals(" Comment ", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        String input = "<root>Character Data</root>";
        Document doc = xmlTreeBuilder.parse(input, "");
        assertNotNull(doc);

        Token.Character characterToken = new Token.Character("Character Data");
        xmlTreeBuilder.insert(characterToken);

        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) nodes.get(0);
        assertEquals("Character Data", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        String input = "<!DOCTYPE html>";
        Document doc = xmlTreeBuilder.parse(input, "");
        assertNotNull(doc);

        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctypeToken);

        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof org.jsoup.nodes.DocumentType);
        org.jsoup.nodes.DocumentType doctypeNode = (org.jsoup.nodes.DocumentType) nodes.get(0);
        assertEquals("html", doctypeNode.attr("html"));
        assertEquals("", doctypeNode.attr("systemIdentifier"));
        assertEquals("", doctypeNode.attr("pubSysKey"));
    }

    @Test
    public void testPopStackToClose() {
        String input = "<div><span>Hello</span><span>World</span></div>";
        Document doc = xmlTreeBuilder.parse(input, "");
        assertNotNull(doc);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);

        List<Node> nodes = doc.childNodes();
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<p>Hello, <b>World!</b></p>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("p", element.nodeName());
        assertEquals("Hello, ", element.text());

        List<Node> childNodes = element.childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element childElement = (Element) childNodes.get(0);
        assertEquals("b", childElement.nodeName());
        assertEquals("World!", childElement.text());
    }

}