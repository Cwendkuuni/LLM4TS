package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagCreationAndEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("DIV");

        assertEquals(tag1, tag1);
        assertEquals(tag1, tag2); // same tag name, so ==
        assertNotEquals(tag1, tag3); // different tag names, so not ==
    }

    @Test
    public void testTagIsBlock() {
        Tag tagP = new Tag("p");
        Tag tagSpan = new Tag("span");

        assertTrue(tagP.isBlock());
        assertFalse(tagSpan.isBlock());
    }

    @Test
    public void testTagCanContainBlock() {
        Tag tagDiv = new Tag("div");
        Tag tagSpan = new Tag("span");
        Tag tagDiv2 = new Tag("div");

        assertTrue(tagDiv.canContainBlock());
        assertFalse(tagSpan.canContainBlock());
        assertTrue(tagDiv2.canContainBlock());
    }

    @Test
    public void testTagCanContainInline() {
        Tag tagDiv = new Tag("div");
        Tag tagSpan = new Tag("span");
        Tag tagDiv2 = new Tag("div");

        assertTrue(tagDiv.canContainInline());
        assertTrue(tagSpan.canContainInline());
        assertTrue(tagDiv2.canContainInline());
    }

    @Test
    public void testTagIsData() {
        Tag tagScript = new Tag("script");
        Tag tagP = new Tag("p");

        assertFalse(tagP.isData());
        assertTrue(tagScript.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag tagImg = new Tag("img");
        Tag tagP = new Tag("p");

        assertFalse(tagP.isEmpty());
        assertTrue(tagImg.isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag tagPre = new Tag("pre");
        Tag tagP = new Tag("p");

        assertFalse(tagP.preserveWhitespace());
        assertTrue(tagPre.preserveWhitespace());
    }

    @Test
    public void testTagCanContain() {
        Tag tagP = new Tag("p");
        Tag tagDiv = new Tag("div");
        Tag tagSpan = new Tag("span");
        Tag tagHead = new Tag("head");

        assertTrue(tagP.canContain(tagSpan));
        assertFalse(tagP.canContain(tagDiv)); // p cannot contain div directly
        assertFalse(tagHead.canContain(tagP)); // head cannot contain p directly
    }

    @Test
    public void testTagIsValidParent() {
        Tag tagDiv = new Tag("div");
        Tag tagP = new Tag("p");
        Tag tagHead = new Tag("head");
        Tag tagForm = new Tag("form");
        Tag tagInput = new Tag("input");

        assertTrue(tagDiv.isValidParent(tagP));
        assertFalse(tagDiv.isValidParent(tagHead));
        assertFalse(tagHead.isValidParent(tagForm));
        assertTrue(tagForm.isValidParent(tagInput));
    }
}