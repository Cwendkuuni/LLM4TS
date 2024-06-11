package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmptyHtml() {
        String html = "<html></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        assertEquals("html", root.tagName());
        assertEquals("http://example.com", root.baseUri());
        assertTrue(root.children().isEmpty());
    }

    @Test
    public void testParseHtmlWithBody() {
        String html = "<html><body></body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        assertEquals("html", root.tagName());
        assertEquals("http://example.com", root.baseUri());
        Element body = root.child(0);
        assertEquals("body", body.tagName());
        assertTrue(body.children().isEmpty());
    }

    @Test
    public void testParseHtmlWithText() {
        String html = "<html><body>Hello World</body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        assertEquals("html", root.tagName());
        assertEquals("http://example.com", root.baseUri());
        Element body = root.child(0);
        Element text = body.child(0);
        assertEquals("body", body.tagName());
        assertEquals("Hello World", text.text());
    }

    @Test
    public void testParseHtmlWithComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        assertEquals("html", root.tagName());
        assertEquals("http://example.com", root.baseUri());
        Element body = root.child(0);
        Comment comment = body.child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseHtmlWithXmlDeclaration() {
        String html = "<html xmlns='http://www.w3.org/1999/xhtml' version='1.0'><body></body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        assertEquals("html", root.tagName());
        assertEquals("http://example.com", root.baseUri());
        XmlDeclaration xmlDeclaration = root.child(0);
        assertEquals("http://www.w3.org/1999/xhtml", xmlDeclaration.xmlNamespace());
        assertEquals("1.0", xmlDeclaration.xmlVersion());
    }

    @Test
    public void testParseHtmlWithTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        assertEquals("html", root.tagName());
        assertEquals("http://example.com", root.baseUri());
        Element body = root.child(0);
        Element p = body.child(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseHtmlWithAttributes() {
        String html = "<html><body><p id='p1' class='class1' title='Title'><a href='link.html'>Link</a></p></body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        assertEquals("html", root.tagName());
        assertEquals("http://example.com", root.baseUri());
        Element body = root.child(0);
        Element p = body.child(0);
        assertEquals("p", p.tagName());
        Attributes attributes = p.attributes();
        assertEquals("p1", attributes.getValue("id"));
        assertEquals("class1", attributes.getValue("class"));
        assertEquals("Title", attributes.getValue("title"));
        Element link = p.child(1);
        assertEquals("a", link.tagName());
        assertEquals("link.html", link.absUrl("href"));
    }

    @Test
    public void testParseHtmlWithBaseTag() {
        String html = "<html><body><base href='/subdir'><p>Hello World</p></body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        assertEquals("html", root.tagName());
        assertEquals("http://example.com", root.baseUri());
        Element base = root.child(0);
        assertEquals("base", base.tagName());
        assertEquals("/subdir", base.absUrl("href"));
        Document parsedDoc = Parser.parse("Hello World", base.absUrl());
        Element parsedRoot = parsedDoc.getRootElement();
        assertEquals("html", parsedRoot.tagName());
        assertEquals("/subdir", parsedRoot.baseUri());
    }

    @Test
    public void testParseHtmlWithScriptTag() {
        String html = "<html><body><script>alert('Hello World');</script></body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        assertEquals("html", root.tagName());
        assertEquals("http://example.com", root.baseUri());
        Element body = root.child(0);
        Element script = body.child(0);
        assertEquals("script", script.tagName());
        DataNode dataNode = (DataNode) script.child(0);
        assertEquals("alert('Hello World');", dataNode.data());
    }

    @Test
    public void testParseHtmlWithTextareaTag() {
        String html = "<html><body><textarea>Hello World</textarea></body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        assertEquals("html", root.tagName());
        assertEquals("http://example.com", root.baseUri());
        Element body = root.child(0);
        Element textarea = body.child(0);
        assertEquals("textarea", textarea.tagName());
        TextNode textNode = (TextNode) textarea.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseHtmlWithImplicitParent() {
        String html = "<html><body><img src='image.png'></body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        assertEquals("html", root.tagName());
        assertEquals("http://example.com", root.baseUri());
        Element body = root.child(0);
        Element img = body.child(0);
        assertEquals("img", img.tagName());
        assertEquals("http://example.com/image.png", img.absUrl("src"));
        Element implicitParent = (Element) img.parent();
        assertEquals("div", implicitParent.tagName());
        assertEquals("http://example.com", implicitParent.baseUri());
    }

    @Test
    public void testParseHtmlWithNestedTags() {
        String html = "<html><body><p><a href='link.html'>Link</a></p></body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        assertEquals("html", root.tagName());
        assertEquals("http://example.com", root.baseUri());
        Element body = root.child(0);
        Element p = body.child(0);
        Element a = p.child(0);
        assertEquals("p", p.tagName());
        assertEquals("body", p.parent().tagName());
        assertEquals("a", a.tagName());
        assertEquals("link.html", a.absUrl("href"));
        assertEquals("p", a.parent().tagName());
    }

    @Test
    public void testParseHtmlWithMixedTags() {
        String html = "<html><body><p><a href='link.html'>Link</a><img src='image.png'></p></body></html>";
        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        assertEquals("html", root.tagName());
        assertEquals("http://example.com", root.baseUri());
        Element body = root.child(0);
        Element p = body.child(0);
        Element a = p.child(0);
        Element img = p.child(1);
        assertEquals("p", p.tagName());
        assertEquals("body", p.parent().tagName());
        assertEquals("a", a.tagName());
        assertEquals("link.html", a.absUrl("href"));
        assertEquals("p", a.parent().tagName());
        assertEquals("img", img.tagName());
        assertEquals("http://example.com/image.png", img.absUrl("src"));
        assertEquals("p", img.parent().tagName());
    }

    @Test
    public void testParseHtmlWithEmptyBodyFragment() {
        String html = "<html><body></body></html>";
        Parser parser = new Parser(html, "http://example.com", true);
        Document doc = parser.parse();
        assertEquals("html", doc.tagName());
        assertEquals("http://example.com", doc.baseUri());
        Element root = doc.getRootElement();
        Element body = root.child(0);
        assertTrue(body.children().isEmpty());
    }

    @Test
    public void testParseHtmlWithTextBodyFragment() {
        String html = "<html><body>Hello World</body></html>";
        Parser parser = new Parser(html, "http://example.com", true);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        Element body = root.child(0);
        assertEquals("body", body.tagName());
        TextNode textNode = (TextNode) body.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseHtmlWithMixedBodyFragment() {
        String html = "<html><body><p>Hello World</p><img src='image.png'></body></html>";
        Parser parser = new Parser(html, "http://example.com", true);
        Document doc = parser.parse();
        Element root = doc.getRootElement();
        Element body = root.child(0);
        Element p = body.child(0);
        TextNode textNode = (TextNode) p.child(0);
        assertEquals("p", p.tagName());
        assertEquals("body", p.parent().tagName());
        assertEquals("Hello World", textNode.text());
        Element img = body.child(1);
        assertEquals("img", img.tagName());
        assertEquals("http://example.com/image.png", img.absUrl("src"));
        assertEquals("body", img.parent().tagName());
    }
}