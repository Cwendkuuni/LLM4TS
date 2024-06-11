package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(2, doc.childNodes().size());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body><p><b>Hello World</b></p></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(1, doc.body().childNodes().size());
        Element body = doc.body();
        assertEquals("p", body.child(0).tagName());
        assertEquals("b", body.child(0).child(0).tagName());
        assertEquals("Hello World", body.child(0).child(0).text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element comment = doc.child(0);
        assertEquals(Comment.class, comment.getClass());
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String html = "<html>" + xmlDecl + "</html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(1, doc.childNodes().size());
        XmlDeclaration decl = doc.child(0);
        assertEquals("UTF-8", decl.encoding());
        assertEquals("1.0", decl.version());
        assertEquals("http://example.com", decl.baseUri());
        assertTrue(decl.isStandalone());
        assertFalse(decl.isDoctypeDecl());
        assertFalse(decl.isXmlDecl());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p><h1></h1></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(3, doc.childNodes().size());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(1, doc.body().child(0).childNodes().size());
        Element h1 = doc.body().child(0).child(0);
        assertEquals(h1Tag, h1.tagName());
        assertNull(h1.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p><h1 id=\"test\">Hello World</h1></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(3, doc.childNodes().size());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(1, doc.body().child(0).childNodes().size());
        Element h1 = doc.body().child(0).child(0);
        assertEquals(h1Tag, h1.tagName());
        assertEquals("Hello World", h1.text());
        assertEquals("test", h1.id());
        Attributes attributes = h1.attributes();
        assertNotNull(attributes);
        assertNull(attributes.getValue("test2"));
        assertEquals("Hello World", attributes.getValue("text"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(3, doc.childNodes().size());
        assertEquals(1, doc.body().childNodes().size());
        assertEquals(1, doc.body().child(0).childNodes().size());
        Element h1 = doc.body().child(0).child(0);
        TextNode textNode = (TextNode) h1.childNodes().get(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[ <script src=\"script.js\"></script> ]]></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(3, doc.childNodes().size());
        assertEquals(1, doc.body().childNodes().size());
        Element script = doc.body().child(0);
        DataNode dataNode = (DataNode) script.childNodes().get(0);
        assertEquals("<script src=\"script.js\"></script>", dataNode.text());
    }
}