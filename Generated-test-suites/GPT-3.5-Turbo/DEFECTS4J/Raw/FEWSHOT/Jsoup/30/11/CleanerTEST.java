import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<html><head></head><body><p>Hello, <b>world!</b></p></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertTrue(cleanDocument.select("html").isEmpty());
        assertTrue(cleanDocument.select("head").isEmpty());
        assertFalse(cleanDocument.select("body").isEmpty());
        assertFalse(cleanDocument.select("p").isEmpty());
        assertFalse(cleanDocument.select("b").isEmpty());
        assertEquals("Hello, world!", cleanDocument.select("p").text());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<html><head></head><body><p>Hello, <b>world!</b></p></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<html><head></head><body><p>Hello, <b>world!</b></p></body></html>");

        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertNotNull(cleanDocument.body());
        assertFalse(cleanDocument.select("html").isEmpty());
        assertTrue(cleanDocument.select("head").isEmpty());
        assertFalse(cleanDocument.select("body").isEmpty());
        assertFalse(cleanDocument.select("p").isEmpty());
        assertFalse(cleanDocument.select("b").isEmpty());
        assertEquals("Hello, world!", cleanDocument.select("p").text());
    }

    @Test
    public void testCreateSafeElement() {
        ElementMeta meta = cleaner.createSafeElement(Jsoup.parseBodyFragment("<b>hello</b>").body());

        assertNotNull(meta.el);
        assertFalse(meta.el.children().isEmpty());
        assertFalse(meta.el.select("b").isEmpty());
        assertEquals("hello", meta.el.select("b").text());
        assertEquals(0, meta.numAttribsDiscarded);

        meta = cleaner.createSafeElement(Jsoup.parseBodyFragment("<script>alert('hello')</script>").body());

        assertNotNull(meta.el);
        assertTrue(meta.el.children().isEmpty());
        assertTrue(meta.el.select("script").isEmpty());
        assertEquals(1, meta.numAttribsDiscarded);
    }

    @Test
    public void testCleanerConstructor() {
        assertNotNull(cleaner);
        assertNotNull(cleaner.whitelist);
    }
}