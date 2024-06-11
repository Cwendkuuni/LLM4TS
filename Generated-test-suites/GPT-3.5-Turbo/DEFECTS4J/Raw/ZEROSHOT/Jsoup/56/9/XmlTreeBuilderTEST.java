package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private String input;
    private String baseUri;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        input = "<root><tag1>value1</tag1><tag2>value2</tag2></root>";
        baseUri = "http://example.com";
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParseWithNoTrackingErrorList() {
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("root", document.nodeName());
    }

    @Test
    public void testInitialiseParse() {
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertNotNull(xmlTreeBuilder.stack);
        assertNotNull(xmlTreeBuilder.doc);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("root", xmlTreeBuilder.currentElement().nodeName());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag(Tag.valueOf("tag1"), "tag1", "attr1=value1", true, settings);
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("tag1", element.nodeName());
        assertEquals("attr1", element.attr("attr1"));
        assertEquals("value1", element.text());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("root");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("tag1"), baseUri));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("root"), baseUri));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("This is a character");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("This is a character", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "public", "system");
        xmlTreeBuilder.insert(doctype);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("html", documentType.nodeName());
        assertEquals("public", documentType.publicIdentifier());
        assertEquals("system", documentType.systemIdentifier());
    }

    @Test
    public void testInsertNode() {
        Node node = new Element(Tag.valueOf("tag1"), baseUri);
        xmlTreeBuilder.insertNode(node);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals(node, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testParseFragment() {
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors, settings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("root", element.nodeName());
    }
}