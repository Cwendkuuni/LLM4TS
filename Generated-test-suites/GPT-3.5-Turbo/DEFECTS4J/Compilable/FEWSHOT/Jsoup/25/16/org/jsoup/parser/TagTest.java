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
    public void testIsBlock() {
        Tag blockTag = Tag.valueOf("div");
        assertTrue(blockTag.isBlock());
        
        Tag inlineTag = Tag.valueOf("span");
        assertFalse(inlineTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag blockTag = Tag.valueOf("div");
        assertTrue(blockTag.formatAsBlock());
        
        Tag inlineTag = Tag.valueOf("span");
        assertFalse(inlineTag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag blockTag = Tag.valueOf("div");
        assertTrue(blockTag.canContainBlock());
        
        Tag inlineTag = Tag.valueOf("span");
        assertFalse(inlineTag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag blockTag = Tag.valueOf("div");
        assertFalse(blockTag.isInline());
        
        Tag inlineTag = Tag.valueOf("span");
        assertTrue(inlineTag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag blockTag = Tag.valueOf("div");
        assertFalse(blockTag.isData());
        
        Tag inlineTag = Tag.valueOf("span");
        assertFalse(inlineTag.isData());
        
        Tag dataOnlyTag = Tag.valueOf("script");
        assertTrue(dataOnlyTag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag nonEmptyTag = Tag.valueOf("div");
        assertFalse(nonEmptyTag.isEmpty());
        
        Tag emptyTag = Tag.valueOf("img");
        assertTrue(emptyTag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag nonSelfClosingTag = Tag.valueOf("div");
        assertFalse(nonSelfClosingTag.isSelfClosing());
        
        Tag selfClosingTag = Tag.valueOf("img");
        assertTrue(selfClosingTag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        Tag knownTag = Tag.valueOf("div");
        assertTrue(knownTag.isKnownTag());
        
        Tag unknownTag = Tag.valueOf("custom");
        assertFalse(unknownTag.isKnownTag());
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag whitespaceTag = Tag.valueOf("pre");
        assertTrue(whitespaceTag.preserveWhitespace());
        
        Tag nonWhitespaceTag = Tag.valueOf("div");
        assertFalse(nonWhitespaceTag.preserveWhitespace());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.equals(tag2));
        
        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag1.equals(tag3));
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());
    }
}