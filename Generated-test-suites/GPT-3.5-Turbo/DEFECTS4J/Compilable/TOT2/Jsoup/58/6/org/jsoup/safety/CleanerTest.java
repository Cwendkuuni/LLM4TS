package org.jsoup.safety;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        
        Document dirtyDocument = new Document("");
        dirtyDocument.body().append("<div>Hello World!</div>");
        
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        
        assertNotNull(cleanedDocument);
        assertEquals("<div>Hello World!</div>", cleanedDocument.body().html());
    }

    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        
        Document dirtyDocument = new Document("");
        dirtyDocument.body().append("<script>alert('XSS attack');</script>");
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse(isValid);
    }

    @Test
    public void testEdgeCases() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Edge case where body is null
        Document dirtyDocument = new Document("");
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        assertNull(cleanedDocument.body());

        // Edge case with empty input
        dirtyDocument = new Document("");
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testErrorScenarios() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Error scenario where invalid tag is present
        Document dirtyDocument = new Document("");
        dirtyDocument.body().append("<invalidTag>Hello</invalidTag>");
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanedDocument);
        assertEquals("", cleanedDocument.body().html());
    }
}