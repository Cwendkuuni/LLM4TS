package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
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
    }

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<xml></xml>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        builder.initialiseParse("<xml></xml>", "http://example.com", errors, settings);

        assertEquals("http://example.com", builder.baseUri);
        assertEquals(errors, builder.errors);
        assertEquals(settings, builder.settings);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.StartTag("tag", "name");
        boolean result = builder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.EndTag("tag", "name");
        boolean result = builder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Comment("comment");
        boolean result = builder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Character("data");
        boolean result = builder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Doctype("doctype", "", "", "");
        boolean result = builder.process(token);
        assertTrue(result);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(new Element("tag"));
        builder.insertNode(new TextNode("text"));
        assertEquals(1, builder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", "name");
        Element el = builder.insert(startTag);
        assertNotNull(el);
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        builder.insert(comment);
        assertEquals(1, builder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("data");
        builder.insert(character);
        assertEquals(1, builder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("doctype", "", "", "");
        builder.insert(doctype);
        assertEquals(1, builder.currentElement().childNodes().size());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element1 = new Element("tag");
        Element element2 = new Element("tag");
        builder.stack.push(element1);
        builder.stack.push(element2);
        Token.EndTag endTag = new Token.EndTag("tag", "name");
        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = builder.parseFragment("<xml></xml>", "http://example.com", errors, settings);
        assertNotNull(nodes);
    }
}