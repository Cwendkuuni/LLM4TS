package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag></tag></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        builder.initialiseParse(input, baseUri, errors);
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.get(0), builder.doc);
    }

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        assertTrue(builder.process(startTag));
        assertEquals(builder.stack.size(), 1);
        assertTrue(builder.stack.get(0) instanceof Element);
        Element element = (Element) builder.stack.get(0);
        assertEquals(element.tagName(), "tag");
        assertEquals(element.baseUri(), "");
        assertEquals(element.attributes().size(), 0);
    }

    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        assertTrue(builder.process(endTag));
        assertEquals(builder.stack.size(), 0);
    }

    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        assertTrue(builder.process(commentToken));
        assertEquals(builder.stack.size(), 1);
        assertTrue(builder.stack.get(0) instanceof Comment);
        Comment comment = (Comment) builder.stack.get(0);
        assertEquals(comment.getData(), "comment");
        assertEquals(comment.baseUri(), "");
    }

    @Test
    public void testProcess_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("text");
        assertTrue(builder.process(characterToken));
        assertEquals(builder.stack.size(), 1);
        assertTrue(builder.stack.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) builder.stack.get(0);
        assertEquals(textNode.text(), "text");
        assertEquals(textNode.baseUri(), "");
    }

    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("publicId", "systemId");
        assertTrue(builder.process(doctypeToken));
        assertEquals(builder.stack.size(), 1);
        assertTrue(builder.stack.get(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) builder.stack.get(0);
        assertEquals(documentType.getName(), "publicId");
        assertEquals(documentType.getPublicIdentifier(), "publicId");
        assertEquals(documentType.getSystemIdentifier(), "systemId");
        assertEquals(documentType.baseUri(), "");
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element("parent"));
        Node node = new Element("child");
        builder.insertNode(node);
        assertEquals(builder.currentElement().childNodeSize(), 1);
        assertEquals(builder.currentElement().childNode(0), node);
    }

    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        Element element = builder.insert(startTag);
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.get(0), element);
        assertEquals(element.tagName(), "tag");
        assertEquals(element.baseUri(), "");
        assertEquals(element.attributes().size(), 0);
    }

    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        builder.insert(commentToken);
        assertEquals(builder.stack.size(), 1);
        assertTrue(builder.stack.get(0) instanceof Comment);
        Comment comment = (Comment) builder.stack.get(0);
        assertEquals(comment.getData(), "comment");
        assertEquals(comment.baseUri(), "");
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("text");
        builder.insert(characterToken);
        assertEquals(builder.stack.size(), 1);
        assertTrue(builder.stack.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) builder.stack.get(0);
        assertEquals(textNode.text(), "text");
        assertEquals(textNode.baseUri(), "");
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("publicId", "systemId");
        builder.insert(doctypeToken);
        assertEquals(builder.stack.size(), 1);
        assertTrue(builder.stack.get(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) builder.stack.get(0);
        assertEquals(documentType.getName(), "publicId");
        assertEquals(documentType.getPublicIdentifier(), "publicId");
        assertEquals(documentType.getSystemIdentifier(), "systemId");
        assertEquals(documentType.baseUri(), "");
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element("tag1"));
        builder.stack.add(new Element("tag2"));
        builder.stack.add(new Element("tag1"));
        builder.stack.add(new Element("tag3"));
        Token.EndTag endTag = new Token.EndTag("tag1");
        builder.popStackToClose(endTag);
        assertEquals(builder.stack.size(), 2);
        assertEquals(builder.stack.get(0).tagName(), "tag2");
        assertEquals(builder.stack.get(1).tagName(), "tag3");
    }

}