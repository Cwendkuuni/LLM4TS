import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);

        dirtyDocument = Jsoup.parse("<html><head></head><body><div><p>Hello</p></div></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals(1, cleanDocument.body().children().size());
    }

    @Test
    public void testClean_emptyDocument() {
        Document emptyDocument = Jsoup.parse("");
        Document cleanDocument = cleaner.clean(emptyDocument);
        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals(0, cleanDocument.body().children().size());
    }

    @Test
    public void testIsValid_validDocument() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_invalidDocument() {
        Document invalidDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script></body></html>");
        boolean isValid = cleaner.isValid(invalidDocument);
        assertFalse(isValid);
    }

    @Test
    public void testIsValid_emptyDocument() {
        Document emptyDocument = Jsoup.parse("");
        boolean isValid = cleaner.isValid(emptyDocument);
        assertTrue(isValid);
    }
}