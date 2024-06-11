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
    public void testIsBlock() {
        Tag block = Tag.valueOf("p");
        assertTrue(block.isBlock());

        Tag inline = Tag.valueOf("a");
        assertFalse(inline.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag block = Tag.valueOf("p");
        assertTrue(block.formatAsBlock());

        Tag inline = Tag.valueOf("a");
        assertFalse(inline.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag canContain = Tag.valueOf("div");
        assertTrue(canContain.canContainBlock());

        Tag cannotContain = Tag.valueOf("a");
        assertFalse(cannotContain.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag inline = Tag.valueOf("a");
        assertTrue(inline.isInline());

        Tag block = Tag.valueOf("p");
        assertFalse(block.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag dataOnly = Tag.valueOf("img");
        assertTrue(dataOnly.isData());

        Tag notDataOnly = Tag.valueOf("p");
        assertFalse(notDataOnly.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag empty = Tag.valueOf("img");
        assertTrue(empty.isEmpty());

        Tag notEmpty = Tag.valueOf("p");
        assertFalse(notEmpty.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag selfClosing = Tag.valueOf("img");
        assertTrue(selfClosing.isSelfClosing());

        Tag notSelfClosing = Tag.valueOf("p");
        assertFalse(notSelfClosing.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknownTag"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag preserve = Tag.valueOf("pre");
        assertTrue(preserve.preserveWhitespace());

        Tag notPreserve = Tag.valueOf("p");
        assertFalse(notPreserve.preserveWhitespace());
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
}