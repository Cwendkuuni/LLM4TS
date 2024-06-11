package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Document.createShell("");
        dirtyDocument.body().append("<p>This is a <b>dirty</b> document.</p>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><p>This is a <b>dirty</b> document.</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testClean_EmptyDocument() {
        Document dirtyDocument = Document.createShell("");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Document.createShell("");
        dirtyDocument.body().append("<p>This is a <b>dirty</b> document.</p>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        Document dirtyDocument = Document.createShell("");
        dirtyDocument.body().append("<script>alert('XSS');</script>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(!isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        root.appendChild(destination);

        Element sourceElement = new Element(Tag.valueOf("p"), "");
        root.appendChild(sourceElement);

        TextNode sourceText = new TextNode("This is a test", "");
        sourceElement.appendChild(sourceText);

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(0, numDiscarded);
        assertEquals("<div><div><p>This is a test</p></div></div>", root.html());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("a"), "");
        sourceEl.attr("href", "http://example.com");
        sourceEl.attr("target", "_blank");

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertNotNull(meta.el);
        assertEquals("a", meta.el.tagName());
        assertEquals("http://example.com", meta.el.attr("href"));
        assertEquals("_blank", meta.el.attr("target"));
        assertEquals(0, meta.numAttribsDiscarded);
    }
}