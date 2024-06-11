package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        // Typical use case
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<xml>", "http://example.com", new ParseErrorList());
        assertEquals(1, builder.stack.size());

        // Edge case
        builder.initialiseParse("", "", new ParseErrorList());
        assertEquals(1, builder.stack.size());

        // Error scenario
        builder.initialiseParse("<xml>", null, new ParseErrorList());
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testProcess() {
        // Typical use case: StartTag
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        builder.process(startTag);
        assertEquals("tag", builder.currentElement().nodeName());

        // Typical use case: EndTag
        XmlTreeBuilder builder2 = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tag");
        builder2.process(endTag);
        assertEquals(0, builder2.stack.size());

        // Typical use case: Comment
        XmlTreeBuilder builder3 = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment();
        comment.data("test comment");
        builder3.process(comment);
        assertEquals("test comment", builder3.currentElement().childNode(0).toString());

        // Typical use case: Character
        XmlTreeBuilder builder4 = new XmlTreeBuilder();
        Token.Character character = new Token.Character();
        character.data("test character");
        builder4.process(character);
        assertEquals("test character", builder4.currentElement().childNode(0).toString());

        // Typical use case: Doctype
        XmlTreeBuilder builder5 = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("test doctype");
        builder5.process(doctype);
        assertEquals("test doctype", builder5.currentElement().childNode(0).toString());

        // Error scenario
        XmlTreeBuilder builder6 = new XmlTreeBuilder();
        Token.Unknown unknown = new Token.Unknown();
        builder6.process(unknown);
        assertNull(builder6.currentElement());

    }

    @Test
    public void testInsertNode() {
        // Typical use case
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("tag"), "http://example.com");
        builder.insertNode(element);
        assertEquals(element, builder.currentElement().childNode(0));

        // Edge case
        XmlTreeBuilder builder2 = new XmlTreeBuilder();
        builder2.insertNode(null);
        assertNull(builder2.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        // Typical use case
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        Element element = builder.insert(startTag);
        assertNotNull(element);
        assertEquals("tag", builder.currentElement().nodeName());

        // Typical use case: Self-closing tag
        XmlTreeBuilder builder2 = new XmlTreeBuilder();
        Token.StartTag selfClosingTag = new Token.StartTag();
        selfClosingTag.name("tag");
        selfClosingTag.selfClosing(true);
        Element element2 = builder2.insert(selfClosingTag);
        assertNotNull(element2);
        assertEquals("tag", builder2.currentElement().nodeName());

        // Edge case
        XmlTreeBuilder builder3 = new XmlTreeBuilder();
        Token.StartTag unknownTag = new Token.StartTag();
        unknownTag.name("unknownTag");
        Element element3 = builder3.insert(unknownTag);
        assertNotNull(element3);
        assertTrue(element3.tag().isKnownTag());
    }

    @Test
    public void testInsertComment() {
        // Typical use case
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment();
        comment.data("test comment");
        builder.insert(comment);
        assertEquals("test comment", builder.currentElement().childNode(0).toString());

        // Error scenario
        XmlTreeBuilder builder2 = new XmlTreeBuilder();
        Token.Comment bogusComment = new Token.Comment();
        bogusComment.bogus(true);
        bogusComment.data("<!DOCTYPE>");
        builder2.insert(bogusComment);
        assertNotNull(builder2.currentElement().childNode(0));
    }

    @Test
    public void testInsertCharacter() {
        // Typical use case
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character();
        character.data("test character");
        builder.insert(character);
        assertEquals("test character", builder.currentElement().childNode(0).toString());

        // Edge case
        XmlTreeBuilder builder2 = new XmlTreeBuilder();
        Token.Character emptyCharacter = new Token.Character();
        emptyCharacter.data("");
        builder2.insert(emptyCharacter);
        assertEquals("", builder2.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsertDoctype() {
        // Typical use case
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("test doctype");
        builder.insert(doctype);
        assertEquals("test doctype", builder.currentElement().childNode(0).toString());

        // Error scenario
        XmlTreeBuilder builder2 = new XmlTreeBuilder();
        Token.Doctype emptyDoctype = new Token.Doctype();
        emptyDoctype.name("");
        builder2.insert(emptyDoctype);
        assertNull(builder2.currentElement().childNode(0));
    }

    @Test
    public void testPopStackToClose() {
        // Typical use case
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tag");
        builder.stack.add(new Element(Tag.valueOf("tag"), ""));
        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());

        // Error scenario
        XmlTreeBuilder builder2 = new XmlTreeBuilder();
        Token.EndTag unknownEndTag = new Token.EndTag();
        unknownEndTag.name("unknownTag");
        builder2.stack.add(new Element(Tag.valueOf("tag"), ""));
        builder2.popStackToClose(unknownEndTag);
        assertEquals(1, builder2.stack.size());
    }

    @Test
    public void testParseFragment() {
        // Typical use case
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<tag></tag>", "http://example.com", new ParseErrorList());
        assertNotNull(nodes);
        assertEquals(1, nodes.size());

        // Edge case
        XmlTreeBuilder builder2 = new XmlTreeBuilder();
        List<Node> emptyNodes = builder2.parseFragment("", "", new ParseErrorList());
        assertNotNull(emptyNodes);
        assertEquals(0, emptyNodes.size());

        // Error scenario
        XmlTreeBuilder builder3 = new XmlTreeBuilder();
        List<Node> nullNodes = builder3.parseFragment("<xml>", null, new ParseErrorList());
        assertNotNull(nullNodes);
        assertEquals(1, nullNodes.size());
    }
}