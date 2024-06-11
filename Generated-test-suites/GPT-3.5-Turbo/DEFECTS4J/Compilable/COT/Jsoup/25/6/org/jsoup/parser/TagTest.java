package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
        
        tag = Tag.valueOf("span");
        assertFalse(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());
        
        tag = Tag.valueOf("span");
        assertFalse(tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
        
        tag = Tag.valueOf("a");
        assertFalse(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
        
        tag = Tag.valueOf("h1");
        assertFalse(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isData());
        
        tag = Tag.valueOf("script");
        assertTrue(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
        
        tag = Tag.valueOf("div");
        assertFalse(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isSelfClosing());
        
        tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("customtag"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
        
        tag = Tag.valueOf("div");
        assertFalse(tag.preserveWhitespace());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag1.equals(tag2));
        
        Tag tag3 = Tag.valueOf("div");
        assertFalse(tag1.equals(tag3));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());
        
        Tag tag3 = Tag.valueOf("div");
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());
    }
}
