package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.Jsoup;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // create a dirty document
        String dirtyHtml = "<html><head></head><body><div id=\"content\"><p>This is some <strong>dirty</strong>HTML.</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // create a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // assert that only the allowed elements and attributes are present in the clean document
        String expectedCleanHtml = "<html><head></head><body><div><p>This is some <strong>dirty</strong>HTML.</p></div></body></html>";
        assertEquals(expectedCleanHtml, cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        // create a dirty document
        String dirtyHtml = "<html><head></head><body><div id=\"content\"><p>This is some <strong>dirty</strong>HTML.</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // create a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // check if the dirty document is valid against the whitelist
        boolean isValid = cleaner.isValid(dirtyDocument);

        // assert that the dirty document is not valid (contains elements and attributes not allowed by the whitelist)
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // create a dirty element
        String dirtyHtml = "<div id=\"content\"><p>This is some <strong>dirty</strong>HTML.</p></div>";
        Element dirtyElement = Jsoup.parseBodyFragment(dirtyHtml).body();

        // create a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // create a clean element
        Element cleanElement = new Element("div");

        // copy safe nodes from the dirty element to the clean element
        int numDiscarded = cleaner.copySafeNodes(dirtyElement, cleanElement);

        // assert that the clean element contains only the allowed elements and attributes from the dirty element
        String expectedCleanHtml = "<div><p>This is some <strong>dirty</strong>HTML.</p></div>";
        assertEquals(expectedCleanHtml, cleanElement.html());

        // assert that the number of discarded elements and attributes is 0
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // create a dirty element
        String dirtyHtml = "<p>This is some <strong>dirty</strong>HTML.</p>";
        Element dirtyElement = Jsoup.parseBodyFragment(dirtyHtml).body();

        // create a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // create a safe element from the dirty element
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(dirtyElement);
        Element safeElement = elementMeta.el;

        // assert that the safe element contains only the allowed attributes from the dirty element
        String expectedSafeHtml = "<p>This is some <strong>dirty</strong>HTML.</p>";
        assertEquals(expectedSafeHtml, safeElement.html());

        // assert that the number of discarded attributes is 0
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

}