package org.jsoup.safety;
import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = new Document("<html><head></head><body><div>Hello World!</div></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals(1, cleanDocument.body().children().size());
        assertEquals("div", cleanDocument.body().child(0).tagName());
        assertEquals("Hello World!", cleanDocument.body().text());
    }

    @Test
    public void testIsValid() {
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanNullDocument() {
        cleaner.clean(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidNullDocument() {
        cleaner.isValid(null);
    }

    @Test
    public void testCleanEmptyDocumentBody() {
        Document emptyBodyDocument = new Document("<html><head></head><body></body></html>");
        Document cleanDocument = cleaner.clean(emptyBodyDocument);
        assertNotNull(cleanDocument);
        assertNull(cleanDocument.body());
    }

    @Test
    public void testCleanNoBody() {
        Document noBodyDocument = new Document("<html><head></head></html>");
        Document cleanDocument = cleaner.clean(noBodyDocument);
        assertNotNull(cleanDocument);
        assertNull(cleanDocument.body());
    }

    @Test
    public void testCleanEnforcedAttributes() {
        Whitelist customWhitelist = Whitelist.basic();
        customWhitelist.addEnforcedAttribute("a", "href", "http://example.com");
        Cleaner customCleaner = new Cleaner(customWhitelist);
        Document dirtyDocumentWithHref = new Document("<html><head></head><body><a href=\"http://example.com\">Link</a></body></html>");
        Document cleanDocument = customCleaner.clean(dirtyDocumentWithHref);
        assertNotNull(cleanDocument);
        assertEquals("http://example.com", cleanDocument.body().child(0).attr("href"));
    }

}