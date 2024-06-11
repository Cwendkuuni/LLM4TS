package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {

    /**
     * Test constructor with valid Whitelist
     */
    @Test
    public void testCleanerConstruction() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    /**
     * Test clean method with valid Document
     */
    @Test
    public void testCleanValidDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
    }

    /**
     * Test isValid method with valid Document
     */
    @Test
    public void testIsValidValidDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    /**
     * Test clean method with invalid Document
     */
    @Test
    public void testCleanInvalidDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('Hello, World!');</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body></body></html>", cleanDocument.html());
    }

    /**
     * Test isValid method with invalid Document
     */
    @Test
    public void testIsValidInvalidDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('Hello, World!');</script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    /**
     * Test clean method with empty Document
     */
    @Test
    public void testCleanEmptyDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html></html>", cleanDocument.html());
    }

    /**
     * Test clean method with null Document
     */
    @Test(expected = NullPointerException.class)
    public void testCleanNullDocument() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = null;
        cleaner.clean(dirtyDocument);
    }

}