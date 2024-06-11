package org.jsoup.parser;

import org.junit.*;
import static org.junit.Assert.*;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }
    
    @Test
    public void testCanContain() {
        Tag parentTag = Tag.valueOf("div");
        Tag blockChildTag = Tag.valueOf("p");
        Tag inlineChildTag = Tag.valueOf("span");
        
        assertTrue(parentTag.canContain(blockChildTag));
        assertTrue(parentTag.canContain(inlineChildTag));
        
        Tag brTag = Tag.valueOf("br");
        assertFalse(parentTag.canContain(brTag));
        
        Tag strongTag = Tag.valueOf("strong");
        assertFalse(strongTag.canContain(blockChildTag));
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
        
        Tag spanTag = Tag.valueOf("span");
        assertFalse(spanTag.isBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
        
        Tag spanTag = Tag.valueOf("span");
        assertFalse(spanTag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
        
        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.isData());
        
        Tag brTag = Tag.valueOf("br");
        assertTrue(brTag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
        
        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.isEmpty());
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
        
        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.preserveWhitespace());
    }
    
    @Test
    public void testGetImplicitParent() {
        Tag parentTag = Tag.valueOf("div");
        Tag childTag = Tag.valueOf("span");
        childTag.ancestors.add(parentTag);
        
        assertEquals(parentTag, childTag.getImplicitParent());
        
        Tag noParentTag = Tag.valueOf("p");
        assertNull(noParentTag.getImplicitParent());
    }
    
    @Test
    public void testIsValidParent() {
        Tag parentTag = Tag.valueOf("div");
        Tag childTag = Tag.valueOf("span");
        childTag.ancestors.add(parentTag);
        
        assertTrue(parentTag.isValidParent(childTag));
        
        Tag otherParentTag = Tag.valueOf("p");
        assertFalse(otherParentTag.isValidParent(childTag));
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.equals(tag2));
        
        Tag spanTag = Tag.valueOf("span");
        assertFalse(tag1.equals(spanTag));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());
        
        Tag spanTag = Tag.valueOf("span");
        assertNotEquals(tag1.hashCode(), spanTag.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());
    }
}