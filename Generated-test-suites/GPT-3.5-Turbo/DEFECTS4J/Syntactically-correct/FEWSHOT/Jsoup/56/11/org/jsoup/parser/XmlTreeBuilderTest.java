package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertTrue(settings.preserveCase);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse("<root></root>", "http://example.com");
        assertEquals("http://example.com", document.baseUri());
        assertEquals(1, document.childNodeSize());
        assertTrue(document.childNode(0) instanceof Element);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true);
        builder.initialiseParse("<root></root>", "http://example.com", errors, settings);
        assertEquals(1, builder.stack.size());
        assertTrue(builder.stack.get(0) instanceof Document);
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token();
        token.type = Token.TokenType.StartTag;
        token.asStartTag().name("root");
        assertTrue(builder.process(token));
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("root"), "http://example.com");
        builder.insertNode(element);
        assertEquals(1, builder.currentElement().childNodeSize());
        assertEquals(element, builder.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("root");
        startTag.isSelfClosing(false);
        startTag.updateAttributes(new Attributes());
        Element element = builder.insert(startTag);
        assertEquals("root", element.tagName());
        assertTrue(element.isBlock());
        assertEquals(builder.baseUri, element.baseUri());
        assertEquals(0, builder.tokeniser.areAppropriateEndTagsRequired());
        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testInsertEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("root");
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        builder.insert(endTag);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("test comment");
        builder.insert(commentToken);
        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character();
        characterToken.data("test character");
        builder.insert(characterToken);
        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");
        doctype.setPublicIdentifier("");
        doctype.setSystemIdentifier("");
        builder.insert(doctype);
        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("root");
        Element element1 = new Element(Tag.valueOf("root"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("root"), "http://example.com");
        element2.appendChild(new TextNode("text", "http://example.com"));
        builder.stack.add(element1);
        builder.stack.add(element2);
        builder.popStackToClose(endTag);
        assertEquals(1, builder.stack.size());
        assertEquals(element1, builder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true);
        List<Node> nodes = builder.parseFragment("<root>Test</root>", "http://example.com", errors, settings);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
}