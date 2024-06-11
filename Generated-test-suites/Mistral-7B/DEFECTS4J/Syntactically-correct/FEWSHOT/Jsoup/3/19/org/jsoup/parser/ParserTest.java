package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Attributes.fromMap;
import static org.jsoup.nodes.Tags.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

public class ParserTest {

    @Test
    public void testParseEmpty() {
        String html = "";
        String baseUri = "baseUri";
        Document doc = Parser.parse(html, baseUri);
        assertEquals(htmlTag(), doc.tagName());
        assertTrue(doc.body().tagName().isEmpty());
        assertTrue(doc.head().tagName().isEmpty());
    }

    @Test
    public void testParseHelloWorld() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "baseUri";
        Document doc = Parser.parse(html, baseUri);
        assertEquals(htmlTag(), doc.tagName());
        Element body = doc.body();
        assertEquals(h1(), body.tagName());
        assertEquals("Hello World", body.text());
    }

    @Test
    public void testParseWithComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        String baseUri = "baseUri";
        Document doc = Parser.parse(html, baseUri);
        assertEquals(htmlTag(), doc.tagName());
        Element comment = doc.body().child(0);
        assertEquals(Comment.class, comment.getClass());
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseWithCdata() {
        String html = "<html><body><![CDATA[ This is some CDATA ]]></body></html>";
        String baseUri = "baseUri";
        Document doc = Parser.parse(html, baseUri);
        assertEquals(htmlTag(), doc.tagName());
        Element body = doc.body();
        TextNode cdata = (TextNode) body.child(0);
        assertEquals("This is some CDATA", cdata.text());
    }

    @Test
    public void testParseWithXmlDeclaration() {
        String html = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:my=\"http://example.com/\">";
        String baseUri = "baseUri";
        Document doc = Parser.parse(html, baseUri);
        assertEquals(htmlTag(), doc.tagName());
        Element xmlDecl = doc.body().child(0);
        assertEquals(XmlDeclaration.class, xmlDecl.getClass());
        assertEquals("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:my=\"http://example.com/\", baseUri", xmlDecl.toString());
    }

    @Test
    public void testParseEmptyBodyFragment() {
        String bodyHtml = "";
        String baseUri = "baseUri";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(htmlTag(), doc.tagName());
        assertTrue(doc.body().tagName().isEmpty());
        assertTrue(doc.head().tagName().isEmpty());
    }

    @Test
    public void testParseHelloWorldBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "baseUri";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(htmlTag(), doc.tagName());
        Element body = doc.body();
        assertEquals(h1(), body.tagName());
        assertEquals("Hello World", body.text());
    }

    @Test
    public void testParseWithAttributes() {
        String html = "<a class=\"red\" href=\"http://example.com/\">Link</a>";
        String baseUri = "baseUri";
        Document doc = Parser.parse(html, baseUri);
        Element link = doc.body().child(0);
        assertEquals(a(), link.tagName());
        Map<String, String> attributes = fromMap(link.attributes());
        assertEquals(2, attributes.size());
        assertEquals("red", attributes.get("class"));
        assertEquals("http://example.com/", attributes.get("href"));
    }

    @Test
    public void testParseWithEmptyTag() {
        String html = "<img src=\"image.jpg\" />";
        String baseUri = "baseUri";
        Document doc = Parser.parse(html, baseUri);
        Element img = doc.body().child(0);
        assertEquals(img(), img.tagName());
        assertTrue(img.text().isEmpty());
    }

    @Test
    public void testParseWithSelfClosingTag() {
        String html = "<input type=\"text\" name=\"username\" />";
        String baseUri = "baseUri";
        Document doc = Parser.parse(html, baseUri);
        Element input = doc.body().child(0);
        assertEquals(input(), input.tagName());
        assertTrue(input.text().isEmpty());
    }

    @Test
    public void testParseWithTextNode() {
        String html = "<p>This is a text node</p>";
        String baseUri = "baseUri";
        Document doc = Parser.parse(html, baseUri);
        Element p = doc.body().child(0);
        TextNode textNode = (TextNode) p.child(0);
        assertEquals("This is a text node", textNode.text());
    }

    @Test
    public void testParseWithDataNode() {
        String html = "<script type=\"text/javascript\">var x = 1;</script>";
        String baseUri = "baseUri";
        Document doc = Parser.parse(html, baseUri);
        Element script = doc.body().child(0);
        DataNode dataNode = (DataNode) script.child(0);
        assertEquals("var x = 1;", dataNode.text());
    }

    @Test
    public void testParseWithBaseTag() {
        String html = "<base href=\"/subdir/\">";
        String baseUri = "baseUri/subdir";
        Document doc = Parser.parse(html, baseUri);
        Element base = doc.body().child(0);
        assertEquals(base(), base.tagName());
        assertEquals("/subdir/", base.absUrl("href"));
        assertEquals("/subdir/", doc.baseUri());
    }
}