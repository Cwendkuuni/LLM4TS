package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = "<html><body><div><script>alert('XSS');</script></div></body></html>";

        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><div></div></body></html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = "<html><body><div><script>alert('XSS');</script></div></body></html>";

        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element(Tag.valueOf("div"), "");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals(sourceEl, elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

}