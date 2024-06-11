package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("test");
        assertEquals("test", tag.getName());
        assertFalse(tag.isBlock());
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testCanContain() {
        Tag parent = Tag.valueOf("div");
        Tag child1 = Tag.valueOf("p");
        Tag child2 = Tag.valueOf("span");

        assertTrue(parent.canContain(child1));
        assertFalse(parent.canContain(child2));
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("h1");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("em");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag child = Tag.valueOf("div");
        child.getImplicitParent();
    }

    @Test
    public void testIsValidParent() {
        Tag parent = Tag.valueOf("div");
        Tag child = Tag.valueOf("p");
        assertTrue(parent.isValidParent(child));
    }
}