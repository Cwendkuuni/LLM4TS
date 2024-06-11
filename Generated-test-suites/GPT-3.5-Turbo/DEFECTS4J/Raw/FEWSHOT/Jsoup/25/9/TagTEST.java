package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag blockTag = Tag.valueOf("div");
        assertTrue(blockTag.isBlock());

        Tag inlineTag = Tag.valueOf("span");
        assertFalse(inlineTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag blockTag = Tag.valueOf("div");
        assertTrue(blockTag.formatAsBlock());

        Tag inlineTag = Tag.valueOf("span");
        assertFalse(inlineTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag blockTag = Tag.valueOf("div");
        assertTrue(blockTag.canContainBlock());

        Tag inlineTag = Tag.valueOf("span");
        assertTrue(inlineTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag blockTag = Tag.valueOf("div");
        assertFalse(blockTag.isInline());

        Tag inlineTag = Tag.valueOf("span");
        assertTrue(inlineTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag blockTag = Tag.valueOf("div");
        assertFalse(blockTag.isData());

        Tag inlineTag = Tag.valueOf("span");
        assertTrue(inlineTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag emptyTag = Tag.valueOf("img");
        assertTrue(emptyTag.isEmpty());

        Tag nonEmptyTag = Tag.valueOf("div");
        assertFalse(nonEmptyTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag emptyTag = Tag.valueOf("img");
        assertTrue(emptyTag.isSelfClosing());

        Tag nonEmptyTag = Tag.valueOf("div");
        assertFalse(nonEmptyTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag knownTag = Tag.valueOf("div");
        assertTrue(knownTag.isKnownTag());

        Tag unknownTag = Tag.valueOf("custom");
        assertFalse(unknownTag.isKnownTag());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag withWhitespace = Tag.valueOf("pre");
        assertTrue(withWhitespace.preserveWhitespace());

        Tag withoutWhitespace = Tag.valueOf("div");
        assertFalse(withoutWhitespace.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("custom");
        assertFalse(tag.isSelfClosing());

        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.equals(tag2));

        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());

        Tag tag3 = Tag.valueOf("span");
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());
    }
}