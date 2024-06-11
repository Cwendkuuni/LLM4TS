package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    @Test
    public void testClean() {
        // Create a new Cleaner object with a default whitelist
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Create a dirty document with unsafe elements
        Document dirtyDocument = Document.createShell("");
        Element unsafeElement = new Element(Tag.valueOf("script"), "");
        unsafeElement.text("alert('XSS attack')");
        dirtyDocument.body().appendChild(unsafeElement);

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert that the unsafe element has been removed
        assertTrue(cleanDocument.body().children().isEmpty());
    }

    @Test
    public void testIsValid() {
        // Create a new Cleaner object with a basic whitelist
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Create a dirty document with unsafe elements
        Document dirtyDocument = Document.createShell("");
        Element unsafeElement = new Element(Tag.valueOf("script"), "");
        unsafeElement.text("alert('XSS attack')");
        dirtyDocument.body().appendChild(unsafeElement);

        // Check if the document is valid
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    // Additional test cases for other public methods can be added here

}