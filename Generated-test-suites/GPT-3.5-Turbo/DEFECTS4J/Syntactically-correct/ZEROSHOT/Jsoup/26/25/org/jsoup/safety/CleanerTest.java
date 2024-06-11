package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><div>Hello World!</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("<html>\n <head></head>\n <body>\n  <div>Hello World!</div>\n </body>\n</html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValidWithValidDocument() {
        Document validDocument = Jsoup.parse("<html><head></head><body><div>Hello World!</div></body></html>");
        assertTrue(cleaner.isValid(validDocument));
    }

    @Test
    public void testIsValidWithInvalidDocument() {
        Document invalidDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><div>Hello <script>alert('XSS')</script> World!</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("<html>\n <head></head>\n <body>\n  <div>Hello  World!</div>\n </body>\n</html>", cleanDocument.outerHtml());
    }

    @Test
    public void testCreateSafeElement() {
        ElementMeta meta = cleaner.createSafeElement(Jsoup.parse("<div class=\"container\"><script>alert('XSS')</script></div>").body());
        assertNotNull(meta);
        assertEquals("<div class=\"container\"></div>", meta.el.outerHtml());
        assertEquals(1, meta.numAttribsDiscarded);
    }
}