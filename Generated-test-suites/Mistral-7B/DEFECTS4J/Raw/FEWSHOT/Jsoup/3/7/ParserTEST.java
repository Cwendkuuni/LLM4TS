package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {

    @Test
    public void testParseEmptyHtml() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        assertTrue(doc.body().child(0) instanceof TextNode);
    }

    @Test
    public void testParseHtmlWithTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        Element body = doc.body();
        Element p = body.child(0);
        TextNode textNode = (TextNode) p.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseHtmlWithComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Comment comment = (Comment) doc.body().child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseHtmlWithScriptTag() {
        String html = "<html><body><script type=\"text/javascript\">console.log('Hello');</script></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element script = doc.body().child(0);
        assertEquals("script", script.tagName());
        DataNode dataNode = (DataNode) script.child(0);
        assertEquals("console.log('Hello');", dataNode.text());
    }

    @Test
    public void testParseHtmlWithTextareaTag() {
        String html = "<html><body><textarea>Hello World</textarea></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element textarea = doc.body().child(0);
        assertEquals("textarea", textarea.tagName());
        TextNode textNode = (TextNode) textarea.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseHtmlWithBaseTag() {
        String html = "<html><head><base href=\"/foo/\"></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("/foo/", doc.baseUri());
    }

    @Test
    public void testParseHtmlWithBaseTagAndRelativeLink() {
        String html = "<html><head><base href=\"/foo/\"></head><body><a href=\"bar.html\"></a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element link = doc.body().child(0).child(0);
        assertEquals("/foo/bar.html", link.absUrl("href"));
    }

    @Test
    public void testParseHtmlWithImplicitParent() {
        String html = "<html><body><img src=\"image.jpg\"></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element img = doc.body().child(0);
        Element imgParent = img.parent();
        assertEquals("div", imgParent.tagName());
    }

    @Test
    public void testParseHtmlWithImplicitTextareaParent() {
        String html = "<html><body><textarea name=\"foo\">bar</textarea></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element textarea = doc.body().child(0);
        Element textareaParent = textarea.parent();
        assertEquals("p", textareaParent.tagName());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseInvalidHtml() {
        String html = "<html><body><p>Hello<script>console.log('World');</script></p></body></html>";
        Parser.parse(html, "http://example.com");
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<html><body><p>Hello World</p></body>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");
        Element p = doc.body().child(0);
        TextNode textNode = (TextNode) p.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseBodyFragmentWithBaseTag() {
        String html = "<html><body><base href=\"/foo/\"><p>Hello World</p></body>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");
        assertEquals("/foo/", doc.baseUri());
    }

    @Test
    public void testParseBodyFragmentWithRelativeLink() {
        String html = "<html><body><base href=\"/foo/\"><a href=\"bar.html\">Link</a></body>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");
        Element link = doc.body().child(0).child(0);
        assertEquals("/foo/bar.html", link.absUrl("href"));
    }
}