package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("test");
    }

    @Test
    public void testGetName() {
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag valueOfTag = Tag.valueOf("test");
        assertNotNull(valueOfTag);
        assertEquals(tag, valueOfTag);

        Tag unknownTag = Tag.valueOf("unknown");
        assertNotNull(unknownTag);
        assertNotEquals(tag, unknownTag);
    }

    @Test
    public void testIsBlock() {
        assertTrue(tag.isBlock());
        tag = new Tag("test").setSelfClosing();
        assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertTrue(tag.formatAsBlock());
        tag = new Tag("test").setSelfClosing();
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(tag.canContainBlock());
        tag = new Tag("test").setSelfClosing();
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(tag.isInline());
        tag = new Tag("test").setSelfClosing();
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        assertFalse(tag.isData());
        tag = new Tag("test").setSelfClosing();
        assertFalse(tag.isData());
        tag = Tag.valueOf("test");
        assertFalse(tag.isData());

        tag = Tag.valueOf("pre");
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tag.isEmpty());
        tag = new Tag("test").setSelfClosing();
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(tag.isSelfClosing());
        tag = new Tag("test").setSelfClosing();
        assertTrue(tag.isSelfClosing());
        tag = Tag.valueOf("test");
        assertFalse(tag.isSelfClosing());

        tag = Tag.valueOf("pre");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertFalse(tag.isKnownTag());
        tag = Tag.valueOf("p");
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithString() {
        assertFalse(Tag.isKnownTag("test"));
        assertTrue(Tag.isKnownTag("p"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
        tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }
}