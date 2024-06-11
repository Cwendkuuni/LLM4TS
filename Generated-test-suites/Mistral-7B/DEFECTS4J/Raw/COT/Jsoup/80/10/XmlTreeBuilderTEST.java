package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child></child></root>";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://test.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertEquals("root", document.tagName());
        Element root = document.getFirstElement();
        assertEquals("child", root.getFirstElement().tagName());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child></child></root>";
        String baseUri = "http://test.com";
        Document document = xmlTreeBuilder.parse(xmlString, baseUri);
        assertEquals("root", document.tagName());
        Element root = document.getFirstElement();
        assertEquals("child", root.getFirstElement().tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child></child></root>";
        Document document = xmlTreeBuilder.parse(xmlString, null);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child></child></root>";
        Reader input = new StringReader(xmlString);
        String baseUri = "http://test.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInvalidTokenType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(-1, "InvalidToken");
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element("tag", "http://test.com");
        xmlTreeBuilder.insertNode(element);
        Element root = xmlTreeBuilder.doc;
        assertEquals(element, root.getFirstChild());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("tag", "http://test.com", null, null, null, null, null, 1, 1, 1, null);
        Element element = xmlTreeBuilder.insert(startTag);
        Element root = xmlTreeBuilder.doc;
        assertEquals(element, root.getFirstChild());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        Comment comment = (Comment) xmlTreeBuilder.doc.getFirstChild();
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token characterToken = new Token.Character('A');
        xmlTreeBuilder.insert(characterToken);
        TextNode textNode = (TextNode) xmlTreeBuilder.doc.getFirstChild();
        assertEquals("A", textNode.text());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token doctypeToken = new Token.Doctype("html", null, null, null, null, null);
        DocumentType documentTypeNode = xmlTreeBuilder.insert(doctypeToken);
        Document document = xmlTreeBuilder.doc;
        assertEquals(documentTypeNode, document.getFirstChild());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root", "http://test.com");
        Element child = new Element("child", "http://test.com");
        xmlTreeBuilder.stack.add(root);
        xmlTreeBuilder.stack.add(child);
        Token endTag = new Token.EndTag("child");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(root, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child></child></root>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlString, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        assertEquals("root", root.tagName());
        assertEquals("child", child.tagName());
    }

    @Test
    public void testParseFragmentWithEmptyInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("", "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertTrue(nodes.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentWithNullInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(null, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
    }
}