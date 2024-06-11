
package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {
    
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
        
        tag = Tag.valueOf("h1");
        assertEquals("h1", tag.getName());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
        
        tag = Tag.valueOf("span");
        assertFalse(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.formatAsBlock());
        
        tag = Tag.valueOf("span");
        assertTrue(tag.formatAsBlock()); // default value
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
        
        tag = Tag.valueOf("span");
        assertTrue(tag.canContainBlock()); // default value
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isInline());
        
        tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isData());
        
        tag = Tag.valueOf("span");
        assertFalse(tag.isData()); // default value
        
        tag = Tag.valueOf("img");
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isEmpty());
        
        tag = Tag.valueOf("img");
        assertFalse(tag.isEmpty()); // default value
        
        tag = Tag.valueOf("input");
        assertTrue(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());
        
        tag = Tag.valueOf("img");
        assertFalse(tag.isSelfClosing()); // default value
        
        tag = Tag.valueOf("input");
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertFalse(Tag.isKnownTag("foo"));
        
        assertTrue(Tag.isKnownTag("div"));
        assertTrue(Tag.isKnownTag("img"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.preserveWhitespace());
        
        tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
        
        tag = Tag.valueOf("plaintext");
        assertTrue(tag.preserveWhitespace());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.equals(tag2));
        
        Tag tag3 = Tag.valueOf("img");
        assertFalse(tag1.equals(tag3));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());
        
        Tag tag3 = Tag.valueOf("img");
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());
        
        tag = Tag.valueOf("h1");
        assertEquals("h1", tag.toString());
    }
}
