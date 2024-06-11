package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        Tag html = Tag.valueOf("html");
        assertTrue(Tag.isKnownTag(html.getName()));

        Tag unknown = new Tag("unknown");
        assertFalse(Tag.isKnownTag(unknown.getName()));

        assertEquals(html, Tag.valueOf(html.getName()));
        assertNotSame(html, Tag.valueOf("HTML"));
    }

    @Test
    public void testIsBlockTag() {
        Tag p = Tag.valueOf("p");
        assertTrue(p.isBlock());

        Tag span = Tag.valueOf("span");
        assertFalse(span.isBlock());
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
        assertTrue(div.canContainBlock());

        Tag img = Tag.valueOf("img");
        assertFalse(img.canContainBlock());
    }

    @Test
    public void testIsEmptyTag() {
        Tag img = Tag.valueOf("img");
        assertTrue(img.isEmpty());

        Tag p = Tag.valueOf("p");
        assertFalse(p.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag img = Tag.valueOf("img");
        assertTrue(img.isSelfClosing());

        Tag p = Tag.valueOf("p");
        assertFalse(p.isSelfClosing());
    }

    @Test
    public void testIsDataTag() {
        Tag script = Tag.valueOf("script");
        assertTrue(script.isData());

        Tag p = Tag.valueOf("p");
        assertFalse(p.isData());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag pre = Tag.valueOf("pre");
        assertTrue(pre.preserveWhitespace());

        Tag p = Tag.valueOf("p");
        assertFalse(p.preserveWhitespace());
    }

    @Test
    public void testEquals() {
        Tag p1 = Tag.valueOf("p");
        Tag p2 = Tag.valueOf("p");
        assertEquals(p1, p2);

        Tag p3 = new Tag("p");
        assertNotSame(p1, p3);

        Tag div1 = Tag.valueOf("div");
        Tag div2 = new Tag("DIV");
        assertFalse(div1.equals(div2));
    }

    @Test
    public void testHashCode() {
        Tag p = Tag.valueOf("p");
        Tag q = Tag.valueOf("p");
        assertEquals(p.hashCode(), q.hashCode());

        Tag div = Tag.valueOf("div");
        Tag span = Tag.valueOf("span");
        assertNotEquals(div.hashCode(), span.hashCode());
    }

    @Test
    public void testToString() {
        Tag p = Tag.valueOf("p");
        assertEquals("p", p.toString());
    }
}