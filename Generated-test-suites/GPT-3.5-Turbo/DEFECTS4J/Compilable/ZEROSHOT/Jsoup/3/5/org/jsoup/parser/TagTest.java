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
        Tag tag = Tag.valueOf("strong");
        assertEquals("strong", tag.getName());
        assertFalse(tag.isBlock());
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testCanContain() {
        Tag parent = Tag.valueOf("p");
        Tag childBlock = Tag.valueOf("div");
        Tag childInline =  Tag.valueOf("span");
        
        assertTrue(parent.canContain(childBlock));
        assertTrue(parent.canContain(childInline));
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("style");
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
    public void testEquals() {
        Tag tag1 = Tag.valueOf("a");
        Tag tag2 = Tag.valueOf("a");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("a");
        Tag tag2 = Tag.valueOf("a");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("html");
        assertEquals("html", tag.toString());
    }
    
}