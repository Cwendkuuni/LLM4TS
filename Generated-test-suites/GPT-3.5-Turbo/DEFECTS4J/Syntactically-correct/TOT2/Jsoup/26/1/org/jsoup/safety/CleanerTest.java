import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical use case
        Document dirtyDocument = new Document("<html><body><p>Hello, World!</p></body></html>");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        
        // Edge case - empty document
        Document emptyDocument = new Document("");
        Document cleanEmptyDocument = cleaner.clean(emptyDocument);
        assertNotNull(cleanEmptyDocument);
        
        // Exception handling - null input
        try {
            cleaner.clean(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testIsValid() {
        // Typical use case
        Document dirtyDocument = new Document("<html><body><p>Hello, World!</p></body></html>");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
        
        // Edge case - invalid document
        Document invalidDocument = new Document("<html><body><script>alert('XSS')</script></body></html>");
        boolean isInvalid = cleaner.isValid(invalidDocument);
        assertFalse(isInvalid);
        
        // Exception handling - null input
        try {
            cleaner.isValid(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCopySafeNodes() {
        // Typical use case
        Element sourceElement = new Element("<div><p>Hello, World!</p></div>");
        Element destElement = new Element("<div></div>");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        int discardedElements = cleaner.copySafeNodes(sourceElement, destElement);
        assertEquals(0, discardedElements);
        
        // Edge case - source with unsafe tags
        Element unsafeElement = new Element("<div><script>alert('XSS')</script></div>");
        int discardedUnsafeElements = cleaner.copySafeNodes(unsafeElement, destElement);
        assertEquals(1, discardedUnsafeElements);
        
        // Exception handling - null input
        int discardedNullElements = cleaner.copySafeNodes(null, destElement);
        assertEquals(0, discardedNullElements);
    }

}