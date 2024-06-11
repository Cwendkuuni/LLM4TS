package org.jsoup.parser;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseReaderString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root></root>"), "http://example.com");
        assertEquals("<root></root>", doc.html());
    }

    @Test
    public void testParseStringString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<root></root>", "http://example.com");
        assertEquals("<root></root>", doc.html());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        builder.initialiseParse(new StringReader("<root></root>"), "http://example.com", errors, settings);
        assertEquals("<root></root>", builder.doc.html());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tag tag = Tag.valueOf("root", ParseSettings.preserveCase);
        Token.StartTag startTag = new Token.StartTag(tag, null);
        Element el = builder.insert(startTag);
        assertEquals("root", el.tagName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tag tag = Tag.valueOf("root", ParseSettings.preserveCase);
        Token.EndTag endTag = new Token.EndTag(tag);
        builder.stack.add(new Element(tag, "http://example.com", null));
        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("test comment");
        builder.insert(commentToken);
        Comment comment = (Comment) builder.doc.childNodes().get(0);
        assertEquals("<!--test comment-->", comment.outerHtml());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character(false, "test character");
        builder.insert(character);
        TextNode textNode = (TextNode) builder.doc.childNodes().get(0);
        assertEquals("test character", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        builder.insert(doctype);
        assertEquals("html", builder.doc.childNode(0).nodeName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = builder.parseFragment("<root></root>", "http://example.com", errors, settings);
        assertEquals(1, nodes.size());
        assertEquals("root", nodes.get(0).nodeName());
    }
}