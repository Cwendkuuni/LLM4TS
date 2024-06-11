
package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p>Test</p></body></html>");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><p>Test</p></body></html>", cleanDocument.html());
        assertTrue(cleanDocument.body().hasText());
        assertEquals("<p>Test</p>", cleanDocument.body().html());
    }

    @Test
    public void testClean_EmptyDocument() {
        Document dirtyDocument = Jsoup.parse("");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body></body></html>", cleanDocument.html());
        assertFalse(cleanDocument.body().hasText());
        assertEquals("", cleanDocument.body().html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClean_NullInput() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        cleaner.clean(null);
    }

    @Test
    public void testIsValid_ValidDocument() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p>Test</p></body></html>");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script></body></html>");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValid_NullInput() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        cleaner.isValid(null);
    }
}
