package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        assertEquals(true, Tag.isKnownTag("html"));
        assertEquals(true, Tag.isKnownTag("p"));
        assertEquals(true, Tag.isKnownTag("body"));
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

        assertTrue(html.isBlock());
        assertFalse(p.isBlock());
        assertFalse(unknown.isBlock());

        assertTrue(html.formatAsBlock());
        assertTrue(p.formatAsBlock());
        assertTrue(unknown.formatAsBlock());

        assertTrue(html.canContainBlock());
        assertTrue(p.canContainBlock());
        assertTrue(unknown.canContainBlock());

        assertFalse(html.isEmpty());
        assertFalse(p.isEmpty());
        assertTrue(unknown.isEmpty());

        assertFalse(html.isSelfClosing());
        assertFalse(p.isSelfClosing());
        assertFalse(unknown.isSelfClosing());

        assertFalse(html.preserveWhitespace());
        assertFalse(p.preserveWhitespace());
        assertFalse(unknown.preserveWhitespace());

        assertEquals(Tag.blockTags, html.getBlockTags());
        assertEquals(Tag.inlineTags, p.getInlineTags());
        assertNull(unknown.getBlockTags());
        assertNull(unknown.getInlineTags());
    }

    @Test
    public void testBlockTags() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");

        assertTrue(html.isBlock());
        assertTrue(p.isInline());

        assertArrayEquals(Tag.blockTags, html.getBlockTags());
        assertArrayEquals(Tag.inlineTags, p.getInlineTags());

        assertEquals(Tag.blockTags.length, html.getBlockTags().length);
        assertEquals(Tag.inlineTags.length, p.getInlineTags().length);
    }

    @Test
    public void testGetInlineTags() {
        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");

        assertTrue(html.isBlock());
        assertTrue(p.isInline());

        assertArrayEquals(Tag.blockTags, html.getBlockTags());
        assertArrayEquals(Tag.inlineTags, p.getInlineTags());

        assertEquals(Tag.blockTags.length, html.getBlockTags().length);
        assertEquals(Tag.inlineTags.length, p.getInlineTags().length);
    }

    @Test
    public void testEmptyTags() {
        Tag meta = Tag.valueOf("meta");
        Tag img = Tag.valueOf("img");
        Tag br = Tag.valueOf("br");

        assertFalse(meta.isBlock());
        assertFalse(meta.isInline());
        assertFalse(meta.isSelfClosing());
        assertFalse(meta.isEmpty());

        assertFalse(img.isBlock());
        assertFalse(img.isInline());
        assertFalse(img.isSelfClosing());
        assertTrue(img.isEmpty());

        assertFalse(br.isBlock());
        assertFalse(br.isInline());
        assertTrue(br.isSelfClosing());
        assertTrue(br.isEmpty());
    }

    @Test
    public void testFormatAsInlineTags() {
        Tag title = Tag.valueOf("title");
        Tag p = Tag.valueOf("p");

        assertFalse(title.isBlock());
        assertTrue(title.formatAsBlock());

        assertTrue(p.isInline());
        assertFalse(p.formatAsBlock());

        assertArrayEquals(new String[]{"title"}, title.getFormatAsInlineTags());
        assertArrayEquals(new String[0], p.getFormatAsInlineTags());
    }

    @Test
    public void testPreserveWhitespaceTags() {
        Tag pre = Tag.valueOf("pre");
        Tag plaintext = Tag.valueOf("plaintext");

        assertTrue(pre.isBlock());
        assertTrue(pre.preserveWhitespace());

        assertTrue(plaintext.isInline());
        assertTrue(plaintext.preserveWhitespace());

        assertArrayEquals(new String[]{"pre"}, pre.getPreserveWhitespaceTags());
        assertArrayEquals(new String[]{"plaintext"}, plaintext.getPreserveWhitespaceTags());
    }
}