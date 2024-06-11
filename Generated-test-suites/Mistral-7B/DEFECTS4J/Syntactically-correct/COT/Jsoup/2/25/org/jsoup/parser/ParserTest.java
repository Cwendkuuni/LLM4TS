package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        this.parser = new Parser(html, "http://test.com", false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        Element root = doc.childNodes().get(0);
        assertEquals(htmlTag, root.tagName());
        assertEquals(2, root.childNodes().size());
        assertEquals("head", root.childNodes().get(0).tagName());
        Element head = root.childNodes().get(0);
        assertEquals("title", head.childNodes().get(0).tagName());
        assertEquals("Test Title", head.childNodes().get(0).text());
        assertEquals("body", root.childNodes().get(1).tagName());
        Element body = root.childNodes().get(1);
        assertEquals("h1", body.childNodes().get(0).tagName());
        assertEquals("Test Heading", body.childNodes().get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Test Heading</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        assertNotNull(doc);
        assertEquals(htmlTag, doc.tagName());
        assertEquals(1, doc.childNodes().size());
        Element root = doc.childNodes().get(0);
        assertEquals(htmlTag, root.tagName());
        assertEquals(1, root.childNodes().size());
        assertEquals("body", root.childNodes().get(0).tagName());
        Element body = root.childNodes().get(0);
        assertEquals("h1", body.tagName());
        assertEquals("Test Heading", body.text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Document doc = parser.parse(html, "http://test.com");
        Element comment = doc.body().child(0);
        assertEquals(Comment.class, comment.getClass());
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[This is some CDATA]]><body></body></html>";
        Document doc = parser.parse(html, "http://test.com");
        Element cdata = doc.body().child(0);
        assertEquals(TextNode.class, cdata.getClass());
        assertEquals("This is some CDATA", cdata.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"http://foo.com\"><head></head><body></body></html>";
        Document doc = parser.parse(html, "http://test.com");
        Element xmlDecl = doc.head().child(0);
        assertEquals(XmlDeclaration.class, xmlDecl.getClass());
        assertEquals("http://foo.com", xmlDecl.attr("xmlns:foo"));
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Test<br/></p></body></html>";
        Document doc = parser.parse(html, "http://test.com");
        Element p = doc.body().child(0);
        assertEquals("p", p.tagName());
        assertEquals("Test", p.text());
        assertEquals(1, p.childNodes().size());
        Element br = p.child(0);
        assertEquals("br", br.tagName());
        assertTrue(br.isEmpty());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p><strong>Test</strong></p></body></html>";
        Document doc = parser.parse(html, "http://test.com");
        Element p = doc.body().child(0);
        assertEquals("p", p.tagName());
        assertEquals(1, p.childNodes().size());
        Element strong = p.child(0);
        assertEquals("strong", strong.tagName());
        assertEquals("Test", strong.text());

        // test parsing attributes
        String htmlWithAttributes = "<html><body><p class=\"test\" id=\"testId\"></p></body></html>";
        Document docWithAttributes = parser.parse(htmlWithAttributes, "http://test.com");
        Element pWithAttributes = docWithAttributes.body().child(0);
        List<String> classes = pWithAttributes.classes();
        assertEquals(1, classes.size());
        assertEquals("test", classes.get(0));
        assertEquals("testId", pWithAttributes.attr("id"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Test Text</p></body></html>";
        Document doc = parser.parse(html, "http://test.com");
        Element p = doc.body().child(0);
        assertEquals("p", p.tagName());
        assertEquals("Test Text", p.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidHtml() {
        String html = "<html><body><p><strong>Test<script>alert('XSS')</script></strong></p>";
        Parser parser = new Parser(html, "http://test.com", false);
        parser.parse();
    }

    @Test
    public void testInvalidAttributeValue() {
        String html = "<html><body><p class=\"test test'\" id=\"testId\"></p></body></html>";
        Document doc = parser.parse(html, "http://test.com");
        Element p = doc.body().child(0);
        assertEquals("test test\"", p.classes().get(0));
        assertEquals("testId", p.attr("id"));
    }
}