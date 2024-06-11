package org.jsoup.safety;

import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);

        Element div = new Element(Tag.valueOf("div"), "");
        body.appendChild(div);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(cleanDocument.body().childNodeSize(), 1);
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basicWithImages();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        body.appendChild(new Element(Tag.valueOf("img"), ""));
        dirtyDocument.appendChild(body);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        source.appendChild(new TextNode("Test text", ""));

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(dest.childNodeSize(), 1);
        assertEquals(numDiscarded, 0);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element(Tag.valueOf("a"), "");
        sourceEl.attr("href", "#");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals(elementMeta.numAttribsDiscarded, 1);
        assertTrue(elementMeta.el.hasAttr("href"));
    }

}