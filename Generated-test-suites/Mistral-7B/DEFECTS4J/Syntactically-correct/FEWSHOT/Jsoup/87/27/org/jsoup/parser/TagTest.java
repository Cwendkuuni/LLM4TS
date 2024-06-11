package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        Tag tag = Tag.valueOf("p");
        assertTrue(Tag.isKnownTag("p"));
        assertEquals(tag, Tag.valueOf("p"));

        Tag unknownTag = new Tag("unknown");
        assertFalse(Tag.isKnownTag("unknown"));
        assertNotEquals(tag, unknownTag);
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());

        Tag inlineTag = Tag.valueOf("span");
        assertFalse(inlineTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());

        Tag inlineTag = Tag.valueOf("span");
        assertFalse(inlineTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainBlock());

        Tag inlineTag = Tag.valueOf("span");
        assertFalse(inlineTag.canContainBlock());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertFalse(tag.isEmpty());

        Tag emptyTag = Tag.valueOf("input");
        assertTrue(emptyTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isSelfClosing());

        Tag selfClosingTag = Tag.valueOf("img");
        assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isData());

        Tag dataTag = Tag.valueOf("script");
        assertTrue(dataTag.isData());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.preserveWhitespace());

        Tag preserveWhitespaceTag = Tag.valueOf("pre");
        assertTrue(preserveWhitespaceTag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isFormListed());

        Tag formListedTag = Tag.valueOf("input");
        assertTrue(formListedTag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isFormSubmittable());

        Tag formSubmittableTag = Tag.valueOf("input");
        assertTrue(formSubmittableTag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("p");
        tag = tag.setSelfClosing();
        assertFalse(tag.isEmpty());
        assertTrue(tag.isSelfClosing());
    }
}