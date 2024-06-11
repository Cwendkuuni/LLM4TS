package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("TestTag");
        assertEquals("TestTag", tag.getName());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("TestTag");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("TestTag");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("TestTag");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("TestTag");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("TestTag");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("TestTag");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("TestTag");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("TestTag");
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("TestTag");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("TestTag");
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("TestTag");
        assertFalse(tag.isFormSubmittable());
    }
}