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
        Tag tag = Tag.valueOf("h1");
        assertEquals("h1", tag.getName());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.canContainInline());
    }

    @Test
    public void testCanContain() {
        Tag parentTag = Tag.valueOf("div");
        Tag childTag1 = Tag.valueOf("p");
        assertTrue(parentTag.canContain(childTag1));
        
        Tag childTag2 = Tag.valueOf("h2");
        assertFalse(parentTag.canContain(childTag2));
    }

    @Test
    public void testIsBlock() {
        Tag tag1 = Tag.valueOf("div");
        assertTrue(tag1.isBlock());

        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.isBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isInline());

        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag2.isInline());
    }

    @Test
    public void testIsEmpty() {
        Tag tag1 = Tag.valueOf("img");
        assertTrue(tag1.isEmpty());

        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag2.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag1.equals(tag2));

        Tag tag3 = Tag.valueOf("div");
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testIsValidParent() {
        Tag parentTag1 = Tag.valueOf("ul");
        Tag childTag1 = Tag.valueOf("li");
        assertTrue(parentTag1.isValidParent(childTag1));

        Tag parentTag2 = Tag.valueOf("div");
        Tag childTag2 = Tag.valueOf("p");
        assertFalse(parentTag2.isValidParent(childTag2));
    }

    // Add more test cases for all other methods if needed

}