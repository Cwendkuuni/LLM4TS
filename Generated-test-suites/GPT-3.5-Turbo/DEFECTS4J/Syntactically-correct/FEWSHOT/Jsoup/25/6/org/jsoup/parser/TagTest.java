package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("span");
        assertNotNull(tag1);
        assertNotNull(tag2);
        assertTrue(tag1.isKnownTag());
        assertFalse(tag2.isKnownTag());
    }

    @Test
    public void testIsBlock() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag1.isBlock());
        assertFalse(tag2.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag1.formatAsBlock());
        assertTrue(tag2.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag1.canContainBlock());
        assertFalse(tag2.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag1.isInline());
        assertTrue(tag2.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("img");
        assertFalse(tag1.isData());
        assertTrue(tag2.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("img");
        assertFalse(tag1.isEmpty());
        assertFalse(tag2.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("img");
        assertFalse(tag1.isSelfClosing());
        assertFalse(tag2.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag1.isKnownTag());
        assertFalse(tag2.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithString() {
        assertTrue(Tag.isKnownTag("h1"));
        assertFalse(Tag.isKnownTag("abc"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag1 = Tag.valueOf("pre");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.preserveWhitespace());
        assertFalse(tag2.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("img");
        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        Tag tag3 = new Tag("span");
        assertTrue(tag1.equals(tag2));
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