package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("html"));
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testValueOf() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag unknown = Tag.valueOf("unknown");

        assertEquals("html", html.getName());
        assertEquals("p", p.getName());
        assertNotEquals(html, unknown);
        assertTrue(html.isKnownTag());
        assertFalse(unknown.isKnownTag());
    }

    @Test
    public void testIsBlock() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag img = Tag.valueOf("img");

        assertTrue(html.isBlock());
        assertTrue(p.isBlock());
        assertFalse(img.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag img = Tag.valueOf("img");

        assertTrue(html.formatAsBlock());
        assertTrue(p.formatAsBlock());
        assertFalse(img.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag html = Tag.valueOf("html");
        Tag body = Tag.valueOf("body");
        Tag p = Tag.valueOf("p");
        Tag img = Tag.valueOf("img");

        assertTrue(html.canContainBlock());
        assertTrue(body.canContainBlock());
        assertTrue(p.canContainBlock());
        assertFalse(img.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag img = Tag.valueOf("img");

        assertFalse(html.isInline());
        assertFalse(p.isInline());
        assertTrue(img.isInline());
    }

    @Test
    public void testIsData() {
        Tag p = Tag.valueOf("p");
        Tag script = Tag.valueOf("script");

        assertFalse(p.isData());
        assertTrue(script.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag img = Tag.valueOf("img");
        Tag br = Tag.valueOf("br");

        assertFalse(img.isEmpty());
        assertTrue(br.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag img = Tag.valueOf("img");
        Tag p = Tag.valueOf("p");

        assertFalse(img.isSelfClosing());
        assertFalse(p.isSelfClosing());
    }

    @Test
    public void testIsKnownTagWithNull() {
        assertFalse(Tag.isKnownTag(null));
    }

    @Test
    public void testValueOfWithNull() {
        assertNull(Tag.valueOf(null));
    }
}