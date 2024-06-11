package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.*;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParserConstructor() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://test.com", false);
        assertNotNull(parser);
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://test.com", false);
        Document doc = parser.parse();

        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Parser parser = new Parser(bodyHtml, "http://test.com", true);
        Document doc = parser.parse();

        assertEquals("Hello World", doc.body().getElementsByTag("h1").text());
        assertNull(doc.head());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParserConstructorNullHtml() {
        Parser parser = new Parser(null, "http://test.com", false);
        fail(); // Test should throw IllegalArgumentException, but JUnit does not support this directly
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParserConstructorNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, null, false);
        fail(); // Test should throw IllegalArgumentException, but JUnit does not support this directly
    }

    @Test
    public void testParseEmptyString() {
        Parser parser = new Parser("", "http://test.com", false);
        Document doc = parser.parse();
        assertTrue(doc.isEmpty());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Parser parser = new Parser(html, "http://test.com", false);
        Document doc = parser.parse();
        assertEquals("This is a comment ", doc.body().text()); // Doc.body().text() includes the comment
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        Parser parser = new Parser(html, "http://test.com", false);
        Document doc = parser.parse();
        XmlDeclaration xmlDeclaration = doc.xml();
        assertEquals("about:legacy-compat", xmlDeclaration.systemId());
    }

    @Test
    public void testParseEndTag() {
        String html = "<div><p>Hello World</p></div>";
        Parser parser = new Parser(html, "http://test.com", false);
        Document doc = parser.parse();
        assertEquals(1, doc.body().getElementsByTag("p").size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEndTagUnknownTag() {
        String html = "<div><foo></foo></div>";
        Parser parser = new Parser(html, "http://test.com", false);
        parser.parseEndTag();
        fail(); // Test should throw IllegalArgumentException, but JUnit does not support this directly
    }

    @Test
    public void testParseStartTag() {
        String html = "<h1 id='test'>Hello World</h1>";
        Parser parser = new Parser(html, "http://test.com", false);
        Document doc = parser.parse();

        Element h1 = doc.getElementsByTag("h1").first();
        assertEquals("Hello World", h1.text());
        assertEquals("test", h1.id());
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href='http://test.com'>Link</a>";
        Parser parser = new Parser(html, "http://test.com", false);
        Document doc = parser.parse();

        Element link = doc.getElementsByTag("a").first();
        assertEquals("http://test.com", link.attr("href"));
    }

    @Test
    public void testParseAttributeQuotedValue() {
        String html = "<a href='http://test.com/test " + "title='Test Title'>Link</a>";
        Parser parser = new Parser(html, "http://test.com", false);
        Document doc = parser.parse();

        Element link = doc.getElementsByTag("a").first();
        assertEquals("Test Title", link.attr("title"));
    }

    @Test
    public void testParseAttributeUnquotedValue() {
        String html = "<a href='http://test.com/test test-attribute>Link</a>";
        Parser parser = new Parser(html, "http://test.com", false);
        Document doc = parser.parse();

        Element link = doc.getElementsByTag("a").first();
        assertEquals("test", link.attr("test-attribute"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Hello World</p>";
        Parser parser = new Parser(html, "http://test.com", false);
        Document doc = parser.parse();
        TextNode textNode = (TextNode) doc.body().child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[Hello World]]>";
        Parser parser = new Parser(html, "http://test.com", false);
        Document doc = parser.parse();
        TextNode textNode = (TextNode) doc.body().child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseInvalidTag() {
        String html = "<invalid>";
        Parser parser = new Parser(html, "http://test.com", false);
        try {
            parser.parse();
            fail();
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testParseEmptyTag() {
        String html = "<img />";
        Parser parser = new Parser(html, "http://test.com", false);
        Document doc = parser.parse();
        Element img = doc.getElementsByTag("img").first();
        assertTrue(img.isEmpty());
    }

    @Test
    public void testParseDataTag() {
        String html = "<textarea>Hello World</textarea>";
        Parser parser = new Parser(html, "http://test.com", false);
        Document doc = parser.parse();
        TextNode textNode = (TextNode) doc.getElementsByTag("textarea").first().child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseBaseTag() {
        String html = "<base href='/newdir' />";
        Parser parser = new Parser(html, "http://test.com", false);
        Document doc = parser.parse();
        assertEquals("/newdir", doc.baseUri());
    }
}