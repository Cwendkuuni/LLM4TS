
package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<xml><tag>Hello World</tag></xml>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("xml", doc.child(0).tagName());
        assertEquals("tag", doc.select("tag").first().tagName());
        assertEquals("Hello World", doc.select("tag").first().text());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true, true);
        builder.initialiseParse("<xml>", "http://example.com", errors, settings);

        assertNotNull(builder.stack);
        assertNotNull(builder.doc);
        assertNotNull(builder.tokeniser);
        assertNotNull(builder.baseUri);
        assertNotNull(builder.errors);
        assertNotNull(builder.settings);
        assertEquals(1, builder.stack.size());
        assertEquals("http://example.com", builder.baseUri);
        assertEquals(errors, builder.errors);
        assertEquals(settings, builder.settings);
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<xml>", "http://example.com", new ParseErrorList(), new ParseSettings(true, true));

        // Test StartTag
        Token token = new Token.StartTag("tag", "html", false);
        assertTrue(builder.process(token));
        assertEquals("tag", builder.currentElement().tagName());

        // Test EndTag
        token = new Token.EndTag("tag", "html");
        assertTrue(builder.process(token));
        assertNull(builder.currentElement());

        // Test Comment
        token = new Token.Comment("This is a comment");
        assertTrue(builder.process(token));
        assertEquals("This is a comment", builder.currentElement().childNode(0).toString());

        // Test Character
        token = new Token.Character("Text Content");
        assertTrue(builder.process(token));
        assertEquals("Text Content", builder.currentElement().childNode(1).toString());

        // Test Doctype
        token = new Token.Doctype("html", "", "", "http://www.w3.org/1999/html");
        assertTrue(builder.process(token));
        assertEquals("html", builder.currentElement().childNode(2).toString());

        // Test EOF
        token = new Token.EOF();
        assertTrue(builder.process(token));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<xml>", "http://example.com", new ParseErrorList(), new ParseSettings(true, true));
        Node node = new Element(Tag.valueOf("tag"), "http://example.com");
        builder.insertNode(node);
        assertEquals("tag", builder.currentElement().tagName());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<xml>", "http://example.com", new ParseErrorList(), new ParseSettings(true, true));
        Token.StartTag startTag = new Token.StartTag("tag", "xml", false);
        Element element = builder.insert(startTag);
        assertNotNull(element);
        assertEquals("tag", element.tagName());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<xml>", "http://example.com", new ParseErrorList(), new ParseSettings(true, true));
        Token.Comment comment = new Token.Comment("This is a comment");
        builder.insert(comment);
        assertEquals("This is a comment", builder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<xml>", "http://example.com", new ParseErrorList(), new ParseSettings(true, true));
        Token.Character character = new Token.Character("Text Content");
        builder.insert(character);
        assertEquals("Text Content", builder.currentElement().childNode(1).toString());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<xml>", "http://example.com", new ParseErrorList(), new ParseSettings(true, true));
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://www.w3.org/1999/html");
        builder.insert(doctype);
        assertEquals("html", builder.currentElement().childNode(2).toString());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<xml>", "http://example.com", new ParseErrorList(), new ParseSettings(true, true));
        builder.stack.add(new Element(Tag.valueOf("html"), "http://example.com"));
        builder.stack.add(new Element(Tag.valueOf("body"), "http://example.com"));
        builder.stack.add(new Element(Tag.valueOf("p"), "http://example.com"));
        builder.popStackToClose(new Token.EndTag("body", "xml"));
        assertEquals(1, builder.stack.size());
        assertEquals("html", builder.currentElement().tagName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<tag>Hello World</tag>", "http://example.com", new ParseErrorList(), new ParseSettings(true, true));
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.tagName());
        assertEquals("Hello World", element.text());
    }
}
