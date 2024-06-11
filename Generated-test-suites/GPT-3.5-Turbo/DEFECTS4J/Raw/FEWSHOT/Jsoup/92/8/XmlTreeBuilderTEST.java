package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;

import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

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
    public void testInitialiseParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(xmlTreeBuilder);
        
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, parser);
        
        assertEquals("https://example.com", xmlTreeBuilder.baseUri());
        Document document = xmlTreeBuilder.doc();
        assertEquals("html", document.nodeName());
        assertFalse(document.outputSettings().prettyPrint());
    }

    @Test
    public void testParse() {
        String input = "<p>Test</p>";

        Document document = xmlTreeBuilder.parse(input, "https://example.com");

        assertEquals("https://example.com", document.baseUri());
        Element rootElement = document.child(0);
        assertTrue(rootElement.tagName().equals("p"));
        assertEquals("Test", rootElement.text());
    }

    @Test
    public void testProcess_startTag() {
        Token token = new Token.StartTag("p");
        Element element = xmlTreeBuilder.process(token);

        assertEquals("p", element.tagName());
        assertTrue(xmlTreeBuilder.stack().contains(element));
    }

    @Test
    public void testProcess_endTag() {
        Token.StartTag startTag = new Token.StartTag("p");
        Element element = xmlTreeBuilder.process(startTag);

        Token.EndTag endTag = new Token.EndTag("p");
        xmlTreeBuilder.process(endTag);

        assertFalse(xmlTreeBuilder.stack().contains(element));
    }

    @Test
    public void testProcess_comment() {
        String commentData = "This is a comment";
        Token.Comment commentToken = new Token.Comment(commentData);
        Comment comment = xmlTreeBuilder.process(commentToken);

        assertEquals(commentData, comment.getData());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(comment));
    }

    @Test
    public void testProcess_character() {
        String characterData = "Test";
        Token.Character characterToken = new Token.Character(characterData);
        TextNode textNode = xmlTreeBuilder.process(characterToken);

        assertEquals(characterData, textNode.text());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(textNode));
    }

    @Test
    public void testProcess_doctype() {
        String name = "html";
        String publicIdentifier = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        Token.Doctype doctypeToken = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        DocumentType documentType = xmlTreeBuilder.process(doctypeToken);

        assertEquals("HTML", documentType.nodeName());
        assertEquals(publicIdentifier, documentType.attr("PUBLIC"));
        assertEquals(systemIdentifier, documentType.attr("SYSTEM"));
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(documentType));
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("div");
        xmlTreeBuilder.insertNode(element);

        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(element));
    }

    @Test
    public void testInsert() {
        Token.StartTag startTag = new Token.StartTag("p");
        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("p", element.tagName());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(element));

        Token.EndTag endTag = new Token.EndTag("p");
        xmlTreeBuilder.process(endTag);

        assertFalse(xmlTreeBuilder.stack().contains(element));
    }

    @Test
    public void testInsert_comment() {
        String commentData = "This is a comment";
        Token.Comment commentToken = new Token.Comment(commentData);
        Comment comment = xmlTreeBuilder.insert(commentToken);

        assertEquals(commentData, comment.getData());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(comment));
    }

    @Test
    public void testInsert_character() {
        String characterData = "Test";
        Token.Character characterToken = new Token.Character(characterData);
        TextNode textNode = xmlTreeBuilder.insert(characterToken);

        assertEquals(characterData, textNode.text());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(textNode));
    }

    @Test
    public void testInsert_doctype() {
        String name = "html";
        String publicIdentifier = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        Token.Doctype doctypeToken = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        DocumentType documentType = xmlTreeBuilder.insert(doctypeToken);

        assertEquals("HTML", documentType.nodeName());
        assertEquals(publicIdentifier, documentType.attr("PUBLIC"));
        assertEquals(systemIdentifier, documentType.attr("SYSTEM"));
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(documentType));
    }

    @Test
    public void testPopStackToClose_tagFound() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);
        xmlTreeBuilder.stack().add(element);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);

        assertFalse(xmlTreeBuilder.stack().contains(element));
    }

    @Test
    public void testPopStackToClose_tagNotFound() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);
        xmlTreeBuilder.stack().add(element);

        Token.EndTag endTag = new Token.EndTag("p");
        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.stack().contains(element));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<p>Test</p>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);

        assertFalse(nodes.isEmpty());
        assertEquals("p", nodes.get(0).nodeName());
        assertEquals("https://example.com", nodes.get(0).baseUri());
    }

    @Test
    public void testParseFragment_withContext() {
        String inputFragment = "<p>Test</p>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);

        assertFalse(nodes.isEmpty());
        assertEquals("p", nodes.get(0).nodeName());
        assertEquals("https://example.com", nodes.get(0).baseUri());
    }
}