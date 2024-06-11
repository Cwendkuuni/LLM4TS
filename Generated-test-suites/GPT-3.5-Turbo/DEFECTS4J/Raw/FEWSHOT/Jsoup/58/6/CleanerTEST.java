package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><body><div><script>alert('XSS');</script></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><div></div></body></html>", cleanDocument.outerHtml());
        assertEquals("head", cleanDocument.head().nodeName());
        assertEquals("body", cleanDocument.body().nodeName());
        assertEquals("div", cleanDocument.body().child(0).nodeName());
    }

    @Test
    public void testClean_invalidTag_removed() {
        Document dirtyDocument = Jsoup.parse("<html><body><foo>Hello</foo></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body></body></html>", cleanDocument.outerHtml());
        assertEquals("head", cleanDocument.head().nodeName());
        assertEquals("body", cleanDocument.body().nodeName());
        assertEquals(0, cleanDocument.body().childNodeSize());
    }

    @Test
    public void testIsValid_validDocument_true() {
        Document dirtyDocument = Jsoup.parse("<html><body><div><p>Content</p></div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_invalidDocument_false() {
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testIsValid_emptyHead_true() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><div><p>Content</p></div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCleaningVisitor_headElement_safeTag_clonedAndCopied() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("body"), "");

        Element source = new Element(Tag.valueOf("div"), "");
        source.text("Content");

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
        cleaningVisitor.head(source, 0);

        assertEquals("<div>Content</div>", destination.outerHtml());
    }

    @Test
    public void testCleaningVisitor_headElement_unsafeTag_notAddedOrCounted() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("body"), "");

        Element source = new Element(Tag.valueOf("foo"), "");
        source.text("Invalid");

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
        cleaningVisitor.head(source, 0);

        assertEquals("", destination.outerHtml());
    }

    @Test
    public void testCleaningVisitor_textNode_copiedToDestination() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("body"), "");

        TextNode source = new TextNode("Content", "");
        
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
        cleaningVisitor.head(source, 0);

        assertEquals("Content", destination.text());
    }

    @Test
    public void testCleaningVisitor_dataNode_safeParentTag_copiedToDestination() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("body"), "");

        Element parent = new Element(Tag.valueOf("div"), "");
        parent.appendChild(new DataNode("Content", ""));

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
        cleaningVisitor.head(parent.childNode(0), 0);

        assertEquals("Content", destination.text());
    }

    @Test
    public void testCleaningVisitor_dataNode_unsafeParentTag_notAddedOrCounted() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("body"), "");

        Element parent = new Element(Tag.valueOf("foo"), "");
        parent.appendChild(new DataNode("Invalid", ""));

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
        cleaningVisitor.head(parent.childNode(0), 0);

        assertEquals("", destination.outerHtml());
    }

    @Test
    public void testCleaningVisitor_tailElement_safeTag_poppedDestinationStack() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("body"), "");
        destination.appendChild(new Element(Tag.valueOf("div"), ""));

        Element source = new Element(Tag.valueOf("div"), "");

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
        cleaningVisitor.tail(source, 0);

        assertEquals(destination, cleaningVisitor.destination);
    }

    @Test
    public void testCopySafeNodes_noDiscardedNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        source.appendChild(new Element(Tag.valueOf("p"), ""));

        Element dest = new Element(Tag.valueOf("body"), "");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
        assertEquals("<body><div><p></p></div></body>", dest.outerHtml());
    }

    @Test
    public void testCopySafeNodes_discardedUnSafeElement() {
        Element source = new Element(Tag.valueOf("div"), "");
        source.appendChild(new Element(Tag.valueOf("foo"), ""));

        Element dest = new Element(Tag.valueOf("body"), "");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(1, numDiscarded);
        assertEquals("<body></body>", dest.outerHtml());
    }

    @Test
    public void testCreateSafeElement_safeTag_allAttributesCopied() {
        Element source = new Element(Tag.valueOf("div"), "");
        source.attr("class", "content");
        source.attr("data-value", "123");

        int numDiscarded = 0;
        ElementMeta elementMeta = cleaner.createSafeElement(source);

        assertEquals("<div class=\"content\" data-value=\"123\"></div>", elementMeta.el.outerHtml());
        assertEquals(numDiscarded, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_unsafeAttributes_discarded() {
        Element source = new Element(Tag.valueOf("div"), "");
        source.attr("onmouseover", "alert('XSS')");
        source.attr("style", "color: red");

        int numDiscarded = 2;
        ElementMeta elementMeta = cleaner.createSafeElement(source);

        assertEquals("<div></div>", elementMeta.el.outerHtml());
        assertEquals(numDiscarded, elementMeta.numAttribsDiscarded);
    }
}