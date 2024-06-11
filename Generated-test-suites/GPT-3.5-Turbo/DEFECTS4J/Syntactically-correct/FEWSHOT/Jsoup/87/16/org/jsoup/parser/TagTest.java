package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Test;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());

        tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        ParseSettings settings = ParseSettings.preserveCase;

        Tag tag = Tag.valueOf("div", settings);
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());

        tag = Tag.valueOf("span", settings);
        assertEquals("span", tag.getName());
        assertFalse(tag.isBlock());

        tag = Tag.valueOf("unknown", settings);
        assertEquals("unknown", tag.getName());
        assertFalse(tag.isBlock());
    }

    @Test
    public void testValueOfWithoutSettings() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());

        tag = new Tag("span");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.formatAsBlock());

        tag = new Tag("span");
        assertTrue(tag.formatAsBlock());

        tag = new Tag("script");
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("div");
        assertFalse(tag.isInline());

        tag = new Tag("span");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("div");
        assertFalse(tag.isData());

        tag = new Tag("span");
        assertTrue(tag.isData());

        tag = new Tag("img");
        assertFalse(tag.isData());
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("div");
        assertFalse(tag.isEmpty());

        tag = new Tag("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("div");
        assertFalse(tag.isSelfClosing());

        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());

        tag = new Tag("img");
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("div");
        assertTrue(tag.isKnownTag());

        tag = new Tag("unknown");
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagName() {
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("div");
        assertFalse(tag.preserveWhitespace());

        tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("input");
        assertTrue(tag.isFormListed());

        tag = new Tag("div");
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("input");
        assertTrue(tag.isFormSubmittable());

        tag = new Tag("div");
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("div");
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
        Tag tag3 = new Tag("span");

        assertTrue(tag1.hashCode() == tag2.hashCode());
        assertFalse(tag1.hashCode() == tag3.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }
}