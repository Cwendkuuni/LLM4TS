package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        assertFalse(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        
        Tag unknownTag = Tag.valueOf("unknown");
        assertEquals("unknown", unknownTag.getName());
        assertFalse(unknownTag.isBlock());
        assertTrue(unknownTag.formatAsBlock());
        assertTrue(unknownTag.canContainBlock());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());
        
        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.formatAsBlock());
        
        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainBlock());
        
        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = new Tag("div");
        assertFalse(tag.isInline());
        
        Tag inlineTag = new Tag("span");
        assertTrue(inlineTag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = new Tag("div");
        assertFalse(tag.isData());
        
        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.isData());
        
        Tag emptyTag = new Tag("img");
        assertFalse(emptyTag.isData());
        
        Tag dataTag = new Tag("object");
        assertTrue(dataTag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("div");
        assertFalse(tag.isEmpty());
        
        Tag emptyTag = new Tag("img");
        assertTrue(emptyTag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("div");
        assertFalse(tag.isSelfClosing());
        
        Tag emptyTag = new Tag("img");
        assertTrue(emptyTag.isSelfClosing());
        
        Tag selfClosingTag = new Tag("foo");
        selfClosingTag.setSelfClosing();
        assertTrue(selfClosingTag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("div");
        assertFalse(tag.preserveWhitespace());
        
        Tag preserveWhitespaceTag = new Tag("pre");
        assertTrue(preserveWhitespaceTag.preserveWhitespace());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.equals(tag2));
        
        Tag tag3 = new Tag("p");
        assertFalse(tag1.equals(tag3));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }
}