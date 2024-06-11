package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParser_Parse() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.body().getElementsByTag("h1").text());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParser_ParseBodyFragment() {
        String html = "<body><h1>Test Heading</h1></body>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, true);
        Document doc = parser.parse();
        assertEquals("Test Heading", doc.body().getElementsByTag("h1").text());
        assertNotNull(doc.head()); // body fragment should still have empty head
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParser_EmptyInput() {
        String html = "";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        assertEquals("", doc.htmlText());
        assertNull(doc.title());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParser_NullHtml() {
        String html = null;
        String baseUri = "http://www.example.com";
        new Parser(html, baseUri, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParser_NullBaseUri() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = null;
        try {
            new Parser(html, baseUri, false);
        } catch (IllegalArgumentException e) {
            assertEquals("baseUri is marked non-null but is null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParser_EmptyBaseUri() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "";
        try {
            new Parser(html, baseUri, false);
        } catch (IllegalArgumentException e) {
            assertEquals("baseUri cannot be empty", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testParser_SimpleHtmlWithScriptAndTextarea() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><script type=\"text/javascript\">alert(\"Hello world!\");</script><textarea name=\"textArea\">Hello world!</textarea></body></html>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element script = doc.body().getElementsByTag("script").first();
        assertEquals("text/javascript", script.attr("type"));
        Element textarea = doc.body().getElementsByTag("textarea").first();
        assertEquals("Hello world!", textarea.text());
    }

    @Test
    public void testParser_Comment() {
        String html = "<html><!-- Test Comment --><head><title>Test Title</title></head><body></body></html>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element comment = doc.body().getElementsByClass("jsoup-comment").first();
        assertEquals("Test Comment", comment.text());
    }

    @Test
    public void testParser_ParseXmlDecl() {
        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        XmlDeclaration xmlDecl = doc.xml();
        assertEquals("http://www.w3.org/1999/xhtml", xmlDecl.xmlns());
    }

    @Test
    public void testParser_ParseEndTag() {
        String html = "<html><head><title>Test Title</title></head><body><p>Test Paragraph</p></body></html>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element p = doc.body().getElementsByTag("p").first();
        assertEquals(1, p.childNodes().size());
        assertEquals("Test Paragraph", p.childNodes().get(0).text());
    }

    @Test
    public void testParser_ParseStartTag() {
        String html = "<html><head></head><body><p><strong><em>Test Paragraph</em></strong></p></body></html>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element p = doc.body().getElementsByTag("p").first();
        Element em = p.getElementsByTag("em").first();
        Element strong = em.getParent();
        assertEquals(4, p.childNodes().size());
        assertEquals("Test Paragraph", p.childNodes().get(0).text());
        assertEquals("em", em.tagName());
        assertEquals("strong", strong.tagName());
    }

    @Test
    public void testParser_ParseAttribute() {
        String html = "<img src=\"logo.png\" alt=\"Test Image\">";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element img = doc.getElementsByTag("img").first();
        assertEquals("logo.png", img.attr("src"));
        assertEquals("Test Image", img.attr("alt"));
    }

    @Test
    public void testParser_ParseTextNode() {
        String html = "<html><head></head><body><p>Test Paragraph</p></body></html>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element p = doc.body().getElementsByTag("p").first();
        assertEquals("Test Paragraph", p.text());
    }

    @Test
    public void testParser_ParseCdata() {
        String html = "<html><head></head><body><![CDATA[Test Paragraph]]></body></html>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element p = doc.body();
        assertEquals("Test Paragraph", p.childNodes().get(0).text());
    }
}