package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        StringReader reader = new StringReader("<html></html>");
        String baseUri = "http://www.example.com";
        Document doc = xmlTreeBuilder.parse(reader, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<html></html>";
        String baseUri = "http://www.example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html></html>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, errors, settings);

        assertEquals(baseUri, xmlTreeBuilder.baseUri());
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(settings, xmlTreeBuilder.settings);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProcessWithUnsupportedToken() {
        Token token = new Token(Token.TokenType.Comment);
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testInsertNode() {
        Document document = new Document("");
        xmlTreeBuilder.stack.add(document);
        Element element = new Element(Tag.valueOf("div"), "");
        xmlTreeBuilder.insertNode(element);
        assertTrue(document.childNodes().contains(element));
    }

    @Test
    public void testInsertWithStartTag() {
        Token.StartTag startTag = new Token.StartTag("div", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertTrue(element.tagName().equals("div"));
    }

    @Test
    public void testInsertWithCommentToken() {
        Token.Comment commentToken = new Token.Comment("Test comment");
        xmlTreeBuilder.insert(commentToken);

        List<Node> nodes = xmlTreeBuilder.currentElement().childNodes();
        assertTrue(nodes.size() > 0);
        assertTrue(nodes.get(nodes.size() - 1) instanceof Comment);
        assertEquals("Test comment", ((Comment) nodes.get(nodes.size() - 1)).getData());
    }

    @Test
    public void testInsertWithCharacterToken() {
        Token.Character characterToken = new Token.Character("Test character");
        xmlTreeBuilder.insert(characterToken);

        List<Node> nodes = xmlTreeBuilder.currentElement().childNodes();
        assertTrue(nodes.size() > 0);
        assertTrue(nodes.get(nodes.size() - 1) instanceof TextNode);
        assertEquals("Test character", ((TextNode) nodes.get(nodes.size() - 1)).getWholeText());
    }

    @Test
    public void testInsertWithDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
        xmlTreeBuilder.insert(doctype);

        List<Node> nodes = xmlTreeBuilder.currentElement().childNodes();
        assertTrue(nodes.size() > 0);
        assertTrue(nodes.get(nodes.size() - 1) instanceof DocumentType);
        assertEquals("html", ((DocumentType) nodes.get(nodes.size() - 1)).getName());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        Element element1 = new Element(Tag.valueOf("div"), "");
        Element element2 = new Element(Tag.valueOf("span"), "");

        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(xmlTreeBuilder.currentElement());

        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        String fragment = "<div></div>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(fragment, baseUri, errors, settings);
        assertNotNull(nodes);
        assertTrue(nodes.size() > 0);
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("div", ((Element) nodes.get(0)).tagName());
    }
}