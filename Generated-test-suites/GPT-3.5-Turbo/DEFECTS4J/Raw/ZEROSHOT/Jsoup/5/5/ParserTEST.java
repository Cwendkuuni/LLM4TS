package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertEquals(1, body.children().size());
        Element h1 = body.child(0);
        assertNotNull(h1);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello, World!", h1.text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertEquals(1, body.children().size());
        Element h1 = body.child(0);
        assertNotNull(h1);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello, World!", h1.text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.children().size());
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertEquals(1, body.children().size());
        Element h1 = body.child(0);
        assertNotNull(h1);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello, World!", h1.text());
    }
}