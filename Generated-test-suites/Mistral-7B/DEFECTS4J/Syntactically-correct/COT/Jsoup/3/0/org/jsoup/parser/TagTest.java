package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.junit.Test;

import java.util.*;

/**
 * TagTest.java - Test file for the Tag class
 */
public class TagTest {

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());

        tag = Tag.valueOf("DIV");
        assertEquals("div", tag.getName());

        tag = Tag.valueOf("UNKNOWN");
        assertFalse(tag.isBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertNull(tag.ancestors);
    }

    @Test
    public void testTagCanContain() {
        Tag parentTag = new Tag("DIV");
        Tag childTag1 = new Tag("P");
        Tag childTag2 = new Tag("DIV");
        Tag childTag3 = new Tag("DIV");
        Tag childTag4 = new Tag("P");

        assertTrue(parentTag.canContain(childTag1));
        assertTrue(parentTag.canContain(childTag2));
        assertTrue(parentTag.canContain(childTag3));
        assertTrue(parentTag.canContain(childTag4));

        Tag tagWithEmptyContent = new Tag("IMG");
        assertFalse(parentTag.canContain(tagWithEmptyContent));

        Tag tagWithBlockChildren = new Tag("UL");
        assertFalse(parentTag.canContain(tagWithBlockChildren));

        Tag tagWithInlineChildren = new Tag("SPAN");
        assertTrue(parentTag.canContain(tagWithInlineChildren));

        Tag tagWithOptionalClosing = new Tag("A");
        assertFalse(parentTag.canContain(tagWithOptionalClosing));

        Tag tagWithDataOnlyContent = new Tag("SCRIPT");
        assertFalse(parentTag.canContain(tagWithDataOnlyContent));

        Tag tagWithSameParent = new Tag("DIV");
        assertFalse(parentTag.canContain(tagWithSameParent));

        Tag tagWithAncestor = new Tag("BODY");
        assertTrue(parentTag.canContain(tagWithAncestor));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagValueOfInvalidTagName() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagValueOfEmptyTagName() {
        Tag.valueOf("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagValueOfWhitespaceTagName() {
        Tag.valueOf(" \t\tTAG ");
    }

    @Test
    public void testTagEquals() {
        Tag tag1 = new Tag("DIV");
        Tag tag2 = new Tag("DIV");
        Tag tag3 = new Tag("P");

        assertTrue(tag1.equals(tag1));
        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
        assertFalse(tag3.equals(tag1));
    }

    @Test
    public void testTagHashCode() {
        Tag tag1 = new Tag("DIV");
        Tag tag2 = new Tag("DIV");
        Tag tag3 = new Tag("P");

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();
        int hashCode3 = tag3.hashCode();

        assertNotSame(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
    }

    @Test
    public void testTagToString() {
        Tag tag = new Tag("DIV");
        assertEquals("div", tag.toString());
    }
}