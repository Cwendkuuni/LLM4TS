package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isKnownTag());
        
        Tag newTag = Tag.valueOf("custom");
        assertFalse(newTag.isKnownTag());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
        
        Tag newTag = Tag.valueOf("custom");
        assertFalse(newTag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());
        
        Tag newTag = Tag.valueOf("custom");
        assertTrue(newTag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainBlock());
        
        Tag newTag = Tag.valueOf("custom");
        assertTrue(newTag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isInline());
        
        Tag newTag = Tag.valueOf("custom");
        assertTrue(newTag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isData());
        
        Tag newTag = Tag.valueOf("custom");
        assertTrue(newTag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isEmpty());
        
        Tag newTag = Tag.valueOf("img");
        assertTrue(newTag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isSelfClosing());
        
        Tag newTag = Tag.valueOf("img");
        assertTrue(newTag.isSelfClosing());
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.preserveWhitespace());
        
        Tag newTag = Tag.valueOf("pre");
        assertTrue(newTag.preserveWhitespace());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1, tag2);
        
        Tag newTag = Tag.valueOf("custom");
        assertFalse(tag1.equals(newTag));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.toString());
    }
    
    @Test
    public void testIsKnownTagWithString() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("custom"));
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.toString());
    }
}