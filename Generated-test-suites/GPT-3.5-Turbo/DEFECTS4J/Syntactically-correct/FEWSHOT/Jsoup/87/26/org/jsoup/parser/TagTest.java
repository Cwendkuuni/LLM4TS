package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Test;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = new Tag("tag");
        assertEquals("tag", tag.getName());
    }

    @Test
    public void testValueOf() {
        ParseSettings settings = ParseSettings.preserveCase;
        Tag tag = Tag.valueOf("tag", settings);
        assertFalse(tag.isBlock());

        tag = Tag.valueOf("TAG");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("tag");
        tag.isBlock = true;
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("tag");
        tag.formatAsBlock = true;
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("tag");
        tag.isBlock = true;
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("tag");
        tag.isBlock = false;
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("tag");
        tag.canContainInline = false;
        tag.empty = false;
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("tag");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("tag");
        tag.empty = true;
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("tag");
        tag.tagName = "p";
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("tag");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("tag");
        Tag tag2 = new Tag("tag");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("tag");
        assertEquals("tag".hashCode(), tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("tag");
        assertEquals("tag", tag.toString());
    }
}