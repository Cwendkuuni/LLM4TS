package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testName() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContain() {
        Tag parent = new Tag("div");
        Tag child = new Tag("span");
        assertTrue(parent.canContain(child));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("ul");
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
    public void testIsData() {
        Tag tag = new Tag("textarea");
        assertTrue(tag.isData());
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
        Tag tag = new Tag("li");
        assertNull(tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parent = new Tag("ul");
        Tag child = new Tag("li");
        assertTrue(parent.isValidParent(child));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("a");
        Tag tag2 = new Tag("a");
        assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("div");
        assertEquals(106947, tag.hashCode());
    }
}