package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String input = "<root><child>Test</child></root>";
        String baseUri = "http://example.com";
        Reader reader = new StringReader(input);

        Document document = xmlTreeBuilder.parse(reader, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(1, document.children().size());
        assertEquals("root", document.child(0).nodeName());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<root><child>Test</child></root>";
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(1, document.children().size());
        assertEquals("root", document.child(0).nodeName());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><child>Test</child></root>";
        String baseUri = "http://example.com";
        Reader reader = new StringReader(input);
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);

        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("root", xmlTreeBuilder.stack.get(0).nodeName());

        assertNotNull(xmlTreeBuilder.doc);
        assertEquals(settings, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("child");
        startTag.attributes = new Attributes();

        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("child", element.nodeName());
        assertEquals(0, element.children().size());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("root");
        xmlTreeBuilder.stack.add(new Element("root"));

        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("Test Comment");

        xmlTreeBuilder.insert(comment);

        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof Comment);
        Comment insertedComment = (Comment) xmlTreeBuilder.doc.childNode(0);
        assertEquals("Test Comment", insertedComment.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("Test Character");

        xmlTreeBuilder.insert(character);

        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof TextNode);
        TextNode insertedText = (TextNode) xmlTreeBuilder.doc.childNode(0);
        assertEquals("Test Character", insertedText.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");

        xmlTreeBuilder.insert(doctype);

        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof DocumentType);
        DocumentType insertedDoctype = (DocumentType) xmlTreeBuilder.doc.childNode(0);
        assertEquals("html", insertedDoctype.nodeName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<child>Test</child>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("child", element.nodeName());
        assertEquals("Test", element.text());
    }
}