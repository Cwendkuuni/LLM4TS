package org.jsoup.parser;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testParseWithoutErrors() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertEquals("https://example.com", document.baseUri());
        assertEquals("html", document.tagName());
        assertEquals(1, document.childNodeSize());

        Node htmlNode = document.childNode(0);
        assertEquals("html", htmlNode.nodeName());
        assertEquals(1, htmlNode.childNodeSize());

        Node bodyNode = htmlNode.childNode(0);
        assertEquals("body", bodyNode.nodeName());
        assertEquals(1, bodyNode.childNodeSize());

        Node h1Node = bodyNode.childNode(0);
        assertEquals("h1", h1Node.nodeName());
        assertEquals(1, h1Node.childNodeSize());

        Node textNode = h1Node.childNode(0);
        assertEquals("#text", textNode.nodeName());
        assertEquals("Hello World", ((TextNode) textNode).text());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><body>Hello World</body></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true, false);

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        Document document = xmlTreeBuilder.getDocument();
        assertEquals(baseUri, document.baseUri());
        assertEquals(true, document.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testProcessStartTag() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, ParseSettings.preserveCase);

        Token token = new Token.StartTag("h1", "h1");
        xmlTreeBuilder.process(token);

        Document document = xmlTreeBuilder.getDocument();
        assertEquals(2, document.childNodeSize());

        Node htmlNode = document.childNode(0);
        assertEquals("html", htmlNode.nodeName());
        assertEquals(0, htmlNode.childNodeSize());

        Node h1Node = document.childNode(1);
        assertEquals("h1", h1Node.nodeName());
        assertEquals(0, h1Node.childNodeSize());
    }

    @Test
    public void testProcessEndTag() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, ParseSettings.preserveCase);

        Token token = new Token.EndTag("h1");
        xmlTreeBuilder.process(token);

        Document document = xmlTreeBuilder.getDocument();
        assertEquals(1, document.childNodeSize());

        Node htmlNode = document.childNode(0);
        assertEquals("html", htmlNode.nodeName());
        assertEquals(1, htmlNode.childNodeSize());

        Node bodyNode = htmlNode.childNode(0);
        assertEquals("body", bodyNode.nodeName());
        assertEquals(0, bodyNode.childNodeSize());
    }

    @Test
    public void testProcessComment() {
        String input = "<html><!--This is a comment--><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, ParseSettings.preserveCase);

        Token token = new Token.Comment("This is a comment");
        xmlTreeBuilder.process(token);

        Document document = xmlTreeBuilder.getDocument();
        assertEquals(2, document.childNodeSize());

        Node htmlNode = document.childNode(0);
        assertEquals("html", htmlNode.nodeName());
        assertEquals(1, htmlNode.childNodeSize());

        Node commentNode = htmlNode.childNode(0);
        assertEquals("#comment", commentNode.nodeName());
        assertEquals("This is a comment", ((Comment) commentNode).getData());
    }

    @Test
    public void testProcessCharacter() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, ParseSettings.preserveCase);

        Token token = new Token.Character("Hello World");
        xmlTreeBuilder.process(token);

        Document document = xmlTreeBuilder.getDocument();
        assertEquals(2, document.childNodeSize());

        Node htmlNode = document.childNode(0);
        assertEquals("html", htmlNode.nodeName());
        assertEquals(1, htmlNode.childNodeSize());

        Node bodyNode = htmlNode.childNode(0);
        assertEquals("body", bodyNode.nodeName());
        assertEquals(1, bodyNode.childNodeSize());

        Node h1Node = bodyNode.childNode(0);
        assertEquals("h1", h1Node.nodeName());
        assertEquals(1, h1Node.childNodeSize());

        Node textNode = h1Node.childNode(0);
        assertEquals("#text", textNode.nodeName());
        assertEquals("Hello World", ((TextNode) textNode).text());
    }

    @Test
    public void testProcessDoctype() {
        String input = "<!DOCTYPE html><html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, ParseSettings.preserveCase);

        Token token = new Token.Doctype("html");
        xmlTreeBuilder.process(token);

        Document document = xmlTreeBuilder.getDocument();
        assertEquals(2, document.childNodeSize());

        Node doctypeNode = document.childNode(0);
        assertEquals("#doctype", doctypeNode.nodeName());
        assertEquals("html", ((DocumentType) doctypeNode).nodeName());
    }

    @Test
    public void testInsertNode() {
        Node node = new Element("h1");
        xmlTreeBuilder.insertNode(node);

        Document document = xmlTreeBuilder.getDocument();
        assertEquals(1, document.childNodeSize());

        Node htmlNode = document.childNode(0);
        assertEquals("html", htmlNode.nodeName());
        assertEquals(1, htmlNode.childNodeSize());

        Node bodyNode = htmlNode.childNode(0);
        assertEquals("body", bodyNode.nodeName());
        assertEquals(1, bodyNode.childNodeSize());

        Node h1Node = bodyNode.childNode(0);
        assertEquals("h1", h1Node.nodeName());
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("h1");
        Element element = xmlTreeBuilder.insert(startTag);

        Document document = xmlTreeBuilder.getDocument();
        assertEquals(2, document.childNodeSize());

        Node htmlNode = document.childNode(0);
        assertEquals("html", htmlNode.nodeName());
        assertEquals(1, htmlNode.childNodeSize());

        Node h1Node = document.childNode(1);
        assertEquals("h1", h1Node.nodeName());
        assertEquals(element, h1Node);
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);

        Document document = xmlTreeBuilder.getDocument();
        assertEquals(2, document.childNodeSize());

        Node htmlNode = document.childNode(0);
        assertEquals("html", htmlNode.nodeName());
        assertEquals(1, htmlNode.childNodeSize());

        Node commentNode = htmlNode.childNode(0);
        assertEquals("#comment", commentNode.nodeName());
        assertEquals("This is a comment", ((Comment) commentNode).getData());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("Hello World");
        xmlTreeBuilder.insert(characterToken);

        Document document = xmlTreeBuilder.getDocument();
        assertEquals(2, document.childNodeSize());

        Node htmlNode = document.childNode(0);
        assertEquals("html", htmlNode.nodeName());
        assertEquals(1, htmlNode.childNodeSize());

        Node bodyNode = htmlNode.childNode(0);
        assertEquals("body", bodyNode.nodeName());
        assertEquals(1, bodyNode.childNodeSize());

        Node h1Node = bodyNode.childNode(0);
        assertEquals("h1", h1Node.nodeName());
        assertEquals(1, h1Node.childNodeSize());

        Node textNode = h1Node.childNode(0);
        assertEquals("#text", textNode.nodeName());
        assertEquals("Hello World", ((TextNode) textNode).text());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctype);

        Document document = xmlTreeBuilder.getDocument();
        assertEquals(2, document.childNodeSize());

        Node doctypeNode = document.childNode(0);
        assertEquals("#doctype", doctypeNode.nodeName());
        assertEquals("html", ((DocumentType) doctypeNode).nodeName());
    }

    @Test
    public void testPopStackToClose() {
        Token.StartTag startTag1 = new Token.StartTag("h1");
        Token.StartTag startTag2 = new Token.StartTag("div");
        xmlTreeBuilder.insert(startTag1);
        xmlTreeBuilder.insert(startTag2);

        Document document = xmlTreeBuilder.getDocument();
        assertEquals(3, document.childNodeSize());

        Node htmlNode = document.childNode(0);
        assertEquals("html", htmlNode.nodeName());
        assertEquals(2, htmlNode.childNodeSize());

        xmlTreeBuilder.popStackToClose(new Token.EndTag("h1"));

        assertEquals(2, document.childNodeSize());

        htmlNode = document.childNode(0);
        assertEquals("html", htmlNode.nodeName());
        assertEquals(1, htmlNode.childNodeSize());

        Node divNode = htmlNode.childNode(0);
        assertEquals("div", divNode.nodeName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<h1>Hello World</h1>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true, false);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertEquals(1, nodes.size());

        Node h1Node = nodes.get(0);

        assertEquals("h1", h1Node.nodeName());

        Document document = xmlTreeBuilder.getDocument();
        assertEquals(null, document);
    }

}