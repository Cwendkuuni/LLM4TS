package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOfWithParseSettings() {
        ParseSettings settings = ParseSettings.preserveCase;
        Tag tag = Tag.valueOf("p", settings);
        assertEquals("p", tag.getName());
        assertFalse(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());

        tag = Tag.valueOf("unknown", settings);
        assertEquals("unknown", tag.getName());
        assertFalse(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testValueOfWithoutParseSettings() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("unknown");
        assertEquals("unknown", tag.getName());
        assertFalse(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.isBlock());

        tag = new Tag("span");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.formatAsBlock());

        tag = new Tag("span");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.canContainBlock());

        tag = new Tag("span");
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("p");
        assertFalse(tag.isInline());

        tag = new Tag("span");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("p");
        assertFalse(tag.isData());

        tag = new Tag("img");
        assertFalse(tag.isData());

        tag = new Tag("span");
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("p");
        assertFalse(tag.isEmpty());

        tag = new Tag("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("p");
        assertFalse(tag.isSelfClosing());

        tag = new Tag("img");
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());

        tag = new Tag("p");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("input");
        assertTrue(tag.isFormListed());

        tag = new Tag("p");
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("input");
        assertTrue(tag.isFormSubmittable());

        tag = new Tag("p");
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("input");
        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertTrue(tag1.equals(tag2));

        Tag tag3 = new Tag("span");
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());

        Tag tag3 = new Tag("span");
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }
}