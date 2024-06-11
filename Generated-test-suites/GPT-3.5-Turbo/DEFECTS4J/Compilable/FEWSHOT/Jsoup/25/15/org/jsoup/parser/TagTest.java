package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.formatAsBlock());
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
        Tag tag1 = Tag.valueOf("span");
        assertFalse(tag1.isData());

        Tag tag2 = Tag.valueOf("pre");
        assertFalse(tag2.isData());

        Tag tag3 = Tag.valueOf("img");
        assertFalse(tag3.isData());

        Tag tag4 = Tag.valueOf("br");
        assertFalse(tag4.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isEmpty());

        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isSelfClosing());

        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("customTag"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.preserveWhitespace());

        Tag tag2 = Tag.valueOf("pre");
        assertTrue(tag2.preserveWhitespace());
    }

}