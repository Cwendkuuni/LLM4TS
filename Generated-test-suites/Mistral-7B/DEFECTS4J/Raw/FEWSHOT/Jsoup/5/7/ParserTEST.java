package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><div><p>Hello World</p></div></body></html>";
        Document doc = Parser.parse(html, "https://example.com/");

        // Check document structure
        assertEquals("https://example.com/", doc.baseUri());
        assertEquals(htmlTag, doc.childNodes().get(0));
        assertEquals(headTag, doc.childNodes().get(0).childNodes().get(0));
        assertEquals(titleTag, doc.childNodes().get(0).childNodes().get(0).childNodes().get(0));
        assertEquals("Test", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).text());
        assertEquals(bodyTag, doc.childNodes().get(1));
        assertEquals(doc.childNodes().get(1).childNodes().size(), 1);
        assertEquals(divTag, doc.childNodes().get(1).childNodes().get(0));
        assertEquals(pTag, doc.childNodes().get(1).childNodes().get(0).childNodes().get(0));
        assertEquals("Hello World", doc.childNodes().get(1).childNodes().get(0).childNodes().get(0).text());

        // Check attribute handling
        assertEquals("https://example.com/image.png", doc.getElementById("image").attr("src"));

        // Check base tag handling
        String baseUri = "https://example.com/base";
        doc = Parser.parse("<html><base href=\"" + baseUri + "\"><body></body></html>", baseUri);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("https://example.com/base/path", doc.getElementById("id").absUrl("path"));

        // Check self closing tags
        String selfClosingHtml = "<html><body><img src=\"image.png\"/></body></html>";
        Document selfClosingDoc = Parser.parse(selfClosingHtml, "https://example.com/");
        Element img = selfClosingDoc.getElementsByTag("img").get(0);
        assertTrue(img.tag().isSelfClosing());

        // Test relaxation
        String relaxHtml = "<div data-test=\"relaxed\">Hello</div>";
        Document relaxedDoc = Parser.parseBodyFragmentRelaxed(relaxHtml, "https://example.com/");
        Element div = relaxedDoc.getElementsByTag("div").get(0);
        assertFalse(div.tag().isData());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com/");
        assertEquals(bodyTag, doc.childNodes().get(0));
        assertEquals("https://example.com/", doc.baseUri());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyRelaxedHtml = "<div data-test>Hello World</div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyRelaxedHtml, "https://example.com/");
        assertEquals("data-test", doc.getElementsByTag("div").get(0).attr("data-test"));
    }
}