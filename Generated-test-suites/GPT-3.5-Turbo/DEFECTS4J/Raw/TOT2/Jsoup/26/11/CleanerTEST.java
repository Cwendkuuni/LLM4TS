import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendChild("<p>This is a <strong>dirty</strong> document.</p>");
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertNotNull(cleanDocument);
        assertEquals("<p>This is a <strong>dirty</strong> document.</p>", cleanDocument.body().html());
    }
    
    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendChild("<p>This is a <strong>dirty</strong> document.</p>");
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse(isValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        
        // Add test cases for copySafeNodes method
    }
    
    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        
        // Add test cases for createSafeElement method
    }

}