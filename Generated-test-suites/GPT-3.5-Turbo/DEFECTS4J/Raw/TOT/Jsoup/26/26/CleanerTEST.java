package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic()); // Using basic whitelist for testing
    }

    @Test
    public void testClean() {
        // Create a dirty document with unsafe elements and attributes
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><script>alert('XSS')</script></div>");

        // Clean the document using the cleaner
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert that the clean document does not contain any unsafe elements or attributes
        assertFalse(cleanDocument.html().contains("<script>"));
        assertFalse(cleanDocument.html().contains("alert('XSS')"));

        // Assert that the clean document contains only the allowed elements and attributes
        assertEquals(1, cleanDocument.getElementsByTag("div").size());
    }

    @Test
    public void testIsValid() {
        // Create a dirty document with unsafe elements and attributes
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><script>alert('XSS')</script></div>");

        // Check if the dirty document is valid against the whitelist
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert that the document is not valid
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Create a source element with unsafe children
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><script>alert('XSS')</script>Safe Text</div>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        Element source = dirtyDocument.body();
        Element dest = cleanDocument.body();

        // Copy safe nodes from source to destination
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        // Assert that the destination element does not contain any unsafe elements or text nodes
        assertFalse(cleanDocument.html().contains("<script>"));
        assertFalse(cleanDocument.html().contains("alert('XSS')"));

        // Assert that the destination element contains only the safe text and no unsafe elements
        assertEquals("Safe Text", cleanDocument.text());

        // Assert that the number of discarded elements is correct
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Create a source element with unsafe attributes
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("onclick", "alert('XSS')");

        // Create a safe element using the cleaner
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element safeElement = elementMeta.el;

        // Assert that the safe element does not contain any unsafe attributes
        assertFalse(safeElement.hasAttr("onclick"));

        // Assert that the number of discarded attributes is correct
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

}