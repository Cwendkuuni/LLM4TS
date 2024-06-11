import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;
    private Document cleanDocument;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = Jsoup.parse("<html><head></head><body><div><h1>Hello</h1></div></body></html>");
        cleanDocument = cleaner.clean(dirtyDocument);
    }

    @Test
    public void testClean() {
        assertNotNull(cleanDocument);
        
        // Test if the input document is not modified
        assertEquals("<html><head></head><body><div><h1>Hello</h1></div></body></html>", dirtyDocument.html());
        
        // Test if only allowed elements are present in the clean document
        assertEquals("<html><head></head><body><div><h1>Hello</h1></div></body></html>", cleanDocument.html());
        
        // Test if the clean document does not contain any unsafe tags
        assertTrue(cleaner.isValid(cleanDocument));
    }

    @Test
    public void testIsValid() {
        assertFalse(cleaner.isValid(dirtyDocument));

        // Test with a document containing only allowed elements
        Document safeDocument = Jsoup.parse("<html><head></head><body><div><h1>Hello</h1></div></body></html>");
        assertTrue(cleaner.isValid(safeDocument));

        // Test with a document containing unsafe tags
        Document unsafeDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS');</script></body></html>");
        assertFalse(cleaner.isValid(unsafeDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Document clean = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), clean.body());

        // Test if the number of discarded elements is correct
        assertEquals(0, numDiscarded);

        // Test if the clean document contains only safe nodes
        assertTrue(cleaner.isValid(clean));
    }

    @Test
    public void testCreateSafeElement() {
        ElementMeta meta = cleaner.createSafeElement(dirtyDocument.body().child(0));

        // Test if the created safe element has the correct tag
        assertEquals("div", meta.el.tagName());

        // Test if the discarded attribute count is correct
        assertEquals(0, meta.numAttribsDiscarded);

        // Test if the created safe element has the enforced attributes
        assertNotNull(meta.el.attributes().get("class"));
        assertNotNull(meta.el.attributes().get("id"));
    }

    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Test if the cleaner is created with the correct whitelist
        assertEquals(whitelist, cleaner.whitelist);
    }
}