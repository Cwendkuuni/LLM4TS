
package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        // Create a new whitelist
        whitelist = Whitelist.basic();

        // Add allowed tags to the whitelist
        whitelist.addTags("p", "a");

        // Add allowed attributes to the whitelist
        whitelist.addAttributes("a", "href");

        // Create a new cleaner with the whitelist
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        // Create a dirty document
        String dirtyHtml = "<html><head></head><body><p>Test</p><script>alert('Attacker!')</script></body></html>";
        Document dirtyDocument = Parser.parseBodyFragment(dirtyHtml, "");

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Ensure that only allowed elements are present in the clean document
        assertEquals("<html><head></head><body><p>Test</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        // Create a dirty document with allowed tags and attributes
        String dirtyHtml1 = "<html><head></head><body><p>Test</p><a href=\"http://example.com\">Link</a></body></html>";
        Document dirtyDocument1 = Parser.parseBodyFragment(dirtyHtml1, "");

        // Ensure that the dirty document is valid
        assertTrue(cleaner.isValid(dirtyDocument1));

        // Create a dirty document with an invalid attribute
        String dirtyHtml2 = "<html><head></head><body><p>Test</p><a target=\"_blank\">Link</a></body></html>";
        Document dirtyDocument2 = Parser.parseBodyFragment(dirtyHtml2, "");

        // Ensure that the dirty document is invalid
        assertFalse(cleaner.isValid(dirtyDocument2));

        // Create a dirty document with an invalid tag
        String dirtyHtml3 = "<html><head></head><body><p>Test</p><div>Content</div></body></html>";
        Document dirtyDocument3 = Parser.parseBodyFragment(dirtyHtml3, "");

        // Ensure that the dirty document is invalid
        assertFalse(cleaner.isValid(dirtyDocument3));
    }
}

