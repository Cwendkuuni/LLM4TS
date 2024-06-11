
package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    Whitelist whitelist = Whitelist.basicWithImages();
    Cleaner cleaner = new Cleaner(whitelist);

    @Test
    public void testClean() {
        String dirtyHtml = "<html><body><div><p>This is a test</p></div></body></html>";
        Document dirtyDocument = Parser.parseBodyFragment(dirtyHtml, "http://example.com");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><div><p>This is a test</p></div></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        String dirtyHtml = "<html><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Parser.parseBodyFragment(dirtyHtml, "http://example.com");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        String dirtyHtml = "<html><body><div><p>This is a test</p></div></body></html>";
        Document dirtyDocument = Parser.parseBodyFragment(dirtyHtml, "http://example.com");

        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        String tagName = "div";
        Element sourceElement = new Element(Tag.valueOf(tagName), "");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals(tagName, elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}
