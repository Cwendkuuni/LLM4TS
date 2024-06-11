package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;

public class CleanerTest {
    private Whitelist whitelist;
    private Document dirtyDocument;
    private Document cleanDocument;

    @org.junit.Before
    public void setUp() {
        // Initialize a whitelist and create a dirty document for testing
        whitelist = new Whitelist(); // Assuming you have a way to initialize a Whitelist instance
        dirtyDocument = Document.createFromString("<html><body><div id='test'><h1>Test<h1><p id='p1'>Test <b>Paragraph</b></p></div></body></html>", null);
    }

    @org.junit.Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(whitelist);
        cleanDocument = cleaner.clean(dirtyDocument);

        // Check that the cleaned document only contains elements and attributes allowed by the whitelist
        assertEquals("<html><body><div><h1></h1><p></p></div></body></html>", cleanDocument.html());
    }

    @org.junit.Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(whitelist);

        // Test a valid document
        assertTrue(cleaner.isValid(dirtyDocument));

        // Test an invalid document
        dirtyDocument = Document.createFromString("<html><body><script>alert('XSS')</script></body></html>", null);
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @org.junit.Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(whitelist);
        cleanDocument = cleaner.clean(dirtyDocument);

        // Test the number of discarded nodes
        Element body = cleanDocument.body();
        int numDiscarded = cleaner.copySafeNodes(body, Document.createShell(dirtyDocument.baseUri()).body());
        assertEquals(2, numDiscarded); // Assuming 2 nodes were discarded in the example

        // Test the copied elements and their attributes
        Element testDiv = dirtyDocument.selectFirst("#test");
        Element testDivClean = cleanDocument.selectFirst("#test");
        Element h1 = testDiv.selectFirst("h1");
        Element p = testDiv.selectFirst("#p1");

        assertEquals(h1.text(), testDivClean.selectFirst("h1").text());
        assertEquals(p.text(), testDivClean.selectFirst("#p1").text());

        // Test the copied elements without attributes
        Element simpleDiv = dirtyDocument.createElement("div");
        Document emptyDocument = Document.createShell(dirtyDocument.baseUri());
        cleaner.copySafeNodes(simpleDiv, emptyDocument.body());
        assertNull(emptyDocument.body().childNodes().get(0).tagName()); // Assuming the copy did not keep the "div" tag
    }
}