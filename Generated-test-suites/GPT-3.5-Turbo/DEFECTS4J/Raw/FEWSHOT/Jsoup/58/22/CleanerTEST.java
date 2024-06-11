package org.jsoup.safety;

import org.jsoup.helper.Validate;
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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    @Test
    public void testClean() {
        // Test simple cleaning of valid document
        Document dirtyDocument = Document.createShell("");
        dirtyDocument.body().append("<p>Hello, <script>alert('World');</script> World!</p>");

        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>Hello,  World!</p>\n" +
                " </body>\n" +
                "</html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        // Test valid base document
        Document dirtyDocument = Document.createShell("");
        dirtyDocument.body().append("<p>Hello, World!</p>");

        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        assertTrue(cleaner.isValid(dirtyDocument));

        // Test invalid base document
        dirtyDocument.body().append("<script>alert('XSS');</script>");
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCleaningVisitor() {
        // Test cleaning of element
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        NodeVisitor cleaningVisitor = new Cleaner.CleaningVisitor(root, destination);

        Element sourceEl = new Element(Tag.valueOf("p"), "");
        cleaningVisitor.head(sourceEl, 0);

        assertEquals(sourceEl.tagName(), destination.children().first().tagName());

        ElementMeta meta = new Cleaner.ElementMeta(destination.children().first(), 0);
        cleaningVisitor.tail(root, 0);
    }

    @Test
    public void testCopySafeNodes() {
        // Test copying of safe nodes
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        int numDiscarded = 0;

        Cleaner cleaner = new Cleaner(Whitelist.none());
        int discarded = cleaner.copySafeNodes(source, dest);

        assertEquals(numDiscarded, discarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Test creating safe element
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.attr("class", "test");
        sourceEl.attr("style", "color: red");

        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals(sourceEl.tagName(), meta.el.tagName());
        assertEquals(sourceEl.childrenSize(), meta.numAttribsDiscarded);
    }

}