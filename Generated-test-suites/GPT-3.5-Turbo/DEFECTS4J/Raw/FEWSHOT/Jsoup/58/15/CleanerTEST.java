package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<html><head><title>Dirty HTML</title></head><body><h1>Hello</h1><script>alert('xss');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(cleanDocument.title(), "");
        assertEquals(cleanDocument.select("h1").text(), "Hello");
        assertTrue(cleanDocument.select("script").isEmpty());
    }

    @Test
    public void testIsValid_validDocument_shouldReturnTrue() {
        String validHtml = "<html><head></head><body><h1>Hello</h1></body></html>";
        Document validDocument = Jsoup.parse(validHtml);

        boolean isValid = cleaner.isValid(validDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_invalidDocument_shouldReturnFalse() {
        String invalidHtml = "<html><head></head><body><h1>Hello</h1><script>alert('xss');</script></body></html>";
        Document invalidDocument = Jsoup.parse(invalidHtml);

        boolean isValid = cleaner.isValid(invalidDocument);

        assertFalse(isValid);
    }
    
    @Test
    public void testCopySafeNodes_safeElement_shouldCopyElement() {
        String html = "<div><h1>Hello</h1></div>";
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div");
        Element source = elements.first();
        Element dest = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(numDiscarded, 0);
        assertTrue(dest.hasText());
        assertEquals(dest.text(), "Hello");
    }

    @Test
    public void testCopySafeNodes_unsafeElement_shouldDiscardElement() {
        String html = "<div><script>alert('xss');</script></div>";
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div");
        Element source = elements.first();
        Element dest = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(numDiscarded, 1);
        assertFalse(dest.hasText());
    }

    @Test
    public void testCopySafeNodes_safeAttribute_shouldCopyAttribute() {
        String html = "<div id='myId'></div>";
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div");
        Element source = elements.first();
        Attributes destAttrs = new Attributes();
        Element dest = new Element(Tag.valueOf("div"), "", destAttrs);

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(numDiscarded, 0);
        assertEquals(dest.attr("id"), "myId");
    }

    @Test
    public void testCopySafeNodes_unsafeAttribute_shouldDiscardAttribute() {
        String html = "<div onclick='alert(\"xss\");'></div>";
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div");
        Element source = elements.first();
        Attributes destAttrs = new Attributes();
        Element dest = new Element(Tag.valueOf("div"), "", destAttrs);

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(numDiscarded, 1);
        assertEquals(destAttrs.size(), 0);
    }

    @Test
    public void testCleanerConstructor_nullWhitelist_shouldThrowException() {
        try {
            Cleaner cleaner = new Cleaner(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), "Whitelist must not be null");
        }
    }
}