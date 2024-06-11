package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        // Test case for parsing a valid HTML document
        String html = "<html><head><title>Parser Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("Parser Test", doc.title().text());
        assertEquals("Hello World", doc.getElementsByTag("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        // Test case for parsing a valid HTML body fragment
        String bodyHtml = "<h1>Fragment Test</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals("Fragment Test", doc.getElementsByTag("h1").text());
    }

    // Add more test cases for individual methods here

    // Test case for parsing an HTML document with comments
    @Test
    public void testParseWithComments() {
        String html = "<html><!-- This is a comment -->" +
                "<head><title>Parser Test</title></head>" +
                "<body><h1>Hello World</h1>" +
                "<!-- Another comment --></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("Parser Test", doc.title().text());
        assertEquals("Hello World", doc.getElementsByTag("h1").text());
        // Check that comments were parsed correctly
        List<Node> comments = doc.getElementsByTag("comment");
        assertEquals(2, comments.size());
    }

    // Test case for parsing an HTML document with an XML declaration
    @Test
    public void testParseWithXmlDeclaration() {
        String html = "<html xmlns='http://www.w3.org/1999/xhtml'><head><title>Parser Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("Parser Test", doc.title().text());
        assertEquals("Hello World", doc.getElementsByTag("h1").text());
        // Check that XML declaration was parsed correctly
        XmlDeclaration xmlDecl = doc.getXmlDeclaration();
        assertEquals("http://www.w3.org/1999/xhtml", xmlDecl.namespace());
    }

    // Test case for parsing an HTML document with an empty tag
    @Test
    public void testParseWithEmptyTag() {
        String html = "<html><head><title>Parser Test</title></head><body><img src='image.png' /></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("Parser Test", doc.title().text());
        // Check that img tag was parsed correctly
        List<Element> images = doc.getElementsByTag("img");
        assertEquals(1, images.size());
        Element img = images.get(0);
        assertNull(img.getElementsByTag("*").size());
    }

    // Test case for parsing an HTML document with an unknown tag
    @Test(expected = org.jsoup.parser.Parser.UnsupportedTagException.class)
    public void testParseWithUnknownTag() {
        String html = "<html><head><title>Parser Test</title></head><body><zap></zap></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        parser.parse();
        fail("Expected Parser.UnsupportedTagException");
    }

    // Test case for parsing an HTML document with an improperly nested tag
    @Test(expected = org.jsoup.parser.Parser.TagException.class)
    public void testParseWithNestedTagError() {
        String html = "<html><head><title>Parser Test</title></head><body><div><div></div></div></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        parser.parse();
        fail("Expected Parser.TagException");
    }

    // Test case for parsing an HTML document with an unmatched end tag
    @Test(expected = org.jsoup.parser.Parser.TagException.class)
    public void testParseWithUnmatchedEndTag() {
        String html = "<html><head><title>Parser Test</title></head><body><div><p></div></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        parser.parse();
        fail("Expected Parser.TagException");
    }

    // Test case for parsing an HTML document with a base tag
    @Test
    public void testParseWithBaseTag() {
        String html = "<html><head><title>Parser Test</title><base href='/test/'></head><body><a href='link.html'>Link</a></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element link = doc.getElementsByTag("a").get(0);
        assertEquals("link.html", link.absUrl("href"));
    }

    // Test case for parsing an HTML document with data tags (textarea, script)
    @Test
    public void testParseWithDataTags() {
        String html = "<html><head><title>Parser Test</title></head><body><textarea>This is a textarea</textarea><script>function test() { alert('Test'); }</script></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element textarea = doc.getElementsByTag("textarea").get(0);
        assertEquals("This is a textarea", textarea.getElementsByTag("*").text());
        Element script = doc.getElementsByTag("script").get(0);
        // Check that script tag was parsed correctly without executing the script
        assertEquals("function test() { alert('Test'); }", script.getWholeText());
    }
}