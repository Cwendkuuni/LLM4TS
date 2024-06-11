package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());

        tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());

        tag = Tag.valueOf("img");
        assertEquals("img", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());

        tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());

        tag = Tag.valueOf("img");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());

        tag = Tag.valueOf("div");
        assertTrue(tag.formatAsBlock());

        tag = Tag.valueOf("img");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("img");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isInline());

        tag = Tag.valueOf("div");
        assertFalse(tag.isInline());

        tag = Tag.valueOf("img");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isData());

        tag = Tag.valueOf("div");
        assertFalse(tag.isData());

        tag = Tag.valueOf("img");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isEmpty());

        tag = Tag.valueOf("div");
        assertFalse(tag.isEmpty());

        tag = Tag.valueOf("img");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isSelfClosing());

        tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());

        tag = Tag.valueOf("img");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertFalse(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("img"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.preserveWhitespace());

        tag = Tag.valueOf("div");
        assertFalse(tag.preserveWhitespace());

        tag = Tag.valueOf("img");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isSelfClosing());

        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag1.equals(tag2));

        tag1 = Tag.valueOf("p");
        tag2 = Tag.valueOf("div");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());

        tag1 = Tag.valueOf("p");
        tag2 = Tag.valueOf("div");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.toString());

        tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());

        tag = Tag.valueOf("img");
        assertEquals("img", tag.toString());
    }
}