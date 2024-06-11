package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Create a test document
        Document doc
                = Document.createShell("https://example.com");
        Element body = doc.body();
        body.html("<p>This is a <b>test</b> <script>alert('xss');</script></p>");

        // Create whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Clean the document
        Document cleanDoc = cleaner.clean(doc);

        // Check if the script tag is removed
        assertFalse(cleanDoc.body().html().contains("<script>"));
    }

    @Test
    public void testIsValid() {
        // Create a test document
        Document doc
                = Document.createShell("https://example.com");
        Element body = doc.body();
        body.html("<p>This is a <b>test</b> <script>alert('xss');</script></p>");

        // Create whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Check if document is valid
        assertFalse(cleaner.isValid(doc));
    }

    @Test
    public void testCopySafeNodes() {
        // Create a test document
        Document doc
                = Document.createShell("https://example.com");
        Element body = doc.body();
        body.html("<p>This is a <b>test</b> <script>alert('xss');</script></p>");

        // Create destination element
        Element dest = new Element(Tag.valueOf("div"), "");
        
        // Create whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Copy safe nodes from body to destination
        int numDiscarded = cleaner.copySafeNodes(body, dest);

        // Check if script tag is discarded
        assertEquals(1, numDiscarded);
        assertFalse(dest.html().contains("<script>"));
    }

    @Test
    public void testCreateSafeElement() {
        // Create a test element
        Element sourceEl = new Element(Tag.valueOf("a"), "");

        // Create whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create safe element
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;

        // Check if element is created with same tag
        assertEquals(sourceEl.tagName(), dest.tagName());
    }

}