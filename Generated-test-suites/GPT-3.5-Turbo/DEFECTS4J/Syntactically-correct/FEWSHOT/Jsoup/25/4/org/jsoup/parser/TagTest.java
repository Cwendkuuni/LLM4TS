package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("html");
        assertEquals("html", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("div");

        assertEquals(tag1, tag2);
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag blockTag = Tag.valueOf("div");
        Tag inlineTag = Tag.valueOf("span");

        assertTrue(blockTag.canContainBlock());
        assertFalse(inlineTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag inlineTag = Tag.valueOf("span");
        Tag dataTag = Tag.valueOf("script");

        assertTrue(inlineTag.isData());
        assertFalse(dataTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag emptyTag = Tag.valueOf("img");
        Tag selfClosingTag = Tag.valueOf("br");

        assertTrue(emptyTag.isSelfClosing());
        assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("customTag"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag whitespaceTag = Tag.valueOf("pre");
        Tag nonWhitespaceTag = Tag.valueOf("p");

        assertTrue(whitespaceTag.preserveWhitespace());
        assertFalse(nonWhitespaceTag.preserveWhitespace());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("span");
        assertEquals("span", tag.toString());
    }
}