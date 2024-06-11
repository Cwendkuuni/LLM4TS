package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        assertEquals(ParseSettings.preserveCase, builder.defaultSettings());
    }

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("", "");
        assertEquals("", doc.html());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(false, false);
        builder.initialiseParse("", "", errors, settings);
        assertEquals(1, errors.size());
        assertEquals("Unexpected token type: EOF", errors.get(0).toString());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", "<tag>", new Attributes());
        Token.EndTag endTag = new Token.EndTag("tag");
        Token.Comment comment = new Token.Comment("comment");
        Token.Character character = new Token.Character("character");
        Token.Doctype doctype = new Token.Doctype("doctype");
        assertTrue(builder.process(startTag));
        assertTrue(builder.process(endTag));
        assertTrue(builder.process(comment));
        assertTrue(builder.process(character));
        assertTrue(builder.process(doctype));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = new Document("");
        builder.stack.add(doc);
        Element element = new Element(Tag.valueOf("tag"), "");
        builder.insertNode(element);
        assertEquals(1, doc.childNodeSize());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = builder.insert(new Token.StartTag("tag", "<tag>", new Attributes()));
        assertEquals("tag", element.tagName());
        assertTrue(element.attributes().isEmpty());
    }

    @Test
    public void testInsertSelfClosingStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.settings = new ParseSettings(true, false);
        Token.StartTag selfClosingTag = new Token.StartTag("tag", "<tag />", new Attributes());
        Element element = builder.insert(selfClosingTag);
        assertTrue(selfClosingTag.isSelfClosing());
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Comment comment = new Comment("comment", "");
        builder.insert(comment);
        assertEquals(1, builder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        TextNode textNode = new TextNode("character", "");
        builder.insert(textNode);
        assertEquals(1, builder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        DocumentType doctype = new DocumentType("doctype", "", "", "");
        builder.insert(doctype);
        assertEquals(1, builder.currentElement().childNodeSize());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("tag"), ""));
        builder.stack.add(new Element(Tag.valueOf("tag"), ""));
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.popStackToClose(endTag);
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(false, false);
        List<Node> nodes = builder.parseFragment("", "", errors, settings);
        assertEquals(0, nodes.size());
        assertEquals(1, errors.size());
    }
}