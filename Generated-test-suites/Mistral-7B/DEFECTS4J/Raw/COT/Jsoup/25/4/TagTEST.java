package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.parser.Tag;

/**
 * JUnit test file for Tag class.
 */
public class TagTest {

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("DIV");
        Tag tag3 = Tag.valueOf("UNKNOWN");

        assertEquals("p", tag1.getName());
        assertEquals("DIV", tag2.getName());
        assertTrue(tag3.isUnknownTag());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());

        tag = Tag.valueOf("a");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());

        tag = Tag.valueOf("a");
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("a");
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertFalse(tag.isEmpty());

        tag = Tag.valueOf("img").setSelfClosing();
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("img");
        assertFalse(tag.isSelfClosing());

        tag = Tag.valueOf("img").setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("UNKNOWN"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullTagName() {
        Tag tag = new Tag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTagName() {
        Tag tag = new Tag("");
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");

        assertEquals(tag1.hashCode(), tag2.hashCode());

        tag1 = new Tag("p").setSelfClosing();
        tag2 = new Tag("p");

        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("p");
        Tag tag4 = new Tag("div");

        assertTrue(tag1.equals(tag1));
        assertTrue(tag1.equals(tag2));
        assertTrue(tag1.equals(tag3));
        assertFalse(tag1.equals(tag4));
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.toString());
    }

    @Test(expected = ClassCastException.class)
    public void testNotSameInstance() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");

        assertEquals(tag1, tag2); // JUnit will report success, but this really fails as they are not the same instance
    }
}