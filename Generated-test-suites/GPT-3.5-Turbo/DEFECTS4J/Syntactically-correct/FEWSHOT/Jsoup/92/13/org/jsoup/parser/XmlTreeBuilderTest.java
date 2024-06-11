package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<html></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        builder.initialiseParse(new StringReader(input), baseUri, parser);
        assertEquals(1, builder.stack.size());
        assertEquals("http://example.com", builder.baseUri);
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testParseReader() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<html></html>";
        String baseUri = "http://example.com";
        Document doc = builder.parse(new StringReader(input), baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<html></html>";
        String baseUri = "http://example.com";
        Document doc = builder.parse(input, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("div");
        Element element = builder.insert(startTag);
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("div");
        builder.stack.add(new Element("div"));
        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("Test comment");
        builder.insert(comment);
        assertEquals(1, builder.doc.childNodes().size());
        assertTrue(builder.doc.childNodes().get(0) instanceof Comment);
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("Test character");
        builder.insert(character);
        assertEquals(1, builder.doc.childNodes().size());
        assertTrue(builder.doc.childNodes().get(0) instanceof TextNode);
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        builder.insert(doctype);
        assertEquals(1, builder.doc.childNodes().size());
        assertTrue(builder.doc.childNodes().get(0) instanceof DocumentType);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node node = new Element("div");
        builder.insertNode(node);
        assertEquals(1, builder.currentElement().childNodes().size());
    }
    
    @Test
    public void testParseFragmentString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<div></div>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, parser);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
    
    @Test
    public void testParseFragmentStringElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<div></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        List<Node> nodes = builder.parseFragment(inputFragment, context, baseUri, parser);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }

}