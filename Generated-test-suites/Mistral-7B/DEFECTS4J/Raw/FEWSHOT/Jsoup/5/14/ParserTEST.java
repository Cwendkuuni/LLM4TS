package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals("http://test.com", doc.baseUri());
        Element body = doc.body();
        Element title = body.getElementsByTag("title").first();
        assertEquals("Test", title.text());
        assertEquals("Hello", body.getElementsByTag("h1").first().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        assertEquals("http://test.com", doc.baseUri());
        Element body = doc.body();
        assertEquals("Hello", body.getElementsByTag("h1").first().text());
        assertNull(doc.head());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<html><body><h1>Hello</h1></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");
        assertEquals("http://test.com", doc.baseUri());
        Element body = doc.body();
        assertEquals("Hello", body.getElementsByTag("h1").first().text());
        assertNull(doc.head());
    }

    @Test
    public void testParseWithXmlDecl() {
        String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals("http://test.com", doc.baseUri());
        Element body = doc.body();
        assertEquals("Hello", body.getElementsByTag("h1").first().text());
        XmlDeclaration xmlDecl = doc.xml();
        assertEquals("http://www.w3.org/1999/xhtml", xmlDecl.xmlns());
    }

    @Test
    public void testParseWithBaseTag() {
        String html = "<html><head><base href=\"/test/\"><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals("http://test.com/test/", doc.baseUri());
        Element body = doc.body();
        assertEquals("Hello", body.getElementsByTag("h1").first().text());
    }

    @Test
    public void testParseWithComment() {
        String html = "<html><!-- This is a comment --><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        assertNotNull(doc.getElementsByTag("!--").first());
    }

    @Test
    public void testParseWithCdata() {
        String html = "<html><body><script><![CDATA[function test() { alert('Hello World'); }]]></script></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element script = doc.getElementsByTag("script").first();
        assertEquals("function test() { alert('Hello World'); }", script.text());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseInvalidHtml() {
        String html = "<html><body><script><!-- function test() { alert('Hello World'); }]]></script></body></html>";
        Parser.parse(html, "http://test.com");
    }
}