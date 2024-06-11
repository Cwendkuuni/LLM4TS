package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParseReaderString() {
        String input = "<root><child>Hello World!</child></root>";
        String baseUri = "http://example.com";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(input), baseUri);
        assertNotNull(doc);
        assertEquals("root", doc.nodeName());
    }

    @Test
    public void testParseStringString() {
        String input = "<root><child>Hello World!</child></root>";
        String baseUri = "http://example.com";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("root", doc.nodeName());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><child>Hello World!</child></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = new ParseSettings(false);
        builder.initialiseParse(new StringReader(input), baseUri, errors, settings);

        assertNotNull(builder.stack);
        assertEquals(1, builder.stack.size());
        assertEquals(builder.doc, builder.stack.get(0));
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("child");
        Token.Attribute attr = new Token.Attribute("id", "1");
        startTag.attributes.add(attr);
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        builder.settings = ParseSettings.preserveCase;

        Element el = builder.insert(startTag);
        assertNotNull(el);
        assertEquals("child", el.nodeName());
        assertNotNull(el.attributes().get("id"));
        assertEquals("1", el.attributes().get("id"));
        assertEquals(builder.baseUri, el.baseUri());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("child");
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        builder.stack.add(new Element(Tag.valueOf("child"), "http://example.com"));

        builder.popStackToClose(endTag);
        assertEquals(1, builder.stack.size());
        assertEquals("root", builder.stack.get(0).nodeName());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));

        builder.insert(commentToken);
        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof Comment);
        Comment comment = (Comment) builder.currentElement().childNode(0);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character charToken = new Token.Character("Hello World!");
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));

        builder.insert(charToken);
        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) builder.currentElement().childNode(0);
        assertEquals("Hello World!", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html");
        doctypeToken.setPublicIdentifier("-//W3C//DTD HTML 4.01//EN");
        doctypeToken.setSystemIdentifier("http://www.w3.org/TR/html4/strict.dtd");
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));

        builder.insert(doctypeToken);
        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof DocumentType);
        DocumentType doctype = (DocumentType) builder.currentElement().childNode(0);
        assertEquals("html", doctype.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));

        Element child = new Element(Tag.valueOf("child"), "http://example.com");
        builder.insertNode(child);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertSame(child, builder.currentElement().childNode(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<child>Hello World!</child>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = new ParseSettings(false);
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element el = (Element) nodes.get(0);
        assertEquals("child", el.nodeName());
        assertEquals("Hello World!", el.text());
    }
}