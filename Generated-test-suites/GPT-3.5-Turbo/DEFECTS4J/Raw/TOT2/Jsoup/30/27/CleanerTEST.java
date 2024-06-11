import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    @Test
    public void testCleanMethod() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = new Document("http://example.com");
        dirtyDocument.body().append("<p>Hello, <span>world!</span></p>");
        
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("http://example.com", cleanedDocument.baseUri());
        assertEquals("<body><p>Hello, <span>world!</span></p></body>", cleanedDocument.body().outerHtml());
    }

    @Test
    public void testIsValidMethod() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = new Document("http://example.com");
        dirtyDocument.body().append("<p>Hello, <script>alert('XSS');</script></p>");
        
        assertFalse(cleaner.isValid(dirtyDocument));
        
        dirtyDocument.body().getElementsByTag("script").remove();
        
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodesMethod() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = new Document("http://example.com");
        dirtyDocument.body().append("<p>Hello, <span>world!</span></p>");
        
        Document cleanedDocument = new Document("http://example.com");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanedDocument.body());
        
        assertEquals(0, numDiscarded);
        assertEquals("<body><p>Hello, <span>world!</span></p></body>", cleanedDocument.body().outerHtml());
    }

    @Test
    public void testCreateSafeElementMethod() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        String sourceHtml = "<p class=\"important\">Hello, world!</p>";
        
        Document dirtyDocument = new Document("http://example.com");
        dirtyDocument.body().append(sourceHtml);
        Element sourceElement = dirtyDocument.body().getElementsByTag("p").first();
        
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        
        assertEquals("<p>Hello, world!</p>", elementMeta.el.outerHtml());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

}