import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setup() {
        whitelist = Whitelist.relaxed(); // choose the appropriate whitelist
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body>Dirty content</body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals("Dirty content", cleanDocument.body().text());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body>Dirty content</body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testClean_invalidDocument() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals("", cleanDocument.body().html()); // All unsafe elements should be removed
    }

    @Test
    public void testIsValid_invalidDocument() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid); // The document is invalid due to the unsafe <script> element
    }
    
    // Add more test cases for edge cases and exception handling scenarios here

}

