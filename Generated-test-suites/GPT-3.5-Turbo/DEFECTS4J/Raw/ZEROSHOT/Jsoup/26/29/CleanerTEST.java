package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
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
        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().append("<div>Hello, <b>world</b>!</div>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("http://example.com", cleanDocument.baseUri());
        assertEquals("<body><div>Hello, <b>world</b>!</div></body>", cleanDocument.body().outerHtml());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().append("<p>Valid document</p>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        source.appendChild(new TextNode("Hello, "));
        Element bold = new Element(Tag.valueOf("b"), "");
        bold.appendChild(new TextNode("world"));
        source.appendChild(bold);
        source.appendChild(new TextNode("!"));

        Element dest = new Element(Tag.valueOf("body"), "");
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
        assertEquals("<body>Hello, <b>world</b>!</body>", dest.outerHtml());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        Attributes sourceAttrs = new Attributes();
        sourceAttrs.put("id", "exampleId");
        sourceAttrs.put("class", "exampleClass");
        sourceEl.attributes().addAll(sourceAttrs);

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertNotNull(meta);
        assertEquals("div", meta.el.tagName());
        assertEquals(2, meta.el.attributes().size());
        assertEquals("exampleId", meta.el.attr("id"));
        assertEquals("exampleClass", meta.el.attr("class"));
        assertEquals(0, meta.numAttribsDiscarded);
    }
}