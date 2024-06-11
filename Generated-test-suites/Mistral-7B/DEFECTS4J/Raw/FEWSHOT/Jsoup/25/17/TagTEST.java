package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {

    @Test
    public void testNewTag() {
        Tag html = Tag.valueOf("html");
        assertEquals("html", html.getName());
        assertTrue(html.isBlock());
        assertTrue(html.formatAsBlock());
        assertTrue(html.canContainBlock());
        assertFalse(html.isInline());
        assertFalse(html.isEmpty());
        assertFalse(html.isSelfClosing());
        assertFalse(html.preserveWhitespace());
    }

    @Test
    public void testUnknownTag() {
        Tag unknown = Tag.valueOf("_unknown_");
        assertEquals("_unknown_", unknown.getName());
        assertFalse(unknown.isBlock());
        assertTrue(unknown.canContainBlock());
        assertTrue(unknown.canContainInline());
        assertFalse(unknown.isEmpty());
        assertFalse(unknown.isSelfClosing());
        assertFalse(unknown.preserveWhitespace());
    }

    @Test
    public void testBlockTags() {
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isBlock());
            assertTrue(tag.formatAsBlock());
            assertTrue(tag.canContainBlock());
            assertFalse(tag.isInline());
            assertFalse(tag.isEmpty());
            assertFalse(tag.isSelfClosing());
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
            assertFalse(tag.isInline());
            assertFalse(tag.isEmpty());
            assertFalse(tag.isSelfClosing());
            assertFalse(tag.preserveWhitespace());
        }
    }

    @Test
    public void testEmptyTags() {
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertFalse(tag.isInline());
            assertTrue(tag.isEmpty());
            assertFalse(tag.isSelfClosing());
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
            assertFalse(tag.isInline());
            assertFalse(tag.isEmpty());
            assertFalse(tag.isSelfClosing());
            assertTrue(tag.formatAsBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertFalse(tag.isInline());
            assertFalse(tag.isEmpty());
            assertFalse(tag.isSelfClosing());
            assertTrue(tag.preserveWhitespace());
        }
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("html"));
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("_unknown_"));
    }

    @Test
    public void testRegisterTag() {
        Tag tag = new Tag("_testTag_");
        tag.isBlock = false;
        tag.formatAsBlock = false;
        tag.canContainBlock = true;
        tag.canContainInline = false;
        tag.empty = false;
        tag.isSelfClosing = false;
        tag.preserveWhitespace = false;
        Tag.register(tag);
        assertTrue(Tag.isKnownTag("_testTag_"));
    }

    @Test
    public void testEqualsHashcode() {
        Tag tag1 = new Tag("h1");
        Tag tag2 = new Tag("h1");

        assertTrue(tag1.equals(tag2));
        assertEquals(tag1.hashCode(), tag2.hashCode());

        Tag tag3 = new Tag("h2");
        assertFalse(tag1.equals(tag3));
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }
}