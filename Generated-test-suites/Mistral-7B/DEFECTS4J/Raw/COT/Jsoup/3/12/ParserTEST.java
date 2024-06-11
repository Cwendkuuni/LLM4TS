import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><p>Test Paragraph</p></body>";
        Parser parser = new Parser(bodyHtml, "http://example.com");
        Document doc = parser.parse();
        assertEquals("Test Paragraph", doc.body().text());
    }

    @Test
    public void testParseEmptyString() {
        Parser parser = new Parser("", "http://example.com");
        Document doc = parser.parse();
        assertTrue(doc.isEmpty());
    }

    @Test
    public void testParseInvalidHtml() {
        Parser parser = new Parser("<", "http://example.com");
        try {
            parser.parse();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns='http://www.w3.org/1999/xhtml'><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        XmlDeclaration xmlDecl = doc.head().selectXml("xml").first();
        assertEquals("http://www.w3.org/1999/xhtml", xmlDecl.xmlns());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Comment comment = doc.select("html>!--").first();
        assertEquals("This is a comment", comment.data());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><div><p>Test</p></div></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element div = doc.select("div").first();
        assertTrue(div.child(0) instanceof TextNode);
        assertEquals("Test", div.child(0).text());
    }

    @Test
    public void testParseInvalidEndTag() {
        String html = "<html><body><div><p>Test</p></div></div></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        try {
            parser.parse();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1>Test Heading</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element h1 = doc.select("h1").first();
        assertEquals("Test Heading", h1.text());
    }

    @Test
    public void testParseInvalidStartTag() {
        String html = "<html><body><h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        try {
            parser.parse();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Test Text</p></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element p = doc.select("p").first();
        assertEquals("Test Text", p.child(0).text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><script><![CDATA[alert('Hello World');]]></script></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element script = doc.select("script").first();
        assertEquals("alert('Hello World');", script.data());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<html><head><base href='/subdir/'><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("/subdir/", doc.baseUri());
    }

    @Test
    public void testParseInvalidBaseTag() {
        String html = "<html><head><base target='_blank'><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("http://example.com", doc.baseUri());
    }

    
}