package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit test file for class Tag.
 */
public class TagTest {

    /**
     * Test constructor Tag.
     */
    @Test
    public void testTagConstructor() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    /**
     * Test method getName.
     */
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    /**
     * Test method valueOf.
     */
    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag1, tag2);

        Tag tag3 = Tag.valueOf("p");
        assertNotEquals(tag1, tag3);
    }

    /**
     * Test method isBlock.
     */
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());

        Tag tagInline = new Tag("span");
        assertFalse(tagInline.isBlock());
    }

    /**
     * Test method formatAsBlock.
     */
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.formatAsBlock());

        Tag tagInline = new Tag("span");
        assertFalse(tagInline.formatAsBlock());
    }

    /**
     * Test method canContainBlock.
     */
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainBlock());

        Tag tagInline = new Tag("span");
        assertFalse(tagInline.canContainBlock());
    }

    /**
     * Test method canContainInline.
     */
    @Test
    public void testCanContainInline() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainInline());

        Tag tagBlock = new Tag("script");
        assertFalse(tagBlock.canContainInline());
    }

    /**
     * Test method isEmpty.
     */
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("div");
        assertFalse(tag.isEmpty());

        Tag tagEmpty = new Tag("img");
        assertTrue(tagEmpty.isEmpty());
    }

    /**
     * Test method isSelfClosing.
     */
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("div");
        assertFalse(tag.isSelfClosing());

        Tag tagSelfClosing = new Tag("img");
        assertTrue(tagSelfClosing.isSelfClosing());
    }

    /**
     * Test method isKnownTag.
     */
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    /**
     * Test method preserveWhitespace.
     */
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("div");
        assertFalse(tag.preserveWhitespace());

        Tag tagPre = new Tag("pre");
        assertTrue(tagPre.preserveWhitespace());
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.equals(tag2));

        Tag tag1Diff = new Tag("span");
        assertFalse(tag1.equals(tag1Diff));
    }

    /**
     * Test empty constructor.
     */
    @Test
    public void testEmptyConstructor() {
        Tag tag = new Tag();
        assertNull(tag.tagName);
        assertTrue(tag.isEmpty());
    }

    /**
     * Test method equals with different objects.
     */
    @Test
    public void testEqualsWithDifferentObjects() {
        Tag tag = new Tag("div");
        Object obj = new Object();
        assertFalse(tag.equals(obj));
    }

    /**
     * Test method hashCode.
     */
    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();
        assertEquals(hashCode1, hashCode2);

        Tag tag3 = new Tag("span");
        int hashCode3 = tag3.hashCode();
        assertNotEquals(hashCode1, hashCode3);
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }

    /**
     * Test method isData.
     */
    @Test
    public void testIsData() {
        Tag tag = new Tag("div");
        assertFalse(tag.isData());

        Tag tagData = new Tag("script");
        assertTrue(tagData.isData());
    }

    /**
     * Test method isInline.
     */
    @Test
    public void testIsInline() {
        Tag tag = new Tag("div");
        assertFalse(tag.isInline());

        Tag tagInline = new Tag("span");
        assertTrue(tagInline.isInline());
    }

    /**
     * Test constructor with empty tag name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyTagName() {
        new Tag("");
    }

    /**
     * Test constructor with null tag name.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTagName() {
        new Tag(null);
    }

    /**
     * Test method valueOf with null tag name.
     */
    @Test(expected = NullPointerException.class)
    public void testValueOfWithNullTagName() {
        Tag.valueOf(null);
    }

    /**
     * Test method valueOf with empty tag name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValueOfWithEmptyTagName() {
        Tag.valueOf("");
    }

    /**
     * Test method valueOf with invalid tag name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValueOfWithInvalidTagName() {
        Tag.valueOf("invalidTagName");
    }

    /**
     * Test method canContainBlock with non-block tag.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCanContainBlockWithNonBlockTag() {
        Tag tag = new Tag("span");
        tag.canContainBlock = true;
    }

    /**
     * Test method canContainInline with non-inline tag.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCanContainInlineWithNonInlineTag() {
        Tag tag = new Tag("div");
        tag.canContainInline = false;
    }

    /**
     * Test method empty with non-empty tag.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyWithNonEmptyTag() {
        Tag tag = new Tag("div");
        tag.empty = true;
    }

    /**
     * Test method selfClosing with non-selfClosing tag.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSelfClosingWithNonSelfClosingTag() {
        Tag tag = new Tag("div");
        tag.selfClosing = true;
    }

    /**
     * Test method preserveWhitespace with non-preserveWhitespace tag.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPreserveWhitespaceWithNonPreserveWhitespaceTag() {
        Tag tag = new Tag("div");
        tag.preserveWhitespace = true;
    }
}