package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

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

        Reader input = new StringReader("<test>data</test>");
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        builder.initialiseParse(input, baseUri, parser);

        assertEquals(2, builder.stack.size());
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Reader input = new StringReader("<test>data</test>");
        String baseUri = "http://example.com";
        Document doc = builder.parse(input, baseUri);

        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof Element);
        Element element = (Element) doc.childNode(0);
        assertEquals("test", element.nodeName());
        assertEquals("data", element.text());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        String input = "<test>data</test>";
        String baseUri = "http://example.com";
        Document doc = builder.parse(input, baseUri);

        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof Element);
        Element element = (Element) doc.childNode(0);
        assertEquals("test", element.nodeName());
        assertEquals("data", element.text());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.StartTag startTag = new Token.StartTag("test");
        startTag.attributes.put("attr", "value");
        builder.process(startTag);

        assertEquals(1, builder.stack.size());
        assertTrue(builder.stack.get(0) instanceof Element);
        Element element = builder.currentElement();
        assertEquals("test", element.nodeName());
        assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        builder.stack.add(new Element(Tag.valueOf("test"), ""));
        builder.stack.add(new Element(Tag.valueOf("div"), ""));
        builder.stack.add(new Element(Tag.valueOf("span"), ""));

        Token.EndTag endTag = new Token.EndTag("div");
        builder.process(endTag);

        assertEquals(2, builder.stack.size());
        assertTrue(builder.stack.get(0) instanceof Element);
        Element element = builder.currentElement();
        assertEquals("test", element.nodeName());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.Comment commentToken = new Token.Comment("This is a comment");
        builder.process(commentToken);

        assertEquals(1, builder.doc.childNodes().size());
        assertTrue(builder.doc.childNode(0) instanceof Comment);
        Comment comment = (Comment) builder.doc.childNode(0);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.Character character = new Token.Character("data");
        builder.process(character);

        assertEquals(1, builder.doc.childNodes().size());
        assertTrue(builder.doc.childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) builder.doc.childNode(0);
        assertEquals("data", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.Doctype doctype = new Token.Doctype("html");
        doctype.setPublicIdentifier("-//W3C//DTD XHTML 1.0 Transitional//EN");
        doctype.setSystemIdentifier("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        doctype.setPubSysKey(null);
        builder.process(doctype);

        assertEquals(1, builder.doc.childNodes().size());
        assertTrue(builder.doc.childNode(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) builder.doc.childNode(0);
        assertEquals("html", documentType.getName());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", documentType.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", documentType.getSystemIdentifier());
        assertNull(documentType.getPubSysKey());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("div"), ""));

        Node node = new CDataNode("data");
        builder.insertNode(node);

        assertEquals(2, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNode(1) instanceof CDataNode);
        CDataNode cdataNode = (CDataNode) builder.currentElement().childNode(1);
        assertEquals("data", cdataNode.getWholeText());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.StartTag startTag = new Token.StartTag("test");
        startTag.attributes.put("attr", "value");
        Element element = builder.insert(startTag);

        assertEquals(1, builder.stack.size());
        assertTrue(builder.stack.get(0) instanceof Element);
        assertEquals("test", element.nodeName());
        assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.Comment commentToken = new Token.Comment("This is a comment");
        builder.insert(commentToken);

        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNode(0) instanceof Comment);
        Comment comment = (Comment) builder.currentElement().childNode(0);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.Character character = new Token.Character("data");
        builder.insert(character);

        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) builder.currentElement().childNode(0);
        assertEquals("data", textNode.getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.Doctype doctype = new Token.Doctype("html");
        doctype.setPublicIdentifier("-//W3C//DTD XHTML 1.0 Transitional//EN");
        doctype.setSystemIdentifier("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        doctype.setPubSysKey(null);
        builder.insert(doctype);

        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(builder.currentElement().childNode(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) builder.currentElement().childNode(0);
        assertEquals("html", documentType.getName());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", documentType.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", documentType.getSystemIdentifier());
        assertNull(documentType.getPubSysKey());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("div"), ""));
        builder.stack.add(new Element(Tag.valueOf("test"), ""));
        builder.stack.add(new Element(Tag.valueOf("div"), ""));

        Token.EndTag endTag = new Token.EndTag("div");
        builder.popStackToClose(endTag);

        assertEquals(2, builder.stack.size());
        assertTrue(builder.stack.get(0) instanceof Element);
        Element element = builder.currentElement();
        assertEquals("test", element.nodeName());
    }

    @Test
    public void testParseFragmentWithoutContext() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        String fragment = "<test>data</test>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        List<Node> nodes = builder.parseFragment(fragment, baseUri, parser);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("test", element.nodeName());
        assertEquals("data", element.text());
    }

    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        String fragment = "<test>data</test>";
        Element context = new Element(Tag.valueOf("div"), "");
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        List<Node> nodes = builder.parseFragment(fragment, context, baseUri, parser);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("test", element.nodeName());
        assertEquals("data", element.text());
    }
}