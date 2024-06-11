package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.parser.ParseSettings;
import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testValueOfWithSettings() {
        ParseSettings settings = ParseSettings.preserveCase;
        Tag tag = Tag.valueOf("div", settings);
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("div");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("div");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("div");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("div");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("div");
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithString() {
        assertTrue(Tag.isKnownTag("div"));
    }

    @Test
    public void testIsKnownTagWithStringNotKnown() {
        assertFalse(Tag.isKnownTag("customTag"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("div");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("div");
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("div");
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("div");
        Tag selfClosingTag = tag.setSelfClosing();
        assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("div");
        assertEquals(tag.getName().hashCode(), tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }
}