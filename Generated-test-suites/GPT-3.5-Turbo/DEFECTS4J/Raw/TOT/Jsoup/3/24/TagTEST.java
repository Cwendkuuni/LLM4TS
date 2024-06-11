package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void getName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void valueOf() {
        Tag tag = Tag.valueOf("h1");
        assertTrue(tag.isBlock());
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void canContain() {
        Tag parent = Tag.valueOf("ul");
        Tag child = Tag.valueOf("li");
        assertTrue(parent.canContain(child));
    }

    @Test
    public void isBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }

    @Test
    public void canContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void isInline() {
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.isBlock());
    }

    @Test
    public void isData() {
        Tag tag = Tag.valueOf("script");
        assertFalse(tag.canContainInline());
        assertFalse(tag.isEmpty());
    }

    @Test
    public void isEmpty() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void preserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void getImplicitParent() {
        Tag tag = Tag.valueOf("li");
        assertNull(tag.getImplicitParent());
    }

    @Test
    public void isValidParent() {
        Tag child = Tag.valueOf("a");
        Tag parent = Tag.valueOf("div");
        assertFalse(parent.isValidParent(child));
    }

    @Test
    public void equals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("P");
        assertEquals(tag1, tag2);
    }

    @Test
    public void hashCode() {
        Tag tag1 = Tag.valueOf("a");
        Tag tag2 = Tag.valueOf("A");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void toString() {
        Tag tag = Tag.valueOf("h1");
        assertEquals("h1", tag.toString());
    }

    // Additional test cases can be added for each method to cover more scenarios.

}