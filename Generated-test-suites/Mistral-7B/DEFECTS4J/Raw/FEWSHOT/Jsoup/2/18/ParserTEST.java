package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element root = doc.body();

        assertEquals(htmlTag, root.tag());
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(headTag, root.child(0).tag());
        assertEquals("Test", root.child(0).child(0).text());
        assertEquals(bodyTag, root.child(1).tag());
        assertEquals(1, root.child(1).child(0).child(0).childCount());
        assertEquals("Hello, World!", root.child(1).child(0).child(0).child(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, Body!</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com");
        Element root = doc;

        assertEquals(htmlTag, root.tag());
        assertEquals("https://example.com", doc.baseUri());
        assertEquals("", doc.head().outerHtml());
        assertEquals(bodyTag, root.child(0).tag());
        assertEquals("Hello, Body!", root.child(0).child(0).text());
    }

    @Test
    public void testParseEmptyDocument() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element root = doc;

        assertEquals(htmlTag, root.tag());
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(headTag, root.child(0).tag());
        assertEquals("", root.child(0).outerHtml());
        assertEquals(bodyTag, root.child(1).tag());
        assertEquals("", root.child(1).outerHtml());
    }

    @Test
    public void testParseWithBaseTag() {
        String html = "<html><head><base href=\"/test/\"><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element root = doc;

        assertEquals(htmlTag, root.tag());
        assertEquals("/test/", doc.baseUri());
        assertEquals("Test", root.child(0).child(0).text());
        assertEquals(bodyTag, root.child(1).tag());
        assertEquals("Hello, World!", root.child(1).child(0).text());
    }

    @Test
    public void testParseWithCdata() {
        String html = "<html><head><title>Test</title></head><body><img src='<![CDATA[http://example.com/image.png]]>'></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element root = doc.body();

        assertEquals(htmlTag, root.tag());
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(headTag, root.child(0).tag());
        assertEquals("Test", root.child(0).child(0).text());
        assertEquals(bodyTag, root.child(1).tag());
        Element img = root.child(1).child(0);
        assertEquals("img", img.tag().name());
        assertEquals("http://example.com/image.png", img.absUrl("src"));
    }

    @Test
    public void testParseWithXmlDecl() {
        String xml = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document doc = Parser.parse(xml, "https://example.com");
        Element root = doc;

        assertEquals(xmlDeclarationTag, root.child(0).tag());
        assertEquals(htmlTag, root.child(1).tag());
        assertEquals("https://example.com", doc.baseUri());
        assertEquals("Test", root.child(1).child(0).child(0).text());
        assertEquals(bodyTag, root.child(1).child(1).tag());
        assertEquals("Hello, World!", root.child(1).child(1).child(0).text());
    }

    @Test
    public void testParseWithScriptAndTextarea() {
        String html = "<html><head><title>Test</title><script src='script.js'></script></head><body><textarea>Hello, Textarea!</textarea></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element root = doc;

        assertEquals(htmlTag, root.tag());
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(headTag, root.child(0).tag());
        assertEquals("Test", root.child(0).child(0).text());
        assertEquals(bodyTag, root.child(1).tag());
        Element script = root.child(0).child(1);
        Element textarea = root.child(1).child(0);
        assertEquals("script", script.tag().name());
        assertEquals("", script.outerHtml());
        assertEquals("textarea", textarea.tag().name());
        assertEquals("Hello, Textarea!", textarea.text());
    }

    @Test
    public void testParseWithImplicitParent() {
        String html = "<img src='image.png'>";
        Document doc = Parser.parse(html, "https://example.com");
        Element root = doc.body();

        assertEquals(htmlTag, root.tag());
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(bodyTag, root.tag());
        Element img = root.child(0);
        assertEquals("img", img.tag().name());
        assertEquals("", img.outerHtml());
    }

    @Test
    public void testParseWithInvalidParent() {
        String html = "<p><a href='https://example.com'>Link</a></p><div>";
        Document doc = Parser.parse(html, "https://example.com");
        Element root = doc.body();

        assertEquals(htmlTag, root.tag());
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(bodyTag, root.tag());
        Element p = root.child(0);
        assertEquals("p", p.tag().name());
        assertEquals("", p.outerHtml());
        Element a = p.child(0);
        assertEquals("a", a.tag().name());
        assertEquals("Link", a.text());
        Element div = root.child(1);
        assertEquals("div", div.tag().name());
        assertEquals("", div.outerHtml());
    }

    @Test
    public void testParseWithBaseTagAndScript() {
        String html = "<html><head><base href=\"/test/\"><title>Test</title><script src='script.js'></script></head><body><h1>Hello, Body!</h1></body></html>";
        Document doc = Parser.parse(html, "https://example.com");
        Element root = doc.body();

        assertEquals(htmlTag, root.tag());
        assertEquals("/test/", doc.baseUri());
        assertEquals("Test", root.child(0).child(0).text());
        assertEquals(bodyTag, root.child(1).tag());
        assertEquals("Hello, Body!", root.child(1).child(0).text());
        Element script = root.child(0).child(1);
        assertEquals("script", script.tag().name());
        assertEquals("https://example.com/test/script.js", script.absUrl("src"));
    }

}