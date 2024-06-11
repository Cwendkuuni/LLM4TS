package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings expectedSettings = ParseSettings.preserveCase;
        ParseSettings actualSettings = xmlTreeBuilder.defaultSettings();
        assertEquals(expectedSettings, actualSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        StringReader input = new StringReader("<root></root>");
        String baseUri = "http://www.example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<root></root>";
        String baseUri = "http://www.example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        // Test for successful initialization
        StringReader input = new StringReader("<root></root>");
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        assertEquals(errors, xmlTreeBuilder.getErrors());
        assertEquals(settings, xmlTreeBuilder.getSettings());

        // Test for updating document settings
        StringReader input2 = new StringReader("<root></root>");
        String baseUri2 = "http://www.example.com";
        ParseErrorList errors2 = ParseErrorList.noTracking();
        ParseSettings settings2 = ParseSettings.htmlDefault;
        
        xmlTreeBuilder.initialiseParse(input2, baseUri2, errors2, settings2);
        
        assertEquals(errors2, xmlTreeBuilder.getErrors());
        assertEquals(settings2, xmlTreeBuilder.getSettings());
    }

    @Test
    public void testProcessWithStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes().put("id", "test");

        xmlTreeBuilder.process(startTag);

        // Check if the element is inserted and the tag is correct
        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("div", element.tagName());

        // Check if the attribute is set correctly
        assertEquals("test", element.attr("id"));

        // Check if the tag is known
        assertTrue(element.tag().isKnownTag());
    }

    @Test
    public void testProcessWithEndTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        xmlTreeBuilder.process(startTag);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.process(endTag);

        // Check if the element is popped from the stack
        List<Element> stack = xmlTreeBuilder.getStack();
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testProcessWithComment() {
        String data = "This is a comment";
        Token.Comment commentToken = new Token.Comment(data);
        xmlTreeBuilder.process(commentToken);

        // Check if the comment is inserted as a child node of the current element
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof Comment);
        assertEquals(data, ((Comment) node).getData());
    }

    @Test
    public void testProcessWithCharacter() {
        String data = "This is some text";
        Token.Character characterToken = new Token.Character(data);
        xmlTreeBuilder.process(characterToken);

        // Check if the text node is inserted as a child node of the current element
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        assertEquals(data, ((TextNode) node).getWholeText());
    }

    @Test
    public void testProcessWithDoctype() {
        String name = "html";
        String publicIdentifier = "-//W3C//DTD HTML 4.01//EN";
        String systemIdentifier = "http://www.w3.org/TR/html4/strict.dtd";
        Token.Doctype doctypeToken = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        xmlTreeBuilder.process(doctypeToken);

        // Check if the doctype node is inserted as a child node of the current element
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);

        DocumentType doctype = (DocumentType) node;
        assertEquals(name, doctype.getName());
        assertEquals(publicIdentifier, doctype.getPublicIdentifier());
        assertEquals(systemIdentifier, doctype.getSystemIdentifier());
    }

    @Test
    public void testInsertNode() {
        Node node = new Element("div");
        xmlTreeBuilder.insertNode(node);

        // Check if the node is inserted as a child node of the current element
        Node childNode = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(childNode);
        assertEquals(node, childNode);
    }

    @Test
    public void testInsertWithStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);

        // Check if the element is inserted and returned correctly
        assertNotNull(element);
        assertEquals("div", element.tagName());

        // Check if the element is added to the stack if it is not self-closing
        List<Element> stack = xmlTreeBuilder.getStack();
        assertFalse(stack.isEmpty());
        assertTrue(stack.contains(element));
    }

    @Test
    public void testInsertWithCommentToken() {
        String data = "This is a comment";
        Token.Comment commentToken = new Token.Comment(data);
        xmlTreeBuilder.insert(commentToken);

        // Check if the comment is inserted as a child node of the current element
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof Comment);
        assertEquals(data, ((Comment) node).getData());
    }

    @Test
    public void testInsertWithCharacterToken() {
        String data = "This is some text";
        Token.Character characterToken = new Token.Character(data);
        xmlTreeBuilder.insert(characterToken);

        // Check if the text node is inserted as a child node of the current element
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        assertEquals(data, ((TextNode) node).getWholeText());
    }

    @Test
    public void testInsertWithDoctypeToken() {
        String name = "html";
        String publicIdentifier = "-//W3C//DTD HTML 4.01//EN";
        String systemIdentifier = "http://www.w3.org/TR/html4/strict.dtd";
        Token.Doctype doctypeToken = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        xmlTreeBuilder.insert(doctypeToken);

        // Check if the doctype node is inserted as a child node of the current element
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);

        DocumentType doctype = (DocumentType) node;
        assertEquals(name, doctype.getName());
        assertEquals(publicIdentifier, doctype.getPublicIdentifier());
        assertEquals(systemIdentifier, doctype.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        String tagName = "div";

        Token.StartTag startTag1 = new Token.StartTag(tagName);
        xmlTreeBuilder.process(startTag1);

        Token.StartTag startTag2 = new Token.StartTag(tagName);
        xmlTreeBuilder.process(startTag2);

        Token.EndTag endTag = new Token.EndTag(tagName);
        xmlTreeBuilder.process(endTag);

        // Check if the second element is removed from the stack
        List<Element> stack = xmlTreeBuilder.getStack();
        assertFalse(stack.isEmpty());
        assertEquals(1, stack.size());
        assertNotEquals(tagName, stack.get(0).tagName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div><span></span></div>";
        String baseUri = "http://www.example.com";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);

        // Check if the fragment is parsed correctly and returned as a list of nodes
        assertNotNull(nodes);
        assertEquals(1, nodes.size());

        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        assertEquals("div", ((Element) node).tagName());
    }
}