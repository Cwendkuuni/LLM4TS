package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root>Hello World!</root>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Assertions
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(baseUri, xmlTreeBuilder.doc.baseUri());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = Token.StartTag.valueOf("span");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("div"), "https://www.example.com"));

        xmlTreeBuilder.process(new Token(startTag));

        // Assertions
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("span", xmlTreeBuilder.currentElement().tagName());
        assertEquals("https://www.example.com", xmlTreeBuilder.baseUri);
    }

    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = Token.EndTag.valueOf("span");
        Element divElement = new Element(Tag.valueOf("div"), "https://www.example.com");
        xmlTreeBuilder.stack.add(divElement);
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("span"), "https://www.example.com"));

        xmlTreeBuilder.process(new Token(endTag));

        // Assertions
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertSame(divElement, xmlTreeBuilder.currentElement());
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment commentToken = Token.Comment.create("This is a comment");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("div"), "https://www.example.com"));

        xmlTreeBuilder.process(new Token(commentToken));

        // Assertions
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) xmlTreeBuilder.currentElement().childNode(0)).getData());
    }

    @Test
    public void testProcess_Character() {
        Token.Character characterToken = Token.Character.create("Hello World!");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("div"), "https://www.example.com"));

        xmlTreeBuilder.process(new Token(characterToken));

        // Assertions
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
        assertEquals("Hello World!", ((TextNode) xmlTreeBuilder.currentElement().childNode(0)).getWholeText());
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctypeToken = Token.Doctype.createFromToken("html", "", "", "https://www.example.com");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("div"), "https://www.example.com"));

        xmlTreeBuilder.process(new Token(doctypeToken));

        // Assertions
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("html", documentType.getName());
        assertEquals("", documentType.getPublicIdentifier());
        assertEquals("", documentType.getSystemIdentifier());
        assertEquals("https://www.example.com", documentType.baseUri());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("span"), "https://www.example.com");
        Element divElement = new Element(Tag.valueOf("div"), "https://www.example.com");
        divElement.appendChild(new TextNode("Hello World!", "https://www.example.com"));
        xmlTreeBuilder.stack.add(divElement);

        xmlTreeBuilder.insertNode(element);

        // Assertions
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertSame(element, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = Token.StartTag.valueOf("span");
        Element divElement = new Element(Tag.valueOf("div"), "https://www.example.com");
        xmlTreeBuilder.stack.add(divElement);

        Element result = xmlTreeBuilder.insert(startTag);

        // Assertions
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertSame(result, xmlTreeBuilder.currentElement().childNode(0));
        assertFalse(result.tag().isKnownTag());
        assertEquals("span", result.tagName());
        assertEquals("https://www.example.com", result.baseUri());
        assertEquals(0, startTag.attributes.size());
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        Token.StartTag startTag = Token.StartTag.valueOf("span");
        startTag.selfClosing = true;

        Element result = xmlTreeBuilder.insert(startTag);

        // Assertions
        assertNull(result);
        assertTrue(startTag.selfClosing);
        assertTrue(startTag.tag.isKnownTag());
        assertTrue(startTag.tag.isSelfClosing());
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = Token.Comment.create("This is a comment");

        xmlTreeBuilder.insert(commentToken);

        // Assertions
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) xmlTreeBuilder.currentElement().childNode(0)).getData());
    }

    @Test
    public void testInsert_Comment_Bogus() {
        Token.Comment commentToken = Token.Comment.create("<!DOCTYPE html>");

        xmlTreeBuilder.insert(commentToken);

        // Assertions
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof XmlDeclaration);
        assertEquals("<!DOCTYPE html>", ((XmlDeclaration) xmlTreeBuilder.currentElement().childNode(0)).getDeclaration());
        assertEquals("https://www.example.com", ((XmlDeclaration) xmlTreeBuilder.currentElement().childNode(0)).baseUri());
        assertTrue(((XmlDeclaration) xmlTreeBuilder.currentElement().childNode(0)).isProcessingInstruction());
    }

    @Test
    public void testInsert_Character() {
        Token.Character characterToken = Token.Character.create("Hello World!");

        xmlTreeBuilder.insert(characterToken);

        // Assertions
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
        assertEquals("Hello World!", ((TextNode) xmlTreeBuilder.currentElement().childNode(0)).getWholeText());
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctypeToken = Token.Doctype.createFromToken("html", "", "", "https://www.example.com");

        xmlTreeBuilder.insert(doctypeToken);

        // Assertions
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("html", documentType.getName());
        assertEquals("", documentType.getPublicIdentifier());
        assertEquals("", documentType.getSystemIdentifier());
        assertEquals("https://www.example.com", documentType.baseUri());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = Token.EndTag.valueOf("span");
        Element divElement = new Element(Tag.valueOf("div"), "https://www.example.com");
        Element spanElement = new Element(Tag.valueOf("span"), "https://www.example.com");
        divElement.appendChild(spanElement);
        xmlTreeBuilder.stack.add(divElement);
        xmlTreeBuilder.stack.add(spanElement);

        xmlTreeBuilder.popStackToClose(endTag);

        // Assertions
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertSame(divElement, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<span>Hello World!</span>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        List<Node> nodeList = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        // Assertions
        assertEquals(1, nodeList.size());
        assertTrue(nodeList.get(0) instanceof Element);
        Element element = (Element) nodeList.get(0);
        assertEquals("span", element.tagName());
        assertEquals("Hello World!", element.text());
        assertEquals("https://www.example.com", element.baseUri());
    }
}