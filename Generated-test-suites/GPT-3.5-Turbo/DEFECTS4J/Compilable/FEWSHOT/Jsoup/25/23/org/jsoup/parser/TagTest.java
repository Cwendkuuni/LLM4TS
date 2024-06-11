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
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.isInline());
        assertFalse(tag.isData());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertTrue(tag.isKnownTag());
        assertTrue(Tag.isKnownTag("p"));
        
        Tag unknownTag = Tag.valueOf("unknown");
        assertFalse(unknownTag.isBlock());
        assertTrue(unknownTag.formatAsBlock());
        assertTrue(unknownTag.canContainBlock());
        assertFalse(unknownTag.isInline());
        assertFalse(unknownTag.isData());
        assertFalse(unknownTag.isEmpty());
        assertFalse(unknownTag.isSelfClosing());
        assertFalse(unknownTag.isKnownTag());
        assertFalse(Tag.isKnownTag("unknown"));
    }
    
    @Test
    public void testIsBlock() {
        Tag blockTag = Tag.valueOf("p");
        assertTrue(blockTag.isBlock());
        
        Tag inlineTag = Tag.valueOf("a");
        assertFalse(inlineTag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag blockTag = Tag.valueOf("p");
        assertTrue(blockTag.formatAsBlock());
        
        Tag inlineTag = Tag.valueOf("a");
        assertFalse(inlineTag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag blockTag = Tag.valueOf("p");
        assertTrue(blockTag.canContainBlock());
        
        Tag inlineTag = Tag.valueOf("a");
        assertFalse(inlineTag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag blockTag = Tag.valueOf("p");
        assertFalse(blockTag.isInline());
        
        Tag inlineTag = Tag.valueOf("a");
        assertTrue(inlineTag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag dataTag = Tag.valueOf("title");
        assertFalse(dataTag.isData());
        
        Tag nonDataTag = Tag.valueOf("p");
        assertFalse(nonDataTag.isData());
        
        Tag emptyTag = Tag.valueOf("img");
        assertFalse(emptyTag.isData());
        
        Tag nonInlineTag = Tag.valueOf("pre");
        assertFalse(nonInlineTag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag emptyTag = Tag.valueOf("img");
        assertTrue(emptyTag.isEmpty());
        
        Tag nonEmptyTag = Tag.valueOf("p");
        assertFalse(nonEmptyTag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag selfClosingTag = Tag.valueOf("img");
        assertTrue(selfClosingTag.isSelfClosing());
        
        Tag nonSelfClosingTag = Tag.valueOf("p");
        assertFalse(nonSelfClosingTag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        
        assertFalse(Tag.isKnownTag("unknown"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag preserveWhitespaceTag = Tag.valueOf("pre");
        assertTrue(preserveWhitespaceTag.preserveWhitespace());
        
        Tag dontPreserveWhitespaceTag = Tag.valueOf("p");
        assertFalse(dontPreserveWhitespaceTag.preserveWhitespace());
    }
    
    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isSelfClosing());
        
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        Tag tag3 = Tag.valueOf("a");
        Tag tag4 = Tag.valueOf("a");
        
        assertTrue(tag1.equals(tag2));
        assertTrue(tag2.equals(tag1));
        assertFalse(tag1.equals(tag3));
        assertFalse(tag3.equals(tag1));
        assertTrue(tag3.equals(tag4));
        assertTrue(tag4.equals(tag3));
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
}