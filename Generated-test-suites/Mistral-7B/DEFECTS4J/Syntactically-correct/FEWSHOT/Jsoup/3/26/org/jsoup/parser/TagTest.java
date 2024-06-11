package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TagTest {

    @Test
    public void testNewTag() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.tagName);
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isData());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertNull(tag.ancestors);
    }

    @Test
    public void testNewEmptyTag() {
        Tag tag = new Tag("img");
        assertEquals("img", tag.tagName);
        assertFalse(tag.isBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertTrue(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertNull(tag.ancestors);
    }

    @Test
    public void testNewInlineTag() {
        Tag tag = new Tag("span");
        assertEquals("span", tag.tagName);
        assertFalse(tag.isBlock());
        assertFalse(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertNull(tag.ancestors);
    }

    @Test
    public void testGetTagByName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.tagName);
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isData());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertNull(tag.ancestors);

        Tag tag2 = Tag.valueOf("img");
        assertEquals("img", tag2.tagName);
        assertFalse(tag2.isBlock());
        assertFalse(tag2.canContainBlock());
        assertFalse(tag2.canContainInline());
        assertTrue(tag2.isEmpty());
        assertFalse(tag2.preserveWhitespace());
        assertNull(tag2.ancestors);
    }

    @Test
    public void testCanContainTag() {
        Tag parent = new Tag("div");
        Tag child = new Tag("p");
        assertTrue(parent.canContain(child));

        child = new Tag("div");
        assertFalse(parent.canContain(child));

        child = new Tag("span");
        assertTrue(parent.canContain(child));

        child = new Tag("button");
        assertFalse(parent.canContain(child));

        child = new Tag("form");
        assertFalse(parent.canContain(child));
    }

    @Test
    public void testIsValidParent() {
        Tag child = new Tag("p");
        Tag parent = new Tag("div");
        assertTrue(parent.isValidParent(child));

        child = new Tag("div");
        assertFalse(parent.isValidParent(child));

        child = new Tag("span");
        assertTrue(parent.isValidParent(child));

        child = new Tag("button");
        assertFalse(parent.isValidParent(child));

        child = new Tag("form");
        assertFalse(parent.isValidParent(child));
    }

    @Test
    public void testEqualsAndHashCode() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");

        assertEquals(tag1, tag2);
        assertEquals(tag1.hashCode(), tag2.hashCode());

        Tag tag3 = new Tag("p");
        assertFalse(tag1.equals(tag3));
        assertFalse(tag1.hashCode() == tag3.hashCode());

        Tag tag4 = new Tag("div").setCanContainBlock(false);
        assertFalse(tag1.equals(tag4));
        assertFalse(tag1.hashCode() == tag4.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }
}