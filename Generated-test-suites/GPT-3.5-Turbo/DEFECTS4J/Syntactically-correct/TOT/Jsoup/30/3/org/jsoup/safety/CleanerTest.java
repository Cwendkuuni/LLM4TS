import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.none());
    }

    @Test
    public void testClean_typicalCase() {
        // Typical input with clean elements
        Document dirtyDocument = Jsoup.parse("<html><body><div>Hello</div><p>World</p></body></html>");
        Document expectedCleanDocument = Jsoup.parse("<html><head></head><body><div>Hello</div><p>World</p></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(expectedCleanDocument, cleanDocument);
    }

    @Test
    public void testClean_emptyBody() {
        // Edge case with empty body
        Document dirtyDocument = Jsoup.parse("<html></html>");
        Document expectedCleanDocument = Jsoup.parse("<html><head></head><body></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(expectedCleanDocument, cleanDocument);
    }

    @Test
    public void testClean_framesetDocument() {
        // Edge case with frameset document
        Document dirtyDocument = Jsoup.parse("<html><frameset></frameset></html>");
        Document expectedCleanDocument = Jsoup.parse("<html><head></head><body></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(expectedCleanDocument, cleanDocument);
    }

    @Test
    public void testIsValid_validDocument() {
        // Typical case with valid document
        Document validDocument = Jsoup.parse("<html><body><div>Hello</div><p>World</p></body></html>");

        boolean isValid = cleaner.isValid(validDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_invalidDocument() {
        // Typical case with invalid document
        Document invalidDocument = Jsoup.parse("<html><body><div onclick=\"alert('XSS')\">Hello</div><p>World</p></body></html>");

        boolean isValid = cleaner.isValid(invalidDocument);

        assertFalse(isValid);
    }

    @Test
    public void testIsValid_emptyBody() {
        // Edge case with empty body
        Document emptyBodyDocument = Jsoup.parse("<html></html>");

        boolean isValid = cleaner.isValid(emptyBodyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_framesetDocument() {
        // Edge case with frameset document
        Document framesetDocument = Jsoup.parse("<html><frameset></frameset></html>");

        boolean isValid = cleaner.isValid(framesetDocument);

        assertTrue(isValid);
    }
}