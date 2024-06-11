package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("div");
        Tag tag3 = new Tag("test");

        assertTrue(tag1.isKownTag());
        assertTrue(tag2.isKownTag());
        assertFalse(tag3.isKownTag());
    }

    @Test
    public void testIsBlock() {
        Tag tag1 = Tag.valueOf("p");
        assertTrue(tag1.isBlock());

        Tag tag2 = Tag.valueOf("img");
        assertFalse(tag2.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag1 = Tag.valueOf("p");
        assertTrue(tag1.formatAsBlock());

        Tag tag2 = Tag.valueOf("img");
        assertFalse(tag2.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag1 = Tag.valueOf("p");
        assertTrue(tag1.canContainBlock());

        Tag tag2 = Tag.valueOf("img");
        assertFalse(tag2.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag1 = Tag.valueOf("p");
        assertFalse(tag1.isInline());

        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag1 = Tag.valueOf("p");
        assertFalse(tag1.isData());

        Tag tag2 = Tag.valueOf("img");
        assertFalse(tag2.isData());

        Tag tag3 = new Tag("test");
        assertTrue(tag3.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag1 = Tag.valueOf("p");
        assertFalse(tag1.isEmpty());

        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag1 = Tag.valueOf("p");
        assertFalse(tag1.isSelfClosing());

        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag1 = Tag.valueOf("p");
        assertTrue(tag1.isKnownTag());

        Tag tag2 = new Tag("test");
        assertFalse(tag2.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("test"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag1 = Tag.valueOf("p");
        assertFalse(tag1.preserveWhitespace());

        Tag tag2 = Tag.valueOf("pre");
        assertTrue(tag2.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("test");
        tag.setSelfClosing();

        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        Tag tag3 = new Tag("test");

        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");

        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.toString());
    }

    @Test
    public void testInternalStaticInitialisers() {
        // Not testable as it only creates and modifies internal objects
    }
}