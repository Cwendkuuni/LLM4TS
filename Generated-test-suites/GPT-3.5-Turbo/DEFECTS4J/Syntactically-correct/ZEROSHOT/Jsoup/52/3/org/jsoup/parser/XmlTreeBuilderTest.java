package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><child1></child1><child2></child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertNotNull(xmlTreeBuilder.stack);
        assertNotNull(xmlTreeBuilder.doc);
        assertNotNull(xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        Token startTagToken = new Token.StartTag("tag", "name", null);
        assertTrue(xmlTreeBuilder.process(startTagToken));

        Token endTagToken = new Token.EndTag("tag");
        assertTrue(xmlTreeBuilder.process(endTagToken));

        Token commentToken = new Token.Comment("comment");
        assertTrue(xmlTreeBuilder.process(commentToken));

        Token characterToken = new Token.Character("character");
        assertTrue(xmlTreeBuilder.process(characterToken));

        Token doctypeToken = new Token.Doctype("doctype");
        assertTrue(xmlTreeBuilder.process(doctypeToken));

        Token eofToken = new Token.EOF();
        assertTrue(xmlTreeBuilder.process(eofToken));
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("tag", "http://example.com");
        xmlTreeBuilder.insertNode(element);

        assertEquals(element, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "name", null);
        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals(element, xmlTreeBuilder.currentElement().childNode(0));
        assertEquals(startTag.attributes, element.attributes());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);

        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
        assertEquals(commentToken.getData(), ((Comment) xmlTreeBuilder.currentElement().childNode(0)).getData());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("character");
        xmlTreeBuilder.insert(characterToken);

        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
        assertEquals(characterToken.getData(), ((TextNode) xmlTreeBuilder.currentElement().childNode(0)).getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("doctype");
        xmlTreeBuilder.insert(doctypeToken);

        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
        assertEquals(doctypeToken.getName(), ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).getName());
        assertEquals(doctypeToken.getPublicIdentifier(), ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).getPublicIdentifier());
        assertEquals(doctypeToken.getSystemIdentifier(), ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).getSystemIdentifier());
        assertEquals(xmlTreeBuilder.baseUri, ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).getBaseUri());
    }

    @Test
    public void testPopStackToClose() {
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");

        xmlTreeBuilder.stack.add(child1);
        xmlTreeBuilder.stack.add(child2);
        xmlTreeBuilder.stack.add(xmlTreeBuilder.doc);

        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);

        assertFalse(xmlTreeBuilder.stack.contains(child1));
        assertFalse(xmlTreeBuilder.stack.contains(child2));
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>content</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNodes().get(0) instanceof Element);
        assertEquals("tag", ((Element) xmlTreeBuilder.doc.childNodes().get(0)).tagName());
        assertEquals("content", ((Element) xmlTreeBuilder.doc.childNodes().get(0)).text());
        assertEquals(baseUri, ((Element) xmlTreeBuilder.doc.childNodes().get(0)).baseUri());
    }
}