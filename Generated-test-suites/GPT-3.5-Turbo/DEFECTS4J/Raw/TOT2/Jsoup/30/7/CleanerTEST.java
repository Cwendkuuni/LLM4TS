import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("http://example.com");
        dirtyDocument.body().append("<div><p>Test</p></div>");
        
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        
        assertNotNull(cleanedDocument);
        assertEquals("http://example.com", cleanedDocument.baseUri());
        assertEquals("<body><div><p>Test</p></div></body>", cleanedDocument.body().html());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("http://example.com");
        dirtyDocument.body().append("<div><script>alert('XSS')</script></div>");
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("http://example.com");
        dirtyDocument.body().append("<div><p>Test</p></div>");
        
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("<div><p>Test</p></div>", cleanedDocument.body().html());
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("http://example.com");
        dirtyDocument.body().append("<div style=\"color:red\"></div>");
        
        cleaner.clean(dirtyDocument);
        
        assertEquals("div", dirtyDocument.body().child(0).tagName());
        assertEquals(1, dirtyDocument.body().child(0).attributes().size());
    }

}