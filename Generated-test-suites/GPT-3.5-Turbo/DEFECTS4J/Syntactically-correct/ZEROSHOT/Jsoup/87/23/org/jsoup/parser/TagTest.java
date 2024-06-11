package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void getName() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void isBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void formatAsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void canContainBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void isInline() {
        Tag tag = new Tag("span");
        assertTrue(tag.isInline());
    }

    @Test
    public void isData() {
        Tag tag = new Tag("script");
        assertFalse(tag.isData());
    }

    @Test
    public void isEmpty() {
        Tag tag = new Tag("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void isSelfClosing() {
        Tag tag = new Tag("br");
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void isKnownTag() {
        Tag tag = new Tag("div");
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void isKnownTag_false() {
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void preserveWhitespace() {
        Tag tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void isFormListed() {
        Tag tag = new Tag("input");
        assertTrue(tag.isFormListed());
    }

    @Test
    public void isFormSubmittable() {
        Tag tag = new Tag("input");
        assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void setSelfClosing() {
        Tag tag = new Tag("input");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void equals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertEquals(tag1, tag2);
    }

    @Test
    public void hashCodeTest() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void toStringTest() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.toString());
    }
}