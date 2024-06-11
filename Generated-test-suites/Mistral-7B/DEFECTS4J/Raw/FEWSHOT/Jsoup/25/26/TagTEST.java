package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("html"));
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknownTag"));
    }

    @Test
    public void testValueOf() {
        Tag html = Tag.valueOf("html");
        assertEquals("html", html.getName());
        assertFalse(html.isBlock());
        assertFalse(html.formatAsBlock());
        // other assertions for other properties as needed

        Tag p = Tag.valueOf("p");
        assertEquals("p", p.getName());
        assertTrue(p.isBlock());
        // other assertions for other properties as needed

        Tag unknownTag = Tag.valueOf("unknownTag");
        assertEquals("unknownTag", unknownTag.getName());
        assertFalse(unknownTag.isBlock()); // or use isInline() if you want to test that
        // other assertions for other properties as needed
    }

    @Test
    public void testBlockTags() {
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isBlock());
            assertTrue(tag.canContainBlock());
            // other assertions for other properties as needed
        }
    }

    @Test
    public void testInlineTags() {
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.canContainBlock());
            // other assertions for other properties as needed
        }
    }

    @Test
    public void testEmptyTags() {
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.canContainBlock());
            assertFalse(tag.canContainInline());
            assertTrue(tag.isEmpty());
            // other assertions for other properties as needed
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            // other assertions for other properties as needed
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.preserveWhitespace());
            // other assertions for other properties as needed
        }
    }
}