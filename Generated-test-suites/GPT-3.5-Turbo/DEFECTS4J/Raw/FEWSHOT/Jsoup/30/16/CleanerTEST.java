package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic(); // Modify with your desired whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Test with dirtyDocument having body
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><p>Test</p></div>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body><div><p>Test</p></div></body></html>", cleanDocument.toString());

        // Test with dirtyDocument having null body
        dirtyDocument = Jsoup.parseBodyFragment("<div></div>");
        cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body><div></div></body></html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic(); // Modify with your desired whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Test with valid dirtyDocument
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Test</p>");
        assertTrue(cleaner.isValid(dirtyDocument));

        // Test with invalid dirtyDocument
        dirtyDocument = Jsoup.parseBodyFragment("<script>alert('XSS')</script>");
        assertFalse(cleaner.isValid(dirtyDocument));
    }
}