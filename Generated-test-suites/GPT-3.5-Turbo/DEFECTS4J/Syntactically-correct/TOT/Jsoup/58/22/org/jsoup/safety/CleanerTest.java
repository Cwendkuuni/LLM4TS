import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<html><head><title>Dirty HTML</title></head><body><h1>Hello, World!</h1><script>alert('XSS Attack');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Check if only allowed elements are present
        Element body = cleanDocument.body();
        assertTrue(body.select("h1").size() == 1); // Typical use case
        assertTrue(body.select("script").isEmpty()); // Edge case: No script tags allowed
    }

    @Test
    public void testIsValid() {
        String validHtml = "<html><head><title>Valid HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        Document validDocument = Jsoup.parse(validHtml);

        boolean isValid = cleaner.isValid(validDocument);

        assertTrue(isValid); // Typical use case

        String invalidHtml = "<html><head><title>Invalid HTML</title></head><body><h1>Hello, World!</h1><script>alert('XSS Attack');</script></body></html>";
        Document invalidDocument = Jsoup.parse(invalidHtml);

        isValid = cleaner.isValid(invalidDocument);

        assertTrue(!isValid); // Typical use case
    }

    @Test
    public void testClean_EmptyDocument() {
        Document dirtyDocument = new Document("");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().children().isEmpty()); // Edge case: Empty document
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClean_NullDocument() {
        cleaner.clean(null); // Error scenario: Null document
    }

    @Test
    public void testIsValid_EmptyDocument() {
        Document dirtyDocument = new Document("");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid); // Edge case: Empty document, no content in head
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValid_NullDocument() {
        cleaner.isValid(null); // Error scenario: Null document
    }

}