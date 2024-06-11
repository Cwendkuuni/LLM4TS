package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.util.List;

/**
 * Test file for {@link Cleaner} class
 */
public class CleanerTest {

    @org.junit.Before
    public void setUp() {
        Whitelist whitelist = Whitelist.simpleText();
        cleaner = new Cleaner(whitelist);
    }

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testConstructor() {
        Cleaner cleaner = new Cleaner(null);
        assertNull(cleaner.whitelist);
    }

    /**
     * Test clean method with valid input
     */
    @org.junit.Test
    public void testCleanValidInput() {
        Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World!</h1></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
    }

    /**
     * Test clean method with invalid input
     */
    @org.junit.Test
    public void testCleanInvalidInput() {
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body></body></html>", cleanDocument.html());
    }

    /**
     * Test isValid method with valid input
     */
    @org.junit.Test
    public void testIsValidValidInput() {
        Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World!</h1></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    /**
     * Test isValid method with invalid input
     */
    @org.junit.Test
    public void testIsValidInvalidInput() {
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    /**
     * Test copySafeNodes method with safe tags
     */
    @org.junit.Test
    public void testCopySafeNodesWithSafeTags() {
        Element sourceEl = Jsoup.parse("<html><body><h1 id='title'>Hello World!</h1></body></html>").getElementsByTag("h1").first();
        Element dest = new Element(Tag.H1, sourceEl.baseUri());
        int numDiscarded = cleaner.copySafeNodes(sourceEl, dest);

        assertEquals(0, numDiscarded);
    }

    /**
     * Test copySafeNodes method with unsafe tags
     */
    @org.junit.Test
    public void testCopySafeNodesWithUnsafeTags() {
        Element sourceEl = Jsoup.parse("<html><body><script src='evil.js'></script></body></html>").getElementsByTag("script").first();
        Element dest = new Element(Tag.SCRIPT, sourceEl.baseUri());
        int numDiscarded = cleaner.copySafeNodes(sourceEl, dest);

        assertEquals(1, numDiscarded);
    }

    /**
     * Test createSafeElement method with safe attributes
     */
    @org.junit.Test
    public void testCreateSafeElementWithSafeAttributes() {
        Element sourceEl = Jsoup.parse("<html><body><a href='http://www.example.com' title='Example'>Example</a></body></html>").getElementsByTag("a").first();
        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals("<a href='http://www.example.com' title='Example'></a>", meta.el.html());
        assertEquals(0, meta.numAttribsDiscarded);
    }

    /**
     * Test createSafeElement method with unsafe attributes
     */
    @org.junit.Test
    public void testCreateSafeElementWithUnsafeAttributes() {
        Element sourceEl = Jsoup.parse("<html><body><a href='javascript:alert(\"XSS\")' title='Example'>Example</a></body></html>").getElementsByTag("a").first();
        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals("<a></a>", meta.el.html());
        assertEquals(1, meta.numAttribsDiscarded);
    }

    private Cleaner cleaner;

}