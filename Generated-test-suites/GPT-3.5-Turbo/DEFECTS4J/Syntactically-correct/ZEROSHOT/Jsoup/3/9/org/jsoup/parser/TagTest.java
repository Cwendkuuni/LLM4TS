package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());

        tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testCanContain() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("a");
        
        assertTrue(tag1.canContain(tag2));
        assertFalse(tag2.canContain(tag1));
    }

    @Test
    public void testIsBlock() {
        Tag tag1 = Tag.valueOf("div");
        assertTrue(tag1.isBlock());
        
        Tag tag2 = Tag.valueOf("a");
        assertFalse(tag2.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag1 = Tag.valueOf("div");
        assertTrue(tag1.canContainBlock());
        
        Tag tag2 = Tag.valueOf("a");
        assertFalse(tag2.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isInline());
        
        Tag tag2 = Tag.valueOf("a");
        assertTrue(tag2.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isData());
        
        Tag tag2 = Tag.valueOf("a");
        assertFalse(tag2.isData());
        
        Tag tag3 = Tag.valueOf("pre");
        assertTrue(tag3.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isEmpty());
        
        Tag tag2 = Tag.valueOf("a");
        assertFalse(tag2.isEmpty());
        
        Tag tag3 = Tag.valueOf("img");
        assertTrue(tag3.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.preserveWhitespace());
        
        Tag tag2 = Tag.valueOf("pre");
        assertTrue(tag2.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag tag = Tag.valueOf("p");
        assertNull(tag.getImplicitParent());
        
        tag.setAncestor("body");
        Tag parent = Tag.valueOf("body");
        tag.ancestors.add(parent);
        assertSame(parent, tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.isValidParent(tag2));
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag1.equals(tag2));

        Tag tag3 = Tag.valueOf("a");
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());

        Tag tag3 = Tag.valueOf("a");
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

}