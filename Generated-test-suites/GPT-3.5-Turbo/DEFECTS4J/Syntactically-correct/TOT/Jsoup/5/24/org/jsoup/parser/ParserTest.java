package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private String html;
    private String baseUri;

    @Before
    public void setUp() {
        html = "<html><head><title>Sample Title</title></head><body><h1>Sample Heading</h1></body></html>";
        baseUri = "http://example.com";
        parser = new Parser(html, baseUri, false);
    }

    @After
    public void tearDown() {
        parser = null;
    }

    @Test
    public void testParse() {
        Document document = parser.parse();

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head><title>Sample Title</title></head><body><h1>Sample Heading</h1></body></html>", document.outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        html = "<h1>Sample Heading</h1>";
        parser = new Parser(html, baseUri, true);
        Document document = parser.parse();

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head></head><body><h1>Sample Heading</h1></body></html>", document.outerHtml());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        html = "<h1>Sample Heading</h1>";
        parser = new Parser(html, baseUri, true);
        parser.relaxed = true;
        Document document = parser.parse();

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head></head><body><h1>Sample Heading</h1></body></html>", document.outerHtml());
    }

    @Test
    public void testParseComment() {
        html = "<!-- This is a comment -->";
        parser = new Parser(html, baseUri, false);
        Document document = parser.parse();

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head></head><body><!-- This is a comment --></body></html>", document.outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        parser = new Parser(html, baseUri, false);
        Document document = parser.parse();

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head></head><body><?xml version=\"1.0\" encoding=\"UTF-8\"?></body></html>", document.outerHtml());
    }

    @Test
    public void testParseEndTag() {
        html = "<h1>Sample Heading</h1></body>";
        parser = new Parser(html, baseUri, false);
        Document document = parser.parse();

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head></head><body><h1>Sample Heading</h1></body></html>", document.outerHtml());
    }

    @Test
    public void testParseStartTag() {
        html = "<h1>Sample Heading</h1>";
        parser = new Parser(html, baseUri, false);
        Document document = parser.parse();

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head></head><body><h1>Sample Heading</h1></body></html>", document.outerHtml());
    }

    @Test
    public void testParseTextNode() {
        html = "Sample Text";
        parser = new Parser(html, baseUri, false);
        Document document = parser.parse();

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head></head><body>Sample Text</body></html>", document.outerHtml());
    }

    @Test
    public void testParseCdata() {
        html = "<![CDATA[<html><h1>Sample Heading</h1></html>]]>";
        parser = new Parser(html, baseUri, false);
        Document document = parser.parse();

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head></head><body>&lt;html&gt;&lt;h1&gt;Sample Heading&lt;/h1&gt;&lt;/html&gt;</body></html>", document.outerHtml());
    }

    @Test
    public void testParseAttribute() {
        html = "<div id='sample' class=\"example\">Sample div</div>";
        parser = new Parser(html, baseUri, false);
        Document document = parser.parse();

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head></head><body><div id=\"sample\" class=\"example\">Sample div</div></body></html>", document.outerHtml());
    }

    @Test
    public void testStackHasValidParent() {
        html = "<table><tr><td>Sample Data</td></tr></table>";
        parser = new Parser(html, baseUri, false);
        Document document = parser.parse();

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head></head><body><table><tbody><tr><td>Sample Data</td></tr></tbody></table></body></html>", document.outerHtml());
    }

    @Test
    public void testPopStackToSuitableContainer() {
        html = "<div id='sample'><p>Sample Paragraph</div>";
        parser = new Parser(html, baseUri, false);
        Document document = parser.parse();

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head></head><body><div id=\"sample\"><p>Sample Paragraph</p></div></body></html>", document.outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        html = "<div id='sample'><p>Sample Paragraph</p></div></div>";
        parser = new Parser(html, baseUri, false);
        Document document = parser.parse();

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<html><head></head><body><div id=\"sample\"><p>Sample Paragraph</p></div></body></html>", document.outerHtml());
    }

    @Test
    public void testLast() {
        Element element = parser.last();

        assertNotNull(element);
        assertTrue(element instanceof Element);
        assertEquals("http://example.com", element.baseUri());
        assertEquals("<html><head><title>Sample Title</title></head><body><h1>Sample Heading</h1></body></html>", element.outerHtml());
    }

}