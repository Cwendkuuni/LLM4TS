package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {
    
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("img");
        assertEquals("p", tag1.getName());
        assertEquals("img", tag2.getName());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag1.isBlock());
        assertFalse(tag2.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag1.formatAsBlock());
        assertFalse(tag2.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag1.canContainBlock());
        assertFalse(tag2.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag1.isInline());
        assertTrue(tag2.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag1.isData());
        assertTrue(tag2.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("img");
        assertFalse(tag1.isEmpty());
        assertTrue(tag2.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("img");
        assertFalse(tag1.isSelfClosing());
        assertTrue(tag2.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("customTag"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag1 = Tag.valueOf("pre");
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag1.preserveWhitespace());
        assertFalse(tag2.preserveWhitespace());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("span");
        Tag tag3 = Tag.valueOf("div");
        assertTrue(tag1.equals(tag1));
        assertFalse(tag1.equals(tag2));
        assertTrue(tag1.equals(tag3));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("span");
        Tag tag3 = Tag.valueOf("div");
        assertTrue(tag1.hashCode() == tag1.hashCode());
        assertFalse(tag1.hashCode() == tag2.hashCode());
        assertTrue(tag1.hashCode() == tag3.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());
    }
    
}