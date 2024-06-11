package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.Tag.*;

public class TagTest {

    @Test
    public void testTagName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());

        tag = Tag.valueOf("DIV");
        assertEquals("div", tag.getName());

        tag = Tag.valueOf("UnknownTag");
        assertTrue(tag.isInline()); // Unknown tags default to inline
    }

    @Test
    public void testBlockTag() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());

        tag = new Tag("p");
        assertTrue(tag.isBlock());

        tag = new Tag("span");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.formatAsBlock());

        tag = new Tag("span");
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testIsDataTag() {
        Tag tag = new Tag("script");
        assertTrue(tag.isData());

        tag = new Tag("noscript");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmptyTag() {
        Tag tag = new Tag("img");
        assertFalse(tag.isEmpty());

        tag = new Tag("input");
        assertFalse(tag.isEmpty());

        tag = new Tag("img");
        tag.empty = true;
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));

        assertFalse(Tag.isKnownTag("UnknownTag"));
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

        tag = new Tag("span");
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("input[type=submit]");
        assertTrue(tag.isFormSubmittable());

        tag = new Tag("span");
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("p");
        assertFalse(tag.isSelfClosing());

        tag = tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstructor() {
        new Tag("");
    }

    @Test(expected = NullPointerException.class)
    public void testNullTagName() {
        new Tag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTagName() {
        new Tag("&");
    }

}