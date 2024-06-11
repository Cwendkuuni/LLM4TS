package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("test");
        assertNotNull(tag);
    }

    @Test
    public void testCanContain() {
        Tag parent = Tag.valueOf("parent");
        Tag child = Tag.valueOf("child");
        assertTrue(parent.canContain(child));
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("test");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("test");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("test");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("test");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("test");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("test");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag parent = Tag.valueOf("parent");
        Tag child = Tag.valueOf("child");
        child.setAncestor(parent.getName());
        assertEquals(parent, child.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parent = Tag.valueOf("parent");
        Tag child = Tag.valueOf("child");
        child.setAncestor(parent.getName());
        assertTrue(parent.isValidParent(child));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("test");
        assertEquals("test".hashCode(), tag.hashCode());
    }

    @Test
    public void testCreateBlock() {
        Tag tag = Tag.valueOf("test");
        assertNotNull(tag);
    }

    @Test
    public void testCreateInline() {
        Tag tag = Tag.valueOf("test");
        assertNotNull(tag);
        assertFalse(tag.isBlock());
        assertFalse(tag.canContainBlock());
    }
}