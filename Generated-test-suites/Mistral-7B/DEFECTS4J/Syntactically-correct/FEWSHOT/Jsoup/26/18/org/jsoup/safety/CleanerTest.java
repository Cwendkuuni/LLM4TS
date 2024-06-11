package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    @Test
    public void testCleaner() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<body><h1>foo</h1><script>alert('xss')</script></body>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<body><h1>foo</h1></body>", cleanDocument.html());
        assertTrue(cleanDocument.body().hasText());
        assertFalse(cleanDocument.body().hasChild( dirtyDocument.body().getElementsByTag("script").get(0) ));
    }

    @Test
    public void testCleanerWithBasicWhitelist() {
        Whitelist whitelist = Whitelist.relaxedModel();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<body><h1>foo</h1><p>bar</p></body>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<body><h1>foo</h1><p>bar</p></body>", cleanDocument.html());
        assertTrue(cleanDocument.body().hasText());
        assertTrue(cleanDocument.body().getElementsByTag("h1").size() > 0);
        assertTrue(cleanDocument.body().getElementsByTag("p").size() > 0);
    }

    @Test
    public void testCleanerWithCustomWhitelist() {
        Whitelist whitelist = Whitelist.custom()
                .addTags("div", "p")
                .addAttributes("div", "class")
                .build();

        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<body><div class='foo'><h1>foo</h1><p>bar</p></div></body>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<body><div class='foo'><p></p></div></body>", cleanDocument.html());
        assertTrue(cleanDocument.body().hasText());
        assertTrue(cleanDocument.body().getElementsByTag("div").size() > 0);
        assertTrue(cleanDocument.body().getElementsByTag("p").size() > 0);
        assertTrue(cleanDocument.body().getElementsByClass("foo").size() > 0);
    }

    @Test
    public void testIsValidWithValidDocument() {
        Whitelist whitelist = Whitelist.relaxedModel();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<body><h1>foo</h1><p>bar</p></body>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidWithInvalidDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<body><h1>foo</h1><script>alert('xss')</script></body>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testIsValidWithEmptyDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse(" ");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<body><h1>foo<script>alert('xss')</script></h1></body>");
        Element dest = new Element(Tag.BODY);
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);

        assertEquals(1, numDiscarded);
        assertTrue(dest.hasText());
        assertFalse(dest.hasChild( dirtyDocument.body().getElementsByTag("script").get(0) ));
    }

    @Test
    public void testCopySafeNodesWithBasicWhitelist() {
        Whitelist whitelist = Whitelist.relaxedModel();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<body><h1>foo</h1><p>bar</p></body>");
        Element dest = new Element(Tag.BODY);
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);

        assertEquals(0, numDiscarded);
        assertTrue(dest.hasText());
        assertTrue(dest.getElementsByTag("h1").size() > 0);
        assertTrue(dest.getElementsByTag("p").size() > 0);
    }

    @Test
    public void testCopySafeNodesWithCustomWhitelist() {
        Whitelist whitelist = Whitelist.custom()
                .addTags("div", "p")
                .addAttributes("div", "class")
                .build();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<body><div class='foo'><h1>foo</h1><p>bar</p></div></body>");
        Element dest = new Element(Tag.BODY);
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);

        assertEquals(0, numDiscarded);
        assertTrue(dest.hasText());
        assertTrue(dest.getElementsByTag("div").size() > 0);
        assertTrue(dest.getElementsByTag("p").size() > 0);
        assertTrue(dest.getElementsByClass("foo").size() > 0);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element("div");
        sourceEl.attr("id", "foo");
        sourceEl.attr("style", "color: red;");

        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;

        assertEquals("div", dest.tagName());
        assertNull(dest.attr("id"));
        assertNull(dest.attr("style"));
        assertEquals(0, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElementWithComplexAttributes() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element("div");
        sourceEl.attr("id", "foo");
        sourceEl.attr("class", "bar baz");
        sourceEl.attr("style", "color: red; background-color: blue;");

        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;

        assertEquals("div", dest.tagName());
        assertNull(dest.attr("id"));
        assertEquals("bar baz", dest.attr("class"));
        assertNull(dest.attr("style"));
        assertEquals(2, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElementWithEnforcedAttributes() {
        Whitelist whitelist = Whitelist.simpleTextOnly()
                .addTagAttribute("div", "class");

        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element("div");
        sourceEl.attr("id", "foo");
        sourceEl.attr("class", "bar baz");

        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;

        assertEquals("div", dest.tagName());
        assertEquals("bar baz", dest.attr("class"));
        assertNull(dest.attr("id"));
        assertEquals(0, meta.numAttribsDiscarded);
    }
}