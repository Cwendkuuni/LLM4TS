package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import org.junit.Test;

/**
 * Test class for Cleaner class.
 */
public class CleanerTest {

    /**
     * Test constructor.
     */
    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    /**
     * Test clean method with valid input.
     */
    @Test
    public void testCleanValidInput() {
        String html = "<html><body><p>Valid HTML</p></body></html>";
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p>Valid HTML</p></body></html>", cleanDocument.html());
    }

    /**
     * Test clean method with invalid input.
     */
    @Test
    public void testCleanInvalidInput() {
        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().text().isEmpty());
    }

    /**
     * Test isValid method with valid input.
     */
    @Test
    public void testIsValidValidInput() {
        String html = "<html><body><p>Valid HTML</p></body></html>";
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse(html);

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    /**
     * Test isValid method with invalid input.
     */
    @Test
    public void testIsValidInvalidInput() {
        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse(html);

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    /**
     * Test copySafeNodes method with safe elements.
     */
    @Test
    public void testCopySafeNodesSafeElements() {
        String html = "<html><body><p id='test' class='test'>Safe HTML<img src='image.jpg'></p></body></html>";
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = new Document();
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals("<html><body><p id='test' class='test'>Safe HTML</p></body></html>", cleanDocument.html());
        assertEquals(0, numDiscarded);
    }

    /**
     * Test copySafeNodes method with unsafe elements.
     */
    @Test
    public void testCopySafeNodesUnsafeElements() {
        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = new Document();
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertTrue(cleanDocument.body().text().isEmpty());
        assertEquals(1, numDiscarded);
    }

    /**
     * Test createSafeElement method with safe elements.
     */
    @Test
    public void testCreateSafeElementSafeElements() {
        String html = "<html><body><p id='test' class='test'>Safe HTML<img src='image.jpg'></p></body></html>";
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = Jsoup.parse(html).body();
        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals("<p id='test' class='test'>Safe HTML</p>", meta.el.html());
        assertEquals(0, meta.numAttribsDiscarded);
    }

    /**
     * Test createSafeElement method with unsafe elements.
     */
    @Test
    public void testCreateSafeElementUnsafeElements() {
        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = Jsoup.parse(html).body();
        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertTrue(meta.el.text().isEmpty());
        assertEquals(1, meta.numAttribsDiscarded);
    }
}