import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = Jsoup.parse("<html><head></head><body><h1>Hello World!</h1></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Check if the output document has only the body tag
        assertEquals(1, cleanDocument.getElementsByTag("body").size());
        
        // Check if the content of the body tag is preserved
        assertEquals("<h1>Hello World!</h1>", cleanDocument.getElementsByTag("body").html());
    }

    @Test
    public void testisValid() {
        assertTrue(cleaner.isValid(dirtyDocument));
        
        // Create dirty documents with unsafe tags and attributes
        Document dirtyDocument2 = Jsoup.parse("<html><head></head><body><script>alert('XSS');</script></body></html>");
        Document dirtyDocument3 = Jsoup.parse("<html><head></head><body><img src='http://example.com/image.png' onerror='alert('XSS');'/></body></html>");
        
        // Check if isValid returns false for dirty documents with unsafe tags and attributes
        assertFalse(cleaner.isValid(dirtyDocument2));
        assertFalse(cleaner.isValid(dirtyDocument3));
    }

    @Test
    public void testCopySafeNodes() {
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        Element rootElement = dirtyDocument.body();
        Element destinationElement = cleanDocument.body();

        int numDiscarded = cleaner.copySafeNodes(rootElement, destinationElement);
        
        // Check if all safe elements and attributes are copied correctly
        assertEquals("<h1>Hello World!</h1>", destinationElement.html());
        assertEquals(0, numDiscarded);
        
        // Create a dirty document with an unsafe tag
        Document dirtyDocument4 = Jsoup.parse("<html><head></head><body><script>alert('XSS');</script></body></html>");
        Element rootElement2 = dirtyDocument4.body();

        cleanDocument.body().empty(); // Clear the clean document
        numDiscarded = cleaner.copySafeNodes(rootElement2, cleanDocument.body());
        
        // Check if unsafe tags are discarded correctly
        assertEquals("", cleanDocument.body().html());
        assertEquals(1, numDiscarded);
    }
}