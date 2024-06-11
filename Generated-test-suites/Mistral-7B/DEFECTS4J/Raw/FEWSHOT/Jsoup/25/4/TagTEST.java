package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagValueOf() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag unknown = Tag.valueOf("unknown");

        assertEquals("html", html.getName());
        assertEquals("p", p.getName());
        assertNotSame(html, p);
        assertTrue(html.isKnownTag());
        assertTrue(p.isKnownTag());
        assertFalse(unknown.isKnownTag());
        assertEquals(Tag.class, unknown.getClass());
    }

    @Test
    public void testTagIsBlock() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag br = Tag.valueOf("br");
        Tag img = Tag.valueOf("img");
        Tag input = Tag.valueOf("input");

        assertTrue(html.isBlock());
        assertTrue(p.isBlock());
        assertFalse(br.isBlock());
        assertFalse(img.isBlock());
        assertFalse(input.isBlock());
    }

    @Test
    public void testTagFormatAsBlock() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag br = Tag.valueOf("br");
        Tag pre = Tag.valueOf("pre");

        assertTrue(html.formatAsBlock());
        assertTrue(p.formatAsBlock());
        assertTrue(pre.formatAsBlock());
        assertFalse(br.formatAsBlock());
    }

    @Test
    public void testTagCanContainBlock() {
        Tag html = Tag.valueOf("html");
        Tag body = Tag.valueOf("body");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");

        assertTrue(html.canContainBlock());
        assertTrue(body.canContainBlock());
        assertTrue(div.canContainBlock());
        assertTrue(p.canContainBlock());
    }

    @Test
    public void testTagCanContainInline() {
        Tag html = Tag.valueOf("html");
        Tag body = Tag.valueOf("body");
        Tag p = Tag.valueOf("p");
        Tag span = Tag.valueOf("span");

        assertTrue(html.canContainInline());
        assertTrue(body.canContainInline());
        assertTrue(p.canContainInline());
        assertTrue(span.canContainInline());
    }

    @Test
    public void testTagIsData() {
        Tag script = Tag.valueOf("script");
        Tag style = Tag.valueOf("style");

        assertTrue(script.isData());
        assertTrue(style.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag img = Tag.valueOf("img");
        Tag input = Tag.valueOf("input");
        Tag meta = Tag.valueOf("meta");

        assertFalse(img.isEmpty());
        assertFalse(input.isEmpty());
        assertTrue(meta.isEmpty());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag img = Tag.valueOf("img");
        Tag input = Tag.valueOf("input");
        Tag br = Tag.valueOf("br");

        assertFalse(img.isSelfClosing());
        assertFalse(input.isSelfClosing());
        assertTrue(br.isSelfClosing());
    }

    @Test
    public void testTagIsKnownTag() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag unknown = Tag.valueOf("unknown");

        assertTrue(Tag.isKnownTag("html"));
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag pre = Tag.valueOf("pre");
        Tag div = Tag.valueOf("div");

        assertFalse(pre.preserveWhitespace());
        assertFalse(div.preserveWhitespace());
    }

    @Test
    public void testTagEquals() {
        Tag p1 = Tag.valueOf("p");
        Tag p2 = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");

        assertTrue(p1.equals(p2));
        assertFalse(p1.equals(div));
    }

    @Test
    public void testTagHashCode() {
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");

        int hashcodeP = p.hashCode();
        int hashcodeDiv = div.hashCode();

        assertTrue(hashcodeP != hashcodeDiv);
    }
}