package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    @Test
    public void getName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }
    
    @Test
    public void valueOf_knownTag() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.isInline());
        assertFalse(tag.isData());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertTrue(tag.isKnownTag());
        assertFalse(tag.preserveWhitespace());
    }
    
    @Test
    public void valueOf_unknownTag() {
        Tag tag = Tag.valueOf("foo");
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.isInline());
        assertTrue(tag.isData());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.isKnownTag());
        assertFalse(tag.preserveWhitespace());
    }
    
    @Test
    public void isKnownTag_existingTag() {
        assertTrue(Tag.isKnownTag("p"));
    }
    
    @Test
    public void isKnownTag_unknownTag() {
        assertFalse(Tag.isKnownTag("foo"));
    }
    
    @Test
    public void isKnownTag_emptyTag() {
        assertTrue(Tag.isKnownTag("img"));
    }
    
    @Test
    public void isBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void formatAsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void canContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void isInline() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isInline());
    }
    
    @Test
    public void isData() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isData());
    }
    
    @Test
    public void isEmpty() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isEmpty());
    }
    
    @Test
    public void isSelfClosing() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());
    }
    
    @Test
    public void isKnownTag() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isKnownTag());
    }
    
    @Test
    public void preserveWhitespace() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.preserveWhitespace());
    }
}
