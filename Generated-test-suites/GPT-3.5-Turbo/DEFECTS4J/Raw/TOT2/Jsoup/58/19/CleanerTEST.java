import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical use case
        Document dirtyDocument = new Document("<html><body><p>Dirty document</p></body></html>");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("Clean document should only contain safe elements", "<html><head></head><body><p>Dirty document</p></body></html>", cleanDocument.toString());
        
        // Edge case: empty document
        dirtyDocument = new Document("");
        cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("Cleaning empty document should result in empty document", "<html><head></head><body></body></html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        // Typical use case
        Document dirtyDocument = new Document("<html><body><p>Valid document</p></body></html>");
        Cleaner cleaner = new Cleaner(Whitelist.simpleText());
        boolean valid = cleaner.isValid(dirtyDocument);
        assertTrue("Valid document should return true", valid);
        
        // Edge case: invalid document with unsafe tag
        dirtyDocument = new Document("<html><body><script>alert('XSS')</script></body></html>");
        valid = cleaner.isValid(dirtyDocument);
        assertFalse("Invalid document with unsafe tag should return false", valid);
    }
}