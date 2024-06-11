package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());

        Tag tag2 = Tag.valueOf("p");
        assertEquals("p", tag2.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());

        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag2.isBlock());

        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag3.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.formatAsBlock());

        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag2.formatAsBlock());

        Tag tag3 = Tag.valueOf("span");
        assertTrue(tag3.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());

        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag2.canContainBlock());

        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag3.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isInline());

        Tag tag2 = Tag.valueOf("p");
        assertFalse(tag2.isInline());

        Tag tag3 = Tag.valueOf("span");
        assertTrue(tag3.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isData());

        Tag tag2 = Tag.valueOf("p");
        assertFalse(tag2.isData());

        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag3.isData());

        Tag tag4 = Tag.valueOf("script");
        assertTrue(tag4.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isEmpty());

        Tag tag2 = Tag.valueOf("p");
        assertFalse(tag2.isEmpty());

        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag3.isEmpty());

        Tag tag4 = Tag.valueOf("img");
        assertTrue(tag4.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());

        Tag tag2 = Tag.valueOf("p");
        assertFalse(tag2.isSelfClosing());

        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag3.isSelfClosing());

        Tag tag4 = Tag.valueOf("img");
        assertTrue(tag4.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isKnownTag());

        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag2.isKnownTag());

        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag3.isKnownTag());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.preserveWhitespace());

        Tag tag2 = Tag.valueOf("p");
        assertFalse(tag2.preserveWhitespace());

        Tag tag3 = Tag.valueOf("pre");
        assertTrue(tag3.preserveWhitespace());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag1, tag2);

        Tag tag3 = Tag.valueOf("p");
        assertNotEquals(tag1, tag3);
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());

        Tag tag2 = Tag.valueOf("p");
        assertEquals("p", tag2.toString());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());

        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag2.isBlock());
        assertTrue(tag2.canContainBlock());

        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag3.isBlock());
        assertTrue(tag3.canContainBlock());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfNull() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfEmpty() {
        Tag.valueOf("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfWhitespace() {
        Tag.valueOf(" ");
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());

        Tag tag3 = Tag.valueOf("p");
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }
}