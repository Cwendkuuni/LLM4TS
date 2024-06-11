package org.jsoup.parser; // make sure to update the package name if different

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testTagName() {
        Tag tag = new Tag("tagName");
        String name = tag.getName();
        assertEquals("tagName", name);
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());

        tag = Tag.valueOf("DIV");
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());

        tag = Tag.valueOf("IMG");
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainInline());
        assertTrue(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());

        tag = Tag.valueOf("input");
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertTrue(tag.preserveWhitespace());
        assertTrue(tag.isFormListed());
        assertTrue(tag.isFormSubmittable());

        tag = Tag.valueOf("textarea");
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertTrue(tag.preserveWhitespace());
        assertTrue(tag.isFormListed());
        assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testTagIsBlock() {
        Tag tag = new Tag("tagName");
        tag.isBlock = false;
        assertFalse(tag.isBlock());

        tag.isBlock = true;
        assertTrue(tag.isBlock());
    }

    @Test
    public void testTagFormatAsBlock() {
        Tag tag = new Tag("tagName");
        tag.formatAsBlock = false;
        assertFalse(tag.formatAsBlock());

        tag.formatAsBlock = true;
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testTagCanContainInline() {
        Tag tag = new Tag("tagName");
        tag.canContainInline = false;
        assertFalse(tag.canContainInline);

        tag.canContainInline = true;
        assertTrue(tag.canContainInline);
    }

    @Test
    public void testTagEmpty() {
        Tag tag = new Tag("tagName");
        tag.empty = true;
        assertTrue(tag.isEmpty());

        tag.empty = false;
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testTagSelfClosing() {
        Tag tag = new Tag("tagName");
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());

        tag.selfClosing = false;
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag tag = new Tag("tagName");
        tag.preserveWhitespace = true;
        assertTrue(tag.preserveWhitespace());

        tag.preserveWhitespace = false;
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testTagFormListed() {
        Tag tag = new Tag("tagName");
        tag.formList = true;
        assertTrue(tag.isFormListed());

        tag.formList = false;
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testTagFormSubmittable() {
        Tag tag = new Tag("tagName");
        tag.formSubmit = true;
        assertTrue(tag.isFormSubmittable());

        tag.formSubmit = false;
        assertFalse(tag.isFormSubmittable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagNullTagName() {
        new Tag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagEmptyTagName() {
        tag = new Tag("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagEmptyMap() {
        Map<String, Tag> emptyMap = new HashMap<>();
        Tag.register(tag);
    }
}