package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {
    @Test
    public void testName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOfKnownTag() {
        Tag tag = Tag.valueOf("p");
        assertEquals(tag, Tag.valueOf("p"));
    }

    @Test
    public void testValueOfUnknownTag() {
        Tag tag = Tag.valueOf("unknown");
        assertNotEquals(tag, Tag.valueOf("unknown"));
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("br");
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());

        tag = Tag.valueOf("br");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testCanContainInline() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainInline());

        tag = Tag.valueOf("br");
        assertTrue(tag.canContainInline());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isInline());

        tag = Tag.valueOf("br");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isData());

        tag = Tag.valueOf("script");
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isEmpty());

        tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.preserveWhitespace());

        tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag tag = Tag.valueOf("p");
        assertNull(tag.getImplicitParent());

        Tag parentTag = Tag.valueOf("body");
        tag = Tag.valueOf("a");
        parentTag.setAncestor(tag.getName());
        assertEquals(tag, parentTag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("a");
        assertTrue(tag1.isValidParent(tag2));

        tag1 = Tag.valueOf("p");
        tag2 = Tag.valueOf("div");
        assertFalse(tag1.isValidParent(tag2));
    }
}