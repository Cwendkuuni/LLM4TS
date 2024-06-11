package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Create whitelist
        Whitelist whitelist = Whitelist.basic();

        // Create dirty document
        Document dirtyDocument = Document.createShell("");

        // Create elements for dirty document
        Element body = dirtyDocument.appendElement("body");
        Element div = body.appendElement("div");
        div.attr("style", "color:red");
        Element p = div.appendElement("p");
        p.text("Hello, World!");

        // Create cleaner with whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Check if only allowed elements are present in the clean document
        Element cleanBody = cleanDocument.body();
        assertEquals(1, cleanBody.children().size());
        Element cleanDiv = cleanBody.child(0);
        assertEquals("div", cleanDiv.tagName());
        assertEquals(0, cleanDiv.attributes().size());
        Element cleanP = cleanDiv.child(0);
        assertEquals("p", cleanP.tagName());
        assertEquals(0, cleanP.attributes().size());
        assertEquals("Hello, World!", cleanP.text());
    }

    @Test
    public void testIsValid() {
        // Create whitelist
        Whitelist whitelist = Whitelist.basic();

        // Create dirty document with safe tags
        Document dirtyDocument1 = Document.createShell("");
        Element body1 = dirtyDocument1.appendElement("body");
        Element p1 = body1.appendElement("p");
        p1.text("Hello, World!");

        // Create dirty document with unsafe tag
        Document dirtyDocument2 = Document.createShell("");
        Element body2 = dirtyDocument2.appendElement("body");
        Element script = body2.appendElement("script");
        script.text("alert('Hello, World!');");

        // Create cleaner with whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Check if dirty documents are valid
        assertTrue(cleaner.isValid(dirtyDocument1));
        assertFalse(cleaner.isValid(dirtyDocument2));
    }

    @Test
    public void testCopySafeNodes() {
        // Create whitelist
        Whitelist whitelist = Whitelist.basic();

        // Create dirty document
        Document dirtyDocument = Document.createShell("");

        // Create elements for dirty document
        Element body = dirtyDocument.appendElement("body");
        Element div = body.appendElement("div");
        div.attr("style", "color:red");
        Element p = div.appendElement("p");
        p.text("Hello, World!");

        // Create cleaner with whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Create clean document
        Document cleanDocument = Document.createShell("");

        // Copy safe nodes from dirty document to clean document
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        // Check if only safe nodes are copied and number of discarded nodes is correct
        assertEquals(2, numDiscarded);
        Element cleanBody = cleanDocument.body();
        assertEquals(1, cleanBody.children().size());
        Element cleanDiv = cleanBody.child(0);
        assertEquals("div", cleanDiv.tagName());
        assertEquals(0, cleanDiv.attributes().size());
        Element cleanP = cleanDiv.child(0);
        assertEquals("p", cleanP.tagName());
        assertEquals(0, cleanP.attributes().size());
        assertEquals("Hello, World!", cleanP.text());
    }

    @Test
    public void testCreateSafeElement() {
        // Create whitelist
        Whitelist whitelist = Whitelist.basic();

        // Create element with attributes
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("style", "color:red");
        sourceEl.attr("class", "container");

        // Create cleaner with whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        // Create safe element
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        // Check if only safe attributes are present in the safe element
        Element dest = elementMeta.el;
        assertEquals("div", dest.tagName());
        assertEquals(1, dest.attributes().size());
        assertEquals("color:red", dest.attr("style"));
        assertNull(dest.attr("class"));
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}