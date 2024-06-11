package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("http://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.documentElementTag());

        Element htmlNode = doc.documentElement();
        assertEquals(htmlTag, htmlNode.tag());
        assertEquals(1, htmlNode.childNodes().size());

        Element headNode = htmlNode.childNodes().get(0);
        assertEquals(headTag, headNode.tag());
        assertEquals(1, headNode.childNodes().size());

        Element titleNode = headNode.childNodes().get(0);
        assertEquals(titleTag, titleNode.tag());
        assertEquals("Test", titleNode.text());

        Element bodyNode = htmlNode.childNodes().get(1);
        assertEquals(bodyTag, bodyNode.tag());
        assertEquals(1, bodyNode.childNodes().size());

        Element pNode = bodyNode.childNodes().get(0);
        assertEquals("p", pNode.tag().name());
        assertEquals("Hello World", pNode.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals("http://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.documentElementTag());

        Element body = doc.body();
        assertEquals("p", body.tag().name());
        assertEquals("Hello World", body.text());

        assertEquals(0, doc.head().childNodes().size());
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("<html><!-- This is a comment --><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://example.com", false);
        Document doc = parser.parse();

        assertEquals(1, doc.childNodes().size());

        Element htmlNode = doc.documentElement();
        assertEquals(1, htmlNode.childNodes().size());

        Element commentNode = htmlNode.childNodes().get(0);
        assertEquals(Comment.class, commentNode.getClass());
        assertEquals("This is a comment", commentNode.text());
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("<html xmlns='http://example.com/ns'><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://example.com", false);
        Document doc = parser.parse();

        Element xmlDeclNode = doc.head().childNodes().get(0);
        assertEquals("XMLDeclaration", xmlDeclNode.tag().name());
        assertEquals("http://example.com/ns", xmlDeclNode.attributes().get("xmlns").getValue());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><div><p>Hello World</p></div></body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();

        Element divNode = doc.getElementsByTag("div").get(0);
        assertEquals(1, divNode.childNodes().size());

        Element pNode = divNode.childNodes().get(0);
        assertEquals("p", pNode.tag().name());
        assertEquals("Hello World", pNode.text());

        parser.parseEndTag();
        assertEquals(0, divNode.childNodes().size());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p>Hello World</p><div><p>Test</p></div></body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();

        Element pNode1 = doc.getElementsByTag("p").get(0);
        assertEquals("p", pNode1.tag().name());
        assertEquals("Hello World", pNode1.text());

        parser.parseStartTag(); // parse <div>

        Element divNode = doc.getElementsByTag("div").get(0);
        assertEquals("div", divNode.tag().name());
        assertEquals(1, divNode.childNodes().size());

        Element pNode2 = divNode.childNodes().get(0);
        assertEquals("p", pNode2.tag().name());
        assertEquals("Test", pNode2.text());
    }

    @Test
    public void testParseAttribute() {
        Parser parser = new Parser("<html><body><p id='foo' class='bar baz'></p></body></html>", "http://example.com");
        Document doc = parser.parse();
        Element pNode = doc.getElementsByTag("p").get(0);

        Attributes attributes = pNode.attributes();
        assertEquals("foo", attributes.getValue("id"));
        assertEquals("bar baz", attributes.getValue("class"));
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("<html><body><p>Hello World</p></body></html>", "http://example.com");
        Document doc = parser.parse();
        Element pNode = doc.getElementsByTag("p").get(0);

        assertEquals("Hello World", pNode.text());

        parser.parseStartTag(); // parse <div>
        Element divNode = doc.getElementsByTag("div").get(0);

        parser.parseTextNode();
        TextNode textNode = (TextNode) divNode.childNodes().get(0);
        assertEquals("", textNode.text());
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("<html><body><p><![CDATA[Hello World]]></p></body></html>", "http://example.com");
        Document doc = parser.parse();
        Element pNode = doc.getElementsByTag("p").get(0);

        DataNode cdataNode = (DataNode) pNode.childNodes().get(0);
        assertEquals("Hello World", cdataNode.text());
    }
}