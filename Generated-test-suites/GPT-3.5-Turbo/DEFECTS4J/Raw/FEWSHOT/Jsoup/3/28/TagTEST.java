package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isInline());
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testCanContain() {
        Tag parent = Tag.valueOf("div");
        Tag childInline = Tag.valueOf("span");
        assertTrue(parent.canContain(childInline));

        Tag childBlock = Tag.valueOf("p");
        assertTrue(parent.canContain(childBlock));
    }

    @Test
    public void testIsBlock() {
        Tag blockTag = Tag.valueOf("div");
        assertTrue(blockTag.isBlock());

        Tag inlineTag = Tag.valueOf("span");
        assertFalse(inlineTag.isBlock());
    }

    @Test
    public void testIsInline() {
        Tag inlineTag = Tag.valueOf("span");
        assertTrue(inlineTag.isInline());

        Tag blockTag = Tag.valueOf("div");
        assertFalse(blockTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag dataTag = Tag.valueOf("script");
        assertTrue(dataTag.isData());

        Tag blockTag = Tag.valueOf("div");
        assertFalse(blockTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag emptyTag = Tag.valueOf("br");
        assertTrue(emptyTag.isEmpty());

        Tag nonEmptyTag = Tag.valueOf("div");
        assertFalse(nonEmptyTag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.preserveWhitespace());

        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag parentTag = Tag.valueOf("div");
        Tag childTag = Tag.valueOf("span");
        childTag.setAncestor("div");
        assertEquals(parentTag, childTag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parentTag = Tag.valueOf("div");
        Tag childTag = Tag.valueOf("span");
        childTag.setAncestor("div");
        assertTrue(parentTag.isValidParent(childTag));
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.toString());
    }
}