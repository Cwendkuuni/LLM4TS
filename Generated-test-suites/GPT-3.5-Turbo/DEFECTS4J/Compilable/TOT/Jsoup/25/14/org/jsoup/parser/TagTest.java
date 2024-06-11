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
    public void testValueOf_knownTag() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isKnownTag());
    }
    
    @Test
    public void testValueOf_unknownTag() {
        Tag tag = Tag.valueOf("unknown");
        assertFalse(tag.isKnownTag());
    }
    
    @Test
    public void testValueOf_nullTagName() {
        try {
            Tag.valueOf(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("tagName is null", e.getMessage());
        }
    }
    
    @Test
    public void testValueOf_emptyTagName() {
        try {
            Tag.valueOf("");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("tagName is empty", e.getMessage());
        }
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("script");
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty_nonEmptyTag() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing_nonSelfClosingTag() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());
    }
    
    @Test
    public void testIsSelfClosing_selfClosingTag() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }
    
    @Test
    public void testEquals_sameTag() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.equals(tag2));
    }
    
    @Test
    public void testEquals_differentTag() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("p");
        assertFalse(tag1.equals(tag2));
    }
    
    @Test
    public void testHashCode_sameTag() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testHashCode_differentTag() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("p");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());
    }
}