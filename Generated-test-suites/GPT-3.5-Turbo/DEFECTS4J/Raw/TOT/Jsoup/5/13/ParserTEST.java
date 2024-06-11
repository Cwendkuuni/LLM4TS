package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private String html;
    private String baseUri;

    @Before
    public void setUp() {
        html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>";
        baseUri = "https://www.example.com/";
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test Title", doc.title());
        assertEquals("Test Heading", doc.select("h1").text());
        assertEquals("Test Paragraph", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test Heading</h1><p>Test Paragraph</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Test Heading", doc.select("h1").text());
        assertEquals("Test Paragraph", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Test Heading</h1><p>Test Paragraph";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Test Heading", doc.select("h1").text());
        assertEquals("Test Paragraph", doc.select("p").text());
    }

    @Test
    public void testParse_EmptyHTML_ReturnsEmptyDocument() {
        String emptyHtml = "";
        Document doc = Parser.parse(emptyHtml, baseUri);
        assertNotNull(doc);
        assertTrue(doc.text().isEmpty());
    }

    @Test
    public void testParseBodyFragment_EmptyBodyHtml_ReturnsEmptyBody() {
        String emptyBodyHtml = "";
        Document doc = Parser.parseBodyFragment(emptyBodyHtml, baseUri);
        assertNotNull(doc);
        assertTrue(doc.body().children().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_NullHtml_ThrowsIllegalArgumentException() {
        String nullHtml = null;
        Parser.parse(nullHtml, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragment_NullBodyHtml_ThrowsIllegalArgumentException() {
        String nullBodyHtml = null;
        Parser.parseBodyFragment(nullBodyHtml, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentRelaxed_NullBodyHtml_ThrowsIllegalArgumentException() {
        String nullBodyHtml = null;
        Parser.parseBodyFragmentRelaxed(nullBodyHtml, baseUri);
    }

    @Test
    public void testParse_MultipleStartTags_ReturnsValidDocument() {
        String html = "<html><head><title>Test Title</title></head><head><title>Test Second Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test Second Title", doc.title());
        assertEquals("Test Heading", doc.select("h1").text());
        assertEquals("Test Paragraph", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragment_MultipleStartTags_ReturnsValidDocumentWithMultipleHeaders() {
        String bodyHtml = "<h1>Test Heading</h1><h1>Test Second Heading</h1><p>Test Paragraph</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Test Second Heading", doc.select("h1").last().text());
        assertEquals("Test Paragraph", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed_MultipleStartTags_ReturnsValidDocumentWithMultipleHeaders() {
        String bodyHtml = "<h1>Test Heading</h1><h1>Test Second Heading</h1><p>Test Paragraph";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Test Second Heading", doc.select("h1").last().text());
        assertEquals("Test Paragraph", doc.select("p").text());
    }

    @Test
    public void testParse_CommentTag_ReturnsValidDocument() {
        String htmlWithComment = "<html><!-- Test Comment --><head><title>Test Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>";
        Document doc = Parser.parse(htmlWithComment, baseUri);
        assertNotNull(doc);
        assertEquals("Test Title", doc.title());
        assertEquals("Test Heading", doc.select("h1").text());
        assertEquals("Test Paragraph", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragment_CommentTag_IgnoresCommentTag() {
        String bodyHtmlWithComment = "<h1>Test Heading</h1><!-- Test Comment --><p>Test Paragraph</p>";
        Document doc = Parser.parseBodyFragment(bodyHtmlWithComment, baseUri);
        assertNotNull(doc);
        assertEquals("Test Heading", doc.select("h1").text());
        assertEquals("Test Paragraph", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed_CommentTag_IgnoresCommentTag() {
        String bodyHtmlWithComment = "<h1>Test Heading</h1><!-- Test Comment --><p>Test Paragraph";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtmlWithComment, baseUri);
        assertNotNull(doc);
        assertEquals("Test Heading", doc.select("h1").text());
        assertEquals("Test Paragraph", doc.select("p").text());
    }
}