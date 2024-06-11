import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import static org.junit.Assert.*;

public class CleanerTest {
    private Document dirtyDocument;
    
    @Before
    public void setUp() {
        dirtyDocument = Jsoup.parse("<html><head></head><body><p>Hello World!</p></body></html>");
    }
    
    @Test
    public void testClean() {
        Clean whitelistCleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = whitelistCleaner.clean(dirtyDocument);
        
        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><p>Hello World!</p></body></html>", cleanDocument.toString());
    }
    
    @Test
    public void testClean_EmptyDocument() {
        dirtyDocument = Jsoup.parse("");
        
        Clean whitelistCleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = whitelistCleaner.clean(dirtyDocument);
        
        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body></body></html>", cleanDocument.toString());
    }
    
    @Test
    public void testIsValid() {
        Clean whitelistCleaner = new Cleaner(Whitelist.basic());
        
        boolean valid = whitelistCleaner.isValid(dirtyDocument);
        assertTrue(valid);
    }
    
    @Test
    public void testIsValid_InvalidDocument() {
        dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script></body></html>");
        
        Clean whitelistCleaner = new Cleaner(Whitelist.basic());
        
        boolean valid = whitelistCleaner.isValid(dirtyDocument);
        assertFalse(valid);
    }
    
    @Test
    public void testCopySafeNodes() {
        Clean whitelistCleaner = new Cleaner(Whitelist.basic());
        
        int numDiscarded = whitelistCleaner.copySafeNodes(dirtyDocument.body(), dirtyDocument.body());
        
        assertEquals(0, numDiscarded);
    }
    
    @Test
    public void testCopySafeNodes_InvalidElement() {
        dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script></body></html>");
        
        Clean whitelistCleaner = new Cleaner(Whitelist.basic());
        
        int numDiscarded = whitelistCleaner.copySafeNodes(dirtyDocument.body(), dirtyDocument.body());
        
        assertEquals(1, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        Element element = Jsoup.parseBodyFragment("<p class='test' id='testId'>Hello World!</p>").body().child(0);
        
        Clean whitelistCleaner = new Cleaner(Whitelist.basic());
        
        Cleaner.ElementMeta elementMeta = whitelistCleaner.createSafeElement(element);
        
        assertEquals("<p>Hello World!</p>", elementMeta.el.outerHtml());
        assertEquals(2, elementMeta.numAttribsDiscarded);
    }
    
    @Test
    public void testCreateSafeElement_EnforcedAttributes() {
        Element element = Jsoup.parseBodyFragment("<a href='http://example.com'>Link</a>").body().child(0);
        
        Clean whitelistCleaner = new Cleaner(Whitelist.basicWithEnforcedAttributes());
        
        Cleaner.ElementMeta elementMeta = whitelistCleaner.createSafeElement(element);
        
        assertEquals("<a href=\"http://example.com\" rel=\"nofollow\">Link</a>", elementMeta.el.outerHtml());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}