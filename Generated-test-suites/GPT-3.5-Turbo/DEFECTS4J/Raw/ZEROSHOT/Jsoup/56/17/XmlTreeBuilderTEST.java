package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder builder;

    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = builder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParse() {
        String input = "<html><body><h1>Heading</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = builder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("<html><body><h1>Heading</h1></body></html>", document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><body><h1>Heading</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = builder.defaultSettings();
        builder.initialiseParse(input, baseUri, errors, settings);
        assertNotNull(builder.stack);
        assertNotNull(builder.doc);
        assertNotNull(builder.tokeniser);
        assertEquals(settings, builder.settings);
        assertEquals(baseUri, builder.baseUri);
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("h1");
        startTag.addAttribute("class", "title");
        builder.stack.add(new Element(Tag.valueOf("body"), "", builder.defaultSettings().normalizeAttributes(startTag.attributes)));

        assertTrue(builder.process(startTag));
        assertEquals("<body><h1 class=\"title\"></h1></body>", builder.doc.html());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("body");
        builder.stack.add(new Element(Tag.valueOf("body"), "", builder.defaultSettings().normalizeAttributes(endTag.attributes)));

        assertTrue(builder.process(endTag));
        assertEquals("", builder.doc.html());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        builder.stack.add(new Element(Tag.valueOf("body"), ""));

        assertTrue(builder.process(comment));
        assertEquals("<body><!-- This is a comment --></body>", builder.doc.html());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("Text");
        builder.stack.add(new Element(Tag.valueOf("body"), ""));

        assertTrue(builder.process(character));
        assertEquals("<body>Text</body>", builder.doc.html());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        builder.stack.add(new Element(Tag.valueOf("body"), ""));

        assertTrue(builder.process(doctype));
        assertEquals("<body></body>", builder.doc.html());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("h1"), "");
        builder.stack.add(new Element(Tag.valueOf("body"), ""));

        builder.insertNode(element);
        assertEquals("<body><h1></h1></body>", builder.doc.html());
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("h1");
        startTag.addAttribute("class", "title");
        builder.stack.add(new Element(Tag.valueOf("body"), ""));

        Element element = builder.insert(startTag);
        assertNotNull(element);
        assertEquals("<body><h1 class=\"title\"></h1></body>", builder.doc.html());
    }

    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        builder.stack.add(new Element(Tag.valueOf("body"), ""));

        builder.insert(comment);
        assertEquals("<body><!-- This is a comment --></body>", builder.doc.html());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("Text");
        builder.stack.add(new Element(Tag.valueOf("body"), ""));

        builder.insert(character);
        assertEquals("<body>Text</body>", builder.doc.html());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        builder.stack.add(new Element(Tag.valueOf("body"), ""));

        builder.insert(doctype);
        assertEquals("<body></body>", builder.doc.html());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("body");
        Element element1 = new Element(Tag.valueOf("body"), "");
        Element element2 = new Element(Tag.valueOf("h1"), "");
        builder.stack.add(element2);
        builder.stack.add(element1);

        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String input = "<h1>Heading</h1>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = builder.defaultSettings();
        List<Node> nodes = builder.parseFragment(input, baseUri, errors, settings);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("<h1>Heading</h1>", nodes.get(0).outerHtml());
    }

}