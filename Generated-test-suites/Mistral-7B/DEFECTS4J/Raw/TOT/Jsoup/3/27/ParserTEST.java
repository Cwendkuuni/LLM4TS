import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.junit.Assert.*;

import java.util.LinkedList;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        String html = "<html><head><title>Test Page</title></head>" +
                "<body><h1>Hello World</h1><p>This is a <textarea>test</textarea></p></body></html>";
        parser = new Parser(html, "http://test.com/");
    }

    @Test
    public void testParseHtml() {
        Document doc = parser.parse();
        Element htmlNode = doc.body();
        Element headNode = doc.head();
        Element titleNode = headNode.title();

        assertEquals("Test Page", titleNode.text());
        assertEquals("Hello World", htmlNode.tagName());
        assertEquals("http://test.com/", doc.baseUri());

        Element h1Node = htmlNode.getElementsByTag("h1").get(0);
        assertEquals(1, h1Node.childNodes().size());
        TextNode textNode = (TextNode) h1Node.childNodes().get(0);
        assertEquals("Hello World", textNode.text());

        Element textareaNode = htmlNode.getElementsByTag("textarea").get(0);
        assertEquals(1, textareaNode.childNodes().size());
        textNode = (TextNode) textareaNode.childNodes().get(0);
        assertEquals("test", textNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        Parser parser = new Parser(null, "http://test.com/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        Parser parser = new Parser("<html></html>", null);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<textarea>test</textarea>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com/");
        Element bodyNode = doc.body();
        Element textareaNode = bodyNode.getElementsByTag("textarea").get(0);

        assertEquals(1, bodyNode.childNodes().size());
        assertEquals(1, textareaNode.childNodes().size());
        TextNode textNode = (TextNode) textareaNode.childNodes().get(0);
        assertEquals("test", textNode.text());
    }

    @Test
    public void testParseEmptyBodyFragment() {
        String bodyHtml = "";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com/");
        Element bodyNode = doc.body();
        assertEquals(0, bodyNode.childNodes().size());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test Page</title></head>" +
                "<body><h1>Hello World</h1>" +
                "<p><img src=\"image.png\"></p></body></html>";

        parser = new Parser(html, "http://test.com/");
        parser.parse();
        Element h1Node = parser.last().getElementsByTag("h1").get(0);
        TextNode h1TextNode = (TextNode) h1Node.childNodes().get(0);
        assertEquals("Hello World", h1TextNode.text());

        Element imgNode = parser.last().getElementsByTag("img").get(0);
        assertEquals("image.png", imgNode.absUrl("src"));
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><head><title>Test Page</title></head>" +
                "<body><h1>Hello World</h1>" +
                "<p><img src=\"image.png\" /></p>" +
                "</body></html>";

        parser = new Parser(html, "http://test.com/");
        parser.parse();
        assertEquals(3, parser.stack.size());
        Element h1Node = parser.last().getElementsByTag("h1").get(0);
        TextNode h1TextNode = (TextNode) h1Node.childNodes().get(0);
        assertEquals("Hello World", h1TextNode.text());

        Element bodyNode = parser.stack.getLast();
        assertEquals("body", bodyNode.tagName());
        Element imgNode = bodyNode.getElementsByTag("img").get(0);
        assertEquals(0, imgNode.childNodes().size());
        assertTrue(imgNode.hasAttr("src"));
        assertEquals("image.png", imgNode.attr("src"));
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"http://foo.com\" xml:lang=\"en\">";
        parser = new Parser(html, "http://test.com/");
        parser.parse();
        Element rootNode = parser.stack.getLast();
        assertTrue(rootNode.hasAttr("xmlns:foo"));
        assertEquals("http://foo.com", rootNode.attr("xmlns:foo"));
        assertTrue(rootNode.hasAttr("xml:lang"));
        assertEquals("en", rootNode.attr("xml:lang"));
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        parser = new Parser(html, "http://test.com/");
        parser.parse();
        Element bodyNode = parser.last();
        assertTrue(bodyNode.childNodes().isEmpty());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[ <script type=\"text/javascript\"> alert('Hello World'); </script>]]>";
        parser = new Parser(html, "http://test.com/");
        parser.parse();
        Element scriptNode = parser.last();
        TextNode scriptTextNode = (TextNode) scriptNode.childNodes().get(0);
        assertEquals(" <script type=\"text/javascript\"> alert('Hello World'); </script>", scriptTextNode.text());
    }

    @Test
    public void testParseEmptyDocument() {
        String html = "<html></html>";
        parser = new Parser(html, "http://test.com/");
        Document doc = parser.parse();
        Element htmlNode = doc.body();
        assertEquals(0, htmlNode.childNodes().size());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body><h1<script>alert('Hello World')</script></h1>";
        try {
            parser = new Parser(html, "http://test.com/");
            parser.parse();
        } catch (RuntimeException e) {
            // expected
        }
        assertEquals(3, parser.stack.size());
        Element bodyNode = parser.stack.getLast();
        Element h1Node = bodyNode.getElementsByTag("h1").get(0);
        assertEquals(0, h1Node.childNodes().size());
    }
}