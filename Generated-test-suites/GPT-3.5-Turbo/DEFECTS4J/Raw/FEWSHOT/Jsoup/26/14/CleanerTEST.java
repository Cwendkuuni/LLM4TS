package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

public class CleanerTest {
    @Test
    public void testClean() {
        // Setup
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("<html><body><div><h1>Hello World!</h1></div></body></html>");

        // Exercise
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Verify
        Element cleanBody = cleanDocument.body();
        assertTrue(cleanBody.select("div").isEmpty());
        assertEquals("Hello World!", cleanBody.select("h1").text());
    }

    @Test
    public void testIsValid() {
        // Setup
        Whitelist whitelist = Whitelist.none();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("<html><body><script>alert('XSS')</script></body></html>");

        // Exercise
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Verify
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Setup
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("<html><body><div><h1>Hello World!</h1></div></body></html>");
        Element source = dirtyDocument.body();
        Element dest = new Element("div");

        // Exercise
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        // Verify
        assertEquals(0, numDiscarded);
        assertTrue(dest.select("div").isEmpty());
        assertEquals("Hello World!", dest.select("h1").text());
    }

    @Test
    public void testCreateSafeElement() {
        // Setup
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element("div");
        sourceEl.attr("class", "text");
        sourceEl.attr("style", "color: red");
        sourceEl.attr("data-id", "123");

        // Exercise
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        // Verify
        Element dest = elementMeta.el;
        assertEquals("div", dest.tagName());
        assertEquals("text", dest.attr("class"));
        assertEquals("color: red", dest.attr("style"));
        assertTrue(dest.attr("data-id").isEmpty());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}