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
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.isInline());
        assertFalse(tag.isData());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("html"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testTostring() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
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
}