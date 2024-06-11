package org.jsoup.safety;

import org.jsoup.Jsoup;
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
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }

    @Test
    public void testClean() {
        String html = "<html><head><title>Test</title></head><body><div><p>Hello World!</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><div><p>Hello World!</p></div></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        String html = "<html><head><title>Test</title></head><body><div><p>Hello World!</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCleaningVisitor() {
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);

        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.text("Hello World!");

        cleaningVisitor.head(sourceEl, 0);

        assertEquals("<p>Hello World!</p>", destination.html());

        cleaningVisitor.tail(sourceEl, 0);

        assertEquals("<div><p>Hello World!</p></div>", destination.html());
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
        assertEquals("<div></div>", dest.html());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.attr("class", "test");
        sourceEl.attr("id", "1");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals("<p></p>", elementMeta.el.html());
        assertEquals(0, elementMeta.numAttribsDiscarded);

        Attributes destAttrs = elementMeta.el.attributes();
        assertEquals("test", destAttrs.get("class"));
        assertEquals("1", destAttrs.get("id"));
    }
}