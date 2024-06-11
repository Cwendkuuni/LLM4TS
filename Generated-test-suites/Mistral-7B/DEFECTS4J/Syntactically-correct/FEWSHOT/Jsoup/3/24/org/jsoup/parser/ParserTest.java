package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals(htmlTag, doc.tagName());
        assertEquals(Arrays.asList(htmlTag, headTag, titleTag, bodyTag, h1Tag), getElementTags(doc));
        assertEquals("Test", titleTag().text());
        assertEquals("Hello World", h1Tag().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Fragment Test</h1>";
        Parser parser = new Parser(bodyHtml, null, true);
        Document doc = parser.parse();

        assertEquals(htmlTag, doc.tagName());
        assertEquals(Arrays.asList(htmlTag, bodyTag, h1Tag), getElementTags(doc));
        assertEquals("Fragment Test", h1Tag().text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><base href=\"/test\"><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "/test/", false);
        Document doc = parser.parse();

        assertEquals("http://test/Test.html", doc.url());
        assertEquals(Arrays.asList(htmlTag, headTag, titleTag, bodyTag, h1Tag), getElementTags(doc));
        assertEquals("Test", titleTag().text());
        assertEquals("Hello World", h1Tag().text());
    }

    @Test
    public void testParseEmptyDocument() {
        String html = "<html></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals(htmlTag, doc.tagName());
        assertEquals(Arrays.asList(htmlTag), getElementTags(doc));
        assertTrue(doc.body().getText().isEmpty());
    }

    @Test
    public void testParseWithScriptTag() {
        String html = "<html><head><title>Test</title></head><body><script src=\"test.js\"></script></body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals("test.js", doc.select("script").first().attr("src"));
    }

    @Test
    public void testParseWithImplicitParent() {
        String html = "<body><img src=\"image.jpg\" alt=\"Image\"></body>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals(htmlTag, doc.tagName());
        assertEquals(Arrays.asList(htmlTag, bodyTag, imgTag), getElementTags(doc));
        assertEquals("image.jpg", imgTag().attr("src"));
        assertEquals("Image", imgTag().attr("alt"));
    }

    @Test
    public void testParseWithNestedTags() {
        String html = "<html><head><title>Test</title></head><body>" +
                "<h1>Section 1<h1>" +
                "<p>Paragraph 1<p>" +
                "<p>Paragraph 2<p>" +
                "<h1>Section 2<h1>" +
                "</body></html>";
        Parser parser = new Parser(html, null, false);
        Document doc = parser.parse();

        assertEquals(htmlTag, doc.tagName());
        assertEquals(Arrays.asList(htmlTag, headTag, titleTag, bodyTag, h1Tag, pTag, pTag, h1Tag), getElementTags(doc));
        assertEquals("Test", titleTag().text());
        assertEquals("Section 1", h1Tag(0).text());
        assertEquals("Paragraph 1", pTag(0).text());
        assertEquals("Paragraph 2", pTag(1).text());
        assertEquals("Section 2", h1Tag(1).text());
    }

    private List<Tag> getElementTags(Document doc) {
        return Arrays.asList(doc.body().tag(), doc.body().childTags());
    }

    private Tag titleTag() {
        return doc.head().getElementsByTag("title").first();
    }

    private Tag h1Tag() {
        return doc.select("h1").first();
    }

    private Tag imgTag() {
        return doc.select("img").first();
    }

    private Tag pTag(int index) {
        return doc.select("p").get(index);
    }

    private Document doc;

}