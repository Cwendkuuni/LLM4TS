package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.util.LinkedList;

/**
 * Test class for Parser class
 */
public class ParserTest {

    @org.junit.Test
    public void testParser_StringHtml_StringBaseUri_False() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        assertEquals("Test", doc.title().text());
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParser_NullHtml_StringBaseUri_False() {
        String baseUri = "http://example.com";
        Parser parser = new Parser(null, baseUri, false);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParser_NullBaseUri_StringHtml_False() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Parser parser = new Parser(html, null, false);
    }

    @org.junit.Test
    public void testParserBodyFragment_StringBodyHtml_StringBaseUri_True() {
        String bodyHtml = "<body><h1>Test</h1></body>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertEquals("Test", doc.title().text());
        assertEquals(bodyHtml, doc.body().html());
        assertEquals(baseUri, doc.baseUri());
        assertTrue(doc.head() == null);
    }

    @org.junit.Test
    public void testParserBodyFragmentRelaxed_StringBodyHtml_StringBaseUri_True() {
        String bodyHtml = "<body><h1>Test</h1></body>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertEquals("Test", doc.title().text());
        assertEquals(bodyHtml, doc.body().html());
        assertEquals(baseUri, doc.baseUri());
        assertTrue(doc.head() == null);
    }

    @org.junit.Test
    public void testParseEmpty() {
        Parser parser = new Parser("");
        Document doc = parser.parse();
        assertTrue(doc.body().childNodeSize() == 0);
    }

    @org.junit.Test
    public void testParseStartTag() {
        String html = "<html><h1 id='test' class='test' title='Test Title'><a href='test.html'>Test Link</a></h1></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);

        Document doc = parser.parse();
        Element h1 = doc.select("h1").first();
        assertEquals("h1", h1.tagName());
        assertEquals("Test", h1.text());
        assertEquals("test", h1.id());
        Attributes attributes = h1.attributes();
        assertEquals("test", attributes.get("class"));
        assertEquals("Test Title", attributes.get("title"));
        Element a = h1.child(0);
        assertEquals("a", a.tagName());
        assertEquals("test.html", a.absUrl("href"));
    }

    @org.junit.Test
    public void testParseEndTag() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);

        Document doc = parser.parse();
        Element h1 = doc.select("h1").first();
        assertEquals("h1", h1.tagName());
        assertEquals("Test", h1.text());
        assertEquals(1, h1.childNodeSize());
        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(1, body.childNodeSize());
        assertEquals("html", body.firstChild().tagName());
    }

    @org.junit.Test
    public void testParseComment() {
        String html = "<html><!-- Test Comment --></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);

        Document doc = parser.parse();
        assertEquals(1, doc.childNodeSize());
        Element htmlTag = doc.child(0);
        assertEquals("html", htmlTag.tagName());
        assertEquals(1, htmlTag.childNodeSize());
        Comment comment = (Comment) htmlTag.child(0);
        assertEquals("Test Comment", comment.data());
    }

    @org.junit.Test
    public void testParseXmlDecl() {
        String html = "<html xmlns='http://www.w3.org/1999/xhtml'><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);

        Document doc = parser.parse();
        Element htmlTag = doc.child(0);
        assertEquals("html", htmlTag.tagName());
        assertEquals(1, htmlTag.childNodeSize());
        XmlDeclaration xmlDeclaration = (XmlDeclaration) htmlTag.child(0);
        assertEquals("http://www.w3.org/1999/xhtml", xmlDeclaration.xmlNs());
    }

    @org.junit.Test
    public void testParseEndTagWithEmptyElement() {
        String html = "<html><body><img/></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);

        Document doc = parser.parse();
        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(1, body.childNodeSize());
        Element img = body.child(0);
        assertEquals("img", img.tagName());
        assertTrue(img.text().isEmpty());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testParseInvalidStartTag() {
        String html = "<html><brokenTag/></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);

        parser.parse();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testParseInvalidEndTag() {
        String html = "<html><body><h1>Test</h1></body></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);

        parser.parse();
    }

    @org.junit.Test
    public void testParseCommentWithEndTag() {
        String html = "<html><!-- Test Comment --></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);

        Document doc = parser.parse();
        assertEquals(1, doc.childNodeSize());
        Element htmlTag = doc.child(0);
        assertEquals("html", htmlTag.tagName());
        assertEquals(1, htmlTag.childNodeSize());
        Comment comment = (Comment) htmlTag.child(0);
        assertEquals("Test Comment", comment.data());
        assertTrue(comment.isClosed());
    }

    @org.junit.Test
    public void testParseCdata() {
        String html = "<html><script><![CDATA[document.write('Test');]]></script></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);

        Document doc = parser.parse();
        Element script = doc.select("script").first();
        assertEquals("script", script.tagName());
        assertEquals("Test", script.text());
    }
}