package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for Tag class.
 */
public class TagTest {

    /**
     * Test Tag constructor.
     */
    @Test
    public void testTagConstructor() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertTrue(tag.preserveWhitespace());
    }

    /**
     * Test Tag valueOf method with known tag.
     */
    @Test
    public void testTagValueOfKnownTag() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertTrue(tag.preserveWhitespace());
    }

    /**
     * Test Tag valueOf method with unknown tag.
     */
    @Test
    public void testTagValueOfUnknownTag() {
        Tag tag = Tag.valueOf("unknown");
        assertEquals("unknown", tag.getName());
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
    }

    /**
     * Test Tag isBlock method.
     */
    @Test
    public void testTagIsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());

        tag = new Tag("span");
        assertFalse(tag.isBlock());
    }

    /**
     * Test Tag formatAsBlock method.
     */
    @Test
    public void testTagFormatAsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.formatAsBlock());

        tag = new Tag("span");
        assertFalse(tag.formatAsBlock());
    }

    /**
     * Test Tag canContainBlock method.
     */
    @Test
    public void testTagCanContainBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainBlock());

        tag = new Tag("span");
        assertTrue(tag.canContainInline());
    }

    /**
     * Test Tag isEmpty method.
     */
    @Test
    public void testTagIsEmpty() {
        Tag tag = new Tag("img");
        assertFalse(tag.isEmpty());

        tag = new Tag("input");
        assertFalse(tag.isEmpty());

        tag = new Tag("br");
        assertTrue(tag.isEmpty());
    }

    /**
     * Test Tag isData method.
     */
    @Test
    public void testTagIsData() {
        Tag tag = new Tag("script");
        assertTrue(tag.isData());

        tag = new Tag("style");
        assertTrue(tag.isData());

        tag = new Tag("p");
        assertFalse(tag.isData());
    }

    /**
     * Test Tag equals method.
     */
    @Test
    public void testTagEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        Tag tag3 = new Tag("span");

        assertTrue(tag1.equals(tag1));
        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
    }

    /**
     * Test Tag hashCode method.
     */
    @Test
    public void testTagHashCode() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        Tag tag3 = new Tag("span");

        int hash1 = tag1.hashCode();
        int hash2 = tag2.hashCode();
        int hash3 = tag3.hashCode();

        Tag tag4 = new Tag("div");
        assertEquals(hash1, tag4.hashCode());
        assertEquals(hash1, hash2);
        assertNotEquals(hash1, hash3);
    }

    /**
     * Test Tag toString method.
     */
    @Test
    public void testTagToString() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }

    /**
     * Test Tag isKnownTag method.
     */
    @Test
    public void testTagIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    /**
     * Test Tag isBlock method for known tags.
     */
    @Test
    public void testTagIsBlockForKnownTags() {
        assertTrue(Tag.isKnownTag("div").isBlock());
        assertFalse(Tag.isKnownTag("span").isBlock());
    }
}