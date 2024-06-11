package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testValueOf() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag unknownTag = Tag.valueOf("unknown");

        assertEquals("p", pTag.getName());
        assertEquals("div", divTag.getName());
        assertEquals("unknown", unknownTag.getName());

        assertTrue(pTag.isBlock());
        assertFalse(pTag.isInline());
        assertFalse(pTag.isData());
        assertFalse(pTag.isEmpty());
        assertFalse(pTag.isSelfClosing());
        assertFalse(pTag.preserveWhitespace());

        assertTrue(divTag.isBlock());
        assertFalse(divTag.isInline());
        assertFalse(divTag.isData());
        assertFalse(divTag.isEmpty());
        assertFalse(divTag.isSelfClosing());
        assertFalse(divTag.preserveWhitespace());

        assertFalse(unknownTag.isBlock());
        assertFalse(unknownTag.isInline());
        assertTrue(unknownTag.isData());
        assertFalse(unknownTag.isEmpty());
        assertFalse(unknownTag.isSelfClosing());
        assertFalse(unknownTag.preserveWhitespace());

        assertEquals(pTag, Tag.valueOf("p"));
        assertEquals(divTag, Tag.valueOf("div"));
        assertEquals(unknownTag, Tag.valueOf("unknown"));
    }

    @Test
    public void testBlockTags() {
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isBlock());
        }
    }

    @Test
    public void testInlineTags() {
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
        }
    }

    @Test
    public void testEmptyTags() {
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.isInline());
            assertTrue(tag.isEmpty());
        }
    }

    @Test
    public void testFormatAsBlockTags() {
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.formatAsBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertTrue(tag.preserveWhitespace());
        }
    }

    @Test
    public void testCanContainBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag tableTag = Tag.valueOf("table");
        Tag imgTag = Tag.valueOf("img");

        assertTrue(pTag.canContainBlock());
        assertTrue(divTag.canContainBlock());
        assertTrue(tableTag.canContainBlock());
        assertFalse(imgTag.canContainBlock());

        assertFalse(pTag.canContainInline());
        assertFalse(divTag.canContainInline());
        assertTrue(imgTag.canContainInline());
    }

    @Test
    public void testCanContainInline() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag imgTag = Tag.valueOf("img");

        assertFalse(pTag.canContainInline());
        assertFalse(divTag.canContainInline());
        assertTrue(imgTag.canContainInline());
    }

    @Test
    public void testIsData() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag scriptTag = Tag.valueOf("script");

        assertFalse(pTag.isData());
        assertFalse(divTag.isData());
        assertTrue(scriptTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        Tag inputTag = Tag.valueOf("input");
        Tag metaTag = Tag.valueOf("meta");

        assertFalse(imgTag.isEmpty());
        assertFalse(inputTag.isEmpty());
        assertTrue(metaTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        Tag inputTag = Tag.valueOf("input");
        Tag brTag = Tag.valueOf("br");

        assertFalse(imgTag.isSelfClosing());
        assertTrue(inputTag.isSelfClosing());
        assertTrue(brTag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag pTag2 = Tag.valueOf("p");

        assertEquals(pTag, pTag);
        assertEquals(pTag, pTag2);
        assertEquals(pTag, Tag.valueOf("p"));
        assertNotEquals(pTag, divTag);
    }

    @Test
    public void testHashCode() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag pTag2 = Tag.valueOf("p");

        int hashCodeP = pTag.hashCode();
        int hashCodeDiv = divTag.hashCode();
        int hashCodeP2 = pTag2.hashCode();

        assertEquals(hashCodeP, hashCodeP);
        assertEquals(hashCodeP, hashCodeP2);
        assertNotEquals(hashCodeP, hashCodeDiv);
    }
}