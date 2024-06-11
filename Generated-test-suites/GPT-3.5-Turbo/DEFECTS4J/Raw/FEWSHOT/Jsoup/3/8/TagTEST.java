package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = new Tag("body");
        assertEquals("body", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.isBlock());
        assertFalse(tag.isInline());

        Tag tag2 = Tag.valueOf("img");
        assertEquals("img", tag2.getName());
        assertFalse(tag2.canContainBlock());
        assertFalse(tag2.isBlock());
        assertTrue(tag2.isInline());
        assertTrue(tag2.isEmpty());
    }

    @Test
    public void testCanContain() {
        Tag tag = Tag.valueOf("body");
        Tag child = Tag.valueOf("p");
        assertTrue(tag.canContain(child));

        Tag tag2 = Tag.valueOf("head");
        Tag child2 = Tag.valueOf("script");
        assertFalse(tag2.canContain(child2));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("span");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag tag = Tag.valueOf("p");
        Tag parent = Tag.valueOf("body");
        tag.getImplicitParent();
        assertEquals("body", parent.getName());
    }


    @Test
    public void testIsValidParent() {
        Tag tag = Tag.valueOf("p");
        Tag child = Tag.valueOf("span");
        child.setAncestor("p", "span");
        assertTrue(tag.isValidParent(child));

        Tag tag2 = Tag.valueOf("h1");
        Tag child2 = Tag.valueOf("body");
        assertFalse(tag2.isValidParent(child2));
    }

    @Test
    public void testEquals() {
        Tag tag = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag.equals(tag2));
    }
    

    @Test
    public void testToString() {
        Tag tag = new Tag("h1");
        assertEquals("h1", tag.toString());
    }
}