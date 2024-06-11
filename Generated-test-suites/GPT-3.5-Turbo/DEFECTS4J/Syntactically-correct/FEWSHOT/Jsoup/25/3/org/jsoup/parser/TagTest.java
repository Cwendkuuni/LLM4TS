package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    
    @Test
    public void testGetName() {
        Tag tag = new Tag("tag");
        assertEquals("tag", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("tag");
        assertEquals("tag", tag.getName());
        
        Tag unknownTag = Tag.valueOf("unknownTag");
        assertEquals("unknownTag", unknownTag.getName());
        assertFalse(unknownTag.isBlock());
        assertTrue(unknownTag.canContainBlock());
    }
    
    @Test
    public void testIsBlock() {
        Tag blockTag = new Tag("blockTag");
        assertTrue(blockTag.isBlock());
        
        Tag inlineTag = new Tag("inlineTag");
        assertFalse(inlineTag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag blockTag = new Tag("blockTag");
        assertTrue(blockTag.formatAsBlock());
        
        Tag inlineTag = new Tag("inlineTag");
        assertFalse(inlineTag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("tag");
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag blockTag = new Tag("blockTag");
        assertFalse(blockTag.isInline());
        
        Tag inlineTag = new Tag("inlineTag");
        assertTrue(inlineTag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = new Tag("tag");
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("tag");
        assertFalse(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("tag");
        assertFalse(tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("tag");
        assertFalse(tag.isKnownTag());
    }
    
    @Test
    public void testIsKnownTagWithName() {
        assertFalse(Tag.isKnownTag("tag"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("tag");
        assertFalse(tag.preserveWhitespace());
    }
    
    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("tag");
        Tag selfClosingTag = tag.setSelfClosing();
        
        assertTrue(selfClosingTag.isSelfClosing());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("tag");
        Tag tag2 = new Tag("tag");
        assertTrue(tag1.equals(tag2));
        
        Tag differentTag = new Tag("differentTag");
        assertFalse(tag1.equals(differentTag));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("tag");
        Tag tag2 = new Tag("tag");
        assertEquals(tag1.hashCode(), tag2.hashCode());
        
        Tag differentTag = new Tag("differentTag");
        assertNotEquals(tag1.hashCode(), differentTag.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = new Tag("tag");
        assertEquals("tag", tag.toString());
    }
}