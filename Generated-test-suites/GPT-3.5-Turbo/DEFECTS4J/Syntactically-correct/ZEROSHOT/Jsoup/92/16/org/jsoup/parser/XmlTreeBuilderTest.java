package org.jsoup.parser;

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
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        xmlTreeBuilder.initialiseParse(input, baseUri, parser);

        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertTrue(xmlTreeBuilder.getStack().contains(xmlTreeBuilder.getDocument()));

        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        assertEquals(syntax, xmlTreeBuilder.getDocument().outputSettings().syntax());
    }

    @Test
    public void testParseReaderString() {
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseStringString() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("xml", "xmlns", "http://www.w3.org/XML/1998/namespace");
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("xml", element.tagName());
        assertEquals("http://www.w3.org/XML/1998/namespace", element.attr("xmlns"));
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("xml");
        xmlTreeBuilder.getStack().add(new Element("xml"));

        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);

        assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
        assertTrue(xmlTreeBuilder.getDocument().childNode(0) instanceof Comment);
        Comment comment = (Comment) xmlTreeBuilder.getDocument().childNode(0);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("This is a character");
        xmlTreeBuilder.insert(characterToken);

        assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
        assertTrue(xmlTreeBuilder.getDocument().childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) xmlTreeBuilder.getDocument().childNode(0);
        assertEquals("This is a character", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd");
        xmlTreeBuilder.insert(doctype);

        assertEquals(1, xmlTreeBuilder.getDocument().childNodes().size());
        assertTrue(xmlTreeBuilder.getDocument().childNode(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.getDocument().childNode(0);
        assertEquals("html", documentType.name());
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.publicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.systemIdentifier());
    }

    @Test
    public void testInsertNode() {
        Node node = new TextNode("This is a test");
        xmlTreeBuilder.insertNode(node);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("This is a test", textNode.text());
    }

    @Test
    public void testInsertSelfClosingTag() {
        Token.StartTag startTag = new Token.StartTag("br", false);
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("br", element.tagName());
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testPopStackToCloseFound() {
        Element element = new Element("div");
        xmlTreeBuilder.getStack().add(new Element("div"));
        xmlTreeBuilder.getStack().add(element);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);

        assertFalse(xmlTreeBuilder.getStack().contains(element));
    }

    @Test
    public void testPopStackToCloseNotFound() {
        Element element1 = new Element("div");
        Element element2 = new Element("span");
        xmlTreeBuilder.getStack().add(element1);
        xmlTreeBuilder.getStack().add(element2);

        Token.EndTag endTag = new Token.EndTag("p");
        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.getStack().contains(element1));
        assertTrue(xmlTreeBuilder.getStack().contains(element2));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<h1>Heading</h1><p>Paragraph</p>";
        String baseUri = "http://example.com";

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, new Parser(xmlTreeBuilder));

        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);
        Element firstElement = (Element) nodes.get(0);
        Element secondElement = (Element) nodes.get(1);
        assertEquals("h1", firstElement.tagName());
        assertEquals("Heading", firstElement.text());
        assertEquals("p", secondElement.tagName());
        assertEquals("Paragraph", secondElement.text());
    }

    @Test
    public void testParseFragmentWithContext() {
        String inputFragment = "<h1>Heading</h1><p>Paragraph</p>";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, new Parser(xmlTreeBuilder));

        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);
        Element firstElement = (Element) nodes.get(0);
        Element secondElement = (Element) nodes.get(1);
        assertEquals("h1", firstElement.tagName());
        assertEquals("Heading", firstElement.text());
        assertEquals("p", secondElement.tagName());
        assertEquals("Paragraph", secondElement.text());
    }
}