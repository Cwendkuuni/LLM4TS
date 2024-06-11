package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Title</title></head><body><h1>Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.body().childElementCount());
        assertEquals("Heading", doc.body().firstElement().text());
        assertEquals("Title", doc.title().text());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Heading</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.body().childElementCount());
        assertEquals("Heading", doc.body().firstElement().text());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(0, doc.body().childElementCount());
        assertEquals("", doc.title().text());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseHtmlWithScriptAndTextarea() {
        String html = "<html><head><title>Title</title></head><body>" +
                "<script type=\"text/javascript\">console.log('Hello World');</script>" +
                "<textarea name=\"textarea\">Hello World</textarea></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.body().childElementCount());

        Element script = doc.body().get(0).child(0);
        assertEquals("script", script.tag().name());
        assertEquals("text/javascript", script.tag().attr("type"));

        Element textarea = doc.body().get(0).child(1);
        assertEquals("textarea", textarea.tag().name());
        assertEquals("Hello World", textarea.text());

        assertEquals("Title", doc.title().text());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseHtmlWithBaseTag() {
        String html = "<html><head></head><body><base href=\"/foo\"><h1>Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals(htmlTag, doc.rootElement().tag());
        assertEquals(1, doc.body().childElementCount());
        assertEquals("Heading", doc.body().firstElement().text());
        assertEquals("/foo", doc.baseUri());
    }

    @Test
    public void testParseHtmlWithImplicitParents() {
        String html = "<html><body><img src=\"image.png\" alt=\"Image\"/></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element img = doc.body().firstElement();
        assertEquals("img", img.tag().name());
        assertEquals(1, img.childElementCount());
        Element div = img.child(0);
        assertEquals("div", div.tag().name());

        assertEquals("http://example.com", doc.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithMissingTag() {
        String html = "<html><body>";
        Parser.parse(html, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithMissingBaseUri() {
        String html = "<html><body></html>";
        Parser.parse(html, null);
    }
}