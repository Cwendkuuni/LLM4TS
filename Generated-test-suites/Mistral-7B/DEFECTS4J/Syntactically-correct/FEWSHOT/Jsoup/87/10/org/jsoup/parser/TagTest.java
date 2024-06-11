package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {

    @Test
    public void testBlockTags() {
        Tag tag = Tag.valueOf("html");
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        // test other block tags similarly
    }

    @Test
    public void testInlineTags() {
        Tag tag = Tag.valueOf("a");
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        // test other inline tags similarly
    }

    @Test
    public void testEmptyTags() {
        Tag tag = Tag.valueOf("img");
        assertFalse(tag.isBlock());
        assertTrue(tag.empty);
        // test other empty tags similarly
    }

    @Test
    public void testFormatAsInlineTags() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.formatAsBlock());
        assertFalse(tag.isInline()); // not the same as !isBlock()
        // test other format as inline tags similarly
    }

    @Test
    public void testPreserveWhitespaceTags() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
        // test other preserve whitespace tags similarly
    }

    @Test
    public void testFormListedTags() {
        Tag tag = Tag.valueOf("input");
        assertTrue(tag.isFormListed());
        // test other form listed tags similarly
    }

    @Test
    public void testFormSubmitTags() {
        Tag tag = Tag.valueOf("input");
        assertTrue(tag.isFormSubmittable());
        // test other form submit tags similarly
    }

    @Test
    public void testUnknownTag() {
        Tag tag = Tag.valueOf("unknown");
        assertFalse(tag.isKnownTag());
        // test other unknown tags similarly
    }
}