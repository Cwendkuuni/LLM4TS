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
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContain(Tag.valueOf("p")));
        assertTrue(tag.canContain(Tag.valueOf("span")));
    }
    
    @Test
    public void testCanContain() {
        Tag tag = Tag.valueOf("ul");
        assertTrue(tag.canContain(Tag.valueOf("li")));
        assertFalse(tag.canContain(Tag.valueOf("div")));
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("h1");
        assertTrue(tag.isBlock());
        Tag tag2 = Tag.valueOf("i");
        assertFalse(tag2.isBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("span");
        assertTrue(tag.canContainBlock());
        Tag tag2 = Tag.valueOf("a");
        assertFalse(tag2.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("em");
        assertTrue(tag.isInline());
        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag2.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("script");
        assertFalse(tag.isData());
        Tag tag2 = Tag.valueOf("plaintext");
        assertTrue(tag2.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("br");
        assertTrue(tag.isEmpty());
        Tag tag2 = Tag.valueOf("a");
        assertFalse(tag2.isEmpty());
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag2.preserveWhitespace());
    }
    
    @Test
    public void testIsValidParent() {
        Tag tag = Tag.valueOf("body");
        assertTrue(tag.isValidParent(Tag.valueOf("div")));
        assertFalse(tag.isValidParent(Tag.valueOf("body")));
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("a");
        Tag tag2 = Tag.valueOf("a");
        assertTrue(tag1.equals(tag2));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("span");
        Tag tag2 = Tag.valueOf("span");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("h2");
        assertEquals("h2", tag.toString());
    }
}