
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.isInline());
        assertFalse(tag.isData());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.isKnownTag());
        assertFalse(Tag.isKnownTag("span"));
        assertFalse(tag.preserveWhitespace());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("h1");
        assertTrue(tag.formatAsBlock());
        Tag tag2 = Tag.valueOf("a");
        assertFalse(tag2.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("a");
        assertTrue(tag.isInline());
        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag2.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("b");
        assertFalse(tag.isData());
        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag2.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isSelfClosing());
        Tag tag2 = Tag.valueOf("a");
        assertFalse(tag2.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isKnownTag());
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.isKnownTag());
    }
    
    @Test
    public void testIsKnownTagWithName() {
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("span"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.preserveWhitespace());
    }
    
    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("input");
        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testEquals() {
        Tag tag = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag, tag2);
    }
    
    @Test
    public void testHashCode() {
        Tag tag = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.toString());
    }
}
