package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        ParseSettings settings = builder.defaultSettings();

        assertTrue(settings.preserveCase());
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";

        Document document = builder.parse(input, baseUri);

        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        String input = "<xml></xml>";
        String baseUri = "http://example.com";

        Document document = builder.parse(input, baseUri);

        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        builder.initialiseParse(input, baseUri, errors, settings);

        assertEquals(1, builder.stack.size());
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token startTagToken = new Token.StartTag("tag");
        builder.process(startTagToken);

        assertEquals("tag", builder.currentElement().tagName());

        Token endTagToken = new Token.EndTag("tag");
        builder.process(endTagToken);

        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Element parentElement = new Element(Tag.valueOf("parent"), "");
        builder.stack.add(parentElement);

        Element childElement = new Element(Tag.valueOf("child"), "");
        builder.insertNode(childElement);

        assertEquals(1, parentElement.childNodeSize());
        assertEquals(childElement, parentElement.childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.StartTag startTag = new Token.StartTag("tag");
        Element element = builder.insert(startTag);

        assertEquals("tag", element.tagName());
        assertEquals(builder.baseUri, element.baseUri());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.Comment commentToken = new Token.Comment("comment");
        builder.insert(commentToken);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.Character characterToken = new Token.Character("character");
        builder.insert(characterToken);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.Doctype doctypeToken = new Token.Doctype("doctype");
        builder.insert(doctypeToken);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Element parentElement = new Element(Tag.valueOf("parent"), "");
        Element childElement = new Element(Tag.valueOf("child"), "");
        builder.stack.add(parentElement);
        builder.stack.add(childElement);

        Token.EndTag endTagToken = new Token.EndTag("child");
        builder.popStackToClose(endTagToken);

        assertEquals(1, builder.stack.size());
        assertEquals(parentElement, builder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        String inputFragment = "<child></child>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
}