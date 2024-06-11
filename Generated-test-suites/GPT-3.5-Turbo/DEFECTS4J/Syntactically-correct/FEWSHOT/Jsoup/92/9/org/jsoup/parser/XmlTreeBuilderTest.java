package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag>data</tag></xml>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);

        builder.initialiseParse(reader, baseUri, parser);

        assertTrue(builder.stack.contains(builder.doc));
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testParseReaderString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag>data</tag></xml>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";

        Document document = builder.parse(reader, baseUri);

        assertEquals(input, document.toString());
        assertEquals(input, document.html());
    }

    @Test
    public void testParseStringString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag>data</tag></xml>";
        String baseUri = "http://example.com";

        Document document = builder.parse(input, baseUri);

        assertEquals(input, document.toString());
        assertEquals(input, document.html());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag tag = new Token.StartTag("tag", "attr1=\"value1\" attr2=\"value2\"", false);

        Element element = builder.insert(tag);

        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertEquals("value1", element.attr("attr1"));
        assertEquals("value2", element.attr("attr2"));
    }

    @Test
    public void testProcessStartTagSelfClosing() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag tag = new Token.StartTag("tag", "attr1=\"value1\" attr2=\"value2\"", true);

        Element element = builder.insert(tag);

        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertEquals("value1", element.attr("attr1"));
        assertEquals("value2", element.attr("attr2"));
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("This is a comment");

        builder.insert(comment);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertEquals("This is a comment", builder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testProcessBogusComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("<!DOCTYPE html>");

        builder.insert(comment);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof XmlDeclaration);
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("This is some text");

        builder.insert(character);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertEquals("This is some text", builder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testProcessCData() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("This is some text");

        builder.insert(character);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertEquals("This is some text", builder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "");

        builder.insert(doctype);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");

        Element element1 = new Element("tag1");
        Element element2 = new Element("tag2");
        Element element3 = new Element("tag");

        builder.stack.add(element1);
        builder.stack.add(element2);
        builder.stack.add(element3);

        builder.popStackToClose(endTag);

        assertEquals(2, builder.stack.size());
        assertFalse(builder.stack.contains(element3));
        assertTrue(builder.stack.contains(element1));
        assertTrue(builder.stack.contains(element2));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag>Data</tag>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);

        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, parser);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag", nodes.get(0).nodeName());
        assertEquals("Data", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag>Data</tag>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);

        Element context = new Element("context");

        List<Node> nodes = builder.parseFragment(inputFragment, context, baseUri, parser);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag", nodes.get(0).nodeName());
        assertEquals("Data", ((Element) nodes.get(0)).text());
    }
}