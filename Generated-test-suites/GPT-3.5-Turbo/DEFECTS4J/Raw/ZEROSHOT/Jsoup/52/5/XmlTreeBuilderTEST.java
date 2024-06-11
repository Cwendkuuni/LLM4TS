package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<test><child1></child1><child2></child2></test>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        assertEquals(baseUri, builder.baseUri);
        assertSame(errors, builder.errors);
        assertNotNull(builder.doc);
        assertNotNull(builder.stack);
        assertSame(builder.doc, builder.stack.get(0));
    }

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<test></test>", "http://example.com", new ParseErrorList());
        Token startTagToken = new Token.StartTag("test");
        Node expectedNode = new Element("test");

        assertTrue(builder.process(startTagToken));
        assertEquals(expectedNode.nodeName(), builder.currentElement().nodeName());
        assertEquals(expectedNode.attributes(), builder.currentElement().attributes());
    }

    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<test></test>", "http://example.com", new ParseErrorList());
        Token startTagToken = new Token.StartTag("test");
        Token endTagToken = new Token.EndTag("test");

        builder.process(startTagToken);
        assertTrue(builder.process(endTagToken));
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<test><!--This is a comment--></test>", "http://example.com", new ParseErrorList());
        Token commentToken = new Token.Comment("This is a comment");
        Node expectedNode = new Comment("This is a comment");

        assertTrue(builder.process(commentToken));
        assertEquals(expectedNode.nodeName(), builder.currentElement().childNode(0).nodeName());
        assertEquals(expectedNode.getData(), builder.currentElement().childNode(0).getData());
    }

    @Test
    public void testProcess_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<test>Text node</test>", "http://example.com", new ParseErrorList());
        Token characterToken = new Token.Character("Text node");
        Node expectedNode = new TextNode("Text node");

        assertTrue(builder.process(characterToken));
        assertEquals(expectedNode.nodeName(), builder.currentElement().childNode(0).nodeName());
        assertEquals(expectedNode.getWholeText(), builder.currentElement().childNode(0).getWholeText());
    }

    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<!DOCTYPE html><test></test>", "http://example.com", new ParseErrorList());
        Token doctypeToken = new Token.Doctype("html");
        Node expectedNode = new Document.Doctype("html");

        assertTrue(builder.process(doctypeToken));
        assertEquals(expectedNode.nodeName(), builder.currentElement().childNode(0).nodeName());
        assertEquals(expectedNode.getOuterHtml(), builder.currentElement().childNode(0).getOuterHtml());
    }

    @Test
    public void testPopStackToClose_ElementFound() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<test><child1></child1><child2></child2></test>", "http://example.com", new ParseErrorList());
        Token startTagToken1 = new Token.StartTag("test");
        Token startTagToken2 = new Token.StartTag("child1");
        Token startTagToken3 = new Token.StartTag("child2");
        Token endTagToken = new Token.EndTag("test");

        builder.process(startTagToken1);
        builder.process(startTagToken2);
        builder.process(startTagToken3);
        builder.process(endTagToken);

        assertEquals(1, builder.stack.size());
        assertEquals("child2", builder.stack.get(0).nodeName());
    }

    @Test
    public void testPopStackToClose_ElementNotFound() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<test><child1></child1></test>", "http://example.com", new ParseErrorList());
        Token startTagToken1 = new Token.StartTag("test");
        Token startTagToken2 = new Token.StartTag("child1");
        Token endTagToken = new Token.EndTag("child2");

        builder.process(startTagToken1);
        builder.process(startTagToken2);
        builder.process(endTagToken);

        assertEquals(2, builder.stack.size());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<test><child1></child1></test>", "http://example.com", new ParseErrorList());
        Token startTagToken = new Token.StartTag("child2");
        Node expectedNode = new Element("child2");

        builder.process(startTagToken);
        builder.insertNode(expectedNode);

        assertEquals(expectedNode.nodeName(), builder.currentElement().childNode(1).nodeName());
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<test><child1></child1></test>", "http://example.com", new ParseErrorList());
        Token startTagToken = new Token.StartTag("test", "self-closing");

        Element el = builder.insert(startTagToken);

        assertTrue(el.isSelfClosing());
        assertTrue(el.tag().isKnownTag());
        assertTrue(el.tag().isSelfClosing());
    }

    @Test
    public void testInsert_StartTag_NotSelfClosing() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<test><child1></child1></test>", "http://example.com", new ParseErrorList());
        Token startTagToken = new Token.StartTag("test");

        Element el = builder.insert(startTagToken);

        assertFalse(el.isSelfClosing());
        assertTrue(el.tag().isKnownTag());
        assertFalse(el.tag().isSelfClosing());
    }

    @Test
    public void testInsert_Comment_NonBogus() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<test><!--This is a comment--></test>", "http://example.com", new ParseErrorList());
        Token commentToken = new Token.Comment("This is a comment");
        Comment expectedComment = new Comment("This is a comment", "http://example.com");

        builder.insert(commentToken);

        assertEquals(expectedComment.nodeName(), builder.currentElement().childNode(0).nodeName());
        assertEquals(expectedComment.getData(), builder.currentElement().childNode(0).getData());
    }

    @Test
    public void testInsert_Comment_Bogus() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<test><!--!This is a comment--></test>", "http://example.com", new ParseErrorList());
        Token commentToken = new Token.Comment("!This is a comment");
        Comment expectedComment = new Comment("!This is a comment", "http://example.com");

        builder.insert(commentToken);

        assertEquals("xml", builder.currentElement().childNode(0).nodeName());
        assertEquals(expectedComment.nodeName(), builder.currentElement().childNode(0).childNode(0).nodeName());
        assertEquals(expectedComment.getData(), builder.currentElement().childNode(0).childNode(0).getData());
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<test>Text node</test>", "http://example.com", new ParseErrorList());
        Token characterToken = new Token.Character("Text node");
        Node expectedNode = new TextNode("Text node", "http://example.com");

        builder.insert(characterToken);

        assertEquals(expectedNode.nodeName(), builder.currentElement().childNode(0).nodeName());
        assertEquals(expectedNode.getWholeText(), builder.currentElement().childNode(0).getWholeText());
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<!DOCTYPE html><test></test>", "http://example.com", new ParseErrorList());
        Token doctypeToken = new Token.Doctype("html");
        Document.Doctype expectedDoctype = new Document.Doctype("html");

        builder.insert(doctypeToken);

        assertEquals(expectedDoctype.nodeName(), builder.currentElement().childNode(0).nodeName());
        assertEquals(expectedDoctype.getOuterHtml(), builder.currentElement().childNode(0).getOuterHtml());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<child1></child1><child2></child2>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        Token.StartTag startTag = new Token.StartTag("test");
        Token.EndTag endTag = new Token.EndTag("test");
        Element expectedElement1 = new Element("child1");
        Element expectedElement2 = new Element("child2");

        builder.initialiseParse("<test>" + inputFragment + "</test>", baseUri, errors);
        builder.process(startTag);

        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(expectedElement1.nodeName(), nodes.get(0).nodeName());
        assertEquals(expectedElement2.nodeName(), nodes.get(1).nodeName());
        assertEquals(2, nodes.size());
    }
}