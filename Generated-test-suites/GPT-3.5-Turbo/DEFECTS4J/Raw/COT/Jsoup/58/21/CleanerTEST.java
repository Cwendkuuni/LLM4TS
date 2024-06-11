
import static org.junit.Assert.*;

import org.junit.Test;

public class CleanerTest {
    
    @Test
    public void testClean() {
        // Test case 1: Test cleaning a document with no unsafe elements
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = Jsoup.parse("<html><body><div><p>Safe text</p></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals(cleanDocument.body().html(), "<div><p>Safe text</p></div>");
    }
    
    @Test
    public void testIsValid() {
        // Test case 1: Test if a document with no unsafe elements is considered valid
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = Jsoup.parse("<html><body><div><p>Safe text</p></div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }
}
