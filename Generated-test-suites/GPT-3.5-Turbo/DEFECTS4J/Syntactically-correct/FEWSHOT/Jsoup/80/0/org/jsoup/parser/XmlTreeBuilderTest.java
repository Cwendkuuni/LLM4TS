package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertFalse(settings.normalizeTag("tag").equals("TAG"));
        assertTrue(settings.normalizeTag("Tag").equals("Tag"));
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Document doc = xmlTreeBuilder.parse(new StringReader(""), "http://example.com");

        assertNull(doc.childNode(0));
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        Document doc = xmlTreeBuilder.parse("", "http://example.com");

        assertNull(doc.childNode(0));
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        xmlTreeBuilder.initialiseParse(new StringReader(""), "http://example.com", errors, settings);

        assertSame(errors, xmlTreeBuilder.errors);
        assertSame(settings, xmlTreeBuilder.settings);
        assertNull(xmlTreeBuilder.baseUri);
    }

    @Test
    public void testProcessStartTag() {
        Element element = new Element(Tag.valueOf("tag"), "http://example.com");
        Token.StartTag startTag = new Token.StartTag("tag", "key", startTag.attributes, false);

        Element result = xmlTreeBuilder.process(startTag);

        assertTrue(result.tagName().equals("tag"));
        assertSame(element, result);
    }

    @Test
    public void testProcessEndTag() {
        Element element = new Element(Tag.valueOf("tag"), "http://example.com");
        Token.EndTag endTag = new Token.EndTag("tag");

        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.process(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        Comment comment = new Comment("comment");
        Token.Comment commentToken = new Token.Comment("comment");

        xmlTreeBuilder.process(commentToken);

        assertEquals(comment.getData(), xmlTreeBuilder.doc.childNodes().get(0).toString());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("data");
        xmlTreeBuilder.process(character);

        assertEquals(character.getData(), xmlTreeBuilder.doc.childNodes().get(0).toString());
    }

    @Test
    public void testProcessDoctype() {
        DocumentType docType = new DocumentType("doctype", "", "");
        Token.Doctype doctype = new Token.Doctype("doctype", "", "");

        xmlTreeBuilder.process(doctype);

        assertEquals(docType.getName(), xmlTreeBuilder.doc.childNodes().get(0).nodeName());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("tag"), "http://example.com");
        xmlTreeBuilder.stack.add(element);

        Comment comment = new Comment("comment");
        xmlTreeBuilder.insertNode(comment);

        assertEquals(comment, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsertStartTag() {
        Element element = new Element(Tag.valueOf("tag"), "http://example.com");
        Token.StartTag startTag = new Token.StartTag("tag", "key", startTag.attributes, false);

        Element result = xmlTreeBuilder.insert(startTag);

        assertTrue(result.tagName().equals("tag"));
        assertSame(element, result);
    }

    @Test
    public void testInsertComment() {
        Comment comment = new Comment("comment");
        Token.Comment commentToken = new Token.Comment("comment");

        xmlTreeBuilder.insert(commentToken);

        assertEquals(comment.getData(), xmlTreeBuilder.doc.childNodes().get(0).toString());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("data");
        xmlTreeBuilder.insert(character);

        assertEquals(character.getData(), xmlTreeBuilder.doc.childNodes().get(0).toString());
    }

    @Test
    public void testInsertDoctype() {
        DocumentType docType = new DocumentType("doctype", "", "");
        Token.Doctype doctype = new Token.Doctype("doctype", "", "");

        xmlTreeBuilder.insert(doctype);

        assertEquals(docType.getName(), xmlTreeBuilder.doc.childNodes().get(0).nodeName());
    }

    @Test
    public void testPopStackToClose() {
        Element element = new Element(Tag.valueOf("tag"), "http://example.com");
        Token.EndTag endTag = new Token.EndTag("tag");

        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>data</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag", ((Element) nodes.get(0)).tagName());
    }
}