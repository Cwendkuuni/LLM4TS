package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseReaderString() {
        String input = "<root><child1>value1</child1><child2>value2</child2></root>";
        Reader reader = new StringReader(input);
        Document document = xmlTreeBuilder.parse(reader, "http://example.com");

        assertEquals("http://example.com", document.baseUri());
        assertEquals(1, document.childNodeSize());
        assertEquals("root", document.child(0).nodeName());

        Element root = document.child(0);
        assertEquals(2, root.childNodeSize());
        assertEquals("child1", root.child(0).nodeName());
        assertEquals("value1", root.child(0).text());
        assertEquals("child2", root.child(1).nodeName());
        assertEquals("value2", root.child(1).text());
    }

    @Test
    public void testParseStringString() {
        String input = "<root><child1>value1</child1><child2>value2</child2></root>";
        Document document = xmlTreeBuilder.parse(input, "http://example.com");

        assertEquals("http://example.com", document.baseUri());
        assertEquals(1, document.childNodeSize());
        assertEquals("root", document.child(0).nodeName());

        Element root = document.child(0);
        assertEquals(2, root.childNodeSize());
        assertEquals("child1", root.child(0).nodeName());
        assertEquals("value1", root.child(0).text());
        assertEquals("child2", root.child(1).nodeName());
        assertEquals("value2", root.child(1).text());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><child1>value1</child1><child2>value2</child2></root>";
        Reader reader = new StringReader(input);
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, settings);

        assertEquals("http://example.com", xmlTreeBuilder.baseUri);
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(settings, xmlTreeBuilder.settings);

        Document doc = xmlTreeBuilder.doc;
        assertEquals(1, doc.childNodeSize());
        assertEquals("root", doc.child(0).nodeName());

        Element root = doc.child(0);
        assertEquals(2, root.childNodeSize());
        assertEquals("child1", root.child(0).nodeName());
        assertEquals("value1", root.child(0).text());
        assertEquals("child2", root.child(1).nodeName());
        assertEquals("value2", root.child(1).text());
    }

    @Test
    public void testProcessStartTagToken() {
        Token.StartTag startTag = new Token.StartTag("child", "<child>", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("child", element.nodeName());
        assertFalse(startTag.isSelfClosing());
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testProcessEndTagToken() {
        Element element = new Element(Tag.valueOf("child"), "http://example.com");
        xmlTreeBuilder.stack.add(element);

        Token.EndTag endTag = new Token.EndTag("child");
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessCommentToken() {
        Token.Comment commentToken = new Token.Comment("<!-- comment -->");
        xmlTreeBuilder.insert(commentToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("<!-- comment -->", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testProcessCharacterToken() {
        Token.Character characterToken = new Token.Character("character data");
        xmlTreeBuilder.insert(characterToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("character data", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testProcessDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctypeToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("<!DOCTYPE html>", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<child1>value1</child1><child2>value2</child2>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertEquals(2, nodes.size());
        assertEquals("child1", nodes.get(0).nodeName());
        assertEquals("value1", nodes.get(0).text());
        assertEquals("child2", nodes.get(1).nodeName());
        assertEquals("value2", nodes.get(1).text());
    }
}