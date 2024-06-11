
import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {
    
    @Test
    public void testClean() {
        // Create a cleaner object
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        
        // Create a dirty document
        Document dirtyDocument = Jsoup.parse("<html><body><div>Hello <b>world</b>!</div></body></html>");
        
        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Check if the cleaned document contains only allowed tags
        assertFalse(Jsoup.isValid(cleanDocument.html(), Whitelist.none()));
    }
    
    @Test
    public void testIsValid() {
        // Create a cleaner object
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        
        // Create a dirty document
        Document dirtyDocument = Jsoup.parse("<html><body><div>Hello <b>world</b>!</div></body></html>");
        
        // Check if the dirty document is valid
        assertTrue(cleaner.isValid(dirtyDocument));
    }
    
    @Test
    public void testCleanExceptionHandling() {
        // Create a cleaner object
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        
        // Create a null dirty document
        Document dirtyDocument = null;
        
        // Clean the dirty document
        try {
            Document cleanDocument = cleaner.clean(dirtyDocument);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}
