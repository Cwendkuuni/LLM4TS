package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        assertEquals(input, builder.getInput());
        assertEquals(baseUri, builder.getBaseUri());
        assertEquals(errors, builder.getErrors());
        assertEquals(Document.OutputSettings.Syntax.xml, builder.getDoc().outputSettings().syntax());
    }

    @Test
    public void testProcessWithStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("", "tag", new Attributes());
        builder.process(startTag);
        Element el = builder.getCurrentElement();
        assertEquals("tag", el.nodeName());
    }

    @Test
    public void testProcessWithEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("", "tag", new Attributes());
        builder.process(startTag);
        Element el = builder.getCurrentElement();

        Token endTag = new Token.EndTag("", "tag");
        builder.process(endTag);

        assertNull(builder.getCurrentElement());
    }

    @Test
    public void testProcessWithComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String commentData = "This is a comment";
        Token comment = new Token.Comment(commentData);
        builder.process(comment);
        Comment com = (Comment) builder.getDoc().childNodes().get(0);
        assertEquals(commentData, com.getData());
    }

    @Test
    public void testProcessWithCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String characterData = "This is a character";
        Token character = new Token.Character(characterData);
        builder.process(character);
        assertEquals(characterData, builder.getDoc().text());
    }

    @Test
    public void testProcessWithDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String name = "doctype";
        String publicIdentifier = "-//TEST//Test//EN";
        String systemIdentifier = "http://example.com";
        Token.Doctype doctype = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        builder.process(doctype);
        DocumentType docType = (DocumentType) builder.getDoc().childNodes().get(0);
        assertEquals(name, docType.getName());
        assertEquals(publicIdentifier, docType.getPublicIdentifier());
        assertEquals(systemIdentifier, docType.getSystemIdentifier());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node node = new Element("tag");
        builder.insertNode(node);
        assertEquals(node, builder.getCurrentElement().childNodes().get(0));
    }

    @Test
    public void testInsertWithStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Attribute attr = new Attribute("name", "value");
        Attributes attrs = new Attributes();
        attrs.add(attr);
        Token.StartTag startTag = new Token.StartTag("", "tag", attrs);
        
        Element el = builder.insert(startTag);

        assertEquals("tag", el.nodeName());
        assertEquals(attr, el.attributes().get("name"));
    }

    @Test
    public void testInsertWithCommentToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String commentData = "This is a comment";
        Token.Comment commentToken = new Token.Comment(commentData);

        builder.insert(commentToken);

        Comment com = (Comment) builder.getDoc().childNodes().get(0);
        assertEquals(commentData, com.getData());
    }

    @Test
    public void testInsertWithCharacterToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String characterData = "This is a character";
        Token.Character characterToken = new Token.Character(characterData);

        builder.insert(characterToken);

        assertEquals(characterData, builder.getDoc().text());
    }

    @Test
    public void testInsertWithDoctypeToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String name = "doctype";
        String publicIdentifier = "-//TEST//Test//EN";
        String systemIdentifier = "http://example.com";
        Token.Doctype doctypeToken = new Token.Doctype(name, publicIdentifier, systemIdentifier);

        builder.insert(doctypeToken);

        DocumentType docType = (DocumentType) builder.getDoc().childNodes().get(0);
        assertEquals(name, docType.getName());
        assertEquals(publicIdentifier, docType.getPublicIdentifier());
        assertEquals(systemIdentifier, docType.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag1 = new Token.StartTag("", "tag1", new Attributes());
        builder.process(startTag1);

        Token.StartTag startTag2 = new Token.StartTag("", "tag2", new Attributes());
        builder.process(startTag2);

        builder.popStackToClose(new Token.EndTag("", "tag2"));

        assertEquals(1, builder.getStack().size());
        assertEquals("tag1", builder.getStack().get(0).nodeName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag>text</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(1, nodes.size());
        Element el = (Element) nodes.get(0);

        assertEquals("tag", el.nodeName());
        assertEquals("text", el.text());
    }

}