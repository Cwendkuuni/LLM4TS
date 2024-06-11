package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        Tag html = Tag.valueOf("html");
        assertTrue(html.isKnownTag());

        Tag unknown = new Tag("unknown");
        assertFalse(unknown.isKnownTag());
    }

    @Test
    public void testValueOf() {
        Tag html = Tag.valueOf("html");
        Tag div = Tag.valueOf("div");
        Tag p = Tag.valueOf("p");

        assertEquals("html", html.getName());
        assertEquals("div", div.getName());
        assertEquals("p", p.getName());
    }

    @Test
    public void testIsBlock() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");

        assertTrue(html.isBlock());
        assertTrue(p.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag p = Tag.valueOf("p");
        assertTrue(p.formatAsBlock());

        Tag span = Tag.valueOf("span");
        assertFalse(span.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag div = Tag.valueOf("div");
        Tag p = Tag.valueOf("p");
        Tag span = Tag.valueOf("span");

        assertTrue(div.canContainBlock());
        assertTrue(p.canContainBlock());
        assertFalse(span.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag span = Tag.valueOf("span");
        assertFalse(span.isBlock());
        assertTrue(span.isInline());
    }

    @Test
    public void testIsData() {
        Tag script = Tag.valueOf("script");
        assertTrue(script.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag img = Tag.valueOf("img");
        assertFalse(img.isEmpty());

        Tag meta = Tag.valueOf("meta");
        assertTrue(meta.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag img = Tag.valueOf("img");
        assertTrue(img.isSelfClosing());

        Tag div = Tag.valueOf("div");
        assertFalse(div.isSelfClosing());
    }

    @Test
    public void testIsKnownTagArray() {
        assertTrue(Tag.isKnownTag("html"));
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag pre = Tag.valueOf("pre");
        assertTrue(pre.preserveWhitespace());

        Tag p = Tag.valueOf("p");
        assertFalse(p.preserveWhitespace());
    }

    @Test
    public void testHashCodeEquals() {
        Tag p1 = Tag.valueOf("p");
        Tag p2 = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");

        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), div.hashCode());
    }
}