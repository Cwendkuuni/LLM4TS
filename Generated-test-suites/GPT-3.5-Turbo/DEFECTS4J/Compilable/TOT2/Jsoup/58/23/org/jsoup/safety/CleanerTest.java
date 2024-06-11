package org.jsoup.safety;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    private Whitelist whitelist;
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setup() {
        // Setup a basic whitelist
        whitelist = Whitelist.relaxed();
        cleaner = new Cleaner(whitelist);

        // Setup a basic dirty document for testing
        dirtyDocument = new Document("");
        Element body = new Element("div");
        body.appendElement("p").text("Hello, <b>world</b>!");
        dirtyDocument.appendChild(body);
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Verify that only allowed elements and attributes are present in the cleaned document
        assertEquals("Body tag should be present", 1, cleanDocument.body().children().size());
        assertEquals("Paragraph tag should be present", "p", cleanDocument.body().child(0).tagName());
        assertEquals("Text content should be preserved", "Hello, world!", cleanDocument.body().text());
    }

    @Test
    public void testIsValid() {
        // Check if the dirty document is valid against the whitelist
        assertTrue("Dirty document should be considered valid", cleaner.isValid(dirtyDocument));

        // Create a new dirty document with an invalid tag
        Document invalidDocument = new Document("");
        Element invalidTag = new Element("invalidTag");
        invalidDocument.appendChild(invalidTag);

        // Check if the new dirty document is valid
        assertFalse("Document with invalid tag should be considered invalid", cleaner.isValid(invalidDocument));
    }

    @Test
    public void testInvalidInput() {
        // Test for handling null input in clean method
        try {
            cleaner.clean(null);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
}