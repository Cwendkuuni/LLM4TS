package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit 4 test cases for the Tag class.
 */
public class TagTest {

    @Test
    public void testValueOfBlockTag() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOfInlineTag() {
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.isBlock());
        assertEquals("span", tag.getName());
    }

    @Test
    public void testValueOfUnknownTag() {
        Tag tag = Tag.valueOf("newTag");
        assertFalse(tag.isBlock());
        assertEquals("newtag", tag.getName());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
    }

    @Test
    public void testIsKnownTagTrue() {
        assertTrue(Tag.isKnownTag("p"));
    }

    @Test
    public void testIsKnownTagFalse() {
        assertFalse(Tag.isKnownTag("newTag"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfNullTagNameException() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfEmptyTagNameException() {
        Tag.valueOf("");
    }

    @Test
    public void testIsDataTag() {
        Tag tag = new Tag("script");
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmptyTag() {
        Tag tag = new Tag("img");
        tag.empty = true;
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosingTag() {
        Tag tag = new Tag("img");
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsSelfClosingTagNotEmpty() {
        Tag tag = new Tag("p");
        tag.selfClosing = true;
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsBlockTag() {
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isBlock());
        }
    }

    @Test
    public void testIsInlineTag() {
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
        }
    }

    @Test
    public void testFormatAsBlockTag() {
        Tag tag = new Tag("p");
        assertTrue(tag.formatAsBlock());
        Tag tag2 = new Tag("pre");
        assertTrue(tag2.formatAsBlock());
    }

    @Test
    public void testFormatAsInlineTag() {
        Tag tag = new Tag("title");
        assertFalse(tag.formatAsBlock());
        Tag tag2 = new Tag("a");
        assertFalse(tag2.formatAsBlock());
    }

    @Test
    public void testPreserveWhitespaceTag() {
        Tag tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());
        Tag tag2 = new Tag("script");
        assertFalse(tag2.preserveWhitespace());
    }

    @Test
    public void testEqualsTag() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("span");

        assertTrue(tag1.equals(tag1));
        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testHashCodeTag() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("span");

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();
        int hashCode3 = tag3.hashCode();

        assertTrue(hashCode1 == hashCode2);
        assertNotSame(hashCode1, hashCode3);
    }

    @Test
    public void testToStringTag() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.toString());
    }
}