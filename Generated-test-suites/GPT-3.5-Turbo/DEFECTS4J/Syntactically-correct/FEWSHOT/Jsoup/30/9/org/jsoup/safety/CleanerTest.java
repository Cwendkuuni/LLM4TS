package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CleanerTest {
    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Test</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals("<html><head></head><body><p>Test</p></body></html>", cleanDocument.toString());

        dirtyDocument = Jsoup.parseBodyFragment("<script>alert('XSS')</script>");
        cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals("<html><head></head><body></body></html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Test</p>");
        assertTrue(cleaner.isValid(dirtyDocument));

        dirtyDocument = Jsoup.parseBodyFragment("<script>alert('XSS')</script>");
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Test</p>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        Element destination = cleanDocument.body();

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);
        assertEquals(0, numDiscarded);
        assertEquals("<p>Test</p>", destination.toString());

        dirtyDocument = Jsoup.parseBodyFragment("<script>alert('XSS')</script>");
        cleanDocument = Document.createShell(dirtyDocument.baseUri());
        destination = cleanDocument.body();

        numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);
        assertEquals(1, numDiscarded);
        assertEquals("", destination.toString());
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element(Tag.valueOf("p"), "");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        assertNotNull(elementMeta);
        assertNotNull(elementMeta.el);
        assertEquals(sourceEl, elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);

        sourceEl = new Element(Tag.valueOf("script"), "");
        elementMeta = cleaner.createSafeElement(sourceEl);
        assertNotNull(elementMeta);
        assertNotNull(elementMeta.el);
        assertEquals(sourceEl, elementMeta.el);
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

}