package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><tag>Hello World</tag></xml>";
        Document doc = xmlTreeBuilder.parse(input, "baseUri");
        assertNotNull(doc);
        Element root = doc.rootElement();
        assertEquals("xml", root.tagName());
        Element tag = root.child(0);
        assertEquals("tag", tag.tagName());
        assertEquals("Hello World", tag.text());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><tag>Hello World</tag></xml>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, "baseUri", errors, settings);
        assertTrue(stackIsEmpty(xmlTreeBuilder.stack));
        assertEquals("baseUri", xmlTreeBuilder.doc.baseUri());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag>Hello World</tag>";
        Token token = xmlTreeBuilder.tokeniser.next();
        xmlTreeBuilder.process(token);
        Element element = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertEquals("tag", element.tagName());
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag><subTag></subTag></tag>";
        Token token = xmlTreeBuilder.tokeniser.next();
        xmlTreeBuilder.process(token); // start tag
        token = xmlTreeBuilder.tokeniser.next();
        xmlTreeBuilder.process(token); // end tag
        assertFalse(xmlTreeBuilder.stack.isEmpty());
        assertEquals(1, xmlTreeBuilder.stack.size());
        Element topElement = (Element) xmlTreeBuilder.stack.get(0);
        assertEquals("tag", topElement.tagName());
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedTokenType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag>";
        Token token = xmlTreeBuilder.tokeniser.next();
        xmlTreeBuilder.process(token); // start tag
        token = xmlTreeBuilder.tokeniser.next();
        xmlTreeBuilder.process(token); // unexpected token type
        Validate.fail("Unexpected token type: " + token.type);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element("tag", "baseUri");
        xmlTreeBuilder.insertNode(element);
        Element root = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertEquals(element, root.child(0));
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!-- This is a comment -->";
        Token token = xmlTreeBuilder.tokeniser.next();
        xmlTreeBuilder.insert(token);
        Node node = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertTrue(node instanceof Comment);
        assertEquals("This is a comment", node.text());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag>Hello World</tag>";
        Token token = xmlTreeBuilder.tokeniser.next();
        xmlTreeBuilder.process(token); // start tag
        token = xmlTreeBuilder.tokeniser.next();
        xmlTreeBuilder.insert(token); // character
        Node node = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertTrue(node instanceof TextNode);
        assertEquals("Hello World", node.text());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!DOCTYPE tag SYSTEM \"systemId\">";
        Token token = xmlTreeBuilder.tokeniser.next();
        xmlTreeBuilder.insert(token);
        Node node = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertTrue(node instanceof DocumentType);
        assertEquals("tag", node.tagName());
        assertEquals("systemId", node.systemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag><subTag></subTag></tag>";
        xmlTreeBuilder.parse(input, "baseUri");
        assertEquals(2, xmlTreeBuilder.stack.size());
        xmlTreeBuilder.tokeniser.next(); // tag end
        xmlTreeBuilder.popStackToClose(tokeniser.next()); // subTag end
        assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragmentWithEmptyInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testParseFragmentWithValidInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag><subTag>Hello World</subTag></tag>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(3, nodes.size());
        Node root = nodes.get(0);
        assertEquals("tag", root.tagName());
        Node subTag = nodes.get(1);
        assertEquals("subTag", subTag.tagName());
        assertEquals("Hello World", subTag.text());
    }

    private boolean stackIsEmpty(Stack<Element> stack) {
        return stack.isEmpty();
    }
}