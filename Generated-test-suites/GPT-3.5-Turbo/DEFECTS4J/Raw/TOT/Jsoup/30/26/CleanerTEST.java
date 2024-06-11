import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Test typical use case
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>", cleanDocument.html());
    }

    @Test
    public void testClean_MultipleNestedElements() {
        // Test edge case with multiple nested elements
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><div><p><span><b>Test</b></span></p></div></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><div><p><span><b>Test</b></span></p></div></body></html>", cleanDocument.html());
    }

    @Test
    public void testClean_InvalidElement() {
        // Test error scenario with an invalid element
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        // Test typical use case with a valid document
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidElement() {
        // Test error scenario with an invalid element
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Test typical use case with a safe element
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><span>Test</span></body></html>");

        Document clean = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), clean.body());

        assertEquals("<body><span>Test</span></body>", clean.html());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodes_UnsafeElement() {
        // Test edge case with an unsafe element
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");

        Document clean = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), clean.body());

        assertEquals("<body></body>", clean.html());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Test typical use case with a safe element
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = Jsoup.parse("<span>Test</span>").select("span").first();

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals("<span>Test</span>", meta.el.html());
        assertEquals(0, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_UnsafeAttribute() {
        // Test edge case with an unsafe attribute
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = Jsoup.parse("<a href='javascript:alert('XSS')'>Test</a>").select("a").first();

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals("<a>Test</a>", meta.el.html());
        assertEquals(1, meta.numAttribsDiscarded);
    }

}