package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag html = Tag.valueOf("html");
        assertEquals("html", html.getName());
        assertTrue(html.isBlock());
        assertTrue(html.formatAsBlock());
        assertTrue(html.canContainBlock());
        assertFalse(html.isInline());
        assertFalse(html.isEmpty());
        assertFalse(html.selfClosing());
        assertFalse(html.preserveWhitespace());
        assertNull(html.register(new Tag("unknown")));
    }

    @Test
    public void testKnownTags() {
        assertEquals(Tag.html(), Tag.valueOf("html"));
        assertEquals(Tag.p(), Tag.valueOf("p"));
        assertEquals(Tag.div(), Tag.valueOf("div"));
        assertEquals(Tag.span(), Tag.valueOf("span"));
        assertEquals(Tag.a(), Tag.valueOf("a"));
        assertEquals(Tag.img(), Tag.valueOf("img"));
        assertEquals(Tag.form(), Tag.valueOf("form"));
        assertEquals(Tag.input(), Tag.valueOf("input"));
        assertEquals(Tag.textarea(), Tag.valueOf("textarea"));
        assertEquals(Tag.button(), Tag.valueOf("button"));
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertTrue(Tag.isKnownTag("span"));
        assertTrue(Tag.isKnownTag("a"));
        assertTrue(Tag.isKnownTag("img"));
        assertTrue(Tag.isKnownTag("form"));
        assertTrue(Tag.isKnownTag("input"));
        assertTrue(Tag.isKnownTag("textarea"));
        assertTrue(Tag.isKnownTag("button"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testUnknownTags() {
        Tag unknown = Tag.valueOf("unknown");
        assertNotNull(unknown);
        assertEquals("unknown", unknown.getName());
        assertFalse(unknown.isBlock());
        assertTrue(unknown.formatAsBlock());
        assertTrue(unknown.canContainBlock());
        assertTrue(unknown.canContainInline());
        assertFalse(unknown.isEmpty());
        assertFalse(unknown.selfClosing());
        assertFalse(unknown.preserveWhitespace());
    }

    @Test
    public void testEmptyTags() {
        Tag meta = Tag.valueOf("meta");
        Tag img = Tag.valueOf("img");
        assertTrue(meta.isEmpty());
        assertTrue(img.isEmpty());

        Tag link = Tag.valueOf("link");
        assertFalse(link.isEmpty());

        Tag frame = Tag.valueOf("frame");
        assertTrue(frame.isEmpty());

        Tag br = Tag.valueOf("br");
        assertTrue(br.isEmpty());
    }

    @Test
    public void testBlockTags() {
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isBlock());
            assertTrue(tag.formatAsBlock());
            assertTrue(tag.canContainBlock());
            assertFalse(tag.isEmpty());
            assertFalse(tag.selfClosing());
            assertFalse(tag.preserveWhitespace());
        }
    }

    @Test
    public void testInlineTags() {
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertFalse(tag.isEmpty());
            assertFalse(tag.selfClosing());
            assertFalse(tag.preserveWhitespace());
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertFalse(tag.isEmpty());
            assertFalse(tag.selfClosing());
            assertFalse(tag.preserveWhitespace());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertFalse(tag.isEmpty());
            assertFalse(tag.selfClosing());
            assertTrue(tag.preserveWhitespace());
        }
    }
}