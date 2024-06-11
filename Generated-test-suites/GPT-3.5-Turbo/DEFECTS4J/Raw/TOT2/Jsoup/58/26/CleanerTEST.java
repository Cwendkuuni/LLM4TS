import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        // Typical use case
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p>Hello, <span>world</span></p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body><p>Hello, <span>world</span></p></body></html>", cleanDocument.outerHtml());

        // Edge case: Empty body in dirty document
        Document dirtyDocumentEmptyBody = Jsoup.parse("<html><head></head><body></body></html>");
        Document cleanDocumentEmptyBody = cleaner.clean(dirtyDocumentEmptyBody);
        assertEquals("<html><head></head><body></body></html>", cleanDocumentEmptyBody.outerHtml());
    }

    @Test
    public void testIsValid() {
        // Typical use case
        Document validDocument = Jsoup.parse("<html><head></head><body><p>Hello, world</p></body></html>");
        assertTrue(cleaner.isValid(validDocument));

        // Edge case: Invalid document with disallowed tags
        Document invalidDocument = Jsoup.parse("<html><head><script>alert('XSS')</script></head><body><p>Hello, world</p></body></html>");
        assertFalse(cleaner.isValid(invalidDocument));
    }

}