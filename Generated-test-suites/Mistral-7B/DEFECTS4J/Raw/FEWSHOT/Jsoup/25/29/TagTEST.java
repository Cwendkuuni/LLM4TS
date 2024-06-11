package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jsoup.parser.Tag;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        assertEquals(true, Tag.isKnownTag("html"));
        assertEquals(true, Tag.isKnownTag("p"));
        assertEquals(false, Tag.isKnownTag("unknown"));
    }

    @Test
    public void testValueOf() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag unknown = Tag.valueOf("unknown");

        assertEquals("html", html.getName());
        assertEquals("p", p.getName());
        assertEquals("unknown", unknown.getName());
        assertEquals(html, Tag.valueOf("HTML"));
        assertEquals(html, Tag.valueOf("HTML".toLowerCase()));
        assertEquals(p, Tag.valueOf("P"));
        assertEquals(p, Tag.valueOf("p".toLowerCase()));
        assertEquals(unknown, Tag.valueOf("unknown"));
        assertEquals(unknown, Tag.valueOf("UNKNOWN"));
    }

    @Test
    public void testIsBlockTag() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");
        Tag img = Tag.valueOf("img");

        assertTrue(html.isBlock());
        assertTrue(p.isBlock());
        assertTrue(div.isBlock());
        assertFalse(img.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");
        Tag img = Tag.valueOf("img");

        assertTrue(html.formatAsBlock());
        assertTrue(p.formatAsBlock());
        assertFalse(div.formatAsBlock());
        assertFalse(img.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");
        Tag img = Tag.valueOf("img");

        assertTrue(html.canContainBlock());
        assertTrue(p.canContainBlock());
        assertTrue(div.canContainBlock());
        assertFalse(img.canContainBlock());
    }

    @Test
    public void testIsEmpty() {
        Tag meta = Tag.valueOf("meta");
        Tag img = Tag.valueOf("img");
        Tag br = Tag.valueOf("br");

        assertFalse(meta.isEmpty());
        assertFalse(img.isEmpty());
        assertTrue(br.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag img = Tag.valueOf("img");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");

        assertFalse(img.isSelfClosing());
        assertFalse(p.isSelfClosing());
        assertFalse(div.isSelfClosing());
        assertTrue(img.setSelfClosing().isSelfClosing());
    }

    @Test
    public void testIsKnownTagArray() {
        Tag[] knownTags = Tag.values();
        for (Tag tag : knownTags) {
            assertTrue(Tag.isKnownTag(tag.getName()));
        }
    }
}