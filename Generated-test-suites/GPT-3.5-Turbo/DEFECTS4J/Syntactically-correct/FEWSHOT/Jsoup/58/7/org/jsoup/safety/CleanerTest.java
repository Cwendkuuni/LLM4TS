package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
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
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('xss');</script><div style='color:red;'>Hello World!</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertFalse(cleanDocument.toString().contains("<script>"));
        assertTrue(cleanDocument.toString().contains("<div style=\"color:red;\">Hello World!</div>"));
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('xss');</script><div style='color:red;'>Hello World!</div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testCleaningVisitorHead() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        CleaningVisitor cleaningVisitor = new CleaningVisitor(root, destination);

        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("style", "color:red;");
        cleaningVisitor.head(sourceEl, 0);
        assertEquals(1, destination.children().size());
        assertEquals("div", destination.child(0).tagName());
        assertEquals("color:red;", destination.child(0).attr("style"));

        Element invalidSourceEl = new Element(Tag.valueOf("script"), "");
        cleaningVisitor.head(invalidSourceEl, 0);
        assertEquals(1, destination.children().size());
        assertEquals("div", destination.child(0).tagName());

        TextNode sourceText = new TextNode("Hello World!", "");
        cleaningVisitor.head(sourceText, 0);
        assertEquals(2, destination.children().size());
        assertEquals("div", destination.child(0).tagName());
        assertEquals("Hello World!", destination.child(1).toString());

        DataNode sourceData = new DataNode("data", "");
        cleaningVisitor.head(sourceData, 0);
        assertEquals(2, destination.children().size());
        assertEquals("div", destination.child(0).tagName());
    }

    @Test
    public void testCleaningVisitorTail() {
        Element root = new Element(Tag.valueOf("html"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        CleaningVisitor cleaningVisitor = new CleaningVisitor(root, destination);

        Element sourceEl = new Element(Tag.valueOf("div"), "");
        cleaningVisitor.head(sourceEl, 0);
        cleaningVisitor.tail(sourceEl, 0);
        assertNull(cleaningVisitor.destination);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        source.attr("style", "color:red;");
        Element dest = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
        assertEquals(1, dest.children().size());
        assertEquals("color:red;", dest.child(0).attr("style"));
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("style", "color:red;");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        assertEquals("div", elementMeta.el.tagName());
        assertEquals("color:red;", elementMeta.el.attr("style"));
        assertEquals(0, elementMeta.numAttribsDiscarded);

        Element invalidSourceEl = new Element(Tag.valueOf("script"), "");
        elementMeta = cleaner.createSafeElement(invalidSourceEl);
        assertEquals("script", elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}