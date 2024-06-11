package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

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

        assertEquals(false, html.isBlock());
        assertEquals(true, p.isBlock());
        assertEquals(true, unknown.isBlock());

        assertEquals(true, html.formatAsBlock());
        assertEquals(true, p.formatAsBlock());
        assertEquals(true, unknown.formatAsBlock());

        assertEquals(true, html.canContainBlock());
        assertEquals(true, p.canContainBlock());
        assertEquals(true, unknown.canContainBlock());

        assertEquals(true, html.canContainInline());
        assertEquals(true, p.canContainInline());
        assertEquals(false, unknown.canContainInline());

        assertEquals(false, html.isEmpty());
        assertEquals(false, p.isEmpty());
        assertEquals(false, unknown.isEmpty());

        assertEquals(false, html.isSelfClosing());
        assertEquals(false, p.isSelfClosing());
        assertEquals(false, unknown.isSelfClosing());

        assertEquals(false, html.preserveWhitespace());
        assertEquals(false, p.preserveWhitespace());
        assertEquals(false, unknown.preserveWhitespace());

        assertEquals(Tag.class, html.getClass());
        assertEquals(Tag.class, p.getClass());
        assertEquals(Tag.class, unknown.getClass());
    }

    @Test
    public void testBlockTags() {
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isBlock());
            assertTrue(tag.formatAsBlock());
            assertTrue(tag.canContainBlock());
        }
    }

    @Test
    public void testInlineTags() {
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertTrue(tag.canContainInline());
        }
    }

    @Test
    public void testEmptyTags() {
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertFalse(tag.canContainInline());
            assertTrue(tag.isEmpty());
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.formatAsBlock());
            assertFalse(tag.isBlock());
            assertFalse(tag.canContainBlock());
            assertTrue(tag.canContainInline());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.preserveWhitespace());
            assertTrue(tag.formatAsBlock());
            assertTrue(tag.isBlock());
            assertTrue(tag.canContainBlock());
            assertTrue(tag.canContainInline());
        }
    }
}