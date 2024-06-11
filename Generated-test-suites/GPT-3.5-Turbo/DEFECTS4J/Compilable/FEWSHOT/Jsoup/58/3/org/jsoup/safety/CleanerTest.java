package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Create a test document
        Document dirtyDocument = new Document("<html><head><title>Test</title></head><body><h1>Test</h1><p>Hello World!</p></body></html>");
        
        Whitelist whitelist = Whitelist.relaxed(); // Use a relaxed whitelist for testing
        
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Check if the cleaned document only contains allowed elements
        assertEquals("<html><head></head><body><h1>Test</h1><p>Hello World!</p></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        // Create a test document
        Document dirtyDocument = new Document("<html><head><title>Test</title></head><body><h1>Test</h1><p>Hello World!</p></body></html>");
        
        Whitelist whitelist = Whitelist.relaxed(); // Use a relaxed whitelist for testing
        
        Cleaner cleaner = new Cleaner(whitelist);
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        // Check if the document is considered valid
        assertTrue(isValid);
    }

}