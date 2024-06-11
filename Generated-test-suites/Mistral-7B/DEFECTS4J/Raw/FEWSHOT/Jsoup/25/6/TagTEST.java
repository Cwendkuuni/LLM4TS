package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        Tag htmlTag = Tag.valueOf("html");
        assertTrue(Tag.isKnownTag(htmlTag.getName()));

        Tag unknownTag = new Tag("unknown");
        assertFalse(Tag.isKnownTag(unknownTag.getName()));
    }

    @Test
    public void testGetTagByName() {
        Tag htmlTag = Tag.valueOf("html");
        Tag newHtmlTag = Tag.valueOf("html");
        assertEquals(htmlTag, newHtmlTag);

        Tag pTag = Tag.valueOf("p");
        Tag newpTag = Tag.valueOf("p");
        assertEquals(pTag, newpTag);

        Tag unknownTag = new Tag("unknown");
        Tag newUnknownTag = new Tag("unknown");
        assertNotEquals(unknownTag, newUnknownTag);
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
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertTrue(tag.canContainInline());
            assertFalse(tag.canContainBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertFalse(tag.canContainInline());
            assertTrue(tag.preserveWhitespace());
        }
    }
}