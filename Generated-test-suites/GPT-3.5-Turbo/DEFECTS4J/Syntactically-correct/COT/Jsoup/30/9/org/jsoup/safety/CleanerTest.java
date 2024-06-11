import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><div><p>Test</p></div></body></html>");
        
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        Element body = cleanDocument.body();
        assertEquals("<div><p>Test</p></div>", body.html());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><div><p>Test</p></div></body></html>");
        
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }
    
    // Additional test cases for edge cases and exceptions can be added here
    
    
    // Test cases for edge cases and exceptions
    
    // Test case for null dirtyDocument input in clean method
    @Test(expected = IllegalArgumentException.class)
    public void testCleanNullDocument() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        cleaner.clean(null);
    }
    
    // Test case for null dirtyDocument input in isValid method
    @Test(expected = IllegalArgumentException.class)
    public void testIsValidNullDocument() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        cleaner.isValid(null);
    }
    
    // Test case for empty dirtyDocument input in clean method
    @Test
    public void testCleanEmptyDocument() {
        Document dirtyDocument = Jsoup.parse("");
        
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        Element body = cleanDocument.body();
        assertEquals("", body.html());
    }
    
    // Test case for empty dirtyDocument input in isValid method
    @Test
    public void testIsValidEmptyDocument() {
        Document dirtyDocument = Jsoup.parse("");
        
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }
    
    // Test case for dirtyDocument with frameset in clean method
    @Test
    public void testCleanFramesetDocument() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><frameset></frameset></html>");
        
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        Element body = cleanDocument.body();
        assertEquals("", body.html());
    }
    
    // Test case for dirtyDocument with frameset in isValid method
    @Test
    public void testIsValidFramesetDocument() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><frameset></frameset></html>");
        
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }
    
}