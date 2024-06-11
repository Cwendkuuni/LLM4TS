package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {
   
    @Test
    public void testClean() {
        // Initialize Cleaner and Whitelist
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        
        // Create dirty document
        String html = "<html><head></head><body><div>This is a dirty document</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        
        // Clean the document
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Assert that the clean document only contains allowed elements
        assertEquals("<html><head></head><body><div>This is a dirty document</div></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        // Initialize Cleaner and Whitelist
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        
        // Create dirty document with no unsafe tags or attributes
        String html = "<html><head></head><body><h1>This is a valid document</h1></body></html>";
        Document validDocument = Jsoup.parse(html);
        
        // Assert that the document is valid
        assertTrue(cleaner.isValid(validDocument));
        
        // Create dirty document with unsafe tags and attributes
        html = "<html><head></head><body><script>alert('XSS')</script></body></html>";
        Document invalidDocument = Jsoup.parse(html);
        
        // Assert that the document is invalid
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCleanWithEnforcedAttributes() {
        // Initialize Cleaner and Whitelist with enforced attributes
        Whitelist whitelist = new Whitelist();
        whitelist.addEnforcedAttribute("a", "href", "https://www.example.com/");
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Create dirty document with unsafe tags and attributes
        String html = "<html><head></head><body><a>This is a link</a></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        
        // Clean the document
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Assert that the clean document only contains allowed elements and enforced attributes
        assertEquals("<html><head></head><body><a href=\"https://www.example.com/\">This is a link</a></body></html>", cleanDocument.html());
    }
}
