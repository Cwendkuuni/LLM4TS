
package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("");
        dirtyDocument.body().appendChild(new Element(Tag.valueOf("p"), ""));
        dirtyDocument.body().appendChild(new Element(Tag.valueOf("script"), ""));
        dirtyDocument.body().appendChild(new TextNode("Hello World"));

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertFalse(cleanDocument.body().getElementsByTag("p").isEmpty());
        assertTrue(cleanDocument.body().getElementsByTag("script").isEmpty());
        assertFalse(cleanDocument.body().text().isEmpty());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("");
        dirtyDocument.body().appendChild(new Element(Tag.valueOf("p"), ""));
        dirtyDocument.body().appendChild(new Element(Tag.valueOf("script"), ""));
        dirtyDocument.body().appendChild(new TextNode("Hello World"));

        assertFalse(cleaner.isValid(dirtyDocument));

        dirtyDocument.body().getElementsByTag("script").remove();

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("");
        dirtyDocument.body().appendChild(new Element(Tag.valueOf("p"), ""));
        dirtyDocument.body().appendChild(new Element(Tag.valueOf("script"), ""));
        dirtyDocument.body().appendChild(new TextNode("Hello World"));

        Document cleanDocument = Document.createShell("");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertFalse(cleanDocument.body().getElementsByTag("p").isEmpty());
        assertTrue(cleanDocument.body().getElementsByTag("script").isEmpty());
        assertFalse(cleanDocument.body().text().isEmpty());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.attr("class", "paragraph");

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals(sourceEl.tagName(), meta.el.tagName());
        assertEquals(sourceEl.baseUri(), meta.el.baseUri());
        assertEquals(0, meta.numAttribsDiscarded);
        assertEquals(1, meta.el.attributes().size());
        assertTrue(meta.el.attributes().hasKey("class"));
        assertEquals(sourceEl.attr("class"), meta.el.attr("class"));
    }

}
