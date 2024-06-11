import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() throws Exception {
        whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
    }

    @After
    public void tearDown() throws Exception {
        whitelist = null;
        cleaner = null;
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><p>Hello, World!</p></div>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><div><p>Hello, World!</p></div></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><p>Hello, World!</p></div>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><p>Hello, World!</p></div>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(0, numDiscarded);
        assertEquals("<div><p>Hello, World!</p></div>", cleanDocument.body().html());
    }

    @Test
    public void testCreateSafeElement() {
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><p>Hello, World!</p></div>");
        Element sourceElement = dirtyDocument.body();
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertNotNull(elementMeta);
        assertEquals("div", elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}