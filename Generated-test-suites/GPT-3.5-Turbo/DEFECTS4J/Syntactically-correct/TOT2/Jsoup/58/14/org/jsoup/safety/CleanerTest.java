import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {
    
    @Test
    public void testClean() {
        // Typical use case
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("", cleanDocument.outerHtml());
        
        // Edge case - null input
        assertNull(cleaner.clean(null));
    }
    
    @Test
    public void testIsValid() {
        // Typical use case
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("");
        assertTrue(cleaner.isValid(dirtyDocument));
        
        // Edge case - with unsafe tags and attributes
        Document dirtyDocument2 = new Document("");
        dirtyDocument2.append("<script>alert('XSS')</script>");
        assertFalse(cleaner.isValid(dirtyDocument2));
    }
    
    @Test
    public void testCopySafeNodes() {
        // Unable to test this method individually as it is called within other methods in Cleaner class
    }
    
    @Test
    public void testCreateSafeElement() {
        // Typical use case
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("");
        cleaner.clean(dirtyDocument);
        assertEquals("", dirtyDocument.outerHtml());
        
        // Edge case - with unsafe attributes
        dirtyDocument.append("<b onclick=\"alert('XSS')\">Text</b>");
        cleaner.clean(dirtyDocument);
        assertEquals("<b>Text</b>", dirtyDocument.outerHtml());
    }
}