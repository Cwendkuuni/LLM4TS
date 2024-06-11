package org.jsoup.safety;

import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical use case test
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = new Document("");
        Element body = new Element("body", "");
        body.appendChild(new Element("div", ""));
        dirtyDocument.appendChild(body);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals("body", cleanDocument.body().tagName());
        assertEquals(1, cleanDocument.body().children().size());
    }

    @Test
    public void testIsValid() {
        // Typical use case test
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = new Document("");
        Element body = new Element("body", "");
        body.appendChild(new Element("p", ""));
        dirtyDocument.appendChild(body);

        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Edge case test
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Element root = new Element("root", "");
        Element destination = new Element("div", "");
        root.appendChild(new TextNode("This is a text node", ""));

        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertEquals(1, numDiscarded);
        assertEquals(1, destination.children().size());
    }

    @Test
    public void testCreateSafeElement() {
        // Edge case test
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Element sourceEl = new Element("div", "");
        sourceEl.attr("id", "test");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        assertNotNull(elementMeta.el);
        assertEquals("div", elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}