package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        // Typical use case
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>";
        String baseUri = "https://example.com/";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals("https://example.com/", xmlTreeBuilder.baseUri);
        assertNotNull(xmlTreeBuilder.doc);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.peek());
    }

    @Test
    public void testProcess_StartTag() {
        // Typical use case
        Token token = new Token.StartTag("tag");
        Element el = xmlTreeBuilder.insert(token.asStartTag());

        assertEquals("tag", el.tagName());
        assertEquals("https://example.com/", el.baseUri());
        assertEquals(0, el.childNodeSize());
    }

    @Test
    public void testProcess_EndTag() {
        // Typical use case
        Token token = new Token.EndTag("tag");
        Element parentElement = new Element("parent", "https://example.com/");
        xmlTreeBuilder.stack.push(parentElement);

        xmlTreeBuilder.popStackToClose(token.asEndTag());

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcess_Comment() {
        // Typical use case
        Token token = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(token.asComment());

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof Comment);
        assertEquals("This is a comment", ((Comment) node).getData());
    }

    @Test
    public void testProcess_Character() {
        // Typical use case
        Token token = new Token.Character("This is a character");
        xmlTreeBuilder.insert(token.asCharacter());

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        assertEquals("This is a character", ((TextNode) node).text());
    }

    @Test
    public void testProcess_Doctype() {
        // Typical use case
        Token.Doctype token = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(token);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);
        assertEquals("html", ((DocumentType) node).name());
    }

    @Test
    public void testInsertNode() {
        // Typical use case
        Element parentElement = new Element("parent", "https://example.com/");
        xmlTreeBuilder.stack.push(parentElement);

        Element childElement = new Element("child", "https://example.com/");
        xmlTreeBuilder.insertNode(childElement);

        assertEquals(1, parentElement.childNodeSize());
        assertEquals(childElement, parentElement.childNode(0));
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        // Typical use case
        Token.StartTag startTag = new Token.StartTag("tag", "attribute", true);
        Element el = xmlTreeBuilder.insert(startTag);

        assertEquals("tag", el.tagName());
        assertTrue(el.isSelfClosing());
        assertEquals("https://example.com/", el.baseUri());
        assertEquals(0, el.childNodeSize());
        assertEquals(1, xmlTreeBuilder.tokeniser.selfClosing);
    }

    @Test
    public void testInsert_StartTag_Unknown() {
        // Typical use case
        Token.StartTag startTag = new Token.StartTag("unknown", "attribute", false);
        Element el = xmlTreeBuilder.insert(startTag);

        assertEquals("unknown", el.tagName());
        assertEquals("https://example.com/", el.baseUri());
        assertEquals(0, el.childNodeSize());
        assertEquals(0, xmlTreeBuilder.tokeniser.selfClosing);
    }

    @Test
    public void testInsert_StartTag_Nested() {
        // Typical use case
        Token.StartTag outerStartTag = new Token.StartTag("outer");
        xmlTreeBuilder.insert(outerStartTag);

        Token.StartTag innerStartTag = new Token.StartTag("inner");
        Element innerElement = xmlTreeBuilder.insert(innerStartTag);

        assertEquals("inner", innerElement.tagName());
        assertEquals("https://example.com/", innerElement.baseUri());
        assertEquals(1, innerElement.childNodeSize());
    }

    @Test
    public void testInsert_Comment() {
        // Typical use case
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof Comment);
        assertEquals("This is a comment", ((Comment) node).getData());
    }

    @Test
    public void testInsert_Character() {
        // Typical use case
        Token.Character characterToken = new Token.Character("This is a character");
        xmlTreeBuilder.insert(characterToken);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        assertEquals("This is a character", ((TextNode) node).text());
    }

    @Test
    public void testInsert_Doctype() {
        // Typical use case
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(doctypeToken);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);
        assertEquals("html", ((DocumentType) node).name());
    }

    @Test
    public void testPopStackToClose_ElementFound() {
        // Typical use case
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element1 = new Element("tag", "https://example.com/");
        Element element2 = new Element("tag", "https://example.com/");
        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testPopStackToClose_ElementNotFound() {
        // Edge case: Element not found in the stack
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element1 = new Element("tag1", "https://example.com/");
        Element element2 = new Element("tag2", "https://example.com/");
        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(2, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testPopStackToClose_ElementFoundAfterRemoval() {
        // Edge case: Element found after removing subsequent elements from the stack
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element1 = new Element("tag1", "https://example.com/");
        Element element2 = new Element("tag", "https://example.com/");
        Element element3 = new Element("tag3", "https://example.com/");
        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);
        xmlTreeBuilder.stack.push(element3);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element1, xmlTreeBuilder.stack.peek());
    }
}