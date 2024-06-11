package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testInitialiseParse() {
        // Test with input, baseUri, and parser
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        StringReader input = new StringReader("<doc>Test</doc>");
        String baseUri = "http://example.com";
        Parser parser = new Parser(treeBuilder);
        treeBuilder.initialiseParse(input, baseUri, parser);

        assertEquals(treeBuilder.stack.size(), 1);
        assertTrue(treeBuilder.stack.get(0) instanceof Document);
        assertEquals(treeBuilder.doc.outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        // Test with input and baseUri
        StringReader input = new StringReader("<doc>Test</doc>");
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals(document.baseUri(), baseUri);
        assertEquals(document.childNodes().size(), 1);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        // Test with input and baseUri
        String input = "<doc>Test</doc>";
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals(document.baseUri(), baseUri);
        assertEquals(document.childNodes().size(), 1);
    }

    @Test
    public void testProcessStartTag() {
        // Test with StartTag token
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes.put("attr", "value");

        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals(element.tagName(), "tag");
        assertEquals(element.attributes().size(), 1);
        assertEquals(element.attributes().get("attr"), "value");
    }

    @Test
    public void testProcessEndTag() {
        // Test with EndTag token
        Token.EndTag endTag = new Token.EndTag("tag");

        xmlTreeBuilder.stack.add(new Element("tag"));
        xmlTreeBuilder.stack.add(new Element("anotherTag"));

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(xmlTreeBuilder.stack.size(), 1);
        assertEquals(xmlTreeBuilder.stack.get(0).tagName(), "anotherTag");
    }

    @Test
    public void testProcessComment() {
        // Test with Comment token
        Token.Comment commentToken = new Token.Comment("Test comment");

        xmlTreeBuilder.insert(commentToken);

        assertEquals(xmlTreeBuilder.currentElement().childNode(0).toString(), "<!--Test comment-->");
    }

    @Test
    public void testProcessCharacter() {
        // Test with Character token
        Token.Character characterToken = new Token.Character("Test character");

        xmlTreeBuilder.insert(characterToken);

        assertEquals(xmlTreeBuilder.currentElement().childNode(0).toString(), "Test character");
    }

    @Test
    public void testProcessDoctype() {
        // Test with Doctype token
        Token.Doctype doctype = new Token.Doctype("html");

        xmlTreeBuilder.insert(doctype);

        assertEquals(xmlTreeBuilder.currentElement().childNode(0).toString(), "<!DOCTYPE html>");
    }

    @Test
    public void testInsertNode() {
        // Test with Node
        Node node = new Node("test");

        xmlTreeBuilder.insertNode(node);

        assertEquals(xmlTreeBuilder.currentElement().childNode(0), node);
    }

    @Test
    public void testParseFragmentWithoutContext() {
        // Test with inputFragment and baseUri
        String inputFragment = "<tag>Test</tag>";
        String baseUri = "http://example.com";

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, new Parser(xmlTreeBuilder));

        assertEquals(nodes.size(), 1);
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals(((Element) nodes.get(0)).tagName(), "tag");
        assertEquals(((Element) nodes.get(0)).childNodes().size(), 1);
    }

    @Test
    public void testParseFragmentWithContext() {
        // Test with inputFragment, context, and baseUri
        String inputFragment = "<tag>Test</tag>";
        Element context = new Element("context");
        String baseUri = "http://example.com";

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, new Parser(xmlTreeBuilder));

        assertEquals(nodes.size(), 1);
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals(((Element) nodes.get(0)).tagName(), "tag");
        assertEquals(((Element) nodes.get(0)).childNodes().size(), 1);
    }
}