package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().text());
        assertEquals(1, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals(1, doc.body().childNodeSize());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div id='foo'>Hello World</div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        assertEquals(1, doc.body().childNodeSize());
        Element foo = doc.body().child(0);
        assertEquals("div", foo.tagName());
        assertEquals("Hello World", foo.text());
    }

    @Test
    public void testParseEmpty() {
        Document doc = Parser.parse("<>", "http://example.com");
        assertTrue(doc.isEmpty());
    }

    @Test
    public void testParseIgnoreBaseTag() {
        String html = "<base href='/foo'><html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("Test", doc.title().text());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseMissingEndTag() {
        String html = "<html><body><h1>Hello World";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(1, doc.body().childNodeSize());
        assertEquals("Hello World", doc.body().child(0).text());
        // missing end tag for h1
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        String html = "<html><body><h1<foo>Hello World";
        Parser.parse(html, "http://example.com");
        // invalid HTML should throw an exception
    }

    @Test
    public void testParseSelfClosedTags() {
        String html = "<html><body><img src='image.png'><input type='text'><br/></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(3, doc.body().childNodeSize());
        Element img = doc.body().child(0);
        assertTrue(img.tag().isKnownTag());
        assertTrue(img.tag().isSelfClosing());
        Element input = doc.body().child(1);
        assertTrue(input.tag().isKnownTag());
        assertFalse(input.tag().isSelfClosing());
        Element br = doc.body().child(2);
        assertTrue(br.tag().isSelfClosing());
    }

    @Test
    public void testParseComments() {
        String html = "<html><!-- This is a comment --><body><h1>Hello World</h1><!-- Another comment --></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(2, doc.childNodeSize());
        Element comment1 = doc.body().child(0);
        assertEquals(Comment.class, comment1.getClass());
        assertEquals("This is a comment", comment1.text());
        Element comment2 = doc.body().child(1);
        assertEquals(1, comment2.childNodeSize());
        assertEquals("Another comment", comment2.child(0).text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns='http://www.w3.org/1999/xhtml'>";
        Document doc = Parser.parse(html, "http://example.com");
        XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.child(0);
        assertEquals("http://www.w3.org/1999/xhtml", xmlDeclaration.xmlns());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><script><![CDATA[function foo() { alert('Hello World'); }]]></script></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element script = doc.body().child(0);
        assertEquals("script", script.tagName());
        Element cdata = script.child(0);
        assertEquals("CDATA", cdata.tagName());
        assertEquals("function foo() {\n alert('Hello World'); \n}", cdata.text());
    }

    @Test
    public void testParseTextarea() {
        String html = "<html><body><textarea>Hello World</textarea></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element textarea = doc.body().child(0);
        assertEquals("textarea", textarea.tagName());
        TextNode textNode = (TextNode) textarea.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href='/foo'><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("/foo", doc.baseUri());
    }

    @Test
    public void testParseCommentWithinScriptTag() {
        String html = "<html><body><script><!-- This is a comment --></script></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element script = doc.body().child(0);
        TextNode textNode = (TextNode) script.child(0);
        assertEquals("function (){\n// This is a comment \n}", textNode.text());
    }

    @Test
    public void testParseScriptTagWithinTextNode() {
        String html = "<html><body><textarea><script>alert('Hello World')</script></textarea></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element textarea = doc.body().child(0);
        TextNode textNode = (TextNode) textarea.child(0);
        assertEquals("alert('Hello World')", textNode.text());
    }

    @Test
    public void testParseEmptyTextNode() {
        String html = "<html><body><textarea></textarea></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element textarea = doc.body().child(0);
        assertEquals(1, textarea.childNodeSize());
        TextNode textNode = (TextNode) textarea.child(0);
        assertEquals("", textNode.text());
    }
}