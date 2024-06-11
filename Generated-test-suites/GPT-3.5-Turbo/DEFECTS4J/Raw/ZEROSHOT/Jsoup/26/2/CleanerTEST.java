package org.jsoup.safety;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><body><h1>Test</h1><p>Hello world!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Elements h1Elements = cleanDocument.select("h1");
        assertEquals(1, h1Elements.size());
        assertEquals("Test", h1Elements.first().text());

        Elements pElements = cleanDocument.select("p");
        assertEquals(1, pElements.size());
        assertEquals("Hello world!", pElements.first().text());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument1 = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument1));

        Document dirtyDocument2 = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument2));
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello world!</p></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        assertEquals(0, numDiscarded);

        Elements pElements = cleanDocument.select("p");
        assertEquals(1, pElements.size());
        assertEquals("Hello world!", pElements.first().text());
    }

    @Test
    public void testCreateSafeElement() {
        ElementMeta elementMeta1 = cleaner.createSafeElement(new Element("h1"));
        assertNotNull(elementMeta1.el);
        assertEquals(0, elementMeta1.numAttribsDiscarded);

        ElementMeta elementMeta2 = cleaner.createSafeElement(new Element("script"));
        assertNotNull(elementMeta2.el);
        assertEquals(0, elementMeta2.numAttribsDiscarded);
    }
}