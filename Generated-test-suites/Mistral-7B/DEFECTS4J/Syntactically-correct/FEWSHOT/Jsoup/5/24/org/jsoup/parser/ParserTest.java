package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(headTag, doc.head().tagName());
        assertEquals(titleTag, doc.head().childNodes().get(0).tagName());
        assertEquals("Test", doc.head().childNodes().get(0).text());
        assertEquals(bodyTag, doc.body().tagName());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(h1Tag, doc.body().childNodes().get(0).tagName());
        assertEquals("Hello", doc.body().childNodes().get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals(bodyTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(h1Tag, doc.childNodes().get(0).tagName());
        assertEquals("Hello", doc.childNodes().get(0).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<html><body><div qux='1'><p>Hello</p></div></body></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        assertEquals(bodyTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(htmlTag, doc.childNodes().get(0).tagName());
        Element bodyElement = doc.childNodes().get(0);
        assertEquals(1, bodyElement.childNodes().size());
        assertEquals(divTag, bodyElement.childNodes().get(0).tagName());
        assertEquals(1, bodyElement.childNodes().get(0).childNodes().size());
        assertEquals(pTag, bodyElement.childNodes().get(0).childNodes().get(0).tagName());
        assertEquals("Hello", bodyElement.childNodes().get(0).childNodes().get(0).text());
        assertEquals("1", bodyElement.attributes().get("qux"));
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        Comment comment = (Comment) doc.childNodes().get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String xml = "<xml version='1.0' encoding='UTF-8' standalone='yes'><![CDATA[This is CDATA]]>";
        Document doc = Parser.parse(xml, "http://example.com");
        assertEquals(xmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        XmlDeclaration xmlDecl = (XmlDeclaration) doc.childNodes().get(0);
        assertEquals("UTF-8", xmlDecl.encoding());
        assertEquals("yes", xmlDecl.standalone());
        assertEquals("1.0", xmlDecl.version());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element body = doc.body();
        Element p = body.childNodes().get(0);
        assertEquals(1, body.childNodes().size());
        assertEquals("Hello", p.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p>Hello</p><h1>World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element body = doc.body();
        Element p = body.childNodes().get(0);
        Element h1 = body.childNodes().get(1);
        assertEquals(2, body.childNodes().size());
        assertEquals("Hello", p.text());
        assertEquals("World", h1.text());
        assertEquals(pTag, p.tagName());
        assertEquals(h1Tag, h1.tagName());
    }

    @Test
    public void testParseAttribute() {
        String html = "<div id='test' class='test class2' style='color:red' title='Test Title'>Test</div>";
        Element div = Parser.parse(html, "http://example.com").body().childNodes().get(0);
        Attributes attributes = div.attributes();
        assertEquals("test", attributes.value("id"));
        assertEquals("test class2", attributes.value("class"));
        assertEquals("color:red", attributes.value("style"));
        assertEquals("Test Title", attributes.value("title"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element body = doc.body();
        Element p = body.childNodes().get(0);
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[This is CDATA]]></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element body = doc.body();
        DataNode data = (DataNode) body.childNodes().get(0);
        assertEquals("This is CDATA", data.text());
    }
}