package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit 4 test file for Tag class.
 */
public class TagTest {

    @Test
    public void testTagCreation() {
        // Test creating a known tag.
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.getName());
        assertTrue(pTag.isKnownTag());

        // Test creating an unknown tag.
        Tag unknownTag = Tag.valueOf("unknown");
        assertEquals("unknown", unknownTag.getName());
        assertFalse(unknownTag.isKnownTag());
    }

    @Test
    public void testBlockTags() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());
        assertTrue(pTag.formatAsBlock());
        assertTrue(pTag.canContainBlock());
        assertTrue(pTag.canContainInline());

        // Test block tags defined in the class.
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isBlock());
            assertTrue(tag.formatAsBlock());
            assertTrue(tag.canContainBlock());
            assertTrue(tag.canContainInline());
        }
    }

    @Test
    public void testInlineTags() {
        Tag spanTag = Tag.valueOf("span");
        assertFalse(spanTag.isBlock());
        assertFalse(spanTag.formatAsBlock());
        assertFalse(spanTag.canContainBlock());
        assertTrue(spanTag.canContainInline());

        // Test inline tags defined in the class.
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
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isBlock());
        assertFalse(imgTag.formatAsBlock());
        assertFalse(imgTag.canContainBlock());
        assertTrue(imgTag.isEmpty());

        // Test empty tags defined in the class.
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertFalse(tag.canContainBlock());
            assertTrue(tag.isEmpty());
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());
        assertTrue(pTag.formatAsBlock());

        Tag titleTag = Tag.valueOf("title");
        assertFalse(titleTag.isBlock());
        assertFalse(titleTag.formatAsBlock());
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        Tag preTag = Tag.valueOf("pre");
        assertFalse(preTag.isBlock());
        assertFalse(preTag.formatAsBlock());

        Tag preTagInstance = new Tag("pre");
        preTagInstance.preserveWhitespace = true;
        assertEquals(preTagInstance, preTag.setPreserveWhitespace(true));

        // Test preserve whitespace tags defined in the class.
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
            assertTrue(tag.preserveWhitespace());
        }
    }

}