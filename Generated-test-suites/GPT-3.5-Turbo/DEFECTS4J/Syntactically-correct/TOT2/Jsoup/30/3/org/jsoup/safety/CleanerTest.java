package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.select.Whitelist;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDoc = new Document("<html><head></head><body><div>Hello</div></body></html>");
        Document cleanDoc = cleaner.clean(dirtyDoc);

        assertEquals("<html><head></head><body><div>Hello</div></body></html>", cleanDoc.toString());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDoc = new Document("<html><head></head><body><div>Hello</div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDoc);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Test for edge cases and exceptions
    }
}