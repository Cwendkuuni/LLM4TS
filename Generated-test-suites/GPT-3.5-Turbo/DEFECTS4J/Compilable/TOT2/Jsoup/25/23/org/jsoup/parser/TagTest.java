package org.jsoup.parser;
import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.isBlock());
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("br");
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("img");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1, tag2);
    }

    @Test
    public void testIsKnownTagWithName() {
        assertTrue(Tag.isKnownTag("p"));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());
    }

}