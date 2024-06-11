package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>foo</p><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("foo", cleanDocument.body().text());
        assertTrue(cleanDocument.body().getElementsByTag("script").isEmpty());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>foo</p></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));

        Document invalidDocument = Jsoup.parse("<html><body><p>foo</p><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodesWithAllowedTag() {
        Whitelist whitelist = new Whitelist.SimpleTagBasedWhitelist("p");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>foo</p></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, cleanDocument.body().childNodeSize());
        assertTrue(cleanDocument.body().get(0) instanceof Element);
        assertEquals("foo", cleanDocument.body().get(0).text());
    }

    @Test
    public void testCopySafeNodesWithDisallowedTag() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>foo</p><script>alert('XSS')</script></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, cleanDocument.body().childNodeSize());
        assertTrue(cleanDocument.body().get(0) instanceof TextNode);
        assertEquals("foo", cleanDocument.body().get(0).text());
    }

    @Test
    public void testCopySafeNodesWithAllowedAttribute() {
        Whitelist whitelist = new Whitelist.SimpleAttributeBasedWhitelist("class", "custom-class");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p class=\"custom-class\">foo</p></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, cleanDocument.body().childNodeSize());
        assertTrue(cleanDocument.body().get(0) instanceof Element);
        assertEquals("foo", cleanDocument.body().get(0).text());
        assertEquals("custom-class", cleanDocument.body().get(0).classes().get(0));
    }

    @Test
    public void testCopySafeNodesWithDisallowedAttribute() {
        Whitelist whitelist = new Whitelist.SimpleAttributeBasedWhitelist("class");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p class=\"custom-class\">foo</p></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, cleanDocument.body().childNodeSize());
        assertTrue(cleanDocument.body().get(0) instanceof TextNode);
        assertEquals("foo", cleanDocument.body().get(0).text());
        assertTrue(cleanDocument.body().get(0).attributes().isEmpty());
    }

    @Test
    public void testCopySafeNodesWithAllowedTextNode() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>foo</p></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, cleanDocument.body().childNodeSize());
        assertTrue(cleanDocument.body().get(0) instanceof TextNode);
        assertEquals("foo", cleanDocument.body().get(0).text());
    }

    @Test
    public void testCopySafeNodesWithDisallowedTextNode() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>foo<script>alert('XSS')</script></p></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, cleanDocument.body().childNodeSize());
        assertTrue(cleanDocument.body().get(0) instanceof TextNode);
        assertEquals("", cleanDocument.body().get(0).text());
    }
}